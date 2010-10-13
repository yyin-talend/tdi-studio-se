// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.xml.sax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * DOC s class global comment. Detailled comment
 * 
 * $Id: SAXLoopHandler.java,v 1.1 2008/03/21 07:20:39 xzhang Exp $
 */
public class SAXLoopHandler extends DefaultHandler {

    //
    private String loopPath = null;

    private String subLoopPath = null;

    private int subLoopCount = 0;

    private List<String> loopCols = new ArrayList<String>();

    private List<Boolean> asXMLs = null;

    private String currentPath = "";

    // current element is include in looppath
    private boolean isLooping = false;

    // is read the text value
    private boolean outputText = false;

    private String[] currentRow = null;

    // to mark the value is set or not
    private boolean[] currentRowHaveValue = null;

    private int indexOfColumn = 0;

    private LoopEntry entry;

    List<Map<String, Object>> listArgs = new ArrayList<Map<String, Object>>();

    // ===============add for bug7632 begin======================================
    private ComplexSAXLooper saxLooper;

    // ========================end===============================================

    public SAXLoopHandler(LoopEntry entry) {
        this.entry = entry;
        this.loopPath = entry.getLoop();
        this.loopCols = entry.getPaths();
        this.asXMLs = entry.getAsXMLs();
        if (entry.getSubLoop() != null) {
            this.subLoopPath = entry.getSubLoop().getLoop();
        } else {
            this.subLoopPath = loopPath;
        }
    }

    public SAXLoopHandler(ComplexSAXLooper saxLooper, LoopEntry entry) {
        this(entry);
        this.saxLooper = saxLooper;
    }

    /**
     * 
     * 
     * @param loopPath
     * @param loopCols
     * @param subLoopPath
     */
    public SAXLoopHandler(String loopPath, List<String> loopCols, String subLoopPath) {
        this.loopPath = loopPath;
        this.subLoopPath = subLoopPath;
        this.loopCols = loopCols;
    }

    public void startDocument() throws SAXException {
        // System.out.println("startDocument");
        // for (String path : loopCols) {
        // System.out.print("|" + path.substring(path.lastIndexOf("/")));
        // }
        // System.out.println();
    }

    public void endDocument() throws SAXException {
        // System.out.println("endDocument");
        // for (String[] tmp : entry.getRows()) {
        // for (String value : tmp) {
        // System.out.print("|" + value);
        // }
        // System.out.println();
        // }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // System.out.println("startElement");

        String loopPath = this.loopPath;
        String subLoopPath = this.subLoopPath;
        List<String> loopCols = new ArrayList<String>();

        if (loopPath.indexOf("*") != -1) {
            loopPath = this.loopPath.replace("*", qName);
            subLoopPath = this.subLoopPath.replace("*", qName);
            for (String loopCol : this.loopCols) {
                if (loopCol.indexOf("@") != -1) {
                    loopCols.add(loopCol.replace("*", qName));
                } else if (currentPath.length() > 0) {
                    if (this.loopPath.indexOf("*") < this.loopPath.length() - 1) {
                        String parent = currentPath.substring(currentPath.lastIndexOf("/") + 1);
                        loopCols.add(loopCol.replace("*", parent));
                    } else if (this.loopPath.indexOf("*") == this.loopPath.length() - 1) {
                        loopCols.add(loopCol.replace("*", qName));
                    }
                }
            }
        } else {
            loopCols.addAll(this.loopCols);
        }

        currentPath += "/" + qName;
        if (currentPath.equals(loopPath)) {
            isLooping = true;
            subLoopCount = 0;
            currentRow = new String[this.loopCols.size() + 1];
            currentRowHaveValue = new boolean[this.loopCols.size() + 1];
        }
        if (isLooping) {
            indexOfColumn = loopCols.indexOf(currentPath);

            // count sub loop number
            if (currentPath.equals(subLoopPath)) {
                subLoopCount++;
            }
            // paser the attributes
            for (int i = 0; i < loopCols.size(); i++) {
                String column = loopCols.get(i);
                boolean asXML = this.asXMLs.get(i);

                if (asXML && (currentPath.equals(column) || currentPath.startsWith(column + "/"))) {
                    indexOfColumn = i; // correct the index for text
                    if (currentRow[i] == null)
                        currentRow[i] = "";
                    currentRow[i] = currentRow[i] + "<" + qName;
                    if (attributes.getLength() > 0) {
                        for (int m = 0; m < attributes.getLength(); m++) {
                            currentRow[i] = currentRow[i] + " " + attributes.getQName(m) + "=" + "\"" + attributes.getValue(m)
                                    + "\"";
                        }
                    }
                    outputText = true;
                    currentRow[i] = currentRow[i] + ">";
                } else {
                    int index = column.lastIndexOf("@");
                    if (index > 0) {
                        if (currentPath.equals(column.substring(0, index - 1))) {
                            String attribute = attributes.getValue(column.substring(index + 1));
                            if (attribute != null && false == currentRowHaveValue[i]) {
                                currentRow[i] = attribute;
                            }
                            currentRowHaveValue[i] = true;
                        }
                    } else {

                        if (currentPath.equals(column)) {
                            outputText = true;
                        }
                    }
                }
            }
            // loop has function, then add the attribute to the args
            if (this.entry.hasFunctions()) {

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("loopPath", loopPath);
                map.put("column", currentPath);
                map.put("value", attributes);
                listArgs.add(map);

            }

        }
    }

