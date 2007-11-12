// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.runprocess.shadow;

/**
 * DOC mhirt class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class FileInputNode extends ShadowNode {

    /**
     * Constructs a new FileInputNode.
     */
    public FileInputNode(String nodeType) {
        super(nodeType);
    }

    public FileInputNode(String nodeType, int nbColumn) {
        super(nodeType, nbColumn);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isStart()
     */
    public boolean isStart() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.shadow.ShadowNode#isSubProcessStart()
     */
    @Override
    public boolean isSubProcessStart() {
        return true;
    }

    public String trimParameter(String para) {
        int length = para.length();
        String result = para;
        if (length > 1 && ((para.startsWith("\"") && para.endsWith("\"")))||(para.startsWith("\'") && para.endsWith("\'"))) {
            result = para.substring(1, length - 1);
        }
        if (result.contains("\\")) {
            result = result.replaceAll("\\\\n", "\n");
            result = result.replaceAll("\\\\b", "\b");
            result = result.replaceAll("\\\\f", "\f");
            result = result.replaceAll("\\\\r", "\r");
            result = result.replaceAll("\\\\t", "\t");
            result = result.replaceAll("\\\\\"", "\"");
            result = result.replaceAll("\\\\\\\\", "\\\\");
        }

        return result;
    }
}
