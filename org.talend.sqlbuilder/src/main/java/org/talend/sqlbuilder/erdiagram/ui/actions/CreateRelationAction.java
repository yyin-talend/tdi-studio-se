// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.sqlbuilder.erdiagram.ui.actions;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.sqlbuilder.erdiagram.ui.editor.ErdiagramDiagramEditor;
import org.talend.sqlbuilder.erdiagram.ui.nodes.Column;
import org.talend.sqlbuilder.erdiagram.ui.parts.ColumnPart;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class CreateRelationAction extends SelectionAction {

    private ColumnPart columnPart;

    private IWorkbenchPart part;

    private static final String NEW_RELATION = "Equal"; 

    public static final String ID = "org.talend.sqlbuider.erdiagram.ui.actions.CreateRelationAction"; //$NON-NLS-1$

    public CreateRelationAction(IWorkbenchPart part) {
        super(part);
        this.part = part;
        setText(NEW_RELATION);
    }

    @Override
    protected boolean calculateEnabled() {
        if (getSelectedObjects().isEmpty()) {
            return false;
        }
        List parts = getSelectedObjects();
        if (parts.size() == 1) {
            Object o = parts.get(0);
            if (!(o instanceof ColumnPart)) {
                return false;
            }
            columnPart = (ColumnPart) o;
            if (!columnPart.isActive()) {
                return false;
            }
            Column col = (Column) columnPart.getModel();
            if (col.getElementName().equals("*")) { //$NON-NLS-1$
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        if (getSelectedObjects().isEmpty()) {
            return;
        }

        List parts = getSelectedObjects();
        if (parts.size() == 1) {
            Object o = parts.get(0);
            if (!(o instanceof ColumnPart)) {
                return;
            }
            columnPart = (ColumnPart) o;
        } else {
            return;
        }
        /**
         * Create a mouse down event that thinks it is over the blob and dispatch it. This is a bit of a fudge to mimic
         * what the user ought to do.
         */
        Point point = null;
        point = columnPart.getFigure().getClientArea().getCenter();
        Point p = point;
        columnPart.getFigure().translateToAbsolute(p);

        Canvas canvas = (Canvas) columnPart.getViewer().getControl();
        Event event = new Event();
        event.button = 1;
        event.count = 0;
        event.detail = 0;
        event.end = 0;
        event.height = 0;
        event.keyCode = 0;
        event.start = 0;
        event.stateMask = 0;
        event.time = 9516624; // any old time... doesn't matter
        event.type = 3;
        event.widget = canvas;
        event.width = 0;
        event.x = p.x + 3;
        event.y = p.y + 3;
        WhereConnectionCreationTool tool = new WhereConnectionCreationTool(new CreationFactory() {

            public Object getNewObject() {
                return null;
            }

            public Object getObjectType() {
                return null;
            }

        });

        columnPart.getViewer().getEditDomain().setActiveTool(tool);
        canvas.notifyListeners(3, event);

    }

    @Override
    public void update() {
        setSelection(((ErdiagramDiagramEditor) part).getViewer().getSelection());
    }
}