    /**
     * Deal with the bug 4402: the content <quotes_encoded>&quot;hello world&quot;</quotes_encoded>. In this case,
     * "characters" will be called three time.
     * 
     * first: "
     * 
     * second: hello world
     * 
     * third: "
     */
    public void characters(char ch[], int start, int length) throws SAXException {
        // System.out.println(new String(ch, start, length) + " | " + isLooping);
        if (isLooping) {
            String text = new String(ch, start, length);
            // System.out.println(text);
            if (text.length() > 0) {
                if (outputText && indexOfColumn >= 0 && false == currentRowHaveValue[indexOfColumn]) {
                    if (currentRow[indexOfColumn] == null) {
                        currentRow[indexOfColumn] = text;
                    } else {
                        currentRow[indexOfColumn] += text;
                    }
                }

                /**
                 * when the loop has functions, add the value of the element to the args
                 */
                if (this.entry.hasFunctions()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("loopPath", loopPath);
                    map.put("column", currentPath);
                    map.put("value", text);
                    listArgs.add(map);
                }

            }
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        // System.out.println("endElement");

        String loopPath = this.loopPath;
        if (loopPath.indexOf("*") != -1) {
            loopPath = this.loopPath.replace("*", qName);
        }

        if (isLooping && outputText && indexOfColumn >= 0) {
            if (currentRow[indexOfColumn] == null) {
                currentRow[indexOfColumn] = "";
            }
            if (currentRow[indexOfColumn].trim().startsWith("<")) {
                currentRow[indexOfColumn] = currentRow[indexOfColumn] + "</" + qName + ">";
            } else {
                currentRowHaveValue[indexOfColumn] = true;
            }
        }

        outputText = false;

        if (currentPath.equals(loopPath)) {
            isLooping = false;

            /**
             * put the the result of the fuctions to the column
             */
            if (this.entry.hasFunctions()) {
                entry.execFunctions(listArgs);

                List<Map<String, String>> results = entry.getFunctionResults();
                for (int i = 0; i < results.size(); i++) {
                    Map<String, String> map = results.get(i);
                    String strKey = map.get("name");
                    int index = this.loopCols.indexOf(strKey);
                    if (index >= 0 && currentRowHaveValue[index] == false) {
                        currentRow[index] = map.get("value");
                        currentRowHaveValue[index] = true;
                    }
                }

            }

            // for (String value : currentRow) {
            // System.out.print("|" + value);
            // }
            // System.out.println();
            currentRow[currentRow.length - 1] = Integer.toString(subLoopCount);
            if (isNotNull(currentRow, currentRowHaveValue, currentRow.length - 1)) {
                entry.getRows().add(currentRow);
                // ===========add for bug7632==========================
                if (this.entry.getOriginalLoopPath() != null) {
                    this.saxLooper.addLoopOrder(this.entry.getOriginalLoopPath());
                }
                // =================end================================
            }
        }
        currentPath = currentPath.substring(0, currentPath.lastIndexOf("/"));
    }

    private boolean isNotNull(String[] os, boolean[] bs, int length) {

        for (int i = 0; i < length; i++) {
            if (os[i] != null || bs[i]==true) {
                return true;
            }
        }
        return false;
    }
}
