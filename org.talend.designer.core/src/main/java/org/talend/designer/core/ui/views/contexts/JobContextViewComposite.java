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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.ui.context.JobContextCompositeForView;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.cmd.ContextAddCommand2;
import org.talend.designer.core.ui.editor.cmd.ContextAddParameterCommand;
import org.talend.designer.core.ui.editor.cmd.ContextChangeDefaultCommand;
import org.talend.designer.core.ui.editor.cmd.ContextModifyCommand;
import org.talend.designer.core.ui.editor.cmd.ContextRemoveCommand2;
import org.talend.designer.core.ui.editor.cmd.ContextRemoveParameterCommand;
import org.talend.designer.core.ui.editor.cmd.ContextRenameParameterCommand;
import org.talend.designer.core.ui.editor.cmd.ContextRepositoryCommand;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 */
public class JobContextViewComposite extends JobContextCompositeForView {

    private MultiPageTalendEditor part;

    private CCombo contextCombo;

    private Map<String, ContextItem> repositoryContextItemMap;

    private BidiMap repositoryContextValueMap;

    private String currentRepositoryContext = null;

    private Process process;

    private CCombo typeCombo;

    protected CommandStack getCommandStack() {
        return (CommandStack) part.getTalendEditor().getAdapter(CommandStack.class);
    }

    public JobContextViewComposite(Composite parent) {
        super(parent);
        repositoryContextItemMap = new HashMap<String, ContextItem>();
        repositoryContextValueMap = new DualHashBidiMap();
    }

    public JobContextViewComposite(Composite parent, MultiPageTalendEditor part) {
        this(parent);
        this.part = part;
        process = part.getTalendEditor().getProcess();
    }

    @Override
    protected void addChoiceComponents(Composite composite) {
        updateContextList();
        CLabel label = new CLabel(composite, SWT.NONE);
        // label.setBackground(this.getBackground());
        label.setText(Messages.getString("ContextProcessSection2.contextType")); //$NON-NLS-1$

        typeCombo = new CCombo(composite, SWT.BORDER);
        typeCombo.setEditable(false);

        typeCombo.setItems(new String[] { EmfComponent.TEXT_BUILTIN, EmfComponent.TEXT_REPOSITORY });
        if (currentRepositoryContext == null) {
            typeCombo.setText(EmfComponent.TEXT_BUILTIN);
        } else {
            typeCombo.setText(EmfComponent.TEXT_REPOSITORY);
        }
        if (process != null) {
            typeCombo.setEnabled(!process.isReadOnly());
        } else {
            typeCombo.setEnabled(false);
        }

        contextCombo = new CCombo(composite, SWT.BORDER);
        contextCombo.setEditable(false);
        if (process != null) {
            contextCombo.setEnabled(!process.isReadOnly());
        } else {
            contextCombo.setEnabled(false);
        }
        if (currentRepositoryContext == null) {
            contextCombo.setVisible(false);
        } else {
            contextCombo.setVisible(true);
        }

        if (process != null && currentRepositoryContext != null) {
            updateContextList();
            contextCombo.setText(currentRepositoryContext);
        }
        addListeners();
        super.addChoiceComponents(composite);
    }

