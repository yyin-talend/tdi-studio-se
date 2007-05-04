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
package org.talend.designer.core.ui.views.contexts;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 */
public class Contexts {

    private static String currentTitle = ""; //$NON-NLS-1$

    private static String newTitle = ""; //$NON-NLS-1$

    public static void switchToCurContextsView() {
        ContextsView cxtView = getView();
        if (cxtView == null) {
            return;
        }
        if (!newTitle.equals(currentTitle)) {
            cxtView.setPartName(newTitle);
            currentTitle = newTitle;
        }
        refreshView(cxtView);
    }

    /**
     * qzhang Comment method "refreshView".
     */
    private static ContextsView getView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.talend.designer.core.ui.views.ContextsView"); //$NON-NLS-1$
        if (view instanceof ContextsView) {
            return (ContextsView) view;
        }
        return null;

    }

    /**
     * qzhang Comment method "refreshView".
     * 
     * @param view
     */
    private static void refreshView(ContextsView view) {
        if (view != null) {
            if (!newTitle.equals(currentTitle)) {
                view.setPartName(newTitle);
                currentTitle = newTitle;
            }
            view.refresh();
        }
    }

    public static void setTitle(String title) {
        newTitle = title;
    }

    /**
     * qzhang Comment method "clearAll".
     */
    public static void clearAll() {
        ContextsView cxtView = getView();
        refreshView(cxtView);
    }

}
