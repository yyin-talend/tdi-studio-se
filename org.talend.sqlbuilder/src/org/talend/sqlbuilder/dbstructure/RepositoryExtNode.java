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
package org.talend.sqlbuilder.dbstructure;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.sqlbuilder.dbstructure.nodes.INode;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;
import org.talend.sqlbuilder.util.ImageUtil;

/**
 * Detailled comment for this class. <br/>
 * $Id: RepositoryExtNode.java,v 1.20 2006/11/03 10:40:12 peiqin.hou Exp $
 * @author Hou Peiqin (Soyatec)
 * 
 */
public class RepositoryExtNode implements INode {
    private RepositoryNode repositoryNode;
    
    public RepositoryExtNode() {
    }

    public RepositoryExtNode(RepositoryNode repositoryNode) {
        this.repositoryNode = repositoryNode;
    }

    /**
     * @param repositoryNode RepositoryNode.
     */
    public void setRepositoryNode(RepositoryNode repositoryNode) {
        this.repositoryNode = repositoryNode;
    }
    
    /**
     * @return RepositoryNode
     */
    public RepositoryNode getRepositoryNode() {
        return repositoryNode;
    }
    
    /**
     * @param composite composite.
     */
    public void fillDetailComposite(Composite composite) {
    }
    
    /**
     * @return Child Nodes.
     */
    public INode[] getChildNodes() {
        List<RepositoryNode> repositoryNodes = repositoryNode.getChildren();
        List<INode> nodes = new ArrayList<INode>();
        for (int i = 0, size = repositoryNodes.size(); i < size; i++) {
            RepositoryNode tempRepositoryNode = repositoryNodes.get(i);
            if (tempRepositoryNode.getObject().getType() != ERepositoryObjectType.METADATA_CONNECTIONS 
                    && !isEmptyFolder(tempRepositoryNode)) {
                RepositoryExtNode repositoryExtNode = new RepositoryExtNode();
                repositoryExtNode.setRepositoryNode(tempRepositoryNode);
                nodes.add(repositoryExtNode);
            } else {
                DatabaseModel databaseModel = ((DatabaseModel) convert2DatabaseModel(tempRepositoryNode));
                if (databaseModel == null || databaseModel.getRoot().getChildNodes().length == 0) {
                    continue;
                }
                nodes.add(databaseModel.getRoot().getChildNodes()[0]);
                
                //if the connection is a valid connection then add to CatalogNodes list.
                if (databaseModel.getSession().getInteractiveConnection() != null) {
                    SessionTreeNodeUtils.getCatelogNodes().add(databaseModel.getRoot().getChildNodes()[0]);
                }
                    
            }
        }
        
        INode[] iNodes = new INode[nodes.size()];
        for (int i = 0, size = nodes.size(); i < size; i++) {
            iNodes[i] = nodes.get(i);
        }
        
        return iNodes;
    }
    
    /**
     * Check if a repositoryNode is a empty folder.
     * 
     * method description.
     * @param repositoryNode2 RepositoryNode
     * @return isEmptyFolder
     * @exception
     */
    private boolean isEmptyFolder(RepositoryNode repositoryNode2) {
        boolean isEmptyFolder = true;
        if (repositoryNode2.hasChildren()) {
            List<RepositoryNode> list = repositoryNode2.getChildren();
            for (RepositoryNode node : list) {
                if (node.getObject().getType() == ERepositoryObjectType.METADATA_CONNECTIONS) {
                    isEmptyFolder = false;
                } else {
                    isEmptyFolder = isEmptyFolder(node);
                }
            }
        }
        return isEmptyFolder;
    }

    /**
     * Convert RepositoryNode ==> DatabaseModel
     * 
     * method description.
     * @param repositoryNode2  the node will be converted.
     * @return INode
     * @exception
     */
    private INode convert2DatabaseModel(RepositoryNode repositoryNode2) {
        if (repositoryNode2 == null || repositoryNode2.getObject() == null 
                || repositoryNode2.getObject().getProperty() == null || repositoryNode2.getObject().getProperty().getItem() == null) {
            return null;
        }
        DatabaseConnection connection = (DatabaseConnection) ((ConnectionItem) repositoryNode2.getObject()
                .getProperty().getItem()).getConnection();
        
        SessionTreeNode sessionTreeNode = SessionTreeNodeUtils.getSessionTreeNode(getRepositoryName(repositoryNode2)
                , connection.getDatabaseType(), connection.getURL(), connection.getUsername(), connection.getPassword(), connection.getSID(), repositoryNode2);
        return sessionTreeNode.getDbModel();
    }
    
