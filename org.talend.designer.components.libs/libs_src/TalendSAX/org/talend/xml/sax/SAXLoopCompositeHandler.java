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
package org.talend.xml.sax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * DOC s class global comment. Detailled comment
 */
public class SAXLoopCompositeHandler extends DefaultHandler {

    private List<DefaultHandler> handlerList = new ArrayList<DefaultHandler>();

    private SAXLooper saxlooper = null;

    private String currentPath = "";

    private String rootPath = null;

    private String[] arrLoopPath = null;

    public SAXLoopCompositeHandler() {
        super();
    }

    public SAXLoopCompositeHandler(SAXLooper saxlooper) {
        super();
        this.saxlooper = saxlooper;
        this.rootPath = this.saxlooper.getRootPath();
        this.arrLoopPath = saxlooper.getArrLoopPaths();
    }

    public void register(DefaultHandler handler) {
        handlerList.add(handler);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#startDocument()
     */
    public void startDocument() throws SAXException {
        for (DefaultHandler handler : handlerList) {
            handler.startDocument();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#endDocument()
     */
    public void endDocument() throws SAXException {
        for (DefaultHandler handler : handlerList) {
            handler.endDocument();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String,
     * org.xml.sax.Attributes)
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        currentPath = currentPath + "/" + qName;
        //
        // if (this.arrLoopPath != null) {
        // for (int i = 0; i < arrLoopPath.length; i++) {
        // if (currentPath.equals(arrLoopPath[i])) {
        // subList = new ArrayList<Map<String, Object>>();
        // break;
        // }
        // }
        // }
        if (currentPath.equals(rootPath)) {
            subList = new ArrayList<Map<String, Object>>();
        }

        for (DefaultHandler handler : handlerList) {
            handler.startElement(uri, localName, qName, attributes);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    public void characters(char ch[], int start, int length) throws SAXException {
        for (DefaultHandler handler : handlerList) {
            handler.characters(ch, start, length);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    List<List<Map<String, Object>>> resultList = new ArrayList<List<Map<String, Object>>>();

    List<Map<String, Object>> subList = null;

    public void endElement(String uri, String localName, String qName) throws SAXException {

        for (DefaultHandler handler : handlerList) {
            handler.endElement(uri, localName, qName);
        }
        // get all the rows under the root loop element
        if (currentPath.equals(rootPath)) {
            resultList.add(subList);
        }

        if (this.arrLoopPath != null) {
            for (int i = 0; i < arrLoopPath.length; i++) {
                if (currentPath.equals(arrLoopPath[i])) {
                    Map<String, Object> map = saxlooper.getSubRootLoopMap(arrLoopPath[i]);
                    subList.add(map);
                    break;
                }
            }
        }

        // // clear all the stored rows
        // for (DefaultHandler handler : handlerList) {
        // ((SAXLoopHandler) handler).clearEntryRows();
        // }

        currentPath = currentPath.substring(0, currentPath.lastIndexOf("/"));
    }

    protected List<List<Map<String, Object>>> getAllResult() {
        return this.resultList;
    }

}
