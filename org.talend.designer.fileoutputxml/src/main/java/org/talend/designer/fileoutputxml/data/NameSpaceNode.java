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
package org.talend.designer.fileoutputxml.data;

/**
 * DOC s class global comment. Detailled comment
 * 
 */
public class NameSpaceNode extends FOXTreeNode {

    public NameSpaceNode() {
    }

    public NameSpaceNode(String label) {
        setLabel(label);
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.talend.designer.fileoutputxml.data.FOXTreeNode#hasChildren()
     */
    @Override
    public boolean hasChildren() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.fileoutputxml.data.FOXTreeNode#getValue()
     */
    @Override
    public String getLabelForViewer() {
        return "xmlns:" + super.getLabel(); //$NON-NLS-1$
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public boolean isLoop() {
        return false;
    }
}
