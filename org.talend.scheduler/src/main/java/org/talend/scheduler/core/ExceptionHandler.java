// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.scheduler.core;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.talend.scheduler.SchedulerPlugin;

/**
 * Detailled comment for this class. <br/> $Id$
 * 
 * @author phou
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
