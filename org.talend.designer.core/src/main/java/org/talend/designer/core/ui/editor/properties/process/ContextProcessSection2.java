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
package org.talend.designer.core.ui.editor.properties.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.ui.context.JobContextComposite;
import org.talend.designer.core.DesignerPlugin;
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
import org.talend.designer.core.ui.editor.outline.ProcessTreeEditPart;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.process.ProcessPart;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * Section used for the contexts in a process.
 * 
 * $Id: ContextProcessSection.java 1781 2007-02-03 07:51:24 +0000 (sam., 03 févr. 2007) nrousseau $
 * 
 */
public class ContextProcessSection2 extends AbstractPropertySection {

    private MultiPageTalendEditor part;

    private PropertyJobComposite composite;

    protected CommandStack getCommandStack() {
        return (CommandStack) part.getTalendEditor().getAdapter(CommandStack.class);
    }

    public void createControls(final Composite parent, final TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);

        parent.setLayout(new GridLayout());
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = SWT.FILL;
        gridData.horizontalAlignment = SWT.FILL;
        parent.setLayoutData(gridData);

        composite = new PropertyJobComposite(parent);

        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = ITabbedPropertyConstants.HSPACE + 2;
        gridLayout.marginHeight = ITabbedPropertyConstants.VSPACE;
        composite.setLayout(gridLayout);

        gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = SWT.FILL;
        gridData.horizontalAlignment = SWT.FILL;
        composite.setLayoutData(gridData);
    }

    public void setInput(final IWorkbenchPart workbenchPart, final ISelection selection) {
        if (!(selection instanceof IStructuredSelection)) {
            return;
        }
        if (workbenchPart instanceof MultiPageTalendEditor) {
            part = (MultiPageTalendEditor) workbenchPart;
        } else {
            part = (MultiPageTalendEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getActiveEditor();
        }
        super.setInput(part, selection);
        Object input = ((IStructuredSelection) selection).getFirstElement();
        Process process = null;
        if (input instanceof ProcessTreeEditPart) {
            process = (Process) ((ProcessTreeEditPart) input).getModel();
        } else if (input instanceof ProcessPart) {
            process = (Process) ((ProcessPart) input).getModel();
        } else {
            return;
        }

        composite.setContextManager(process.getContextManager());
        composite.setProcess(process);

        updateContextView();
    }

    public void refresh() {
        composite.refresh();
    }

    public void updateContextView() {
        composite.disposeAllComponents();
        boolean modified = composite.updateContextFromRepository();
        composite.addComponents();
        composite.refresh();

        if (modified) {
            part.getTalendEditor().setDirty(true);
        }
    }

    /**
     * DOC nrousseau ContextProcessSection2 class global comment. Detailled comment <br/>
     * 
     * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
     * 
     */
    private class PropertyJobComposite extends JobContextComposite {

        private CCombo contextCombo;

        private Map<String, ContextItem> repositoryContextItemMap;

        private BidiMap repositoryContextValueMap;

        private String currentRepositoryContext = null;

        private Process process;

        public PropertyJobComposite(Composite parent) {
            super(parent);
            repositoryContextItemMap = new HashMap<String, ContextItem>();
            repositoryContextValueMap = new DualHashBidiMap();
        }

        @Override
        protected void addChoiceComponents(Composite composite) {
            updateContextList();

            CLabel label = new CLabel(composite, SWT.NONE);
            label.setBackground(this.getBackground());
            label.setText("Context Type");

            CCombo combo = new CCombo(composite, SWT.BORDER);
            combo.setEditable(false);

            combo.setItems(new String[] { EmfComponent.TEXT_BUILTIN, EmfComponent.TEXT_REPOSITORY });
            if (currentRepositoryContext == null) {
                combo.setText(EmfComponent.TEXT_BUILTIN);
            } else {
                combo.setText(EmfComponent.TEXT_REPOSITORY);
            }
            combo.setEnabled(!process.isReadOnly());
            combo.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                }

                public void widgetSelected(SelectionEvent e) {
                    CCombo combo = (CCombo) e.getSource();
                    if (combo.getText().equals(EmfComponent.TEXT_REPOSITORY)) {
                        updateContextList();
                        contextCombo.setText(currentRepositoryContext);
                        contextCombo.setVisible(true);
                    } else {
                        contextCombo.setVisible(false);
                        getCommandStack().execute(new ContextRepositoryCommand(process, null));
                    }
                }
            });

            contextCombo = new CCombo(composite, SWT.BORDER);
            contextCombo.setEditable(false);
            contextCombo.setEnabled(!process.isReadOnly());
            if (currentRepositoryContext == null) {
                contextCombo.setVisible(false);
            } else {
                contextCombo.setVisible(true);
            }
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
            if (currentRepositoryContext != null) {
                updateContextList();
                contextCombo.setText(currentRepositoryContext);
            }

            super.addChoiceComponents(composite);
        }

        @Override
        protected void onContextAdd(JobContextComposite composite, IContext newContext, CCombo combo) {
            getCommandStack().execute(new ContextAddCommand2(composite, newContext, combo));
        }

        @Override
        protected void onContextRemove(JobContextComposite composite, String contextName, CCombo combo) {
            getCommandStack().execute(new ContextRemoveCommand2(composite, contextName, combo));
        }

        @Override
        protected void onContextChangeDefault(IContextManager contextManager, IContext newDefault) {
            getCommandStack().execute(new ContextChangeDefaultCommand(contextManager, newDefault));

        }

        @Override
        protected void onContextModify(IContextManager contextManager, IContext oldContext, IContext newContext) {
            getCommandStack().execute(new ContextModifyCommand(contextManager, oldContext, newContext));
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
                        String name = "CONTEXT:" + contextItem.getProperty().getLabel();
                        String value = contextItem.getProperty().getId();
                        repositoryContextItemMap.put(name, contextItem);
                        repositoryContextValueMap.put(value, name);
                        contextNamesList.add(name);
                    }
                }
                repositoryContextNames = (String[]) contextNamesList.toArray(new String[0]);
            }
            loadRepositoryContextFromProcess();
            if (contextCombo != null && !contextCombo.isDisposed()) {
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
                            IContextManager tmpManager = new JobContextManager(item.getContext(), item
                                    .getDefaultContext());
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

    }
}
