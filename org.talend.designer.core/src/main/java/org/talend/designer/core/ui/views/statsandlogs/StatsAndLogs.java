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
package org.talend.designer.core.ui.views.statsandlogs;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;

/**
 * ftang class global comment. Detailed comment <br/>
 * 
 */
public class StatsAndLogs {

    private static String currentTitle = ""; //$NON-NLS-1$

    private static String newTitle = ""; //$NON-NLS-1$

    public static void switchToCurStatsAndLogsView() {
        StatsAndLogsView statsAndLogsView = getView();
        if (statsAndLogsView == null) {
            return;
        }
        if (!newTitle.equals(currentTitle)) {
            statsAndLogsView.setPartName(newTitle);
            currentTitle = newTitle;
        }
        refreshView(statsAndLogsView);
    }

    /**
     * ftang Comment method "refreshView".
     */
    private static StatsAndLogsView getView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView(StatsAndLogsView.ID); //$NON-NLS-1$
        if (view == null) {
            try {
                view = page.showView(StatsAndLogsView.ID); //$NON-NLS-1$
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
            return null;
        }
        else if (view instanceof StatsAndLogsView) {
            return (StatsAndLogsView) view;
        }
        return null;

    }

    /**
     * ftang Comment method "refreshView".
     * 
     * @param view
     */
    private static void refreshView(StatsAndLogsView view) {
        if (view != null) {
            if (!newTitle.equals(currentTitle)) {
                view.setPartName(newTitle);
                currentTitle = newTitle;
            }
            view.refresh();
        }
    }
    
    /**
     * ftang Comment method "refreshView".
     * 
     * @param view
     */
    private static void emptyView(StatsAndLogsView view) {
        if (view != null) {
            if (!newTitle.equals(currentTitle)) {
                view.setPartName(newTitle);
                currentTitle = newTitle;
            }
            view.emptyView();
        }
    }

    public static void setTitle(String title) {
        newTitle = title;
    }

    /**
     * ftang Comment method "clearAll".
     */
    public static void clearAll() {
        StatsAndLogsView statsAndLogsView = getView();
        emptyView(statsAndLogsView);
    }

}
