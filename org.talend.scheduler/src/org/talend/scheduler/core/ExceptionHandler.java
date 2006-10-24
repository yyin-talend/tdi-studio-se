// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
package org.talend.scheduler.core;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.talend.scheduler.SchedulerPlugin;

/**
 * Detailled comment for this class. <br/> $Id$
 * 
 * @author Hou Peiqin (Soyatec)
 * 
 */
public class ExceptionHandler {

    public static void handleErrorWithLog(Exception e) {
        SchedulerPlugin.log(e);
    }

    public static void handleErrorWithDialog(Shell shell, Exception e, String message) {
        handleErrorWithLog(e);
        MessageDialog.openError(shell, "Warn", message);
    }

    public static void handleErrorWithDialog(Shell shell, Exception e) {
        handleErrorWithLog(e);
        MessageDialog.openError(shell, "Warn", e.getMessage());
    }

    public static void handleErrorWithDialog(Shell shell, String message) {
        MessageDialog.openError(shell, "Warn", message);
    }
}
