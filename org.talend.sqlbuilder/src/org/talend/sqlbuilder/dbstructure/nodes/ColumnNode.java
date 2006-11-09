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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.talend.core.ui.ImageProvider;
import org.talend.core.ui.ImageProvider.EImage;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;


/**
 * @author Davy Vanherbergen
 * 
 */
public class ColumnNode extends AbstractNode {

    private boolean pisForeignKey = false;

    private boolean pisPrimaryKey = false;

    private String plabelDecoration = null;

    private TableNode pparentTable;
    
    private boolean isSameToColumn = true;
    
    private boolean isFromRepository = false;

    private String sourceName;
    
   
    public ColumnNode(INode node, String name, SessionTreeNode session, TableNode parentTable, boolean showKeyLabels) throws Exception {

        pparent = node;
        pparentTable = parentTable;
        psessionNode = session;
        pname = name;
        pimageKey = "Images.ColumnNodeIcon";

        if (showKeyLabels) {
        if (pparentTable.getPrimaryKeyNames().contains(pname)) {
            pisPrimaryKey = true;
            pimageKey = "Images.PrimaryKeyIcon";
        }
        // this has been disabled for now.
        // foreign key determination turns out to be a real performance hog for oracle
//        if (_parentTable.getForeignKeyNames().contains(_name)) {
//            _isForeignKey = true;
//            if (_isPrimaryKey) {
//                _imageKey = "Images.PKForeignKeyIcon";
//            } else {
//                _imageKey = "Images.ForeignKeyIcon";
//            }
//        }
        }
    }
    
    /**
     * @return the source name (table name in db)
     */
    public String getSourceName() {
        return sourceName;
    }
    
    /**
     * @param sourceName sourceName.
     */
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    
    /**
     * @return isSameToColumn.
     */
    public boolean isSameToColumn() {
        return isSameToColumn;
    }
    
    /**
     * ss.
     * @param isSameToColumn2 isSameToColumn.
     */
    public void setSameToColumn(boolean isSameToColumn2) {
        this.isSameToColumn = isSameToColumn2;
    }
    
    /**
     * @return isFromRepository.
     */
    public boolean isFromRepository() {
        return isFromRepository;
    }
    
    /**
     * @param isFromRepository2 isFromRepository.
     */
    public void setFromRepository(boolean isFromRepository2) {
        this.isFromRepository = isFromRepository2;
    }
    
    /**
     * @return LabelDecoration.
     */
    public String getLabelDecoration() {

        return plabelDecoration;
    }
    
    /**
     * @return QualifiedParentTableName
     */
    public String getQualifiedParentTableName() {
        return pparentTable.getQualifiedName();
    }
    
    /**
     * @return QualifiedName
     */
    public String getQualifiedName() {

        return pparentTable.getName() + "." + pname;
    }

    /**
     * @return type.
     */
    public String getType() {

        return "column";
    }

    /**
     * @return UniqueIdentifier.
     */
    public String getUniqueIdentifier() {

        return pparentTable.getQualifiedName() + "." + pname;
    }
    
    /**
     * @return isEndNode.
     */
    public boolean isEndNode() {

        return true;
    }

    /**
     * @return isForeignKey.
     */
    public boolean isForeignKey() {

        return pisForeignKey;
    }

    /**
     * @return isPrimaryKey.
     */
    public boolean isPrimaryKey() {

        return pisPrimaryKey;
    }

    /**
     * LoadChildren.
     */
    public void loadChildren() {

    }

    /**
     * @param text labeldecoration
     */
    public void setLabelDecoration(String text) {

        plabelDecoration = text;
    }
    
    /**
     * @return Background.
     */
    @Override
    public Color getBackground() {
        if (getLabelAtColumn(0).equals(getSourceName()) && isSameToColumn) {
            return super.getBackground();
        } else if (getLabelAtColumn(1) == null || getLabelAtColumn(1).trim().equals("")) {
            return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
        } else {
            return Display.getDefault().getSystemColor(SWT.COLOR_RED);
        }
    }
    
    /**
     * @return Foreground.
     */
    @Override
    public Color getForeground() {
        if (getLabelAtColumn(0).equals(getSourceName()) && isSameToColumn) {
            return super.getBackground();
        } else {
            return Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
        }
    }
    
    /**
     * @return Image.
     */
    @Override
    public Image getImage() {
        if (!isSameToColumn) {
            return  ImageProvider.getImage(EImage.REFRESH_ICON);
        }
        return super.getImage();
    }
 
    /**
     * @return Parent Node.
     */
    @Override
    public INode getParent() {
        INode node = super.getParent();
        if (node instanceof ColumnFolderNode) {
            return node.getParent();
        }
        return node;
    }
}
