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
package org.talend.designer.core.ui.editor.properties;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.core.model.process.Element;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.properties.controllers.SqlMemoController;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;
import org.talend.sqlbuilder.ui.SQLBuilderDialog;
import org.talend.sqlbuilder.ui.progress.OpenSQLBuilderDialogProgress;
import org.talend.sqlbuilder.util.ConnectionParameters;
import org.talend.sqlbuilder.util.TextUtil;
import org.talend.sqlbuilder.util.UIUtils;

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

    private SqlMemoController controller;

    /**
     * DOC dev OpenDialogJob constructor comment.
     * 
     * @param connectionParameters
     * @param composite
     * @param elem
     * @param propertyName
     * @param commandStack
     * @param controller
     */
    public OpenSQLBuilderDialogJob(ConnectionParameters connectionParameters, Composite composite, Element elem,
            String propertyName, CommandStack commandStack, SqlMemoController controller) {
        super(Messages.getString("OpenSQLBuilderDialogJob.openSqlbuilderDialog")); //$NON-NLS-1$
        this.connectionParameters = connectionParameters;

        this.composite = composite;
        this.commandStack = commandStack;
        this.elem = elem;
        this.propertyName = propertyName;
        this.controller = controller;
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
        final Process process = controller.getDynamicTabbedPropertySection().part.getTalendEditor().getProcess();
        try {
            loginProgress.run(monitor);
            if (connectionParameters.isStatus()) {
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {
                        Shell parentShell = new Shell(composite.getShell().getDisplay());
                        TextUtil.setDialogTitle(process.getName(),
                                (String) ((Node) elem).getElementParameter("LABEL").getValue(), elem.getElementName());
                        SQLBuilderDialog dial = new SQLBuilderDialog(parentShell);
                        UIUtils.addSqlBuilderDialog(process.getName(), dial);
                        dial.setConnParameters(connectionParameters);
                        if (Window.OK == dial.open()) {
                            if (!composite.isDisposed() && !connectionParameters.isNodeReadOnly()) {
                                String sql = connectionParameters.getQuery();
                                sql = TalendTextUtils.addSQLQuotes(sql);
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
                        if (new ErrorDialogWidthDetailArea(composite.getShell(), pid, mainMsg, connectionParameters
                                .getConnectionComment()).getCodeOfButton() == Dialog.OK) {

                            ConfigureConnParamDialog paramDialog = new ConfigureConnParamDialog(composite.getShell(),
                                    connectionParameters, process.getContextManager());
                            if (paramDialog.open() == Window.OK) {
                                controller.openSqlBuilderBuildIn(connectionParameters, propertyName);
                            }
                        }
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
