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
package org.talend.designer.core.ui.views.contexts;

import java.util.Map;

import org.apache.commons.collections.BidiMap;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.ui.context.ContextComposite;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.cmd.ContextAddParameterCommand;
import org.talend.designer.core.ui.editor.cmd.ContextChangeDefaultCommand;
import org.talend.designer.core.ui.editor.cmd.ContextRemoveParameterCommand;
import org.talend.designer.core.ui.editor.cmd.ContextRenameParameterCommand;
import org.talend.designer.core.ui.editor.cmd.ContextTemplateModifyCommand;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * A concrete class of ContextComposite for the context view. <br/>
 * 
 */
public class ContextViewComposite extends ContextComposite {

    MultiPageTalendEditor part;

    // private CCombo typeCombo;
    //
    // private CCombo repositoryCombo;

    private String currentRepositoryContext = null;

    private Map<String, ContextItem> repositoryContextItemMap = null;

    private BidiMap repositoryContextValueMap = null;

    /**
     * bqian ContextComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public ContextViewComposite(Composite parent, ContextsView contextView) {
        super(parent, false);
    }

    public void setPart(MultiPageTalendEditor part) {
        this.part = part;
        refresh();
    }

    //
    // private Map<String, ContextItem> getRepositoryContextItemMap() {
    // if (repositoryContextItemMap == null) {
    // repositoryContextItemMap = new HashMap<String, ContextItem>();
    // }
    // return repositoryContextItemMap;
    // }
    //
    // private BidiMap getRepositoryContextValueMap() {
    // if (repositoryContextValueMap == null) {
    // repositoryContextValueMap = new DualHashBidiMap();
    // }
    // return repositoryContextValueMap;
    // }

    protected void initializeUI() {
        super.initializeUI();
    }

    // @Override
    // protected void addChoiceComponents(Composite composite) {
    // updateContextList();
    // CLabel label = new CLabel(composite, SWT.NONE);
    // // label.setBackground(this.getBackground());
    // label.setText(Messages.getString("ContextProcessSection2.contextType")); //$NON-NLS-1$
    //
    // typeCombo = new CCombo(composite, SWT.BORDER);
    // typeCombo.setEditable(false);
    //
    // typeCombo.setItems(new String[] { EmfComponent.TEXT_BUILTIN, EmfComponent.TEXT_REPOSITORY });
    // if (currentRepositoryContext == null) {
    // typeCombo.setText(EmfComponent.TEXT_BUILTIN);
    // } else {
    // typeCombo.setText(EmfComponent.TEXT_REPOSITORY);
    // }
    //
    // repositoryCombo = new CCombo(composite, SWT.BORDER);
    // repositoryCombo.setEditable(false);
    //
    // if (currentRepositoryContext == null) {
    // repositoryCombo.setVisible(false);
    // } else {
    // repositoryCombo.setVisible(true);
    // }
    //
    // if (getProcess() != null && currentRepositoryContext != null) {
    // updateContextList();
    // repositoryCombo.setText(currentRepositoryContext);
    // }
    // addListeners();

    // super.addChoiceComponents(composite);
    // }

    protected void refreshChoiceComposite() {
        // updateContextList();
        // if (currentRepositoryContext == null) {
        // typeCombo.setText(EmfComponent.TEXT_BUILTIN);
        // } else {
        // typeCombo.setText(EmfComponent.TEXT_REPOSITORY);
        // }
        // if (currentRepositoryContext == null) {
        // repositoryCombo.setVisible(false);
        // } else {
        // repositoryCombo.setVisible(true);
        // }
        // if (getProcess() != null && currentRepositoryContext != null) {
        // updateContextList();
        // repositoryCombo.setText(currentRepositoryContext);
        // }
        super.refreshChoiceComposite();
    }

    // private void updateContextList() {
    // if (getProcess() == null) {
    // return;
    // }
    //
    // IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
    // List<ContextItem> contextItemList = null;
    // String[] repositoryContextNames = new String[] {};
    // // String[] repositoryContextValues = new String[] {};
    // List<String> contextNamesList = new ArrayList<String>();
    // try {
    // contextItemList = factory.getContextItem();
    // } catch (PersistenceException e) {
    // throw new RuntimeException(e);
    // }
    // if (contextItemList != null) {
    // getRepositoryContextItemMap().clear();
    // for (ContextItem contextItem : contextItemList) {
    // if (factory.getStatus(contextItem) != ERepositoryStatus.DELETED) {
    // String name = Messages.getString("ContextProcessSection2.context") + contextItem.getProperty().getLabel();
    // //$NON-NLS-1$
    // String value = contextItem.getProperty().getId();
    // getRepositoryContextItemMap().put(name, contextItem);
    // getRepositoryContextValueMap().put(value, name);
    // contextNamesList.add(name);
    // }
    // }
    // repositoryContextNames = (String[]) contextNamesList.toArray(new String[0]);
    // }
    // loadRepositoryContextFromProcess();
    // repositoryCombo.setItems(repositoryContextNames);
    // if (getJob().getRepositoryId() == null) {
    //
    // } else if (getProcess() != null && repositoryCombo != null && !repositoryCombo.isDisposed()) {
    //
    // if (repositoryContextNames.length != 0 && (!contextNamesList.contains(currentRepositoryContext))) {
    // currentRepositoryContext = repositoryContextNames[0];
    // repositoryCombo.setText(repositoryContextNames[0]);
    // layoutButtonBar();
    // ContextItem contextItem = getRepositoryContextItemMap().get(repositoryContextNames[0]);
    // getCommandStack().execute(new ContextRepositoryCommand(getJob(), contextItem));
    // }
    // }
    // }
    //
    // private void loadRepositoryContextFromProcess() {
    // if (getProcess() == null) {
    // currentRepositoryContext = null;
    // setReadOnly(true);
    // return;
    // }
    // String repositoryId = ((Process) getProcess()).getRepositoryId();
    // if (getRepositoryContextValueMap().containsKey(repositoryId)) {
    // currentRepositoryContext = (String) getRepositoryContextValueMap().get(repositoryId);
    // setReadOnly(true);
    // } else {
    // currentRepositoryContext = null;
    // setReadOnly(getProcess().isReadOnly());
    // }
    // }

    /**
     * qzhang Comment method "addListeners".
     */
    // private void addListeners() {
    // typeCombo.addSelectionListener(new SelectionListener() {
    //
    // public void widgetDefaultSelected(SelectionEvent e) {
    // }
    //
    // public void widgetSelected(SelectionEvent e) {
    // CCombo combo = (CCombo) e.getSource();
    //
    // if (combo.getText().equals(EmfComponent.TEXT_REPOSITORY)) {
    // boolean comfirm = MessageDialog.openConfirm(ContextViewComposite.this.getShell(), "Comfirm",
    // "The current built-in context will be removed, are you sure to continue?");
    // if (!comfirm) {
    // combo.setText(EmfComponent.TEXT_BUILTIN);
    // return;
    // }
    //
    // updateContextList();
    // if (repositoryCombo.getItemCount() == 0) {
    // repositoryCombo.setText("");
    // } else {
    // if (currentRepositoryContext == null) {
    // repositoryCombo.select(0);
    // }
    // }
    // repositoryCombo.setVisible(true);
    // ContextItem contextItem = getRepositoryContextItemMap().get(repositoryCombo.getText());
    // getCommandStack().execute(new ContextRepositoryCommand(getJob(), contextItem));
    // } else {
    // repositoryCombo.setVisible(false);
    // currentRepositoryContext = null;
    // getCommandStack().execute(new ContextRepositoryCommand(getJob(), null));
    // }
    // }
    // });
    // repositoryCombo.addSelectionListener(new SelectionListener() {
    //
    // public void widgetDefaultSelected(SelectionEvent e) {
    // }
    //
    // public void widgetSelected(SelectionEvent e) {
    // CCombo combo = (CCombo) e.getSource();
    // currentRepositoryContext = combo.getText();
    // ContextItem contextItem = getRepositoryContextItemMap().get(currentRepositoryContext);
    // getCommandStack().execute(new ContextRepositoryCommand(getJob(), contextItem));
    // }
    // });
    // }
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.ContextComposite#refresh()
     */
    @Override
    public void refresh() {
        super.refresh();
        DesignerPlugin.getDefault().getRunProcessService().refreshView();
    }

