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
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.enablement.oda.xml.util.ui.ATreeNode;
import org.eclipse.datatools.enablement.oda.xml.util.ui.SchemaPopulationUtil;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.prefs.ITalendCorePrefConstants;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class TreePopulator {

    private final Tree availableXmlTree;

    private final BidiMap xPathToTreeItem = new DualHashBidiMap();

    private String filePath;

    private static int limit;

    /**
     * DOC amaumont TreePopulator constructor comment.
     * 
     * @param availableXmlTree
     */
    public TreePopulator(Tree availableXmlTree) {
        super();
        this.availableXmlTree = availableXmlTree;
    }

    /**
     * populate xml tree.
     * 
     */
    public boolean populateTree(String filePath, ATreeNode treeNode) {
        availableXmlTree.removeAll();
        xPathToTreeItem.clear();
        if (filePath != null && !filePath.equals("")) { //$NON-NLS-1$
            try {
                treeNode = SchemaPopulationUtil.getSchemaTree(filePath, true, limit);
            } catch (MalformedURLException e) {
                ExceptionHandler.process(e);
            } catch (OdaException e) {
                ExceptionHandler.process(e);
            } catch (URISyntaxException e) {
                ExceptionHandler.process(e);
            } catch (Exception e) {
                return false;
            }
            if (treeNode == null || treeNode.getChildren().length == 0) {
                // OdaException ex = new OdaException(Messages.getString("dataset.error.populateXMLTree"));
                // //$NON-NLS-1$
                // ExceptionHandler.process(ex);
                return false;
            } else {
                Object[] childs = treeNode.getChildren();
                populateTreeItems(availableXmlTree, childs, 0, ""); //$NON-NLS-1$
                this.filePath = filePath;
                return true;
            }
        }
        return false;
    }

    /**
     * populate tree items.
     * 
     * @param tree
     * @param node
     */
    private void populateTreeItems(Object tree, Object[] node, int level, String parentXPath) {
        level++;
        if (level > 10) {
            return;
        } else {
            for (int i = 0; i < node.length; i++) {
                TreeItem treeItem;
                if (tree instanceof Tree) {
                    treeItem = new TreeItem((Tree) tree, 0);
                } else {
                    treeItem = new TreeItem((TreeItem) tree, 0);
                }

                ATreeNode treeNode = (ATreeNode) node[i];
                treeItem.setData(treeNode);
                int type = treeNode.getType();
                if (type == ATreeNode.ATTRIBUTE_TYPE) {
                    treeItem.setText("@" + treeNode.getValue().toString()); //$NON-NLS-1$
                } else {
                    treeItem.setText(treeNode.getValue().toString());
                }

                String currentXPath = parentXPath + "/" + treeItem.getText(); //$NON-NLS-1$
                xPathToTreeItem.put(currentXPath, treeItem);

                if (treeNode.getChildren() != null && treeNode.getChildren().length > 0) {
                    populateTreeItems(treeItem, treeNode.getChildren(), level, currentXPath);
                }
                setExpanded(treeItem);
            }
        }
    }

    // expand the tree
    private void setExpanded(TreeItem treeItem) {
        if (treeItem.getParentItem() != null) {
            setExpanded(treeItem.getParentItem());
        }
        treeItem.setExpanded(true);
    }

    public TreeItem getTreeItem(String absoluteXPath) {
        return (TreeItem) xPathToTreeItem.get(absoluteXPath);
    }

    public String getAbsoluteXPath(TreeItem treeItem) {
        return (String) xPathToTreeItem.getKey(treeItem);
    }

    /**
     * Getter for filePath.
     * 
     * @return the filePath
     */
    public String getFilePath() {
        return this.filePath;
    }

    public static int getMaximumRowsToPreview() {
        return CorePlugin.getDefault().getPreferenceStore().getInt(ITalendCorePrefConstants.PREVIEW_LIMIT);
    }

    /**
     * Sets the limit.
     * 
     * @param limit the limit to set
     */
    public void setLimit(int lit) {
        limit = lit;
    }

    /**
     * Getter for limit.
     * 
     * @return the limit
     */
    public static int getLimit() {
        return limit;
    }

}
