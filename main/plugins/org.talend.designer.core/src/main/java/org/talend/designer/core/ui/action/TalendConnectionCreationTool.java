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
package org.talend.designer.core.ui.action;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.ConnectionCreationTool;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;

/**
 * This tool is used to create a connection in the context menu. After the constructor, the function
 * "performConnectionStartWith" must be called, it will define the first point for the connection. <br/>
 * 
 * $Id: TalendConnectionCreationTool.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class TalendConnectionCreationTool extends ConnectionCreationTool {

    private boolean fromMenu;

    public TalendConnectionCreationTool(CreationFactory factory, boolean fromMenu) {
        super(factory);
        this.fromMenu = fromMenu;
        setUnloadWhenFinished(true);
    }

    /**
     * The node part of the source must be given in argument. This will define the fist point of the connection.
     * 
     * @param sourcePart the edit part that will be the source of the connection
     */
    public void performConnectionStartWith(EditPart sourcePart) {
        setConnectionSource(sourcePart);
        updateTargetRequest();
        Command cmd = ((AbstractEditPart) sourcePart).getCommand(getTargetRequest());
        setCurrentCommand(cmd);
        setState(STATE_CONNECTION_STARTED);
    }

    /*
     * override this method for issue 1253.
     * 
     * @see org.eclipse.gef.tools.AbstractConnectionCreationTool#handleCreateConnection()
     */
    @Override
    protected boolean handleCreateConnection() {
        Command endCommand = getCommand();
        if (endCommand != null) {
            return super.handleCreateConnection();
        }
        if (isInState(STATE_TERMINAL)) {
            // Fake a drag to cause feedback to be displayed immediately on mouse down.
            setState(STATE_CONNECTION_STARTED);
            handleDrag();
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.ConnectionCreationTool#handleButtonDown(int)
     */
    @Override
    protected boolean handleButtonDown(int button) {
        if (!fromMenu && button == 3) {
            return super.handleButtonDown(1);
        }
        return super.handleButtonDown(button);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractConnectionCreationTool#handleButtonUp(int)
     */
    @Override
    protected boolean handleButtonUp(int button) {
        if (!fromMenu && button == 3) {
            super.handleButtonDown(1);
        }
        return super.handleButtonUp(button);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractConnectionCreationTool#handleCommandStackChanged()
     */
    @Override
    protected boolean handleCommandStackChanged() {
        // TODO Auto-generated method stub
        return super.handleCommandStackChanged();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractConnectionCreationTool#handleDrag()
     */
    @Override
    protected boolean handleDrag() {
        // TODO Auto-generated method stub
        return super.handleDrag();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractConnectionCreationTool#handleDragInProgress()
     */
    @Override
    protected boolean handleDragInProgress() {
        // TODO Auto-generated method stub
        return super.handleDragInProgress();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractConnectionCreationTool#handleHover()
     */
    @Override
    protected boolean handleHover() {
        // TODO Auto-generated method stub
        return super.handleHover();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractConnectionCreationTool#handleInvalidInput()
     */
    @Override
    protected boolean handleInvalidInput() {
        // TODO Auto-generated method stub
        return super.handleInvalidInput();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractConnectionCreationTool#handleMove()
     */
    @Override
    protected boolean handleMove() {
        // TODO Auto-generated method stub
        return super.handleMove();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractConnectionCreationTool#handleSourceDeactivated()
     */
    @Override
    protected void handleSourceDeactivated() {
        // TODO Auto-generated method stub
        super.handleSourceDeactivated();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleDragStarted()
     */
    @Override
    protected boolean handleDragStarted() {
        // TODO Auto-generated method stub
        return super.handleDragStarted();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleFinished()
     */
    @Override
    protected void handleFinished() {
        // TODO Auto-generated method stub
        super.handleFinished();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleFocusGained()
     */
    @Override
    protected boolean handleFocusGained() {
        // TODO Auto-generated method stub
        return super.handleFocusGained();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleKeyTraversed(org.eclipse.swt.events.TraverseEvent)
     */
    @Override
    protected void handleKeyTraversed(TraverseEvent event) {
        // TODO Auto-generated method stub
        super.handleKeyTraversed(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleKeyUp(org.eclipse.swt.events.KeyEvent)
     */
    @Override
    protected boolean handleKeyUp(KeyEvent e) {
        // TODO Auto-generated method stub
        return super.handleKeyUp(e);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleNativeDragFinished(org.eclipse.swt.dnd.DragSourceEvent)
     */
    @Override
    protected boolean handleNativeDragFinished(DragSourceEvent event) {
        // TODO Auto-generated method stub
        return super.handleNativeDragFinished(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleNativeDragStarted(org.eclipse.swt.dnd.DragSourceEvent)
     */
    @Override
    protected boolean handleNativeDragStarted(DragSourceEvent event) {
        // TODO Auto-generated method stub
        return super.handleNativeDragStarted(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleViewerEntered()
     */
    @Override
    protected boolean handleViewerEntered() {
        // TODO Auto-generated method stub
        return super.handleViewerEntered();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractTool#mouseDoubleClick(org.eclipse.swt.events.MouseEvent,
     * org.eclipse.gef.EditPartViewer)
     */
    @Override
    public void mouseDoubleClick(MouseEvent me, EditPartViewer viewer) {
        // TODO Auto-generated method stub
        super.mouseDoubleClick(me, viewer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractTool#mouseDown(org.eclipse.swt.events.MouseEvent,
     * org.eclipse.gef.EditPartViewer)
     */
    @Override
    public void mouseDown(MouseEvent me, EditPartViewer viewer) {
        // TODO Auto-generated method stub
        super.mouseDown(me, viewer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractTool#mouseDrag(org.eclipse.swt.events.MouseEvent,
     * org.eclipse.gef.EditPartViewer)
     */
    @Override
    public void mouseDrag(MouseEvent me, EditPartViewer viewer) {
        // TODO Auto-generated method stub
        super.mouseDrag(me, viewer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractTool#mouseHover(org.eclipse.swt.events.MouseEvent,
     * org.eclipse.gef.EditPartViewer)
     */
    @Override
    public void mouseHover(MouseEvent me, EditPartViewer viewer) {
        // TODO Auto-generated method stub
        super.mouseHover(me, viewer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractTool#mouseMove(org.eclipse.swt.events.MouseEvent,
     * org.eclipse.gef.EditPartViewer)
     */
    @Override
    public void mouseMove(MouseEvent me, EditPartViewer viewer) {
        // TODO Auto-generated method stub
        super.mouseMove(me, viewer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractTool#mouseUp(org.eclipse.swt.events.MouseEvent,
     * org.eclipse.gef.EditPartViewer)
     */
    @Override
    public void mouseUp(MouseEvent me, EditPartViewer viewer) {
        // TODO Auto-generated method stub
        super.mouseUp(me, viewer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractTool#movedPastThreshold()
     */
    @Override
    protected boolean movedPastThreshold() {
        // TODO Auto-generated method stub
        return super.movedPastThreshold();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractTool#nativeDragFinished(org.eclipse.swt.dnd.DragSourceEvent,
     * org.eclipse.gef.EditPartViewer)
     */
    @Override
    public void nativeDragFinished(DragSourceEvent event, EditPartViewer viewer) {
        // TODO Auto-generated method stub
        super.nativeDragFinished(event, viewer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractTool#nativeDragStarted(org.eclipse.swt.dnd.DragSourceEvent,
     * org.eclipse.gef.EditPartViewer)
     */
    @Override
    public void nativeDragStarted(DragSourceEvent event, EditPartViewer viewer) {
        // TODO Auto-generated method stub
        super.nativeDragStarted(event, viewer);
    }

}