    /**
     * Get RepsotiryNode name.
     * 
     * method description.
     * @param element -- RepositoryNode
     * @return the name of the RepositoryNode
     * @exception
     */
    public String getRepositoryName(RepositoryNode element) {
        RepositoryNode node = (RepositoryNode) element;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            IRepositoryObject object = node.getObject();

            StringBuffer string = new StringBuffer();
            string.append(object.getLabel());

            return string.toString();
        } else if (node.getType() == ENodeType.SIMPLE_FOLDER) {
            StringBuffer string = new StringBuffer(node.getProperties(EProperties.LABEL).toString());

            return string.toString();
        } else {
            return node.getProperties(EProperties.LABEL).toString();
        }
    
    }

    /**
     * @return ExpandedImage
     */
    public Image getExpandedImage() {
        return null;
    }

    /**
     * @return Image
     */
    public Image getImage()  {
        return ImageUtil.getImage("Images.closedFolder");
    }
    
    /**
     * EProperties.LABEL.
     * @return LabelDecoration
     */
    public String getLabelDecoration() {
        return repositoryNode.getProperties(EProperties.LABEL).toString();
    }
    
    /**
     * @return Label text.
     */
    public String getLabelText() {
        return repositoryNode.getProperties(EProperties.LABEL).toString();
    }
    
    /**
     * @return name.
     */
    public String getName() {
        return null;
    }
    
    /**
     * @return parent node.
     */
    public INode getParent() {
        if (repositoryNode.getParent() == null) {
            return null;
        }
        return new RepositoryExtNode(repositoryNode.getParent());
    }
    
    /**
     * @return QualifiedName
     */
    public String getQualifiedName() {
        return null;
    }
    
    /**
     * @return SchemaOrCatalogName.
     */
    public String getSchemaOrCatalogName() {
        return null;
    }
    
    /**
     * @return SessionTreeNode.
     */
    public SessionTreeNode getSession() {
        return null;
    }
    
    /**
     * @return Type.
     */
    public String getType() {
        return null;
    }
    
    /**
     * @return UniqueIdentifier.
     */
    public String getUniqueIdentifier() {
        return null;
    }
    
    /**
     * @return hasChildNodes.
     */
    public boolean hasChildNodes() {
        return repositoryNode.hasChildren();
    }
    
    /**
     * @param parent parent node.
     * @param name name
     * @param sessionNode SessionTreeNode
     */
    public void initialize(INode parent, String name,
            SessionTreeNode sessionNode) {
        
    }
    
    /**
     * @return isEndNode.
     */
    public boolean isEndNode() {
        return false;
    }
    
    /**
     * @return isExpanded.
     */
    public boolean isExpanded() {
        return false;
    }
    
    /**
     * refresh.
     */
    public void refresh() {

    }
    
    /**
     * @param expanded expanded. 
     */
    public void setExpanded(boolean expanded) {
    }

    /**
     * @param columnIndex columnIndex
     * @return label text.
     */
    public String getLabelAtColumn(int columnIndex) {
        if (columnIndex == 0) {
            return getLabelText();
        } else if (columnIndex == 1) {
            return null;
        }
        
        return null;
    }
    
    /**
     * @param columnIndex column index.
     * @return Image.
     */
    public Image getImageAtColumn(int columnIndex) {
        if (columnIndex == 0) {
            return getImage();
        } else if (columnIndex == 1) {
            return null;
        }
        return null;
    }
    
    /**
     * @return Background color.
     */
    public Color getBackground() {
        return Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
    }

    /**
     * @return Foreground color.
     */
    public Color getForeground() {
        return Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
    }
    
}
