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
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.ToolbarLayout;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class VariableContainerFigure extends Figure {

    protected ToolBarButtonImageFigure checkImage;

    protected Label nameLabel;

    private VarNode varNode;

    public VariableContainerFigure(VarNode varNode) {
        this.varNode = varNode;
        createContents();
    }

    /**
     * DOC Administrator Comment method "createContents".
     */
    protected void createContents() {
        this.setLayoutManager(new ToolbarLayout(true));
        // if(varNode.)
        checkImage = new CheckButtonImageFigure(ImageProvider.getImage(EImage.UNCHECKED_ICON));
        Label nullableToolTip = new Label("Nullable");
        checkImage.setToolTip(nullableToolTip);
        nameLabel = new Label();
        nameLabel.setText(varNode.getName());
        this.add(checkImage);
        this.add(nameLabel);
        addCheckListener();
    }

    private void addCheckListener() {
        checkImage.addMouseListener(new MouseListener() {

            public void mouseReleased(MouseEvent me) {

                checkImage.setBackgroundColor(ColorConstants.button);
            }

            public void mousePressed(MouseEvent me) {
                boolean isCheck = checkImage.getImage().equals(ImageProvider.getImage(EImage.CHECKED_ICON)) ? true : false;
                if (isCheck) {
                    checkImage.setImage(ImageProvider.getImage(EImage.UNCHECKED_ICON));

                } else {
                    checkImage.setImage(ImageProvider.getImage(EImage.CHECKED_ICON));
                }
                // varNode.setNUllable();
                checkImage.setBackgroundColor(ColorConstants.buttonDarkest);

            }

            public void mouseDoubleClicked(MouseEvent me) {

            }
        });
    }
}
