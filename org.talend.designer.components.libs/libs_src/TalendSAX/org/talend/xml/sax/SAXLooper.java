package org.talend.xml.sax;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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
     * Parse the XML file. Buffer the result in LoopEntry.
     * 
     * @param fileURL file URL
     */
    public void parse(String fileURL) {
        try {
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            DefaultHandler hd = newHandler();
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
        SAXLoopCompositeHandler result = new SAXLoopCompositeHandler();
        LoopEntry tmpEntry = this.entry;
        while (tmpEntry != null) {
            result.register(new SAXLoopHandler(tmpEntry));
            tmpEntry = tmpEntry.getSubLoop();
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
            String loopPath = "/row/subrow/*[name()]";
            String[] pathList = new String[] { ".", "." + "/@xsi:nil" };

            // String file = "./src/org/talend/xml/sax/in1.xml";
            // String loopPath = "/schools/school/class/student";// "/areas/area/street/home";
            // String[] pathList = new String[] { "../../@school_name", "../@class_name", "@stu_no", "name", "name" +
            // "/@xsi:nil",
            // "age", "age" + "/@xsi:nil", "sex", "sex" + "/@xsi:nil", "/*[name()]" };// new String[] { "@number",
            // "owner",
            // "house", "../../@city", "../@name",
            // "../@id",
            // "../length","../../../@provite" };

            // String file = "F:/test/XML/multiNS.xml";
            // String loopPath = "/pol:root/pol:order/pol:project";
            // String[] pathList = new String[] { "pol:code", "pol:name", "pol:customer/pol:code",
            // "pol:customer/pol:name" };

            // String file = "F:/to others/toMicheal/outt.xml";
            // String loopPath = "/REF-ETI/ID-COURIE/DOC-REF/EXPLRE-DOC-REF";
            // String[] pathList = new String[] { "../../../@REF-ETI", "../../../AUTEUR", "../../@ID-COURIE",
            // "../../INFO-COURIE/STE-JUR-CT", "../../INFO-COURIE/POL-NUM-STE", "../../INFO-COURIE/PERS-NUM",
            // "../../INFO-COURIE/ID-ARV", "../../INFO-COURIE/COD-EVC", "../../VAR-COURIE/MASTER-I",
            // "../../VAR-COURIE/INTERFACE", "../../VAR-COURIE/INTERFACE/CPOST-APPORT-INT", "../../GEST-REJET",
            // "../@DOC-REF", "../../DOC-EXTN/ID-DOC-EXTN", "COD-ROUTAG", "POSTAL-COD", "NUM-PST-DEST" };

            SAXLooper looper = new SAXLooper(loopPath, pathList);
            looper.parse(file);
            Iterator<Map<String, String>> it = looper.iterator();
            while (it.hasNext()) {
                Map<String, String> tmp = it.next();
                for (String value : pathList) {
                    System.out.print("|" + tmp.get(value));
                }
                System.out.println();
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
