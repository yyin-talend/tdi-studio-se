package org.talend.xml.sax;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.talend.xml.sax.function.inter.Function;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
/**
 * This class is the interface for tFileInputXML component. DOC s class global comment. Detailled comment
 * 
 * $Id: SAXLooper.java,v 1.1 2008/03/21 07:20:39 xzhang Exp $
 */
public class SAXLooper {

    SAXLoopCompositeHandler result;

    // loop path in the tFileInputXML component.
    private String loopPath;

    // node paths special which tab will be read as the row value
    private String[] nodePaths;

    private LoopEntry entry;

    /**
     * DOC xzhang SAXLooper constructor comment.
     * 
     * @param loopPath :loop path in the tFileInputXML component.
     * @param nodePaths :node paths special which tab will be read as the row value
     */
    public SAXLooper(String loopPath, String[] nodePaths) {
        this.loopPath = loopPath;
        this.nodePaths = nodePaths;
        initLoopEntry();
    }

    /**
     * 
     * DOC wliu SAXLooper constructor comment.
     * 
     * @param rootPath:root loop path in the tFileInputMSXML component
     * @param mapping:LOOP_PATH=sub loop path, MAPPING=query columns
     */
    private String rootPath = null;

    private LoopEntry[] entries = null;

    private String[] arrLoopPath = null;

    private String[][] arrNodePaths = null;

    private String[] arrOrigLoopPath = null;

    public SAXLooper(String rootPath, String[] arrLoopPath, String[][] arrNodePaths) {

        this.arrLoopPath = new String[arrLoopPath.length];

        this.arrOrigLoopPath = arrLoopPath;

        this.arrNodePaths = arrNodePaths;

        String tmpRootPath = rootPath;
        if (tmpRootPath.endsWith("/")) {
            tmpRootPath = tmpRootPath.substring(0, tmpRootPath.length() - 1);
        }

        this.rootPath = tmpRootPath;

        for (int i = 0; i < arrLoopPath.length; i++) {
            String tmpLoopPath = arrLoopPath[i];

            if (tmpLoopPath.startsWith("/")) {
                tmpLoopPath = tmpLoopPath.substring(1);
            }
            this.arrLoopPath[i] = tmpRootPath + "/" + tmpLoopPath;
        }

        entries = new LoopEntry[arrLoopPath.length];

        initLoopEntries();
    }

    protected String getRootPath() {
        return this.rootPath;
    }

    protected String[] getArrLoopPaths() {
        return this.arrLoopPath;
    }

