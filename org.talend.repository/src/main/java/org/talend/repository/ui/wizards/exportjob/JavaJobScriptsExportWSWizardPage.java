// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards.exportjob;

import java.io.File;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.repository.documentation.ExportFileResource;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManager;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManagerFactory;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManager.ExportChoice;

/**
 * DOC x class global comment. Detailled comment <br/>
 * 
 */
public class JavaJobScriptsExportWSWizardPage extends JavaJobScriptsExportWizardPage {

    public static final String EXPORTTYPE_POJO = "Autonomous Job"; //$NON-NLS-1$

    public static final String EXPORTTYPE_WSWAR = "Axis WebService (WAR)"; //$NON-NLS-1$

    public static final String EXPORTTYPE_WSZIP = "Axis WebService (ZIP)"; //$NON-NLS-1$

    public static final String EXPORTTYPE_JBOSSESB = "JBoss ESB"; //$NON-NLS-1$

    public static final String EXPORTTYPE_JBI = "JBI (JSR 208)"; //$NON-NLS-1$

    public static final String ESBTYPE_JBOSS_MQ = "JBoss MQ"; //$NON-NLS-1$

    public static final String ESBTYPE_JBOSS_MESSAGING = "JBoss Messaging"; //$NON-NLS-1$

    protected Combo exportTypeCombo;

    protected Combo esbTypeCombo;

    protected Composite pageComposite;

    protected Composite optionsGroupComposite;

    protected Composite destinationNameFieldComposite;

    protected Composite destinationNameFieldInnerComposite;

    protected Button webXMLButton;

    protected Button configFileButton;

    protected Button axisLibButton;

    protected Button wsddButton;

    protected Button wsdlButton;

    protected Button chkButton;

    protected Text esbQueueMessageName;

    protected Text esbServiceName;

    protected Text esbCategory;

    public static final String STORE_EXPORTTYPE_ID = "JavaJobScriptsExportWizardPage.STORE_EXPORTTYPE_ID"; //$NON-NLS-1$

    public static final String STORE_WEBXML_ID = "JavaJobScriptsExportWizardPage.STORE_WEBXML_ID"; //$NON-NLS-1$

    public static final String STORE_CONFIGFILE_ID = "JavaJobScriptsExportWizardPage.STORE_CONFIGFILE_ID"; //$NON-NLS-1$

    public static final String STORE_AXISLIB_ID = "JavaJobScriptsExportWizardPage.STORE_AXISLIB_ID"; //$NON-NLS-1$

    public static final String STORE_WSDD_ID = "JavaJobScriptsExportWizardPage.STORE_WSDD_ID"; //$NON-NLS-1$

    public static final String STORE_WSDL_ID = "JavaJobScriptsExportWizardPage.STORE_WSDL_ID"; //$NON-NLS-1$

    public static final String EXTRACT_ZIP_FILE = "JavaJobScriptsExportWizardPage.EXTRACT_ZIP_FILE"; //$NON-NLS-1$

    protected String exportTypeFixed;

    public JavaJobScriptsExportWSWizardPage(IStructuredSelection selection, String exportType) {
        super(selection);
        // there assign the manager again
        exportTypeFixed = exportType;
        manager = createJobScriptsManager();
        manager.setMultiNodes(isMultiNodes());
    }

    public String getCurrentExportType() {
        if (exportTypeCombo != null && !exportTypeCombo.getText().equals("")) { //$NON-NLS-1$
            return exportTypeCombo.getText();
        } else {
            IDialogSettings settings = getDialogSettings();
            if (settings != null && settings.get(STORE_EXPORTTYPE_ID) != null) {
                return settings.get(STORE_EXPORTTYPE_ID);
            }
        }
        return EXPORTTYPE_POJO;
    }

    @Override
    public void createControl(Composite parent) {

        initializeDialogUnits(parent);
        GridLayout layout = new GridLayout();

        if (exportTypeFixed == null || !exportTypeFixed.equals(EXPORTTYPE_JBOSSESB)) {
            SashForm sash = createExportTree(parent);

            pageComposite = new Group(sash, 0);
            pageComposite.setLayout(layout);
            pageComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
            pageComposite.setFont(parent.getFont());
            setControl(sash);
            sash.setWeights(new int[] { 0, 1, 23 });
        } else {
            pageComposite = new Group(parent, 0);
            pageComposite.setLayout(layout);
            pageComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
            pageComposite.setFont(parent.getFont());
            setControl(parent);
        }
        layout = new GridLayout();
        destinationNameFieldComposite = new Composite(pageComposite, SWT.NONE);
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        destinationNameFieldComposite.setLayoutData(gridData);
        destinationNameFieldComposite.setLayout(layout);

        destinationNameFieldInnerComposite = new Composite(destinationNameFieldComposite, SWT.NONE);
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        destinationNameFieldInnerComposite.setLayoutData(gridData);
        destinationNameFieldInnerComposite.setLayout(layout);

        createDestinationGroup(destinationNameFieldInnerComposite);
        // createExportTree(pageComposite);
        if (!isMultiNodes()) {
            createJobVersionGroup(pageComposite);
        }

        createExportTypeGroup(pageComposite);

        createOptionsGroupButtons(pageComposite);

        restoreResourceSpecificationWidgetValues(); // ie.- local

        updateWidgetEnablements();
        setPageComplete(determinePageCompletion());

        giveFocusToDestination();

    }

