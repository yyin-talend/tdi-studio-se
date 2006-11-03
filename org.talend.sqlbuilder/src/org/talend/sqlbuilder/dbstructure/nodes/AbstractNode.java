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
package org.talend.sqlbuilder.dbstructure.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;
import org.talend.sqlbuilder.util.ImageUtil;

/**
 * Abstract implementation of INode. Extend this class to create your own node
 * types.
 * 
 * @author Davy Vanherbergen
 */
public abstract class AbstractNode implements INode {

    protected ArrayList pchildren = new ArrayList();

    private boolean pchildrenLoaded = false;

    protected String pexpandedImageKey = null;

    protected Image pimage;

    protected String pimageKey = "Images.DefaultNodeImage";

    private boolean pisExpanded = false;

    protected String pname;

    protected INode pparent;

    protected SessionTreeNode psessionNode;

    protected String ptype;
    
    protected String prepositoryName;

    private static final Log LOGGER = LogFactory.getLog(AbstractNode.class);

    /**
     * DOC dev Comment method "addChildNode".
     * @param childNode ChildNode
     */
    @SuppressWarnings("unchecked")
    public final void addChildNode(INode childNode) {

        pchildren.add(childNode);
    }

    /**
     * getRepositoryName.
     * @param
     * @return the repository name.
     * @exception
     */
    public String getRepositoryName() {
        return prepositoryName;
    }

    /**
     * setRepositoryName.
     * @param repositoryName repositoryName.
     * @return
     * @exception
     */
    public void setRepositoryName(String repositoryName) {
        prepositoryName = repositoryName;
    }


    /**
     * @param composite Composite
     */
    public void fillDetailComposite(Composite composite) {
    }


    /**
     * Get an iterator to all child nodes. If child nodes haven't been loaded
     * yet, loading is triggered.
     * 
     * @return Iterator of child elements
     */
    public final Iterator getChildIterator() {

        if (!pchildrenLoaded) {
            load();
        }

        return pchildren.iterator();
    }


    /**
     * Get all the children of this node. If child nodes haven't been loaded
     * yet, loading is triggered.
     * 
     * @return All child nodes of this node.
     * @see net.sourceforge.sqlexplorer.db.INode#getChildren()
     */
    @SuppressWarnings("unchecked")
    public INode[] getChildNodes() {

        if (!pchildrenLoaded) {
            load();
            Comparator comp = getComparator();
            if (comp != null) {
                Collections.sort(pchildren, getComparator());
            }
        }

        if (pchildren.size() == 0) {
            return null;
        }

        return (INode[]) pchildren.toArray(new INode[] {});
    }


    /**
     * Override this method to implement custom sorting of child nodes.
     * @return Comparator.
     */
    public Comparator getComparator() {

        return new Comparator() {

            public int compare(Object arg0, Object arg1) {

                if (arg0 == null || arg1 == null) {
                    return 0;
                }
                String name0 = ((INode) arg0).getName();
                String name1 = ((INode) arg1).getName();

                if (name0 == null || name1 == null) {
                    return 0;
                }

                return name0.compareTo(name1);
            }

        };
    }


    /**
     * Override this method to change the image that is displayed for this node
     * in the database structure outline.
     * 
     * @return Image
     */
    public final Image getExpandedImage() {

        if (pexpandedImageKey == null) {
            return null;
        }
        return ImageUtil.getImage(pexpandedImageKey);
    }


    /**
     * Override this method to change the image that is displayed for this node
     * in the database structure outline.
     * 
     * @return Image
     */
    public Image getImage() {

        if (pimage != null) {
            return pimage;
        }
        if (pimageKey == null) {
            return pimage;
        }
        return ImageUtil.getImage(pimageKey);
    }

    /**
     * @return LabelDecoration.
     */
    public String getLabelDecoration() {

        return null;
    }


    /**
     * Override this method to change the text that is displayed in the database
     * structure outline for this node.
     * 
     * @return LabelText.
     */
    public String getLabelText() {

        return getName();
    }


    /**
     * @return simple name for this node.
     */
    public String getName() {

        if (pname == null) {
            return "<null>";
        }
        return pname;
    }


    /**
     * Get the parent of this node.
     * 
     * @return Parent node of this node.
     * @see net.sourceforge.sqlexplorer.db.INode#getParent()
     */
    public INode getParent() {

        return pparent;
    }


    /**
     * @return QualifiedName
     */
    public String getQualifiedName() {

        return getName();
    }

