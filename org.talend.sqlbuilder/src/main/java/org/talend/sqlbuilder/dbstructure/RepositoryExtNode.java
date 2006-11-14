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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.sqlbuilder.dbstructure.nodes.INode;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;
import org.talend.sqlbuilder.util.ConnectionParameters;
import org.talend.sqlbuilder.util.ImageUtil;

/**
 * Detailled comment for this class. <br/>
 * $Id: RepositoryExtNode.java,v 1.28 2006/11/09 02:59:09 peiqin.hou Exp $
 * @author Hou Peiqin (Soyatec)
 * 
 */
public class RepositoryExtNode implements INode {
    private RepositoryNode repositoryNode;
    private ConnectionParameters connectionParameters;
    private boolean isShowAllConnections = false;
    private INode[] childNodes;
    
    public RepositoryExtNode() {
    }
    
    
    public boolean isShowAllConnections() {
        return isShowAllConnections;
    }


    public void setShowAllConnections(boolean isShowAllConnections) {
        this.isShowAllConnections = isShowAllConnections;
    }



    public void setConnectionParameters(ConnectionParameters connectionParameters) {
        this.connectionParameters = connectionParameters;
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
    
    
    public void setChildNodes(INode[] childNodes) {
        this.childNodes = childNodes;
    }

    /**
     * @return Child Nodes.
     */
    public INode[] getChildNodes() {
        if(childNodes != null) {
            return childNodes;
        } else {
            return SessionTreeNodeUtils.getCurrentNodes(isShowAllConnections, repositoryNode, connectionParameters);
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
