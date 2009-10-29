// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.preferences;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.swt.preferences.CheckBoxFieldEditor;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.repository.ProjectManager;
import org.talend.repository.ui.views.IRepositoryView;

public class PerformancePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private CheckBoxFieldEditor manuallyRefreshEditor;

    private CheckBoxFieldEditor creatingRefreshEditor;

    private CheckBoxFieldEditor savingRefreshEditor;

    private CheckBoxFieldEditor deletingRefreshEditor;

    // private CheckBoxFieldEditor mergingReferenceProject;

    private CheckBoxFieldEditor dbConnTimeoutActive;

    private IntegerFieldEditor dbConnTimeout;

    public PerformancePreferencePage() {
        super(GRID);
        setPreferenceStore(DesignerPlugin.getDefault().getPreferenceStore());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    @Override
    protected void createFieldEditors() {
        Composite comp = new Composite(getFieldEditorParent(), SWT.NULL);
        comp.setLayout(new GridLayout());

        Group refreshGroup = new Group(comp, SWT.NULL);
        refreshGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        refreshGroup.setText(Messages.getString("PerformancePreferencePage.RepositoryPreferencePage.RefreshTitle")); //$NON-NLS-1$

        manuallyRefreshEditor = new CheckBoxFieldEditor(IRepositoryPrefConstants.MANUALLY_REFRESH, Messages
                .getString("PerformancePreferencePage.RepositoryPreferencePage.RefreshManually"), //$NON-NLS-1$
                refreshGroup);
        GridDataFactory.swtDefaults().indent(5, 5).applyTo(manuallyRefreshEditor.getButton());

        Composite childGroup = new Composite(refreshGroup, SWT.NULL);
        GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
        layoutData.horizontalIndent = 5;
        childGroup.setLayoutData(layoutData);

        creatingRefreshEditor = new CheckBoxFieldEditor(IRepositoryPrefConstants.CREATING_REFRESH, Messages
                .getString("PerformancePreferencePage.RepositoryPreferencePage.RefreshCreated"), childGroup); //$NON-NLS-1$
        GridDataFactory.swtDefaults().indent(10, 0).applyTo(creatingRefreshEditor.getButton());

        savingRefreshEditor = new CheckBoxFieldEditor(IRepositoryPrefConstants.SAVING_REFRESH, Messages
                .getString("PerformancePreferencePage.RepositoryPreferencePage.RefreshSaved"), //$NON-NLS-1$
                childGroup);
        GridDataFactory.swtDefaults().indent(10, 0).applyTo(savingRefreshEditor.getButton());

        deletingRefreshEditor = new CheckBoxFieldEditor(IRepositoryPrefConstants.DELETING_REFRESH, Messages
                .getString("PerformancePreferencePage.RepositoryPreferencePage.RefreshDeleted"), //$NON-NLS-1$
                childGroup);
        GridDataFactory.swtDefaults().indent(10, 0).applyTo(deletingRefreshEditor.getButton());

        // if (PluginChecker.isRefProjectLoaded()) {
        // mergingReferenceProject = new CheckBoxFieldEditor(IRepositoryPrefConstants.MERGE_REFERENCE_PROJECT, Messages
        //                    .getString("PerformancePreferencePage.RepositoryPreferencePage.ReferenceProjectMerged"), //$NON-NLS-1$
        // comp);
        // GridDataFactory.swtDefaults().indent(10, 0).applyTo(mergingReferenceProject.getButton());
        // addField(mergingReferenceProject);
        // }
        addField(manuallyRefreshEditor);
        addField(creatingRefreshEditor);
        addField(savingRefreshEditor);
        addField(deletingRefreshEditor);

        addListeners();

        addField(new BooleanFieldEditor(ITalendCorePrefConstants.DEACTIVE_REPOSITORY_UPDATE, Messages
                .getString("PerformancePreferencePage.display.deactiveRepositoryUpdate"),//$NON-NLS-1$
                getFieldEditorParent()));
        addField(new BooleanFieldEditor(TalendDesignerPrefConstants.PROPERTY_CODE_CHECK, Messages
                .getString("PerformancePreferencePage.propertyCodeCheck"), getFieldEditorParent())); //$NON-NLS-1$

        addField(new BooleanFieldEditor(TalendDesignerPrefConstants.GENERATE_CODE_WHEN_OPEN_JOB, Messages
                .getString("PerformancePreferencePage.generateCode"),//$NON-NLS-1$
                getFieldEditorParent()));
        addField(new BooleanFieldEditor(TalendDesignerPrefConstants.CHECK_ONLY_LAST_VERSION, Messages
                .getString("PerformancePreferencePage.checkVersion"), //$NON-NLS-1$
                getFieldEditorParent()));
        addField(new BooleanFieldEditor(TalendDesignerPrefConstants.PROPAGATE_CONTEXT_VARIABLE, Messages
                .getString("PerformancePreferencePage.addOrDeleteVariable"),//$NON-NLS-1$
                getFieldEditorParent()));

        dbConnTimeoutActive = new CheckBoxFieldEditor(ITalendCorePrefConstants.DB_CONNECTION_TIMEOUT_ACTIVED, Messages
                .getString("PerformancePreferencePage.ActivedTimeoutSetting"), getFieldEditorParent()); //$NON-NLS-1$
        dbConnTimeoutActive.getButton().addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                checkDBTimeout();
            }
        });
        dbConnTimeout = new IntegerFieldEditor(ITalendCorePrefConstants.DB_CONNECTION_TIMEOUT, Messages
                .getString("PerformancePreferencePage.ConnectionTimeout"), //$NON-NLS-1$
                getFieldEditorParent());
        Text textControl = dbConnTimeout.getTextControl(getFieldEditorParent());
        textControl.setToolTipText(Messages.getString("PerformancePreferencePage.ConnectionTimeoutTip")); //$NON-NLS-1$
        dbConnTimeout.setValidRange(0, Short.MAX_VALUE);
        textControl.setEnabled(getPreferenceStore().getBoolean(ITalendCorePrefConstants.DB_CONNECTION_TIMEOUT_ACTIVED));

        addField(dbConnTimeoutActive);
        addField(dbConnTimeout);
        if (true) { // disable it. will check it later
            CheckBoxFieldEditor itemIndex = new CheckBoxFieldEditor(ITalendCorePrefConstants.ITEM_INDEX, Messages
                    .getString("PerformancePreferencePage.itemsRelationsCheckbox"), getFieldEditorParent()); //$NON-NLS-1$

            itemIndex.getButton().addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    Button sourceCheckBox = ((Button) e.getSource());
                    if (sourceCheckBox.getSelection()) {
                        // need to update to ask question about use or not
                        boolean needBuild = false;
                        needBuild = !RelationshipItemBuilder.getInstance().isAlreadyBuilt(
                                ProjectManager.getInstance().getCurrentProject());
                        if (!needBuild) {
                            if (MessageDialog.openQuestion(sourceCheckBox.getShell(), Messages
                                    .getString("PerformancePreferencePage.itemsRelationDialogTitle"), //$NON-NLS-1$
                                    "Do you want to force to rebuild all the relations in the project ?")) {
                                needBuild = true;
                            }
                        }
                        if (needBuild) {
                            if (MessageDialog.openQuestion(sourceCheckBox.getShell(), Messages
                                    .getString("PerformancePreferencePage.itemsRelationDialogTitle"), //$NON-NLS-1$
                                    Messages.getString("PerformancePreferencePage.itemsRelationDialogMessage"))) { //$NON-NLS-1$
                                IRunnableWithProgress runnable = new IRunnableWithProgress() {

                                    public void run(IProgressMonitor monitor) throws InvocationTargetException,
                                            InterruptedException {
                                        RelationshipItemBuilder.getInstance().buildIndex(
                                                ProjectManager.getInstance().getCurrentProject(), monitor);
                                    }
                                };
                                ProgressMonitorDialog dialog = new ProgressMonitorDialog(sourceCheckBox.getShell());
                                try {
                                    dialog.run(false, true, runnable);
                                } catch (InvocationTargetException e1) {
                                    MessageBoxExceptionHandler.process(e1);
                                } catch (InterruptedException e1) {
                                    // force uncheck as index is not finished.
                                    ((CheckBoxFieldEditor) e.getSource()).setChecked(false);
                                }
                            }
                        }
                    }
                }

            });
            addField(itemIndex);
        }
    }

    private void addListeners() {

        manuallyRefreshEditor.getButton().addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                boolean value = manuallyRefreshEditor.getBooleanValue();
                creatingRefreshEditor.setChecked(!value);
                savingRefreshEditor.setChecked(!value);
                deletingRefreshEditor.setChecked(!value);
            }
        });
        final SelectionAdapter listener = new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                boolean value = creatingRefreshEditor.getBooleanValue() || savingRefreshEditor.getBooleanValue()
                        || deletingRefreshEditor.getBooleanValue();
                manuallyRefreshEditor.setChecked(!value);
            }
        };
        creatingRefreshEditor.getButton().addSelectionListener(listener);
        savingRefreshEditor.getButton().addSelectionListener(listener);
        deletingRefreshEditor.getButton().addSelectionListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */

    public void init(IWorkbench workbench) {
    }

    @Override
    public void dispose() {
        super.dispose();
        IRepositoryView view = RepositoryManager.getRepositoryView();
        view.refresh();
    }

    private void checkDBTimeout() {
        if (dbConnTimeout != null) {
            Text textControl = dbConnTimeout.getTextControl(getFieldEditorParent());
            if (textControl != null && dbConnTimeoutActive != null) {
                textControl.setEnabled(dbConnTimeoutActive.getBooleanValue());
            }
        }
    }

    @Override
    protected void performApply() {
        super.performApply();
    }

    @Override
    public boolean performOk() {
        final boolean toReturn = super.performOk();
        return toReturn;
    }

}