    /**
     * @return SchemaOrCatalogName
     */
    public String getSchemaOrCatalogName() {

        INode node = this;
        while (!(node.getType().equalsIgnoreCase("schema") || node.getType().equalsIgnoreCase("catalog"))) {
            node = node.getParent();
            if (node == null) {
                return null;
            }
        }
        return node.getName();
    }


    /**
     * @return SessionTreeNode for this node.
     */
    public final SessionTreeNode getSession() {

        return psessionNode;
    }

    /**
     * @return type
     */
    public String getType() {

        return ptype;
    }


    /**
     * Implement this method to return a unique identifier for this node. It is
     * used to identify the node in the detail cache.
     * 
     * @return UniqueIdentifier
     * @see org.talend.sqlbuilder.dbstructure.nodes.INode#getUniqueIdentifier()
     */
    public String getUniqueIdentifier() {

        return getParent().getQualifiedName() + "." + getQualifiedName();
    }


    /**
     * Checks if this node has children. If child nodes haven't been loaded yet,
     * this method always returns true. This defers the loading of metadata used
     * in the database structure outline until it is actually required.
     * 
     * @return true if this node has children.
     */
    public final boolean hasChildNodes() {

        if (!pchildrenLoaded && !isEndNode()) {
            return true;
        }

        if (pchildren == null || pchildren.size() == 0) {
            return false;
        }

        return true;
    }


    /**
     * Initialize this node.
     * 
     * @param parent the parent INode of this node.
     * @param name the name of this node.
     * @param sessionNode the session this node belongs too.
     */
    public void initialize(INode parent, String name, SessionTreeNode sessionNode) {

        pparent = parent;
        pname = name;
        psessionNode = sessionNode;
    }


    /**
     * Returns true. Override this method to return false if your node cannot
     * have any children. This will avoid the twistie being displayed in the
     * database structure outline for nodes that cannot have children.
     * 
     * @return isEndNode
     * @see org.talend.sqlbuilder.dbstructure.nodes.INode#isEndNode()
     */
    public boolean isEndNode() {

        return false;
    }

    /**
     * @return isExpanded.
     */
    public boolean isExpanded() {

        return pisExpanded;
    }


    /**
     * Loads all the children for this node if they haven't been loaded yet.
     */
    public final void load() {

        if (!pchildrenLoaded) {

            try {

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Loading child nodes for " + pname);
                }

                loadChildren();
                pchildrenLoaded = true;

            } catch (AbstractMethodError e) {

                SqlBuilderPlugin.log("Could not load child nodes for " + pname, e);

            } catch (Throwable e) {

                SqlBuilderPlugin.log("Could not load child nodes for " + pname, e);

            }
        }
    }


    /**
     * Load all the children of this node here. Do not call this method, but use
     * load() instead.
     */
    public abstract void loadChildren();


    /**
     * Refresh. This will clear the nodes' children and reload them. It will
     * also update the dictionary for this node & descendants
     */
    public final void refresh() {

        pchildren.clear();
        pchildrenLoaded = false;
        load();

    }

    /**
     * @param expanded isExpanded
     */
    public final void setExpanded(boolean expanded) {

        pisExpanded = expanded;
    }

    /**
     * @param image image 
     */
    public void setImage(Image image) {

        pimage = image;
    }


    /**
     * Set parent node for this node.
     * 
     * @param parent parent.
     */
    public final void setParent(INode parent) {

        pparent = parent;
    }


    /**
     * Set sessiontreenode for this node.
     * 
     * @param session SessionTreeNode.
     */
    public final void setSession(SessionTreeNode session) {

        psessionNode = session;
    }

    /**
     * set the type.
     * 
     * @param type type.
     */
    public void setType(String type) {

        ptype = type;
    }

    /**
     * override the toString().
     * @return String.
     */
    public String toString() {

        return getName();
    }

    
    /**
     * Default label text is "".
     * 
     * @param columnIndex column index.
     * @return the column label.
     */
    public String getLabelAtColumn(int columnIndex) {
        if (columnIndex == 0) {
            return getLabelText();
        } else if (columnIndex == 1) {
            return getRepositoryName();
        }
        return null;
    }
    
    /**
     * clear all the child nodes.
     */
    public void clearChildren() {
        pchildren.clear();
    }

    /**
     * Get the image of the column.
     * @param columnIndex columnIndex.
     * @return Image.
     */
    public Image getImageAtColumn(int columnIndex) {
        if (columnIndex == 0) {
            return getImage();
        }
        return null;
    }

    /**
     * @return Background color.
     */
    public Color getBackground() {
        return null;
    }

    /**
     * @return Foreground color.
     */
    public Color getForeground() {
        return null;
    }


    
}