    protected void createExportTypeGroup(Composite parent) {
        // options group
        Group optionsGroup = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        optionsGroup.setLayout(layout);
        optionsGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
        optionsGroup.setText(Messages.getString("JavaJobScriptsExportWSWizardPage.ExportType")); //$NON-NLS-1$
        optionsGroup.setFont(parent.getFont());

        optionsGroup.setLayout(new GridLayout(1, true));

        Composite left = new Composite(optionsGroup, SWT.NONE);
        left.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false));
        left.setLayout(new GridLayout(3, false));

        Label label = new Label(left, SWT.NONE);
        label.setText(Messages.getString("JavaJobScriptsExportWSWizardPage.ExportyLabel")); //$NON-NLS-1$

        exportTypeCombo = new Combo(left, SWT.PUSH);
        GridData gd = new GridData();
        gd.horizontalSpan = 1;
        exportTypeCombo.setLayoutData(gd);

        exportTypeCombo.add(EXPORTTYPE_POJO);
        exportTypeCombo.add(EXPORTTYPE_WSWAR);
        exportTypeCombo.add(EXPORTTYPE_WSZIP);
        exportTypeCombo.add(EXPORTTYPE_JBOSSESB);
        // exportTypeCombo.add("JBI (JSR 208)");

        exportTypeCombo.setText(getCurrentExportType());
        if (exportTypeFixed != null) {
            left.setVisible(false);
            optionsGroup.setVisible(false);
            exportTypeCombo.setText(exportTypeFixed);
        }

        chkButton = new Button(left, SWT.CHECK);
        chkButton.setText(Messages.getString("JavaJobScriptsExportWSWizardPage.extractZipFile")); //$NON-NLS-1$
        if (exportTypeCombo.getText().equals(EXPORTTYPE_WSWAR)) {
            chkButton.setVisible(false);
            zipOption = null;

        } else {
            chkButton.setVisible(true);
            zipOption = String.valueOf(chkButton.getSelection());
        }
        chkButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                chkButton.setSelection(chkButton.getSelection());
                zipOption = String.valueOf(chkButton.getSelection());
            }
        });
        exportTypeCombo.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {

            }

            public void widgetSelected(SelectionEvent e) {
                destinationNameFieldInnerComposite.dispose();
                GridLayout layout = new GridLayout();
                destinationNameFieldInnerComposite = new Composite(destinationNameFieldComposite, SWT.NONE);
                GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
                destinationNameFieldInnerComposite.setLayoutData(gridData);
                destinationNameFieldInnerComposite.setLayout(layout);
                createDestinationGroup(destinationNameFieldInnerComposite);

                destinationNameFieldComposite.layout();

                optionsGroupComposite.dispose();
                createOptionsGroupButtons(pageComposite);

                pageComposite.layout();
                if (exportTypeCombo.getText().equals(EXPORTTYPE_WSWAR)) {
                    chkButton.setVisible(false);
                    zipOption = null;
                } else {
                    chkButton.setVisible(true);
                    zipOption = String.valueOf(chkButton.getSelection());
                }
                checkExport();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.exportjob.JavaJobScriptsExportWizardPage#createJobScriptsManager()
     */
    @Override
    public JobScriptsManager createJobScriptsManager() {
        ECodeLanguage language = LanguageManager.getCurrentLanguage();

        return JobScriptsManagerFactory.getInstance().createManagerInstance(language, getCurrentExportType());
    }

    @Override
    protected String getOutputSuffix() {
        if (getCurrentExportType().equals(EXPORTTYPE_WSWAR)) {
            return ".war"; //$NON-NLS-1$
        } else if (getCurrentExportType().equals(EXPORTTYPE_JBOSSESB)) {
            return ".esb"; //$NON-NLS-1$ 
        } else {
            return ".zip"; //$NON-NLS-1$
        }
    }

    /**
     * Open an appropriate destination browser so that the user can specify a source to import from.
     */
    @Override
    protected void handleDestinationBrowseButtonPressed() {
        FileDialog dialog = new FileDialog(getContainer().getShell(), SWT.SAVE);
        if (getCurrentExportType().equals(EXPORTTYPE_WSWAR)) {
            dialog.setFilterExtensions(new String[] { "*.war", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$
        } else if (getCurrentExportType().equals(EXPORTTYPE_JBOSSESB)) {
            dialog.setFilterExtensions(new String[] { "*.esb", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            dialog.setFilterExtensions(new String[] { "*.zip", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$
        }
        dialog.setText(""); //$NON-NLS-1$
        // this is changed by me shenhaize
        dialog.setFileName((String) this.getDefaultFileName().get(0));
        String currentSourceString = getDestinationValue();
        int lastSeparatorIndex = currentSourceString.lastIndexOf(File.separator);
        if (lastSeparatorIndex != -1) {
            dialog.setFilterPath(currentSourceString.substring(0, lastSeparatorIndex));
        }

        String selectedFileName = dialog.open();
        if (selectedFileName == null) {
            return;
        }
        if (!selectedFileName.endsWith(this.getOutputSuffix()))
            selectedFileName += this.getOutputSuffix();
        // when user change the name of job,will add the version auto
        if (selectedFileName != null && !selectedFileName.endsWith(this.getSelectedJobVersion() + this.getOutputSuffix())) {
            String b = selectedFileName.substring(0, (selectedFileName.length() - 4));
            File file = new File(b);

            String str = file.getName();

            String s = (String) this.getDefaultFileName().get(0);

            if (str.equals(s)) {
                selectedFileName = b + "_" + this.getDefaultFileName().get(1) + this.getOutputSuffix(); //$NON-NLS-1$
            } else {
                selectedFileName = b + this.getOutputSuffix();
            }

        }
        if (selectedFileName != null) {
            setErrorMessage(null);
            setDestinationValue(selectedFileName);
            if (getDialogSettings() != null) {
                IDialogSettings section = getDialogSettings().getSection(DESTINATION_FILE);//$NON-NLS-1$
                if (section == null) {
                    section = getDialogSettings().addNewSection(DESTINATION_FILE);//$NON-NLS-1$
                }
                section.put(DESTINATION_FILE, selectedFileName);//$NON-NLS-1$//$NON-NLS-1$
            }

        }
    }

    @Override
    public void handleEvent(Event e) {
        super.handleEvent(e);
        Widget source = e.widget;
        if (source instanceof Combo) {
            String destination = ((Combo) source).getText();
            if (getDialogSettings() != null) {
                IDialogSettings section = getDialogSettings().getSection(DESTINATION_FILE);//$NON-NLS-1$
                if (section == null) {
                    section = getDialogSettings().addNewSection(DESTINATION_FILE);//$NON-NLS-1$
                }
                section.put(DESTINATION_FILE, destination);//$NON-NLS-1$
            }
        }
    }

    protected void restoreWidgetValuesForESB() {
        IDialogSettings settings = getDialogSettings();
        if (settings != null) {
            String[] directoryNames = settings.getArray(STORE_DESTINATION_NAMES_ID);
            if (directoryNames != null) {
                // destination
                String filterName = ".esb"; //$NON-NLS-1$

                for (int i = 0; i < directoryNames.length; i++) {
                    if (directoryNames[i].toLowerCase().endsWith(filterName)) {
                        addDestinationItem(directoryNames[i]);

                    }
                }
            }
            setDefaultDestination();

            IDialogSettings section = getDialogSettings().getSection(DESTINATION_FILE);//$NON-NLS-1$ 
            if (section == null) {
                section = getDialogSettings().addNewSection(DESTINATION_FILE);//$NON-NLS-1$ 
            }
            if (exportDependencies != null && !exportDependencies.isDisposed()) {
                exportDependencies.setSelection(settings.getBoolean(STORE_DEPENDENCIES_ID));
            }
            if (jobScriptButton != null && !jobScriptButton.isDisposed()) {
                jobScriptButton.setSelection(settings.getBoolean(STORE_SOURCE_ID));
            }
            if (contextButton != null && !contextButton.isDisposed()) {
                contextButton.setSelection(settings.getBoolean(STORE_CONTEXT_ID));
            }
            if (applyToChildrenButton != null && !applyToChildrenButton.isDisposed()) {
                applyToChildrenButton.setSelection(settings.getBoolean(APPLY_TO_CHILDREN_ID));
            }
            if (jobItemButton != null && !jobItemButton.isDisposed()) {
                jobItemButton.setSelection(settings.getBoolean(STORE_JOB_ID));
            }

            if (section.get(ESB_EXPORT_TYPE) != null) {
                esbTypeCombo.setText(section.get(ESB_EXPORT_TYPE));
                if (section.get(ESB_SERVICE_NAME) != null) {
                    esbServiceName.setText(section.get(ESB_SERVICE_NAME));
                }
                if (section.get(ESB_CATEGORY) != null) {
                    esbCategory.setText(section.get(ESB_CATEGORY));
                }
                if (section.get(QUERY_MESSAGE_NAME) != null) {
                    this.esbQueueMessageName.setText(section.get(QUERY_MESSAGE_NAME));
                }
            }
        }

        if (process.length > 0 && contextCombo != null) {
            try {
                process[0].setProcess((ProcessItem) ProxyRepositoryFactory.getInstance().getUptodateProperty(
                        process[0].getItem().getProperty()).getItem());
            } catch (PersistenceException e) {
                e.printStackTrace();
            }
            List<String> contextNames = manager.getJobContexts((ProcessItem) process[0].getItem());
            contextCombo.setItems(contextNames.toArray(new String[contextNames.size()]));
            if (contextNames.size() > 0) {
                contextCombo.select(0);
            }
        }
    }

    protected void restoreWidgetValuesForWS() {
        IDialogSettings settings = getDialogSettings();
        if (settings != null) {
            String[] directoryNames = settings.getArray(STORE_DESTINATION_NAMES_ID);
            if (directoryNames != null) {
                // destination
                String filterName = ".zip"; //$NON-NLS-1$

                if (exportTypeCombo.getText().equals(EXPORTTYPE_WSWAR)) {
                    filterName = ".war"; //$NON-NLS-1$
                } else {
                    filterName = ".zip"; //$NON-NLS-1$
                }

                for (int i = 0; i < directoryNames.length; i++) {
                    if (directoryNames[i].toLowerCase().endsWith(filterName)) {
                        addDestinationItem(directoryNames[i]);

                    }
                }
            }
            setDefaultDestination();

            webXMLButton.setSelection(settings.getBoolean(STORE_WEBXML_ID));
            configFileButton.setSelection(settings.getBoolean(STORE_CONFIGFILE_ID));
            axisLibButton.setSelection(settings.getBoolean(STORE_AXISLIB_ID));
            wsddButton.setSelection(settings.getBoolean(STORE_WSDD_ID));
            wsdlButton.setSelection(settings.getBoolean(STORE_WSDL_ID));
            jobScriptButton.setSelection(settings.getBoolean(STORE_SOURCE_ID));
            contextButton.setSelection(settings.getBoolean(STORE_CONTEXT_ID));
            applyToChildrenButton.setSelection(settings.getBoolean(APPLY_TO_CHILDREN_ID));
            chkButton.setSelection(settings.getBoolean(EXTRACT_ZIP_FILE));
            if (chkButton.isVisible()) {
                zipOption = String.valueOf(chkButton.getSelection());
            } else {
                zipOption = "false"; //$NON-NLS-1$
            }

        }

        if (process.length > 0 && contextCombo != null) {
            try {
                process[0].setProcess((ProcessItem) ProxyRepositoryFactory.getInstance().getUptodateProperty(
                        process[0].getItem().getProperty()).getItem());
            } catch (PersistenceException e) {
                e.printStackTrace();
            }
            List<String> contextNames = manager.getJobContexts((ProcessItem) process[0].getItem());
            contextCombo.setItems(contextNames.toArray(new String[contextNames.size()]));
            if (contextNames.size() > 0) {
                contextCombo.select(0);
            }
        }
    }

    protected void restoreWidgetValuesForPOJO() {
        IDialogSettings settings = getDialogSettings();

        Property property = null;
        if (process.length == 1) {
            property = process[0].getItem().getProperty();
        }

        if (settings != null) {
            String[] directoryNames = settings.getArray(STORE_DESTINATION_NAMES_ID);
            if (directoryNames != null) {
                // destination
                for (int i = 0; i < directoryNames.length; i++) {
                    if (directoryNames[i].toLowerCase().endsWith(".zip")) { //$NON-NLS-1$
                        addDestinationItem(directoryNames[i]);
                    }
                }
            }

            setDefaultDestination();
            shellLauncherButton.setSelection(settings.getBoolean(STORE_SHELL_LAUNCHER_ID));
            systemRoutineButton.setSelection(settings.getBoolean(STORE_SYSTEM_ROUTINE_ID));
            userRoutineButton.setSelection(settings.getBoolean(STORE_USER_ROUTINE_ID));
            modelButton.setSelection(settings.getBoolean(STORE_MODEL_ID));
            jobItemButton.setSelection(settings.getBoolean(STORE_JOB_ID));
            exportDependencies.setEnabled(settings.getBoolean(STORE_JOB_ID));
            exportDependencies.setSelection(settings.getBoolean(STORE_DEPENDENCIES_ID));

            jobScriptButton.setSelection(settings.getBoolean(STORE_SOURCE_ID));
            contextButton.setSelection(settings.getBoolean(STORE_CONTEXT_ID));
            applyToChildrenButton.setSelection(settings.getBoolean(APPLY_TO_CHILDREN_ID));
            chkButton.setSelection(settings.getBoolean(EXTRACT_ZIP_FILE));
            zipOption = String.valueOf(chkButton.getSelection());
            // genCodeButton.setSelection(settings.getBoolean(STORE_GENERATECODE_ID));
        }

        launcherCombo.setItems(manager.getLauncher());
        if (manager.getLauncher().length > 0) {
            launcherCombo.select(0);
        }
        if (process.length > 0 && contextCombo != null) {
            try {
                process[0].setProcess((ProcessItem) ProxyRepositoryFactory.getInstance().getUptodateProperty(
                        process[0].getItem().getProperty()).getItem());
            } catch (PersistenceException e) {
                e.printStackTrace();
            }
            ProcessItem item = (ProcessItem) process[0].getItem();
            try {
                String id = item.getProperty().getId();
                IRepositoryObject lastVersion = ProxyRepositoryFactory.getInstance().getLastVersion(id);
                item = (ProcessItem) lastVersion.getProperty().getItem();
            } catch (PersistenceException e) {
                throw new RuntimeException(e);
            }
            List<String> contextNames;
            contextNames = manager.getJobContexts(item);

            contextCombo.setItems(contextNames.toArray(new String[contextNames.size()]));
            if (contextNames.size() > 0) {
                contextCombo.select(0);
            }
        }

    }

    @Override
    protected void internalSaveWidgetValues() {
        // update directory names history
        IDialogSettings settings = getDialogSettings();
        if (settings != null) {
            String[] directoryNames = settings.getArray(STORE_DESTINATION_NAMES_ID);
            if (directoryNames == null) {
                directoryNames = new String[0];
            }

            directoryNames = addToHistory(directoryNames, getDestinationValue());

            settings.put(STORE_EXPORTTYPE_ID, getCurrentExportType());
            settings.put(STORE_DESTINATION_NAMES_ID, directoryNames);
            if (contextButton != null) {
                settings.put(STORE_CONTEXT_ID, contextButton.getSelection());
            }
            if (applyToChildrenButton != null) {
                settings.put(APPLY_TO_CHILDREN_ID, applyToChildrenButton.getSelection());
            }
            if (jobScriptButton != null && !jobScriptButton.isDisposed()) {
                settings.put(STORE_SOURCE_ID, jobScriptButton.getSelection());
            }
            if (jobItemButton != null && !jobItemButton.isDisposed()) {
                settings.put(STORE_JOB_ID, jobItemButton.getSelection());
            }
            if (exportDependencies != null && !exportDependencies.isDisposed()) {
                settings.put(STORE_DEPENDENCIES_ID, exportDependencies.getSelection());
            }

            if (getCurrentExportType().equals(EXPORTTYPE_POJO)) {
                settings.put(STORE_SHELL_LAUNCHER_ID, shellLauncherButton.getSelection());
                settings.put(STORE_SYSTEM_ROUTINE_ID, systemRoutineButton.getSelection());
                settings.put(STORE_USER_ROUTINE_ID, userRoutineButton.getSelection());
                settings.put(STORE_MODEL_ID, modelButton.getSelection());
                settings.put(EXTRACT_ZIP_FILE, chkButton.getSelection());
                return;
            } else if (getCurrentExportType().equals(EXPORTTYPE_WSZIP)) {
                settings.put(STORE_WEBXML_ID, webXMLButton.getSelection());
                settings.put(STORE_CONFIGFILE_ID, configFileButton.getSelection());
                settings.put(STORE_AXISLIB_ID, axisLibButton.getSelection());
                settings.put(STORE_WSDD_ID, wsddButton.getSelection());
                settings.put(STORE_WSDL_ID, wsdlButton.getSelection());
                settings.put(EXTRACT_ZIP_FILE, chkButton.getSelection());
            }

        }
    }

    @Override
    protected Map<ExportChoice, Object> getExportChoiceMap() {

        if (exportTypeCombo.getText().equals(EXPORTTYPE_POJO)) {
            return JavaJobScriptsExportWSWizardPage.super.getExportChoiceMap();
        }
        Map<ExportChoice, Object> exportChoiceMap = new EnumMap<ExportChoice, Object>(ExportChoice.class);
        exportChoiceMap.put(ExportChoice.needJobItem, false);
        exportChoiceMap.put(ExportChoice.needSourceCode, false);

        if (exportTypeCombo.getText().equals(EXPORTTYPE_JBOSSESB)) {
            exportChoiceMap.put(ExportChoice.needMetaInfo, true);
            exportChoiceMap.put(ExportChoice.needContext, contextButton.getSelection());
            exportChoiceMap.put(ExportChoice.esbQueueMessageName, esbQueueMessageName.getText());
            exportChoiceMap.put(ExportChoice.esbServiceName, esbServiceName.getText());
            exportChoiceMap.put(ExportChoice.esbCategory, esbCategory.getText());
            exportChoiceMap.put(ExportChoice.esbExportType, esbTypeCombo.getText());
            exportChoiceMap.put(ExportChoice.needDependencies, exportDependencies.getSelection());
            exportChoiceMap.put(ExportChoice.needJobItem, jobItemButton.getSelection());
            exportChoiceMap.put(ExportChoice.needSourceCode, jobItemButton.getSelection()); // take source code also
            // when take item
            return exportChoiceMap;
        }

        if (exportTypeCombo.getText().equals(EXPORTTYPE_WSWAR)) {
            exportChoiceMap.put(ExportChoice.needMetaInfo, true);
        } else {
            exportChoiceMap.put(ExportChoice.needMetaInfo, false);
        }
        // fix bug 9150, export items and code source, added by nma
        exportChoiceMap.put(ExportChoice.needJobItem, jobScriptButton.getSelection());
        exportChoiceMap.put(ExportChoice.needSourceCode, jobScriptButton.getSelection());

        exportChoiceMap.put(ExportChoice.needWEBXML, webXMLButton.getSelection());
        exportChoiceMap.put(ExportChoice.needCONFIGFILE, configFileButton.getSelection());
        exportChoiceMap.put(ExportChoice.needAXISLIB, axisLibButton.getSelection());
        exportChoiceMap.put(ExportChoice.needWSDD, wsddButton.getSelection());
        exportChoiceMap.put(ExportChoice.needWSDL, wsdlButton.getSelection());
        exportChoiceMap.put(ExportChoice.needJobScript, jobScriptButton.getSelection());
        exportChoiceMap.put(ExportChoice.needContext, contextButton.getSelection());
        exportChoiceMap.put(ExportChoice.applyToChildren, applyToChildrenButton.getSelection());
        return exportChoiceMap;
    }

    protected void createOptionsGroupButtons(Composite parent) {

        GridLayout layout = new GridLayout();
        optionsGroupComposite = new Composite(parent, SWT.NONE);
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        gridData.heightHint = 200;
        optionsGroupComposite.setLayoutData(gridData);
        optionsGroupComposite.setLayout(layout);
        // options group
        Group optionsGroup = new Group(optionsGroupComposite, SWT.NONE);

        optionsGroup.setLayout(layout);

        optionsGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));

        optionsGroup.setText(IDEWorkbenchMessages.WizardExportPage_options);
        optionsGroup.setFont(parent.getFont());

        Font font = optionsGroup.getFont();
        optionsGroup.setLayout(new GridLayout(1, true));

        Composite left = new Composite(optionsGroup, SWT.NONE);
        gridData = new GridData(SWT.LEFT, SWT.TOP, true, false);
        left.setLayoutData(gridData);
        left.setLayout(new GridLayout(3, true));

        if (getCurrentExportType().equals(EXPORTTYPE_POJO)) {
            createOptions(left, font);
            restoreWidgetValuesForPOJO();
        } else if (getCurrentExportType().equals(EXPORTTYPE_JBOSSESB)) {
            contextButton = new Button(left, SWT.CHECK | SWT.LEFT);
            contextButton.setText(Messages.getString("JobScriptsExportWizardPage.contextPerlScripts")); //$NON-NLS-1$
            contextButton.setSelection(true);
            contextButton.setFont(font);

            String jobLabel = ""; //$NON-NLS-1$
            contextCombo = new Combo(left, SWT.PUSH);
            if (process.length > 0) {
                try {
                    process[0].setProcess((ProcessItem) ProxyRepositoryFactory.getInstance().getUptodateProperty(
                            process[0].getItem().getProperty()).getItem());
                } catch (PersistenceException e) {
                    e.printStackTrace();
                }
                jobLabel = (process[0].getItem()).getProperty().getLabel();
                List<String> contextNames = manager.getJobContexts((ProcessItem) process[0].getItem());
                contextCombo.setItems(contextNames.toArray(new String[contextNames.size()]));
                if (contextNames.size() > 0) {
                    contextCombo.select(0);
                }
            }

            applyToChildrenButton = new Button(left, SWT.CHECK | SWT.LEFT);
            applyToChildrenButton.setText(Messages.getString("JavaJobScriptsExportWSWizardPage.ApplyToChildren")); //$NON-NLS-1$
            applyToChildrenButton.setSelection(true);

            jobItemButton = new Button(left, SWT.CHECK | SWT.LEFT);
            jobItemButton.setText(Messages.getString("JobScriptsExportWizardPage.sourceFiles")); //$NON-NLS-1$
            jobItemButton.setSelection(true);
            jobItemButton.setFont(font);
            GridData gd = new GridData(GridData.FILL_HORIZONTAL);
            gd.horizontalSpan = 1;
            jobItemButton.setLayoutData(gd);

            exportDependencies = new Button(left, SWT.CHECK);
            exportDependencies.setText("Export Dependencies"); //$NON-NLS-1$
            exportDependencies.setFont(font);
            gd = new GridData(GridData.FILL_HORIZONTAL);
            gd.horizontalSpan = 2;
            jobItemButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {

                    exportDependencies.setEnabled(jobItemButton.getSelection());
                    if (!jobItemButton.getSelection()) {
                        exportDependencies.setSelection(false);
                    }
                }
            });
            exportDependencies.setLayoutData(gd);

            Label esbTypeLabel = new Label(left, SWT.None);
            esbTypeLabel.setText(Messages.getString("JavaJobScriptsExportWSWizardPage.esbExportTypeLabel")); //$NON-NLS-1$

            esbTypeCombo = new Combo(left, SWT.PUSH);
            gd = new GridData();
            gd.horizontalSpan = 2;
            esbTypeCombo.setLayoutData(gd);

            esbTypeCombo.add(ESBTYPE_JBOSS_MQ);
            esbTypeCombo.add(ESBTYPE_JBOSS_MESSAGING);
            esbTypeCombo.select(0);

            Label esbServiceNameLabel = new Label(left, SWT.RIGHT);
            esbServiceNameLabel.setText(Messages.getString("JavaJobScriptsExportWSWizardPage.esbServiceNameLabel")); //$NON-NLS-1$

            esbServiceName = new Text(left, SWT.BORDER);
            esbServiceName.setText("DefaultServiceName"); //$NON-NLS-1$
            gd = new GridData(GridData.FILL_HORIZONTAL);
            gd.horizontalSpan = 2;
            esbServiceName.setLayoutData(gd);

            Label esbCategoryLabel = new Label(left, SWT.None);
            esbCategoryLabel.setText(Messages.getString("JavaJobScriptsExportWSWizardPage.esbCategoryLabel")); //$NON-NLS-1$

            esbCategory = new Text(left, SWT.BORDER);
            esbCategory.setText("DefaultCategory"); //$NON-NLS-1$
            gd = new GridData(GridData.FILL_HORIZONTAL);
            gd.horizontalSpan = 2;
            esbCategory.setLayoutData(gd);

            Label queueLabel = new Label(left, SWT.None);
            queueLabel.setText(Messages.getString("JavaJobScriptsExportWSWizardPage.queueName")); //$NON-NLS-1$

            esbQueueMessageName = new Text(left, SWT.BORDER);
            esbQueueMessageName.setText(Messages.getString("JavaJobScriptsExportWSWizardPage.actionRequest", jobLabel)); //$NON-NLS-1$
            gd = new GridData(GridData.FILL_HORIZONTAL);
            gd.horizontalSpan = 2;
            esbQueueMessageName.setLayoutData(gd);
            restoreWidgetValuesForESB();
        } else {
            createOptionsForWS(left, font);
        }

    }

    protected void createOptionsForWS(Composite optionsGroup, Font font) {

        webXMLButton = new Button(optionsGroup, SWT.CHECK | SWT.LEFT);
        webXMLButton.setText(Messages.getString("JavaJobScriptsExportWSWizardPage.WEBXML")); //$NON-NLS-1$
        webXMLButton.setFont(font);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 1;
        webXMLButton.setLayoutData(gd);
        webXMLButton.setSelection(true);

        configFileButton = new Button(optionsGroup, SWT.CHECK | SWT.LEFT);
        configFileButton.setText(Messages.getString("JavaJobScriptsExportWSWizardPage.ServerConfigFile")); //$NON-NLS-1$
        configFileButton.setFont(font);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        configFileButton.setLayoutData(gd);
        configFileButton.setSelection(true);

        wsddButton = new Button(optionsGroup, SWT.CHECK | SWT.LEFT);
        wsddButton.setText(Messages.getString("JavaJobScriptsExportWSWizardPage.WSDDFile")); //$NON-NLS-1$
        wsddButton.setFont(font);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 1;
        wsddButton.setLayoutData(gd);
        wsddButton.setSelection(true);

        wsdlButton = new Button(optionsGroup, SWT.CHECK | SWT.LEFT);
        wsdlButton.setText(Messages.getString("JavaJobScriptsExportWSWizardPage.WSDLFile")); //$NON-NLS-1$
        wsdlButton.setFont(font);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        wsdlButton.setLayoutData(gd);
        wsdlButton.setSelection(true);

        jobScriptButton = new Button(optionsGroup, SWT.CHECK | SWT.LEFT);
        jobScriptButton.setText(Messages.getString("JobScriptsExportWizardPage.sourceFiles")); //$NON-NLS-1$
        jobScriptButton.setSelection(true);
        jobScriptButton.setFont(font);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 1;
        jobScriptButton.setLayoutData(gd);

        axisLibButton = new Button(optionsGroup, SWT.CHECK | SWT.LEFT);
        axisLibButton.setText(Messages.getString("JavaJobScriptsExportWSWizardPage.AxisLib")); //$NON-NLS-1$
        axisLibButton.setFont(font);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        axisLibButton.setLayoutData(gd);
        axisLibButton.setSelection(true);

        contextButton = new Button(optionsGroup, SWT.CHECK | SWT.LEFT);
        contextButton.setText(Messages.getString("JobScriptsExportWizardPage.contextPerlScripts")); //$NON-NLS-1$
        contextButton.setSelection(true);
        contextButton.setFont(font);

        contextCombo = new Combo(optionsGroup, SWT.PUSH);

        applyToChildrenButton = new Button(optionsGroup, SWT.CHECK | SWT.LEFT);
        applyToChildrenButton.setText(Messages.getString("JavaJobScriptsExportWSWizardPage.ApplyToChildren")); //$NON-NLS-1$
        applyToChildrenButton.setSelection(true);

        restoreWidgetValuesForWS();

        if (exportTypeCombo.getText().equals(EXPORTTYPE_WSWAR)) {
            webXMLButton.setEnabled(false);
            webXMLButton.setSelection(true);
            configFileButton.setEnabled(false);
            configFileButton.setSelection(true);
            wsddButton.setEnabled(false);
            wsddButton.setSelection(true);
            wsdlButton.setEnabled(false);
            wsdlButton.setSelection(true);
            jobScriptButton.setEnabled(false);
            jobScriptButton.setSelection(true);
            axisLibButton.setEnabled(false);
            axisLibButton.setSelection(true);
            contextButton.setEnabled(false);
            contextButton.setSelection(true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.exportjob.JavaJobScriptsExportWizardPage#getExportResources()
     */
    @Override
    public List<ExportFileResource> getExportResources() throws ProcessorException {
        Map<ExportChoice, Object> exportChoiceMap = getExportChoiceMap();
        if (getCurrentExportType().equals(EXPORTTYPE_POJO)) {
            return manager.getExportResources(process, exportChoiceMap, contextCombo.getText(), launcherCombo.getText(),
                    IProcessor.NO_STATISTICS, IProcessor.NO_TRACES);
        } else {
            return manager.getExportResources(process, exportChoiceMap,
                    contextCombo == null ? "Default" : contextCombo.getText(), "all", //$NON-NLS-1$  //$NON-NLS-2$
                    IProcessor.NO_STATISTICS, IProcessor.NO_TRACES);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.exportjob.JobScriptsExportWizardPage#setTopFolder(java.util.List,
     * java.lang.String)
     */
    @Override
    public void setTopFolder(List<ExportFileResource> resourcesToExport, String topFolder) {
        if (getCurrentExportType().equals(EXPORTTYPE_WSWAR) || getCurrentExportType().equals(EXPORTTYPE_WSZIP)
                || getCurrentExportType().equals(EXPORTTYPE_JBOSSESB)) {
            return;
        }
        for (ExportFileResource fileResource : resourcesToExport) {
            String directory = fileResource.getDirectoryName();
            fileResource.setDirectoryName(topFolder + "/" + directory); //$NON-NLS-1$
        }
    }

    public String getExtractOption() {
        if (chkButton != null) {
            return String.valueOf(chkButton.getSelection());
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.exportjob.JobScriptsExportWizardPage#checkExport()
     */
    @Override
    public boolean checkExport() {
        boolean noError = true;
        this.setErrorMessage(null);
        this.setPageComplete(true);
        if (getCurrentExportType().equals(EXPORTTYPE_JBOSSESB)) {
            if (this.isMultiNodes()) {
                StringBuffer buff = new StringBuffer();
                buff.append(Messages.getString("JavaJobScriptsExportWSWizardPage.singleJobExport")); //$NON-NLS-1$
                this.setErrorMessage(buff.toString());
                this.setPageComplete(false);
                noError = false;
            }

            // check if the needed librairy is installed.
            String requiredJar = "jbossesb-rosetta.jar"; //$NON-NLS-1$

            List<ModuleNeeded> toCheck = ModulesNeededProvider.getModulesNeeded();
            for (ModuleNeeded current : toCheck) {
                if (requiredJar.equals(current.getModuleName())) {
                    if (current.getStatus() == ELibraryInstallStatus.NOT_INSTALLED) {
                        StringBuffer buff = new StringBuffer();
                        buff.append(Messages.getString("JavaJobScriptsExportWSWizardPage.exportForJBoss")); //$NON-NLS-1$
                        buff.append(Messages.getString("JavaJobScriptsExportWSWizardPage.checkVersion")); //$NON-NLS-1$

                        this.setErrorMessage(buff.toString());
                        this.setPageComplete(false);
                        noError = false;
                    }
                }
            }
        }
        if (getCheckNodes().length == 0) {
            StringBuffer buff = new StringBuffer();
            buff.append(Messages.getString("JavaJobScriptsExportWSWizardPage.needOneJobSelected")); //$NON-NLS-1$
            this.setErrorMessage(buff.toString());
            this.setPageComplete(false);
            noError = false;
        }

        return noError;
    }

    @Override
    public boolean finish() {
        if (exportTypeCombo != null && exportTypeCombo.getText().equals(EXPORTTYPE_JBOSSESB)) {
            if (getDialogSettings() != null) {
                IDialogSettings section = getDialogSettings().getSection(DESTINATION_FILE);//$NON-NLS-1$ 
                if (section == null) {
                    section = getDialogSettings().addNewSection(DESTINATION_FILE);//$NON-NLS-1$ 
                }
                section.put(ESB_EXPORT_TYPE, this.esbTypeCombo.getText());//$NON-NLS-1$//$NON-NLS-1$ 
                section.put(ESB_SERVICE_NAME, this.esbServiceName.getText());
                section.put(ESB_CATEGORY, this.esbCategory.getText());
                section.put(QUERY_MESSAGE_NAME, this.esbQueueMessageName.getText());
            }

        }
        return super.finish();
    }

}
