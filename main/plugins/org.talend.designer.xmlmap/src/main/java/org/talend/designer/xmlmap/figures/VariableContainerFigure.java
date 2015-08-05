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
package org.talend.designer.xmlmap.figures;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class VariableContainerFigure extends Figure {

    protected ImageFigure checkImage;

    protected VarNodeTextLabel variableLabel;

    private VarNode varNode;

    public VariableContainerFigure(VarNode varNode) {
        this.varNode = varNode;
        createContents();
    }

    /**
     * DOC Administrator Comment method "createContents".
     */
    protected void createContents() {
        ToolbarLayout manager = new ToolbarLayout() {

            @Override
            public void layout(IFigure parent) {
                super.layout(parent);
                List children = parent.getChildren();
                if (children.size() == 2) {
                    Figure checkImageFigure = (Figure) children.get(0);
                    Figure variableFigure = (Figure) children.get(1);
                    Rectangle varFigureBounds = variableFigure.getBounds().getCopy();
                    int avialableWith = parent.getBounds().width - checkImageFigure.getBounds().width;
                    if (avialableWith > varFigureBounds.width) {
                        varFigureBounds.width = avialableWith;
                    }
                    variableFigure.setBounds(varFigureBounds);
                }

            }
        };
        manager.setVertical(false);
        manager.setStretchMinorAxis(false);
        this.setLayoutManager(manager);
        // if(varNode.)
        Label nullableToolTip = new Label("Nullable");
        if (!varNode.isNullable()) {
            checkImage = new ImageFigure(ImageProvider.getImage(EImage.UNCHECKED_ICON));
        } else if (varNode.isNullable()) {
            checkImage = new ImageFigure(ImageProvider.getImage(EImage.CHECKED_ICON));
        }
        checkImage.setToolTip(nullableToolTip);
        variableLabel = new VarNodeTextLabel();
        variableLabel.setText(varNode.getName());
        variableLabel.setLabelAlignment(PositionConstants.LEFT);
        variableLabel.setBorder(new MarginBorder(2, 5, 2, -1));

        this.add(checkImage);
        this.add(variableLabel);
        addCheckListener();
    }

    private void addCheckListener() {
        checkImage.addMouseListener(new MouseListener() {

            public void mouseReleased(MouseEvent me) {

                checkImage.setBackgroundColor(ColorConstants.button);
            }

            public void mousePressed(MouseEvent me) {
                boolean isCheck = checkImage.getImage().equals(ImageProvider.getImage(EImage.CHECKED_ICON)) ? true : false;
                varNode.setNullable(!isCheck);
                // varNode.setNUllable();
                checkImage.setBackgroundColor(ColorConstants.buttonDarkest);

            }

            public void mouseDoubleClicked(MouseEvent me) {

            }
        });
    }

    public VarNodeTextLabel getVariableLabel() {
        return this.variableLabel;
    }

    public ImageFigure getCheckImage() {
        return this.checkImage;
    }

}
