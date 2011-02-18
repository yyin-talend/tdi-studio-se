// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.designer.xmlmap.image.ImageInfo;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class ButtonsImageToolBarFigure extends Figure {

    protected ImageFigure add, remove, move_up, move_down, miniSize;

    protected boolean newStateIsMinimized;

    private VarTable parentTable;

    private Image restorImage = ImageProvider.getImage(ImageInfo.RESTORE_ICON);

    private Image miniImage = ImageProvider.getImage(ImageInfo.MINIMIZE_ICON);

    private static Label minitooltip = new Label("Minimize");

    private static Label restoretooltip = new Label("Restore");

    public ButtonsImageToolBarFigure(VarTable parentTable) {
        this.parentTable = parentTable;
        newStateIsMinimized = parentTable.isMinimized();
        createToolbar();
    }

    /**
     * DOC hywang Comment method "createToolbar".
     */
    protected void createToolbar() {
        ToolbarLayout manager = new ToolbarLayout();
        manager.setVertical(false);
        this.setLayoutManager(manager);
        add = new AddButtonImageFigure(ImageProvider.getImage(EImage.ADD_ICON));
        remove = new RemoveButtonImageFigure(ImageProvider.getImage(EImage.MINUS_ICON));
        remove.setEnabled(false);
        move_up = new MoveUpButtonImageFigure(ImageProvider.getImage(EImage.UP_ICON));
        move_up.setEnabled(false);
        move_down = new MoveDownButtonImageFigure(ImageProvider.getImage(EImage.DOWN_ICON));
        move_down.setEnabled(false);
        if (newStateIsMinimized) {
            miniSize = new MinisizeButtonImageFigure(restorImage);
        } else if (!newStateIsMinimized) {
            miniSize = new MinisizeButtonImageFigure(miniImage);
        }
        setToolTips();
        this.add(add);
        this.add(remove);
        this.add(move_up);
        this.add(move_down);
        this.add(miniSize);
        addClickListeners();
    }

    /**
     * DOC Administrator Comment method "addClickListeners".
     */
    private void addClickListeners() {
        add.addMouseListener(new MouseListener() {

            public void mouseReleased(MouseEvent me) {
                add.setBackgroundColor(ColorConstants.button);
            }

            public void mousePressed(MouseEvent me) {
                add.setBackgroundColor(ColorConstants.buttonDarkest);
                VarNode newNode = XmlmapFactory.eINSTANCE.createVarNode();
                newNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
                newNode.setName(XmlMapUtil.findUniqueVarColumnName("Var", parentTable));
                parentTable.getNodes().add(newNode);
                parentTable.setMinimized(false);
                // VarTableContainerFigure varTableContainerFigure = ((CenterVarFigure)
                // ButtonsImageToolBarFigure.this.getParent()
                // .getParent()).getVarTableContainerFigure();
                // int tableContainerHeight = varTableContainerFigure.getPreferredSize().height;
                // for (IFigure current : (List<IFigure>) varTableContainerFigure.getChildren()) {
                // tableContainerHeight = tableContainerHeight + current.getPreferredSize().height;
                // }
                // varTableContainerFigure.setPreferredSize(width, tableContainerHeight);
                // varTableContainerFigure.validate();
                if (!remove.isEnabled()) {
                    remove.setEnabled(true);
                }
            }

            public void mouseDoubleClicked(MouseEvent me) {
                // TODO Auto-generated method stub

            }
        });

        remove.addMouseListener(new MouseListener() {

            public void mouseReleased(MouseEvent me) {
                remove.setBackgroundColor(ColorConstants.button);
            }

            public void mousePressed(MouseEvent me) {
                remove.setBackgroundColor(ColorConstants.buttonDarkest);
                CenterVarFigure centerVarFigure = (CenterVarFigure) remove.getParent().getParent().getParent();
                Figure columnsContainer = centerVarFigure.getVarTableContainerFigure().getColumnsContainer();
                for (VarNode nodeToDelete : centerVarFigure.getSelectionNodes()) {
                    XmlMapUtil.detachConnectionsSouce(nodeToDelete, (XmlMapData) parentTable.eContainer());
                    nodeToDelete.getIncomingConnections().clear();
                    XmlMapUtil.detachConnectionsTarget(nodeToDelete, (XmlMapData) parentTable.eContainer());
                    nodeToDelete.getOutgoingConnections().clear();
                    parentTable.getNodes().remove(nodeToDelete);
                }
                if (columnsContainer.getChildren().isEmpty()) {
                    remove.setEnabled(false);
                }
            }

            public void mouseDoubleClicked(MouseEvent me) {

            }
        });

        miniSize.addMouseListener(new MouseListener() {

            public void mousePressed(MouseEvent me) {
                boolean isMini = parentTable.isMinimized();
                if (isMini) {
                    parentTable.setMinimized(false);
                    // miniSize.setImage(miniImage);
                    // miniSize.setToolTip(minitooltip);
                }
                if (!isMini) {
                    parentTable.setMinimized(true);
                    // miniSize.setImage(restorImage);
                    // miniSize.setToolTip(restoretooltip);
                }
            }

            public void mouseReleased(MouseEvent me) {
            }

            public void mouseDoubleClicked(MouseEvent me) {

            }

        });

        move_up.addMouseListener(new MouseListener() {

            public void mouseReleased(MouseEvent me) {

            }

            public void mousePressed(MouseEvent me) {
                // VarNode selectionNode = (VarNode) me.getSource();
                // EList<VarNode> children = parentTable.getNodes();
                // int oldPosition = children.indexOf(children);
                // int oldPrevPosition = oldPosition - 1;
                // VarNode copiedNode = EcoreUtil.copy(selectionNode);
                // VarNode copiedPrevNode = EcoreUtil.copy(children.get(oldPrevPosition));
                // children.remove(oldPosition);
                // children.remove(oldPrevPosition);
                // children.add(oldPrevPosition, copiedNode);
                // children.add(oldPosition, copiedPrevNode);
            }

            public void mouseDoubleClicked(MouseEvent me) {

            }
        });

        move_down.addMouseListener(new MouseListener() {

            public void mouseReleased(MouseEvent me) {

            }

            public void mousePressed(MouseEvent me) {
                // VarNode selectionNode = (VarNode) me.getSource();
                // EList<VarNode> children = parentTable.getNodes();
                // int oldPosition = children.indexOf(children);
                // int oldPrevPosition = oldPosition - 1;
                // VarNode copiedNode = EcoreUtil.copy(selectionNode);
                // VarNode copiedPrevNode = EcoreUtil.copy(children.get(oldPrevPosition));
                // children.remove(oldPosition);
                // children.remove(oldPrevPosition);
                // children.add(oldPrevPosition, copiedNode);
                // children.add(oldPosition, copiedPrevNode);
            }

            public void mouseDoubleClicked(MouseEvent me) {

            }
        });
    }

    private void setToolTips() {
        Label tooltip = new Label();
        tooltip.setText("Add variable");
        add.setToolTip(tooltip);
        tooltip = new Label();
        tooltip.setText("Remove selected variable(s)");
        remove.setToolTip(tooltip);
        tooltip = new Label();
        tooltip.setText("Move up selected variable(s)");
        move_up.setToolTip(tooltip);
        tooltip = new Label();
        tooltip.setText("Move down selected variable(s)");
        move_down.setToolTip(tooltip);
        if (newStateIsMinimized) {
            miniSize.setToolTip(restoretooltip);
        } else if (!newStateIsMinimized) {
            miniSize.setToolTip(minitooltip);
        }
    }

    public ImageFigure getAdd() {
        return this.add;
    }

    public ImageFigure getRemove() {
        return this.remove;
    }

    public ImageFigure getMove_up() {
        return this.move_up;
    }

    public ImageFigure getMove_down() {
        return this.move_down;
    }

    public ImageFigure getMiniSize() {
        return this.miniSize;
    }

    public boolean isNewStateIsMinimized() {
        return this.newStateIsMinimized;
    }

    public VarTable getParentTable() {
        return this.parentTable;
    }

    public Image getRestorImage() {
        return this.restorImage;
    }

    public Image getMiniImage() {
        return this.miniImage;
    }

    public static Label getMinitooltip() {
        return minitooltip;
    }

    public static Label getRestoretooltip() {
        return restoretooltip;
    }

}
