// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.designer.xmlmap.image.ImageInfo;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class ButtonsImageToolBarFigure extends Figure {

    protected ImageFigure add, remove, move_up, move_down, miniSize;

    protected boolean newStateIsMinimized = true;

    private VarTable parentTable;

    public ButtonsImageToolBarFigure(VarTable parentTable) {
        this.parentTable = parentTable;
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
            miniSize = new MinisizeButtonImageFigure(ImageProvider.getImage(ImageInfo.RESTORE_ICON));
        } else if (!newStateIsMinimized) {
            miniSize = new MinisizeButtonImageFigure(ImageProvider.getImage(ImageInfo.MINIMIZE_ICON));
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
                parentTable.getNodes().add(newNode);
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
                    parentTable.getNodes().remove(nodeToDelete);
                }
                if (columnsContainer.getChildren().isEmpty()) {
                    remove.setEnabled(false);
                }
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
        tooltip = new Label();
        if (newStateIsMinimized) {
            tooltip.setText("Restore");
            miniSize.setToolTip(tooltip);
        } else if (!newStateIsMinimized) {
            tooltip.setText("Minimize");
            miniSize.setToolTip(tooltip);
        }
    }

}
