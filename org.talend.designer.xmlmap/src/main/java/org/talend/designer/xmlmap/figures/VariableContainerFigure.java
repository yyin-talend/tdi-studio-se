// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
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
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.designer.xmlmap.figures.layout.EqualWidthLayout;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class VariableContainerFigure extends ToolBarContainer {

    protected ToolBarButtonImageFigure checkImage;

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
        this.setLayoutManager(new EqualWidthLayout());
        // if(varNode.)
        Label nullableToolTip = new Label("Nullable");
        if (!varNode.isNullable()) {
            checkImage = new CheckButtonImageFigure(ImageProvider.getImage(EImage.UNCHECKED_ICON));
        } else if (varNode.isNullable()) {
            checkImage = new CheckButtonImageFigure(ImageProvider.getImage(EImage.CHECKED_ICON));
        }
        checkImage.setToolTip(nullableToolTip);
        variableLabel = new VarNodeTextLabel();
        variableLabel.setText(varNode.getVariable());
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

    public ToolBarButtonImageFigure getCheckImage() {
        return this.checkImage;
    }

}
