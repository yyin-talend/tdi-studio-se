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

    public SAXLoopCompositeHandler() {
        super();
    }

    public SAXLoopCompositeHandler(SAXLooper saxlooper) {
        super();
        this.saxlooper = saxlooper;
        this.rootPath = this.saxlooper.getRootPath();
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
    List<Map<String, List<Map<String, String>>>> resultList = new ArrayList<Map<String, List<Map<String, String>>>>();

    public void endElement(String uri, String localName, String qName) throws SAXException {

        for (DefaultHandler handler : handlerList) {
            handler.endElement(uri, localName, qName);
        }

        // get all the rows under the root loop element
        if (currentPath.equals(rootPath)) {
            Map<String, List<Map<String, String>>> map = null;
            map = saxlooper.getSubRootLoopList();
            resultList.add(map);
            // clear all the stored rows
            for (DefaultHandler handler : handlerList) {
                ((SAXLoopHandler) handler).clearEntryRows();
            }
        }

        currentPath = currentPath.substring(0, currentPath.lastIndexOf("/"));
    }

    protected List<Map<String, List<Map<String, String>>>> getAllResult() {
        return this.resultList;
    }

}
