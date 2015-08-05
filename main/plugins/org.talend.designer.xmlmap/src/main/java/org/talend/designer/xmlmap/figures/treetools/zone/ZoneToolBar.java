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
package org.talend.designer.xmlmap.figures.treetools.zone;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.designer.xmlmap.XmlMapComponent;
import org.talend.designer.xmlmap.editor.XmlMapGraphicViewer;
import org.talend.designer.xmlmap.figures.treetools.ToolBarButtonImageFigure;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.parts.XmlMapDataEditPart;
import org.talend.designer.xmlmap.ui.resource.ImageInfo;
import org.talend.designer.xmlmap.ui.resource.ImageProviderMapper;

/**
 * DOC talend class global comment. Detailled comment
 */
public abstract class ZoneToolBar extends Figure {

    protected ToolBarButtonImageFigure move_up, move_down, min_size;

    protected XmlMapData mapData;

    protected XmlMapDataEditPart mapDataPart;

    protected XmlMapGraphicViewer graphicViewer;

    protected boolean minimized = true;

    protected Image restorImage = ImageProviderMapper.getImage(ImageInfo.RESTORE_ICON);

    protected Image miniImage = ImageProviderMapper.getImage(ImageInfo.MINIMIZE_ICON);

    protected XmlMapComponent mapperComponent;

    public ZoneToolBar(XmlMapDataEditPart mapDataPart) {
        this.mapDataPart = mapDataPart;
        this.mapData = (XmlMapData) mapDataPart.getModel();
        if (mapDataPart.getViewer() instanceof XmlMapGraphicViewer) {
            this.graphicViewer = (XmlMapGraphicViewer) mapDataPart.getViewer();
            mapperComponent = this.graphicViewer.getMapperManager().getMapperComponent();
        }

    }

    public abstract void moveUp();

    public abstract void moveDown();

    public abstract void minSize();

    class MoveUpButton extends ToolBarButtonImageFigure {

        public MoveUpButton() {
            super(ImageProvider.getImage(EImage.UP_ICON));
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            if (graphicViewer != null) {
                CommandStack commandStack = graphicViewer.getEditDomain().getCommandStack();
                commandStack.execute(new Command() {

                    @Override
                    public void execute() {
                        moveUp();
                    }
                });
            }

        }
    }

    class MoveDownButton extends ToolBarButtonImageFigure {

        public MoveDownButton() {
            super(ImageProvider.getImage(EImage.DOWN_ICON));
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            if (graphicViewer != null) {

                CommandStack commandStack = graphicViewer.getEditDomain().getCommandStack();
                commandStack.execute(new Command() {

                    @Override
                    public void execute() {
                        moveDown();
                    }
                });

            }
        }
    }

    class MinSizeButton extends ToolBarButtonImageFigure {

        public MinSizeButton(Image image) {
            super(image);
        }

        @Override
        public void toolBarButtonPressed(MouseEvent me) {
            super.toolBarButtonPressed(me);
            CommandStack commandStack = mapDataPart.getViewer().getEditDomain().getCommandStack();
            commandStack.execute(new Command() {

                @Override
                public void execute() {
                    minSize();
                }
            });

            if (minimized) {
                setImage(restorImage);
            } else {
                setImage(miniImage);
            }
            mapDataPart.getViewer().deselectAll();
        }
    }

}
