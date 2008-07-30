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
package org.talend.designer.core.ui.editor.update.cmd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.context.UpdateRunJobComponentContextHelper;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.context.UpdateContextVariablesHelper;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.update.EUpdateResult;
import org.talend.core.model.update.UpdateResult;
import org.talend.designer.core.DesignerPlugin;
import org.talend.repository.RepositoryPlugin;

/**
 * ggu class global comment. Detailled comment
 */
public class UpdateContextParameterCommand extends Command {

    private UpdateResult result;

    public UpdateContextParameterCommand(UpdateResult result) {
        this.result = result;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    public void execute() {
        if (result == null) {
            return;
        }
        Object job = result.getJob();
        if (job == null) {
            return;
        }
        if (job instanceof IProcess2) {
            IProcess2 process = (IProcess2) job;

            Set<String> names = (Set<String>) result.getUpdateObject();

            for (IContext context : process.getContextManager().getListContext()) {
                for (IContextParameter param : context.getContextParameterList()) {
                    ContextItem item = null;
                    if (names != null && names.contains(param.getName())) {
                        switch (result.getResultType()) {
                        case UPDATE:
                            item = (ContextItem) result.getParameter();

                            if (item != null && item.getProperty().getLabel().equals(param.getSource()) && result.isChecked()) {

                                ContextUtils.updateParameterFromRepository(item, param, context.getName());
                            } else {
                                param.setSource(IContextParameter.BUILT_IN);
                            }
                            break;
                        case RENAME:
                            List<Object> parameter = (List<Object>) result.getParameter();
                            if (parameter.size() >= 3) {
                                item = (ContextItem) parameter.get(0);
                                String oldName = (String) parameter.get(1);
                                String newName = (String) parameter.get(2);
                                if (oldName.equals(param.getName())) {
                                    if (newName != null) {
                                        param.setName(newName);
                                        ContextUtils.updateParameterFromRepository(item, param, context.getName());
                                    }
                                }

                            }
                            break;
                        case BUIL_IN: // built-in
                        default:
                            param.setSource(IContextParameter.BUILT_IN);
                            break;
                        }
                    }
                }
            }
            // update parameters
            if (result.getResultType() == EUpdateResult.RENAME) {
                List<Object> parameter = (List<Object>) result.getParameter();
                if (parameter.size() >= 3) {
                    String oldName = (String) parameter.get(1);
                    String newName = (String) parameter.get(2);
                    UpdateContextVariablesHelper.updateProcessForRenamed(process, oldName, newName);

                    // tRunJob parameters rename
                    IEditorReference[] reference = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                            .getEditorReferences();
                    List<IProcess> processes = RepositoryPlugin.getDefault().getDesignerCoreService().getOpenedProcess(reference);
                    Map<String, String> renamedMap = new HashMap<String, String>();
                    renamedMap.put(newName, oldName);

                    UpdateRunJobComponentContextHelper.updateOpenedJobRunJobComponentReference(processes, renamedMap, process
                            .getId(), null);
                    try {
                        // perhaps, need optimize.
                        UpdateRunJobComponentContextHelper.updateItemRunJobComponentReference(DesignerPlugin.getDefault()
                                .getProxyRepositoryFactory(), renamedMap, process.getId(), null);
                    } catch (PersistenceException e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
    }
}