    /**
     * Parse the XML file. Buffer the result in LoopEntry.
     * 
     * @param fileURL file URL
     */
    public void parse(String fileURL) {
        try {
            DefaultHandler hd = null;
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            if (rootPath == null || rootPath.equals("")) {
                hd = newHandler();
            } else {
                hd = newHandler2();
            }
            saxParser.parse(fileURL, hd);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get result iterator. This must be call after the parse method.
     * 
     * @return Iterator
     */
    public Iterator<Map<String, String>> iterator() {
        return new SAXLoopIterator(this.entry);
    }

    /**
     * create a handler
     * 
     * @return
     */
    private DefaultHandler newHandler() {
        SAXLoopCompositeHandler result;
        if (rootPath == null || rootPath.equals("")) {
            result = new SAXLoopCompositeHandler();
        } else {
            result = new SAXLoopCompositeHandler(this);
        }
        LoopEntry tmpEntry = this.entry;
        while (tmpEntry != null) {
            result.register(new SAXLoopHandler(tmpEntry));
            tmpEntry = tmpEntry.getSubLoop();
        }
        return result;
    }

    /**
     * create a handler
     * 
     * @return
     */
    private DefaultHandler newHandler2() {

        if (rootPath == null || rootPath.equals("")) {
            result = new SAXLoopCompositeHandler();
        } else {
            result = new SAXLoopCompositeHandler(this);
        }
        for (int i = 0; i < this.entries.length; i++) {
            LoopEntry tmpEntry = this.entries[i];
            while (tmpEntry != null) {
                result.register(new SAXLoopHandler(tmpEntry));
                tmpEntry = tmpEntry.getSubLoop();
            }
        }
        return result;
    }

    /**
     * init the LoopEntry according by "nodePaths" and "loopPath".
     * 
     * @return
     */
    private void initLoopEntry() {
        Map<String, LoopEntry> entryMap = new HashMap<String, LoopEntry>();

        FunctionRegister funcRegister = new FunctionRegister();// list of all the functions
        Function function = null;
        // function is in looppath
        if (this.loopPath.indexOf("/*[") >= 0 && this.loopPath.indexOf(")]") >= 0) {
            if (nodePaths.length > 0) {
                String strTmp = loopPath.substring(loopPath.lastIndexOf("/"));
                String strFuncName = strTmp.substring(strTmp.indexOf("*[") + 2, strTmp.indexOf("("));

                if (funcRegister.isFuncRegistered(strFuncName)) {
                    function = funcRegister.getFunction(strFuncName);
                }
            }
            loopPath = loopPath.substring(0, loopPath.lastIndexOf("/"));
        }
        // parse the node path to loopEntry
        for (String column : nodePaths) {
            String resultCol = this.loopPath;
            String tmpLoopPath = null;
            String[] splits = column.split("/");
            for (String tmp : splits) {
                if (tmp.equals("..")) {
                    resultCol = resultCol.substring(0, resultCol.lastIndexOf("/"));
                    tmpLoopPath = resultCol;
                } else if (tmp.equals(".")) {
                    tmpLoopPath = resultCol;
                } else {

                    if (tmp.indexOf("*[") >= 0 && tmp.indexOf(")]") >= 0) {// has funcion in column
                        resultCol = resultCol.substring(0, resultCol.lastIndexOf("/"));
                        tmpLoopPath = resultCol;// find the function node

                        // get the function name
                        String strFuncName = tmp.substring(tmp.indexOf("*[") + 2, tmp.indexOf("("));

                        if (funcRegister.isFuncRegistered(strFuncName)) {
                            function = funcRegister.getFunction(strFuncName);
                        }

                    } else {
                        resultCol += "/" + tmp;
                    }

                }
            }
            if (tmpLoopPath == null) {
                tmpLoopPath = loopPath;
            }
            if (!entryMap.containsKey(tmpLoopPath)) {
                entryMap.put(tmpLoopPath, new LoopEntry(tmpLoopPath));
            }

            if (function == null) {
                entryMap.get(tmpLoopPath).addPath(resultCol, column);
            } else {// add the exist function to the loopentry
                entryMap.get(tmpLoopPath).addPath(column, column);
                entryMap.get(tmpLoopPath).addFunction(column, function);
                function = null;
            }

        }

        // set sub entry
        String[] splits = loopPath.split("/");
        String path = "";
        LoopEntry tmpentry = null;
        for (String tmp : splits) {
            if (tmp.trim().length() > 0) {
                path += "/" + tmp;
                if (entryMap.containsKey(path)) {
                    if (tmpentry == null) {
                        tmpentry = entryMap.get(path);
                        this.entry = tmpentry;
                    } else {
                        tmpentry.setSubLoop(entryMap.get(path));
                        tmpentry = entryMap.get(path);

                    }
                }
            }
        }
    }

    private void initLoopEntries() {

        for (int i = 0; i < this.entries.length; i++) {

            Map<String, LoopEntry> entryMap = new HashMap<String, LoopEntry>();

            FunctionRegister funcRegister = new FunctionRegister();// list of all the functions
            Function function = null;
            // function is in looppath
            if (this.arrLoopPath[i].indexOf("/*[") >= 0 && this.arrLoopPath[i].indexOf(")]") >= 0) {
                if (arrNodePaths[i].length > 0) {
                    String strTmp = arrLoopPath[i].substring(arrLoopPath[i].lastIndexOf("/"));
                    String strFuncName = strTmp.substring(strTmp.indexOf("*[") + 2, strTmp.indexOf("("));

                    if (funcRegister.isFuncRegistered(strFuncName)) {
                        function = funcRegister.getFunction(strFuncName);
                    }
                }
                arrLoopPath[i] = arrLoopPath[i].substring(0, arrLoopPath[i].lastIndexOf("/"));
            }
            // parse the node path to loopEntry
            for (String column : arrNodePaths[i]) {
                String resultCol = this.arrLoopPath[i];
                String tmpLoopPath = null;
                String[] splits = column.split("/");
                for (String tmp : splits) {
                    if (tmp.equals("..")) {
                        resultCol = resultCol.substring(0, resultCol.lastIndexOf("/"));
                        tmpLoopPath = resultCol;
                    } else if (tmp.equals(".")) {
                        tmpLoopPath = resultCol;
                    } else {

                        if (tmp.indexOf("*[") >= 0 && tmp.indexOf(")]") >= 0) {// has funcion in column
                            resultCol = resultCol.substring(0, resultCol.lastIndexOf("/"));
                            tmpLoopPath = resultCol;// find the function node

                            // get the function name
                            String strFuncName = tmp.substring(tmp.indexOf("*[") + 2, tmp.indexOf("("));

                            if (funcRegister.isFuncRegistered(strFuncName)) {
                                function = funcRegister.getFunction(strFuncName);
                            }

                        } else {
                            resultCol += "/" + tmp;
                        }

                    }
                }
                if (tmpLoopPath == null) {
                    tmpLoopPath = arrLoopPath[i];
                }
                if (!entryMap.containsKey(tmpLoopPath)) {
                    entryMap.put(tmpLoopPath, new LoopEntry(tmpLoopPath));
                }

                if (function == null) {
                    entryMap.get(tmpLoopPath).addPath(resultCol, column);
                } else {// add the exist function to the loopentry
                    entryMap.get(tmpLoopPath).addPath(column, column);
                    entryMap.get(tmpLoopPath).addFunction(column, function);
                    function = null;
                }

            }

            // set sub entry
            String[] splits = arrLoopPath[i].split("/");
            String path = "";
            LoopEntry tmpentry = null;
            for (String tmp : splits) {
                if (tmp.trim().length() > 0) {
                    path += "/" + tmp;
                    if (entryMap.containsKey(path)) {
                        if (tmpentry == null) {
                            tmpentry = entryMap.get(path);
                            this.entries[i] = tmpentry;
                        } else {
                            tmpentry.setSubLoop(entryMap.get(path));
                            tmpentry = entryMap.get(path);

                        }
                    }
                }
            }

        }// for(int i=0;i<length;i++)

    }

    /**
     * 
     * DOC wliu Comment method "getRootLoopList".
     * 
     * @return
     */

    protected Map<String, Object> getSubRootLoopMap(String tmpLoopPath) {

        Map<String, Object> mapping = new HashMap<String, Object>();

        if (this.rootPath == null || rootPath.equals("")) {
            return null;
        }

        for (int i = 0; i < this.entries.length; i++) {
            if (entries[i].getLoop().equals(tmpLoopPath)) {
                Iterator<Map<String, String>> iter = new SAXLoopIterator(this.entries[i]);
                if (iter.hasNext()) {
                    mapping.put("value", iter.next());
                }
                mapping.put("path", this.arrOrigLoopPath[i]);

                this.entries[i].clearRows();

                break;
            }
        }

        return mapping;
    }

    public List<List<Map<String, Object>>> getAllResultList() {
        if (rootPath != null && !rootPath.equals("")) {
            return result.getAllResult();
        }

        return null;
    }

    /**
     * Testing code
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {

            Runtime.getRuntime().gc();
            long start = Runtime.getRuntime().freeMemory();
            long startall = Runtime.getRuntime().maxMemory();
            long timeStart = System.currentTimeMillis();

            String file = "./src/org/talend/xml/sax/in.xml";
            String rootPath = "/orderdata/order/";
            String[] loopPath = new String[] { "/header", "line-detail" };
            String[][] pathList = new String[][] {
                    { "cust-vendor-num", "cust-vendor-num" + "/@xsi:nil", "cust", "cust" + "/@xsi:nil" },
                    { "prod-num", "prod-num" + "/@xsi:nil", "custom-var", "custom-var" + "/@xsi:nil" } };

            SAXLooper looper = new SAXLooper(rootPath, loopPath, pathList);
            looper.parse(file);

            List<List<Map<String, Object>>> rootlist = looper.getAllResultList();

            for (List<Map<String, Object>> subList : rootlist) {

                Iterator<Map<String, Object>> itGroup = subList.iterator();
                while (itGroup.hasNext()) {

                    Map<String, Object> map = itGroup.next();
                    System.out.print(map.get("path") + "|");
                    Map<String, String> tmpMap = (Map<String, String>) map.get("value");

                    String tmpLoopPath = null;
                    int i = 0;
                    for (; i < loopPath.length; i++) {
                        if (loopPath[i].equals(map.get("path"))) {
                            break;
                        }
                    }
                    for (int j = 0; j < pathList[i].length; j++) {
                        if (j == pathList[i].length - 1) {
                            System.out.println(tmpMap.get(pathList[i][j]));
                        } else {
                            System.out.print(tmpMap.get(pathList[i][j]) + "|");
                        }
                    }

                }

            }

            System.out.println("==Time=" + (System.currentTimeMillis() - timeStart));

            System.out.println("==Memory start all =" + startall);
            System.out.println("==Memory start free =" + start);
            System.out.println("==Memory end all =" + Runtime.getRuntime().maxMemory());
            System.out.println("==Memory end free =" + Runtime.getRuntime().freeMemory());
            System.out.println("==Memory=" + (start - Runtime.getRuntime().freeMemory()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
