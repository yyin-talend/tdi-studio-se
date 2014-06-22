// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.xml.sax.simpleparser.model;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class XMLNode {

    public boolean isLooping = false;

    public boolean hasValue = false;

    public boolean outputText = false;

    public String loopPath;

    public String nodePath;

    public String originPath;

    public boolean isAsXML = false;

    public boolean isDot = false;

    private StringBuffer value = new StringBuffer();
    private boolean isNullValue = true;

    public XMLNode(String loopPath, String originPath, String nodePath, boolean isAsXML, boolean isDot) {
        this.loopPath = loopPath;
        this.nodePath = nodePath;
        this.originPath = originPath;
        this.isAsXML = isAsXML;
        this.isDot = isDot;
    }

    public XMLNode(String loopPath, String originPath, String nodePath, boolean isAsXML) {
        this(loopPath, originPath, nodePath, isAsXML, false);
    }

    public XMLNode(String loopPath, String originPath, String nodePath) {
        this(loopPath, originPath, nodePath, false, false);
    }

    public void addTextValue(String appendValue) {
    	isNullValue = false;
    	
        if (appendValue != null) {
        	value.append(appendValue);
        }
    }

    public String getTextValue() {
    	if(isNullValue) return null;
    	return value.toString();
    }

    public void resetValue() {
    	isNullValue = true;
    	value.setLength(0);
    }

}