    public CommandStack getCommandStack() {
        return part == null ? null : (CommandStack) (part.getTalendEditor().getAdapter(CommandStack.class));
    }

    public IContextManager getContextManager() {
        return getProcess() == null ? null : getProcess().getContextManager();
    }

    public IProcess getProcess() {
        return part == null ? null : part.getTalendEditor().getProcess();
    }

    private Process getJob() {
        return (Process) getProcess();
    }

    public void onContextChangeDefault(IContextManager contextManager, IContext newDefault) {
        // contextManager.setDefaultContext(newDefault);
        // refresh();
        getCommandStack().execute(new ContextChangeDefaultCommand(contextManager, newDefault));
    }

    public void onContextRenameParameter(IContextManager contextManager, String oldName, String newName) {
        getCommandStack().execute(new ContextRenameParameterCommand(contextManager, oldName, newName));
    }

    public void onContextModify(IContextManager contextManager, IContextParameter parameter) {
        getCommandStack().execute(new ContextTemplateModifyCommand(getProcess(), contextManager, parameter));
    }

    public void onContextAddParameter(IContextManager contextManager, IContextParameter parameter) {
        getCommandStack().execute(new ContextAddParameterCommand(getContextManager(), parameter));
    }

    public void onContextRemoveParameter(IContextManager contextManager, String paramName) {
        getCommandStack().execute(new ContextRemoveParameterCommand(getContextManager(), paramName));
    }

}
