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

import java.util.Set;

import org.eclipse.gef.commands.Command;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.update.UpdateResult;

/**
 * ggu class global comment. Detailled comment
 */
public class UpdateContextParameterCommand extends Command {

    private org.talend.designer.core.ui.editor.process.Process process;

    private UpdateResult result;

    public UpdateContextParameterCommand(org.talend.designer.core.ui.editor.process.Process process, UpdateResult result) {
        this.process = process;
        this.result = result;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    public void execute() {
        if (process == null || result == null) {
            return;
        }
        Set<String> names = (Set<String>) result.getUpdateObject();
        ContextItem item = (ContextItem) result.getParameter();

        for (IContext context : process.getContextManager().getListContext()) {
            for (IContextParameter param : context.getContextParameterList()) {
                if (names != null && names.contains(param.getName())) {
                    if (item != null && item.getProperty().getLabel().equals(param.getSource()) && result.isChecked()) {

                        ContextUtils.updateParameterFromRepository(item, param, context.getName());
                    } else { // built-in
                        param.setSource(IContextParameter.BUILT_IN);
                    }
                }
            }
        }
    }
}
