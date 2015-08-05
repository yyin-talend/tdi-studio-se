// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.figures.treetools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.designer.xmlmap.figures.layout.TreeToolBarLayout;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.VarNodeEditPart;
import org.talend.designer.xmlmap.parts.VarTableEditPart;
import org.talend.designer.xmlmap.ui.resource.ImageInfo;
import org.talend.designer.xmlmap.ui.resource.ImageProviderMapper;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class VarToolBarFigure extends Figure {

    protected ToolBarButtonImageFigure add, remove, move_up, move_down, miniSize;

    protected boolean newStateIsMinimized;

    private VarTableEditPart tablePart;

    private VarTable parentTable;

    private Image restorImage = ImageProviderMapper.getImage(ImageInfo.RESTORE_ICON);

    private Image miniImage = ImageProviderMapper.getImage(ImageInfo.MINIMIZE_ICON);

    private static Label minitooltip = new Label("Minimize");

    private static Label restoretooltip = new Label("Restore");

    public VarToolBarFigure(VarTableEditPart tablePart) {
        this.tablePart = tablePart;
        this.parentTable = (VarTable) tablePart.getModel();
        newStateIsMinimized = parentTable.isMinimized();
        createToolbar();
    }

    /**
     * DOC hywang Comment method "createToolbar".
     */
    protected void createToolbar() {
        TreeToolBarLayout manager = new TreeToolBarLayout();
        manager.setVertical(false);
        this.setLayoutManager(manager);
        add = new AddButton();
        remove = new RemoveButton();
        remove.setEnabled(false);
        move_up = new MoveUpButton();
        move_up.setEnabled(false);
        move_down = new MoveDownButton();
        move_down.setEnabled(false);
        if (newStateIsMinimized) {
            miniSize = new MinSizeButton(restorImage);
        } else if (!newStateIsMinimized) {
            miniSize = new MinSizeButton(miniImage);
        }
        setToolTips();
        this.add(add);
        this.add(remove);
        this.add(move_up);
        this.add(move_down);
        this.add(miniSize);
    }

    class AddButton extends ToolBarButtonImageFigure {

        public AddButton() {
            super(ImageProvider.getImage(EImage.ADD_ICON));
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            if (tablePart != null) {
                CommandStack commandStack = tablePart.getViewer().getEditDomain().getCommandStack();
                commandStack.execute(new Command() {

                    @Override
                    public void execute() {
                        VarNode newNode = XmlmapFactory.eINSTANCE.createVarNode();
                        newNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
                        newNode.setName(XmlMapUtil.findUniqueVarColumnName("Var", parentTable));
                        parentTable.getNodes().add(newNode);
                        parentTable.setMinimized(false);

                        EditPart toSelect = null;

                        int index = parentTable.getNodes().indexOf(newNode);
                        if (index < tablePart.getChildren().size()) {
                            toSelect = (EditPart) tablePart.getChildren().get(index);
                            tablePart.getViewer().select(toSelect);
                        }

                        if (!remove.isEnabled()) {
                            remove.setEnabled(true);
                        }

                    }
                });
            }
        }
    }

    class RemoveButton extends ToolBarButtonImageFigure {

        public RemoveButton() {
            super(ImageProvider.getImage(EImage.MINUS_ICON));
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            if (tablePart != null) {
                CommandStack commandStack = tablePart.getViewer().getEditDomain().getCommandStack();
                commandStack.execute(new Command() {

                    @Override
                    public void execute() {
                        List selectedEditParts = tablePart.getViewer().getSelectedEditParts();
                        final List<VarNode> toRemove = new ArrayList<VarNode>();

                        int minIndex = parentTable.getNodes().size() - 1;
                        for (Object obj : selectedEditParts) {
                            if (obj instanceof VarNodeEditPart) {
                                VarNode model = (VarNode) ((VarNodeEditPart) obj).getModel();
                                toRemove.add(model);
                                XmlMapUtil.detachNodeConnections(model, (XmlMapData) parentTable.eContainer(), true);
                                int index = parentTable.getNodes().indexOf(model);
                                if (index < minIndex) {
                                    minIndex = index;
                                }
                            }
                        }
                        parentTable.getNodes().removeAll(toRemove);

                        if (!tablePart.getChildren().isEmpty()) {
                            if (minIndex > tablePart.getChildren().size() - 1) {
                                minIndex = tablePart.getChildren().size() - 1;
                            }
                            tablePart.getViewer().select((EditPart) tablePart.getChildren().get(minIndex));
                        } else {
                            remove.setEnabled(false);
                        }

                    }
                });

            }
        }
    }

    class MoveUpButton extends ToolBarButtonImageFigure {

        public MoveUpButton() {
            super(ImageProvider.getImage(EImage.UP_ICON));
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);

            CommandStack commandStack = tablePart.getViewer().getEditDomain().getCommandStack();
            commandStack.execute(new Command() {

                @Override
                public void execute() {
                    List selectedEditParts = tablePart.getViewer().getSelectedEditParts();
                    List<Integer> indexToMove = new ArrayList<Integer>();
                    EList<VarNode> nodes = parentTable.getNodes();
                    for (int i = 0; i < selectedEditParts.size(); i++) {
                        Object obj = selectedEditParts.get(i);
                        if (obj instanceof VarNodeEditPart) {
                            VarNode node = (VarNode) ((VarNodeEditPart) obj).getModel();
                            int indexOf = nodes.indexOf(node);
                            if (indexOf != -1 && indexOf > 0) {
                                indexToMove.add(indexOf);
                            }
                        }
                    }

                    Collections.sort(indexToMove);

                    for (int i = 0; i < indexToMove.size(); i++) {
                        int index = indexToMove.get(i);
                        VarNode temp = nodes.get(index);
                        nodes.remove(temp);
                        nodes.add(index - 1, temp);
                    }

                    for (int i = 0; i < indexToMove.size(); i++) {
                        EditPart part = (EditPart) tablePart.getChildren().get(indexToMove.get(i) - 1);
                        tablePart.getViewer().appendSelection(part);
                    }

                }
            });

        }
    }

    class MoveDownButton extends ToolBarButtonImageFigure {

        public MoveDownButton() {
            super(ImageProvider.getImage(EImage.DOWN_ICON));
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);

            CommandStack commandStack = tablePart.getViewer().getEditDomain().getCommandStack();
            commandStack.execute(new Command() {

                @Override
                public void execute() {
                    List selectedEditParts = tablePart.getViewer().getSelectedEditParts();
                    List<Integer> indexToMove = new ArrayList<Integer>();
                    EList<VarNode> nodes = parentTable.getNodes();
                    for (int i = 0; i < selectedEditParts.size(); i++) {
                        Object obj = selectedEditParts.get(i);
                        if (obj instanceof VarNodeEditPart) {
                            VarNode node = (VarNode) ((VarNodeEditPart) obj).getModel();
                            int indexOf = nodes.indexOf(node);
                            if (indexOf != -1 && indexOf < nodes.size() - 1) {
                                indexToMove.add(indexOf);
                            }
                        }
                    }
                    Collections.sort(indexToMove);
                    Collections.reverse(indexToMove);

                    for (int i = 0; i < indexToMove.size(); i++) {
                        int index = indexToMove.get(i);
                        VarNode temp = nodes.get(index);
                        nodes.remove(temp);
                        nodes.add(index + 1, temp);
                    }

                    for (int i = 0; i < indexToMove.size(); i++) {
                        EditPart part = (EditPart) tablePart.getChildren().get(indexToMove.get(i) + 1);
                        tablePart.getViewer().appendSelection(part);
                    }
                }
            });

        }
    }

    class MinSizeButton extends ToolBarButtonImageFigure {

        public MinSizeButton(Image image) {
            super(image);
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            CommandStack commandStack = tablePart.getViewer().getEditDomain().getCommandStack();
            commandStack.execute(new Command() {

                @Override
                public void execute() {

                    parentTable.setMinimized(!parentTable.isMinimized());
                    if (parentTable.isMinimized()) {
                        miniSize.setImage(restorImage);
                    } else {
                        miniSize.setImage(miniImage);
                    }
                    tablePart.getViewer().deselectAll();
                }
            });

        }
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

    public void setRemoveEnable(boolean value) {
        this.remove.setEnabled(value);
    }

    public void setMoveUpEnable(boolean value) {
        this.move_up.setEnabled(value);
    }

    public void setMoveDownEnable(boolean value) {
        this.move_down.setEnabled(value);
    }

}
