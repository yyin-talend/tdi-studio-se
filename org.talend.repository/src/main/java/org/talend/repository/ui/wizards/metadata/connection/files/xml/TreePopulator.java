// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
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
import org.talend.repository.i18n.Messages;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class TreePopulator {

    private Tree availableXmlTree;

    private BidiMap xPathToTreeItem = new DualHashBidiMap();

    private String filePath;

    public static final int MAXIMUM_ROWS_TO_PREVIEW = CorePlugin.getDefault().getPreferenceStore().getInt(
            ITalendCorePrefConstants.PREVIEW_LIMIT);

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
    public void populateTree(String filePath, ATreeNode treeNode) {
            availableXmlTree.removeAll();
        xPathToTreeItem.clear();
        if (filePath != null && !filePath.equals("")) {
            int numberOfElement = MAXIMUM_ROWS_TO_PREVIEW;
            try {
                treeNode = SchemaPopulationUtil.getSchemaTree(filePath, true, numberOfElement);
            } catch (MalformedURLException e) {
                ExceptionHandler.process(e);
            } catch (OdaException e) {
                ExceptionHandler.process(e);
            } catch (URISyntaxException e) {
                ExceptionHandler.process(e);
            }
            if (treeNode == null || treeNode.getChildren().length == 0) {
                OdaException ex = new OdaException(Messages.getString("dataset.error.populateXMLTree"));
                ExceptionHandler.process(ex);
            } else {
                Object[] childs = treeNode.getChildren();
                populateTreeItems(availableXmlTree, childs, 0, "");
                this.filePath = filePath;
            }
        }
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
                    treeItem.setText("@" + treeNode.getValue().toString());
                } else {
                    treeItem.setText(treeNode.getValue().toString());
                }

                String currentXPath = parentXPath + "/" + treeItem.getText();
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

}
