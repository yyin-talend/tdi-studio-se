// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.xml.sax.simpleparser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.talend.xml.sax.simpleparser.model.XMLNode;
import org.talend.xml.sax.simpleparser.model.XMLNodes;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class SimpleSAXLoopHandler extends DefaultHandler {

    private XMLNodes nodes;

    private String currentPath = "";

    private DataBufferCache bufferCache = null;

    public SimpleSAXLoopHandler(XMLNodes nodes, DataBufferCache bcache) {
        super();
        this.nodes = nodes;
        this.bufferCache = bcache;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#startDocument()
     */
    public void startDocument() throws SAXException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#endDocument()
     */
    public void endDocument() throws SAXException {
        // DataCache.getInstance().setReadEnd(true);
        bufferCache.setIsEnd();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String,
     * org.xml.sax.Attributes)
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        this.currentPath = this.currentPath + "/" + qName;
        for (XMLNode node : nodes.getNodesCollection()) {
            node.outputText = false;
            if (this.currentPath.equals(node.loopPath)) {
                node.isLooping = true;
            }
            if (node.isLooping) {
                if (node.isAsXML && (this.currentPath.equals(node.nodePath) || this.currentPath.startsWith(node.nodePath + "/"))) {
                    node.addTextValue("<" + qName);
                    if (attributes.getLength() > 0) {
                        for (int m = 0; m < attributes.getLength(); m++) {
                            node.addTextValue(" " + attributes.getQName(m) + "=" + "\"" + attributes.getValue(m) + "\"");
                        }
                    }
                    node.outputText = true;
                    node.hasValue = false;
                    node.addTextValue(">");
                } else if (node.isDot
                        && (this.currentPath.equals(node.nodePath) || this.currentPath.startsWith(node.nodePath + "/"))) {
                    node.outputText = true;
                    node.hasValue = false;
                } else {
                    int index = node.nodePath.lastIndexOf("@");
                    if (index > 0) {
                        if (currentPath.equals(node.nodePath.substring(0, index - 1))) {
                            String attribute = attributes.getValue(node.nodePath.substring(index + 1));
                            if (attribute != null && false == node.hasValue) {
                                node.addTextValue(attribute);
                                node.hasValue = true;
                            }
                        }
                    } else {
                        if (currentPath.equals(node.nodePath)) {
                            node.outputText = true;
                        }
                    }

                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    public void characters(char ch[], int start, int length) throws SAXException {
        String text = new String(ch, start, length);
        if (text.length() <= 0)
            return;
        for (XMLNode node : nodes.getNodesCollection()) {
            if (node.isLooping) {
                if (node.outputText && node.hasValue == false) {
                    node.addTextValue(text);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    public void endElement(String uri, String localName, String qName) throws SAXException {
        for (XMLNode node : nodes.getNodesCollection()) {
            if (node.isLooping) {
                if (node.outputText) {
                    if (node.hasValue == false) {
                        node.addTextValue("");
                    }
                    node.hasValue = true;
                }
                if ((node.isAsXML || node.isDot)
                        && (this.currentPath.equals(node.nodePath) || this.currentPath.startsWith(node.nodePath + "/"))) {
                    if (node.isAsXML) {
                        node.addTextValue("</" + qName + ">");
                    }
                    if (this.currentPath.equals(node.nodePath)) {
                        node.hasValue = true;
                    }
                }
            }

            node.outputText = false;
        }
        if (this.currentPath.equals(nodes.getLoopPath())) {
            if (isNotNull(nodes.getNodesCollection(), nodes.size())) {
                Map<String, String> map = new HashMap<String, String>();
                for (XMLNode node : nodes.getNodesCollection()) {
                    map.put(node.originPath, node.getTextValue());
                    // ---------------------------------------------

                    // System.out.print("" + node.getTextValue() + "#");
                    // ---------------------------------------------
                }
                bufferCache.writeData(map);
                // System.out.println();

            }
            nodes.resetAll();
        }
        this.currentPath = this.currentPath.substring(0, this.currentPath.lastIndexOf("/"));

    }

    private boolean isNotNull(Collection<XMLNode> cl, int length) {

        for (XMLNode node : cl) {
            if (node.getTextValue() != null) {
                return true;
            }
        }
        return false;
    }

}
