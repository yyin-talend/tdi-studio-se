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
package org.talend.designer.core.ui.editor.cmd;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IProcess;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.views.contexts.ContextsView;

/**
 * Command that will modify a context.
 * 
 * $Id$
 * 
 */
public class ContextModifyCommand extends Command {

    IContext oldContext;

    IContext currentContext;

    IContext tmpContext;

    IContextManager contextManager;

    private IProcess process;

    public ContextModifyCommand(IProcess process, IContextManager contextManager, IContext oldContext, IContext newContext) {
        this.process = process;
        this.contextManager = contextManager;
        this.oldContext = oldContext;
        this.currentContext = newContext;
        setLabel(Messages.getString("ContextModifyCommand.label")); //$NON-NLS-1$
    }

    private void refreshPropertyView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
        PropertySheet sheet = (PropertySheet) view;
        final IPage currentPage = sheet.getCurrentPage();
        if (currentPage instanceof TabbedPropertySheetPage) {
            TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) currentPage;
            tabbedPropertySheetPage.refresh();
        }
        IViewPart view2 = page.findView("org.talend.designer.core.ui.views.ContextsView"); //$NON-NLS-1$
        if (view2 instanceof ContextsView) {
            ((ContextsView) view2).refresh();
        }
    }

    /**
     * qzhang Comment method "refreshContextView".
     */
    private void refreshContextView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view2 = page.findView("org.talend.designer.core.ui.views.ContextsView"); //$NON-NLS-1$
        if (view2 instanceof ContextsView) {
            ((ContextsView) view2).updateContextView(true, false);
        }
    }

    public void execute() {
        // check modified type
        for (IContextParameter param : oldContext.getContextParameterList()) {
            String paramName = param.getName();
            IContextParameter newParam = currentContext.getContextParameter(paramName);
            if (!newParam.getType().equals(param.getType())) {
                propagateType(contextManager, newParam);
            }
        }
        contextManager.fireContextsChangedEvent();
        refreshContextView();
        // Removes the attached context files
        try {
            CorePlugin.getDefault().getRepositoryService().getProxyRepositoryFactory().removeContextFiles(process, oldContext);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    private void propagateType(IContextManager contextManager, IContextParameter param) {
        for (IContext context : contextManager.getListContext()) {
            IContextParameter paramToModify = context.getContextParameter(param.getName());
            paramToModify.setType(param.getType());
        }
    }

    @Override
    public void redo() {
        tmpContext = currentContext.clone();
        currentContext.setName(oldContext.getName());
        currentContext.setConfirmationNeeded(oldContext.isConfirmationNeeded());

        List<IContextParameter> oldListContextParam = oldContext.getContextParameterList();
        List<IContextParameter> curListContextParam = currentContext.getContextParameterList();

        boolean found;
        IContextParameter oldParam = null;
        IContextParameter curParam;
        String name = null;
        for (int i = 0; i < oldListContextParam.size(); i++) {
            found = false;
            oldParam = oldListContextParam.get(i);
            for (int j = 0; j < curListContextParam.size() && !found; j++) {
                curParam = curListContextParam.get(j);
                name = oldParam.getName();
                if (curParam.getName().equals(name)) {
                    found = true;
                    curParam.setPromptNeeded(oldParam.isPromptNeeded());
                    curParam.setComment(oldParam.getComment());
                    curParam.setPrompt(oldParam.getPrompt());
                    curParam.setType(oldParam.getType());
                    curParam.setValue(oldParam.getValue());
                }
            }
        }

        oldContext = tmpContext;
        for (IContextParameter param : oldContext.getContextParameterList()) {
            String paramName = param.getName();
            IContextParameter newParam = currentContext.getContextParameter(paramName);
            if (!newParam.getType().equals(param.getType())) {
                propagateType(contextManager, newParam);
            }
        }

        contextManager.fireContextsChangedEvent();
        refreshContextView();
    }

    @Override
    public void undo() {
        tmpContext = currentContext.clone();
        currentContext.setName(oldContext.getName());
        currentContext.setConfirmationNeeded(oldContext.isConfirmationNeeded());

        List<IContextParameter> oldListContextParam = oldContext.getContextParameterList();
        List<IContextParameter> curListContextParam = currentContext.getContextParameterList();

        boolean found;
        IContextParameter oldParam = null;
        IContextParameter curParam;
        String name = null;
        for (int i = 0; i < oldListContextParam.size(); i++) {
            found = false;
            oldParam = oldListContextParam.get(i);
            for (int j = 0; j < curListContextParam.size() && !found; j++) {
                curParam = curListContextParam.get(j);
                name = oldParam.getName();
                if (curParam.getName().equals(name)) {
                    found = true;
                    curParam.setPromptNeeded(oldParam.isPromptNeeded());
                    curParam.setComment(oldParam.getComment());
                    curParam.setPrompt(oldParam.getPrompt());
                    curParam.setType(oldParam.getType());
                    curParam.setValue(oldParam.getValue());
                }
            }
        }
        oldContext = tmpContext;
        for (IContextParameter param : oldContext.getContextParameterList()) {
            String paramName = param.getName();
            IContextParameter newParam = currentContext.getContextParameter(paramName);
            if (!newParam.getType().equals(param.getType())) {
                propagateType(contextManager, newParam);
            }
        }

        contextManager.fireContextsChangedEvent();
        refreshContextView();
    }
}
