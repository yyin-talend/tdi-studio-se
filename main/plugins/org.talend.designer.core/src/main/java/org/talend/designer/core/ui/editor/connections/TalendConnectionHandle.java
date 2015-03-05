// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.editor.connections;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.handles.SquareHandle;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.process.INodeConnector;
import org.talend.designer.core.ui.action.TalendCreateConnectionTool;
import org.talend.designer.core.ui.editor.nodes.NodePart;

/**
 * DOC Talend class global comment. Detailled comment
 */
public class TalendConnectionHandle extends SquareHandle implements PropertyChangeListener {

    private NodePart nodePart;

    public TalendConnectionHandle(NodePart nodePart) {
        this.nodePart = nodePart;
        this.setOwner(nodePart);
        setLayoutManager(new StackLayout());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.handles.AbstractHandle#createDragTracker()
     */
    @Override
    protected DragTracker createDragTracker() {

        final INodeConnector mainConnector = new NodeConnectorTool(nodePart).getConnector();
        if (mainConnector == null) {
            return null;
        }

        final List<Object> listArgs = new ArrayList<Object>();
        listArgs.add(null);
        listArgs.add(null);
        listArgs.add(null);
        TalendCreateConnectionTool myConnectTool = new TalendCreateConnectionTool(new CreationFactory() {

            @Override
            public Object getNewObject() {
                return listArgs;
            }

            @Override
            public Object getObjectType() {
                return mainConnector.getName();
            }
        }, nodePart);

        return myConnectTool;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.handles.SquareHandle#init()
     */
    @Override
    protected void init() {
        // TODO Auto-generated method stub
        super.init();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.handles.SquareHandle#paintFigure(org.eclipse.draw2d.Graphics)
     */
    @Override
    public void paintFigure(Graphics g) {
        // TODO Auto-generated method stub
        super.paintFigure(g);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.handles.AbstractHandle#ancestorAdded(org.eclipse.draw2d.IFigure)
     */
    @Override
    public void ancestorAdded(IFigure ancestor) {
        // TODO Auto-generated method stub
        super.ancestorAdded(ancestor);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.handles.AbstractHandle#validate()
     */
    @Override
    public void validate() {
        if (isValid()) {
            return;
        }

        removeAll();
        Image image = ImageProvider.getImage(ECoreImage.UNKNOWN);

        ImageFigure imageFigure = new ImageFigure(image);
        imageFigure.setSize(image.getBounds().width, image.getBounds().height);

        add(imageFigure);
        setSize(imageFigure.getSize());
        super.validate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.handles.AbstractHandle#setLocator(org.eclipse.draw2d.Locator)
     */
    @Override
    public void setLocator(Locator locator) {
        super.setLocator(locator);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.handles.ConnectionHandle#addNotify()
     */
    @Override
    public void addNotify() {
        super.addNotify();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.Figure#findFigureAt(int, int, org.eclipse.draw2d.TreeSearch)
     */
    @Override
    public IFigure findFigureAt(int x, int y, TreeSearch search) {
        // return the ConnectionHandle and not the children figures
        if (containsPoint(x, y)) {
            return this;
        }
        return super.findFigureAt(x, y, search);
    }

}
