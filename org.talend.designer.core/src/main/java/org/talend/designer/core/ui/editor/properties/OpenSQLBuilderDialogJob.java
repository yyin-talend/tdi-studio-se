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
package org.talend.designer.core.ui.editor.properties;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.core.model.process.Element;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;
import org.talend.sqlbuilder.ui.SQLBuilderDialog;
import org.talend.sqlbuilder.ui.progress.OpenSQLBuilderDialogProgress;
import org.talend.sqlbuilder.util.ConnectionParameters;

/**
 * Open SqlBuilderDialog Job.
 * 
 * @author qzhang
 * 
 */
public class OpenSQLBuilderDialogJob extends Job {

    private SQLBuilderRepositoryNodeManager manager = new SQLBuilderRepositoryNodeManager();

    private ConnectionParameters connectionParameters;

    private static String id = SqlBuilderPlugin.PLUGIN_ID;

    private OpenSQLBuilderDialogProgress loginProgress;

    private Composite composite;

    private Element elem;

    private String propertyName;

    private CommandStack commandStack;

    /**
     * DOC dev OpenDialogJob constructor comment.
     * 
     * @param connectionParameters
     * @param composite
     * @param elem
     * @param propertyName
     * @param commandStack
     */
    public OpenSQLBuilderDialogJob(ConnectionParameters connectionParameters, Composite composite, Element elem,
            String propertyName, CommandStack commandStack) {
        super(Messages.getString("OpenSQLBuilderDialogJob.openSqlbuilderDialog")); //$NON-NLS-1$
        this.connectionParameters = connectionParameters;

        this.composite = composite;
        this.commandStack = commandStack;
        this.elem = elem;
        this.propertyName = propertyName;
    }

    /**
     * DOC dev OpenDialogJob constructor comment.
     */
    public OpenSQLBuilderDialogJob(ConnectionParameters connectionParameters) {
        super(Messages.getString("OpenSQLBuilderDialogJob.openSqlbuilderDialog")); //$NON-NLS-1$
        this.connectionParameters = connectionParameters;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    protected IStatus run(IProgressMonitor monitor) {
        loginProgress = new OpenSQLBuilderDialogProgress(connectionParameters, manager, composite);
        try {
            loginProgress.run(monitor);
            if (connectionParameters.isStatus()) {
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {
                        Shell parentShell = new Shell(composite.getShell().getDisplay());
                        SQLBuilderDialog dial = new SQLBuilderDialog(parentShell);
                        dial.setConnParameters(connectionParameters);
                        if (Window.OK == dial.open()) {
                            if (!composite.isDisposed()) {
                                String sql = connectionParameters.getQuery();
                                if (ConnectionParameters.isJavaProject()) {
                                    sql = "\"" + sql + "\"";
                                } else {
                                    sql = "'" + sql + "'";
                                }
                                Command cmd = new PropertyChangeCommand(elem, propertyName, sql); 
                                commandStack.execute(cmd);
                            }
                        }
                    }

                });
            } else {
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {
                        String pid = SqlBuilderPlugin.PLUGIN_ID;
                        String mainMsg = Messages.getString("ConnectionError.Message"); //$NON-NLS-1$
                        new ErrorDialogWidthDetailArea(composite.getShell(), pid, mainMsg, connectionParameters
                                .getConnectionComment());
                    }
                });

            }
        } catch (InterruptedException ie) {
            loginProgress.cleanUp();
            return new Status(IStatus.CANCEL, id, IStatus.CANCEL, "Progress.OpenSqlBuilderDialog.Cancelled", null); //$NON-NLS-1$
        } catch (Exception e) {
            loginProgress.cleanUp();
            return new Status(IStatus.ERROR, id, IStatus.CANCEL, "Progress.OpenSqlBuilderDialog.Error", e); //$NON-NLS-1$
        }
        return new Status(IStatus.OK, id, IStatus.OK, "tested ok ", null); //$NON-NLS-1$
    }
}
