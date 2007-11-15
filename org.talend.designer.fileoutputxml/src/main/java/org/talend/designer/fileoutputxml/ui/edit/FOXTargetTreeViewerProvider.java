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
package org.talend.designer.fileoutputxml.ui.edit;

import java.util.List;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.talend.designer.fileoutputxml.data.Attribute;
import org.talend.designer.fileoutputxml.data.FOXTreeNode;
import org.talend.designer.fileoutputxml.i18n.Messages;
import org.talend.designer.fileoutputxml.util.TreeUtil;

/**
 * bqian Label provider and content provider for FOX target tree viewer. <br/>
 * 
 * $Id: FOXTargetTreeViewerProvider.java,v 1.1 2007/06/12 07:20:38 gke Exp $
 * 
 */
public class FOXTargetTreeViewerProvider extends LabelProvider implements ITableLabelProvider, ITreeContentProvider {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren(Object parentElement) {
        FOXTreeNode treeNode = (FOXTreeNode) parentElement;
        List<FOXTreeNode> children = treeNode.getChildren();
        return children.toArray();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    public Object getParent(Object element) {
        FOXTreeNode treeNode = (FOXTreeNode) element;
        return treeNode.getParent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object element) {
        FOXTreeNode treeNode = (FOXTreeNode) element;
        return !treeNode.getChildren().isEmpty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement) {
        List list = (List) inputElement;
        return list.toArray();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     * java.lang.Object)
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
     */
    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     */
    public String getColumnText(Object element, int columnIndex) {
        FOXTreeNode treeNode = (FOXTreeNode) element;
        switch (columnIndex) {
        case 0:
            return treeNode.getLabelForViewer();
        case 1:
            return treeNode.getColumnLabel();
        case 2:
            if (treeNode instanceof Attribute) {
                return "-"; //$NON-NLS-1$
            } else if (treeNode.isGroup()) {
                return Messages.getString("FOXTargetTreeViewerProvider.1"); //$NON-NLS-1$
            } else if (treeNode.isLoop()) {
                return Messages.getString("FOXTargetTreeViewerProvider.2"); //$NON-NLS-1$
            } else if (TreeUtil.isSubLoopNode(treeNode)) {
                return Messages.getString("FOXTargetTreeViewerProvider.3"); //$NON-NLS-1$
            } else {
                return ""; //$NON-NLS-1$
            }
        default:
            return ""; //$NON-NLS-1$
        }
    }

}