    /**
     * qzhang Comment method "addListeners".
     */
    private void addListeners() {
        typeCombo.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                CCombo combo = (CCombo) e.getSource();
                if (combo.getText().equals(EmfComponent.TEXT_REPOSITORY)) {
                    updateContextList();
                    if (contextCombo.getItemCount() == 0) {
                        contextCombo.setText("");
                    } else {
                        contextCombo.setText(currentRepositoryContext);
                    }
                    contextCombo.setVisible(true);
                } else {
                    contextCombo.setVisible(false);
                    currentRepositoryContext = null;
                    getCommandStack().execute(new ContextRepositoryCommand(process, null));
                }
            }
        });
        contextCombo.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                CCombo combo = (CCombo) e.getSource();
                currentRepositoryContext = combo.getText();
                ContextItem contextItem = repositoryContextItemMap.get(currentRepositoryContext);
                getCommandStack().execute(new ContextRepositoryCommand(process, contextItem));
            }
        });
    }

    @Override
    protected void onContextAdd(JobContextCompositeForView composite, IContext newContext, CCombo combo) {
        getCommandStack().execute(new ContextAddCommand2(composite, newContext, combo));
    }

    @Override
    protected void onContextRemove(JobContextCompositeForView composite, String contextName, CCombo combo) {
        getCommandStack().execute(new ContextRemoveCommand2(process, composite, contextName, combo));
    }

    @Override
    protected void onContextChangeDefault(IContextManager contextManager, IContext newDefault) {
        getCommandStack().execute(new ContextChangeDefaultCommand(contextManager, newDefault));

    }

    @Override
    protected void onContextModify(IContextManager contextManager, IContext oldContext, IContext newContext) {
        getCommandStack().execute(new ContextModifyCommand(process, contextManager, oldContext, newContext));
    }

    @Override
    protected void onContextAddParameter(IContextManager contextManager, IContextParameter contextParam) {
        getCommandStack().execute(new ContextAddParameterCommand(contextManager, contextParam));
    }

    @Override
    protected void onContextRemoveParameter(IContextManager contextManager, String contextParamName) {
        getCommandStack().execute(new ContextRemoveParameterCommand(contextManager, contextParamName));
    }

    @Override
    protected void onContextRenameParameter(IContextManager contextManager, String oldName, String newName) {
        getCommandStack().execute(new ContextRenameParameterCommand(contextManager, oldName, newName));
    }

    private void updateContextList() {
        IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
        List<ContextItem> contextItemList = null;
        String[] repositoryContextNames = new String[] {};
        // String[] repositoryContextValues = new String[] {};
        List<String> contextNamesList = new ArrayList<String>();
        try {
            contextItemList = factory.getContextItem();
        } catch (PersistenceException e) {
            throw new RuntimeException(e);
        }
        if (contextItemList != null) {
            repositoryContextItemMap.clear();
            for (ContextItem contextItem : contextItemList) {
                if (factory.getStatus(contextItem) != ERepositoryStatus.DELETED) {
                    String name = Messages.getString("ContextProcessSection2.context") + contextItem.getProperty().getLabel(); //$NON-NLS-1$
                    String value = contextItem.getProperty().getId();
                    repositoryContextItemMap.put(name, contextItem);
                    repositoryContextValueMap.put(value, name);
                    contextNamesList.add(name);
                }
            }
            repositoryContextNames = (String[]) contextNamesList.toArray(new String[0]);
        }
        loadRepositoryContextFromProcess();
        if (process != null && contextCombo != null && !contextCombo.isDisposed()) {
            contextCombo.setItems(repositoryContextNames);
            if (repositoryContextNames.length != 0 && (!contextNamesList.contains(currentRepositoryContext))) {
                currentRepositoryContext = repositoryContextNames[0];
                contextCombo.setText(repositoryContextNames[0]);
                ContextItem contextItem = repositoryContextItemMap.get(repositoryContextNames[0]);
                getCommandStack().execute(new ContextRepositoryCommand(process, contextItem));
            }
        }
    }

    public void disposeAllComponents() {
        Control[] controls = getChildren();
        for (int i = 0; i < controls.length; i++) {
            controls[i].dispose();
        }
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    private void loadRepositoryContextFromProcess() {
        if (process == null) {
            currentRepositoryContext = null;
            setReadOnly(true);
            return;
        }
        String repositoryId = process.getRepositoryId();
        if (repositoryContextValueMap.containsKey(repositoryId)) {
            currentRepositoryContext = (String) repositoryContextValueMap.get(repositoryId);
            setReadOnly(true);
        } else {
            currentRepositoryContext = null;
            setReadOnly(process.isReadOnly());
        }
    }

    public boolean updateContextFromRepository() {

        String repositoryId = process.getRepositoryId();
        IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
        List<ContextItem> contextItemList = null;
        try {
            contextItemList = factory.getContextItem();
        } catch (PersistenceException e) {
            throw new RuntimeException(e);
        }
        if (contextItemList != null) {
            for (ContextItem item : contextItemList) {
                if (factory.getStatus(item) != ERepositoryStatus.DELETED) {
                    String id = item.getProperty().getId();
                    if (id.equals(repositoryId)) {
                        IContextManager tmpManager = new JobContextManager(item.getContext(), item.getDefaultContext());
                        if (tmpManager.sameAs(getContextManager())) {
                            return false;
                        }
                        getContextManager().setListContext(tmpManager.getListContext());
                        getContextManager().setDefaultContext(tmpManager.getDefaultContext());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public MultiPageTalendEditor getPart() {
        return this.part;
    }

    public void setPart(MultiPageTalendEditor part) {
        if (part != null) {
            process = part.getTalendEditor().getProcess();
            setReadOnly(process.isReadOnly());
        } else {
            currentRepositoryContext = null;
        }
        this.part = part;
    }

}