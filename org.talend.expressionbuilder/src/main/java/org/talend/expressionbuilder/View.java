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
package org.talend.expressionbuilder;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.talend.expressionbuilder.ui.ExpressionBuilderDialog;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: View.java 上午10:05:45 2007-7-24 +0000 (2007-7-24) yzhang $
 * 
 */
public class View extends ViewPart {

    public static final String ID = "org.talend.expressionbuilder.view";

    private TableViewer viewer;

    /**
     * The content provider class is responsible for providing objects to the view. It can wrap existing objects in
     * adapters or simply return objects as-is. These objects may be sensitive to the current input of the view, or
     * ignore it and always show the same content (like Task List, for example).
     */
    class ViewContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        public void dispose() {
        }

        public Object[] getElements(Object parent) {
            return new String[] { "One", "Two", "Three" };
        }
    }

    /**
     * yzhang View class global comment. Detailled comment <br/>
     * 
     * $Id: View.java 上午10:05:55 2007-7-24 +0000 (2007-7-24) yzhang $
     * 
     */
    class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

        public String getColumnText(Object obj, int index) {
            return getText(obj);
        }

        public Image getColumnImage(Object obj, int index) {
            return getImage(obj);
        }

        public Image getImage(Object obj) {
            return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
        }
    }

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    public void createPartControl(Composite parent) {
        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        viewer.setContentProvider(new ViewContentProvider());
        viewer.setLabelProvider(new ViewLabelProvider());
        viewer.setInput(getViewSite());

        Action action = new Action() {

            public void run() {
                ExpressionBuilderDialog dialog = new ExpressionBuilderDialog(View.this.getViewSite().getShell());
                dialog.open();
                dialog.setBlockOnOpen(true);
            }
        };
        action.setText("expression");
        this.getViewSite().getActionBars().getToolBarManager().add(action);

    }

    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() {
        viewer.getControl().setFocus();
    }

}
