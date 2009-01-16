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
package org.talend.repository.ui.wizards.exportjob;

import java.io.File;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.designer.runprocess.IProcessor;
import org.talend.repository.documentation.ExportFileResource;
import org.talend.repository.i18n.Messages;
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

    public static final String EXPORTTYPE_JBI = "JBI (JSR 208)"; //$NON-NLS-1$

    protected Combo exportTypeCombo;

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

    public static final String STORE_EXPORTTYPE_ID = "JavaJobScriptsExportWizardPage.STORE_EXPORTTYPE_ID"; //$NON-NLS-1$

    public static final String STORE_WEBXML_ID = "JavaJobScriptsExportWizardPage.STORE_WEBXML_ID"; //$NON-NLS-1$

    public static final String STORE_CONFIGFILE_ID = "JavaJobScriptsExportWizardPage.STORE_CONFIGFILE_ID"; //$NON-NLS-1$

    public static final String STORE_AXISLIB_ID = "JavaJobScriptsExportWizardPage.STORE_AXISLIB_ID"; //$NON-NLS-1$

    public static final String STORE_WSDD_ID = "JavaJobScriptsExportWizardPage.STORE_WSDD_ID"; //$NON-NLS-1$

    public static final String STORE_WSDL_ID = "JavaJobScriptsExportWizardPage.STORE_WSDL_ID"; //$NON-NLS-1$

    public static final String EXTRACT_ZIP_FILE = "JavaJobScriptsExportWizardPage.EXTRACT_ZIP_FILE"; //$NON-NLS-1$

    public JavaJobScriptsExportWSWizardPage(IStructuredSelection selection) {
        super(selection);
        // there assign the manager again
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

        pageComposite = new Composite(parent, SWT.NULL);
        pageComposite.setLayout(new GridLayout());
        pageComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
        pageComposite.setFont(parent.getFont());

        GridLayout layout = new GridLayout();
        destinationNameFieldComposite = new Composite(pageComposite, SWT.NONE);
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        destinationNameFieldComposite.setLayoutData(gridData);
        destinationNameFieldComposite.setLayout(layout);

        destinationNameFieldInnerComposite = new Composite(destinationNameFieldComposite, SWT.NONE);
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        destinationNameFieldInnerComposite.setLayoutData(gridData);
        destinationNameFieldInnerComposite.setLayout(layout);

        createDestinationGroup(destinationNameFieldInnerComposite);

        if (!isMultiNodes()) {
            createJobVersionGroup(pageComposite);
        }

        createExportTypeGroup(pageComposite);

        createOptionsGroupButtons(pageComposite);

        restoreResourceSpecificationWidgetValues(); // ie.- local

        updateWidgetEnablements();
        setPageComplete(determinePageCompletion());
        setErrorMessage(null); // should not initially have error message

        setControl(pageComposite);
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
        // exportTypeCombo.add("JBI (JSR 208)");

        exportTypeCombo.setText(getCurrentExportType());
        chkButton = new Button(left, SWT.CHECK);
        chkButton.setText("Extract the zip file");
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
        // TODO Auto-generated method stub
        ECodeLanguage language = LanguageManager.getCurrentLanguage();

        return JobScriptsManagerFactory.getInstance().createManagerInstance(language, getCurrentExportType());
    }

    @Override
    protected String getOutputSuffix() {
        if (getCurrentExportType().equals(EXPORTTYPE_WSWAR)) {
            return ".war"; //$NON-NLS-1$
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
        // when user change the name of job,will add the version auto
        if (!selectedFileName.endsWith(this.getSelectedJobVersion() + this.getOutputSuffix())) {
            // String b = selectedFileName.substring(0, (selectedFileName.length() - 4));
            // if (this.getSelectedJobVersion() != null) O{
            // selectedFileName = b + this.getSelectedJobVersion() + this.getOutputSuffix();
            // } else {
            //
            // // String version = processItem.getProperty().getVersion();
            // selectedFileName = b + this.getOutputSuffix();
            //
            // }

            String b = selectedFileName.substring(0, (selectedFileName.length() - 4));
            File file = new File(b);

            String str = file.getName();

            String s = (String) this.getDefaultFileName().get(0);

            if (str.equals(s)) {
                selectedFileName = b + "_" + this.getDefaultFileName().get(1) + this.getOutputSuffix();
            } else {
                selectedFileName = b + this.getOutputSuffix();
            }

        }
        if (selectedFileName != null) {
            setErrorMessage(null);
            setDestinationValue(selectedFileName);

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
            sourceButton.setSelection(settings.getBoolean(STORE_SOURCE_ID));
            contextButton.setSelection(settings.getBoolean(STORE_CONTEXT_ID));
            applyToChildrenButton.setSelection(settings.getBoolean(APPLY_TO_CHILDREN_ID));
            chkButton.setSelection(settings.getBoolean(EXTRACT_ZIP_FILE));
            zipOption = String.valueOf(chkButton.getSelection());
        }

        if (process.length > 0) {
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
            jobButton.setSelection(settings.getBoolean(STORE_JOB_ID));
            exportDependencies.setEnabled(settings.getBoolean(STORE_JOB_ID));
            exportDependencies.setSelection(settings.getBoolean(STORE_DEPENDENCIES_ID));

            sourceButton.setSelection(settings.getBoolean(STORE_SOURCE_ID));
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
        if (process.length > 0) {
            List<String> contextNames = manager.getJobContexts((ProcessItem) process[0].getItem());
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

            settings.put(STORE_EXPORTTYPE_ID, exportTypeCombo.getText());
            settings.put(STORE_DESTINATION_NAMES_ID, directoryNames);

            if (exportTypeCombo.getText().equals(EXPORTTYPE_POJO)) {
                settings.put(STORE_SOURCE_ID, sourceButton.getSelection());
                settings.put(STORE_CONTEXT_ID, contextButton.getSelection());
                settings.put(APPLY_TO_CHILDREN_ID, applyToChildrenButton.getSelection());
                settings.put(STORE_SHELL_LAUNCHER_ID, shellLauncherButton.getSelection());
                settings.put(STORE_SYSTEM_ROUTINE_ID, systemRoutineButton.getSelection());
                settings.put(STORE_USER_ROUTINE_ID, userRoutineButton.getSelection());
                settings.put(STORE_MODEL_ID, modelButton.getSelection());
                settings.put(STORE_JOB_ID, jobButton.getSelection());
                settings.put(STORE_DEPENDENCIES_ID, exportDependencies.getSelection());
                settings.put(EXTRACT_ZIP_FILE, chkButton.getSelection());
                return;
            } else if (exportTypeCombo.getText().equals(EXPORTTYPE_WSZIP)) {
                settings.put(STORE_SOURCE_ID, sourceButton.getSelection());
                settings.put(STORE_CONTEXT_ID, contextButton.getSelection());
                settings.put(APPLY_TO_CHILDREN_ID, applyToChildrenButton.getSelection());
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
    protected Map<ExportChoice, Boolean> getExportChoiceMap() {

        if (exportTypeCombo.getText().equals(EXPORTTYPE_POJO)) {
            return JavaJobScriptsExportWSWizardPage.super.getExportChoiceMap();
        }
        Map<ExportChoice, Boolean> exportChoiceMap = new EnumMap<ExportChoice, Boolean>(ExportChoice.class);

        if (exportTypeCombo.getText().equals(EXPORTTYPE_WSWAR)) {
            exportChoiceMap.put(ExportChoice.needMetaInfo, true);
        } else {
            exportChoiceMap.put(ExportChoice.needMetaInfo, false);
        }

        exportChoiceMap.put(ExportChoice.needWEBXML, webXMLButton.getSelection());
        exportChoiceMap.put(ExportChoice.needCONFIGFILE, configFileButton.getSelection());
        exportChoiceMap.put(ExportChoice.needAXISLIB, axisLibButton.getSelection());
        exportChoiceMap.put(ExportChoice.needWSDD, wsddButton.getSelection());
        exportChoiceMap.put(ExportChoice.needWSDL, wsdlButton.getSelection());
        exportChoiceMap.put(ExportChoice.needSource, sourceButton.getSelection());
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

        sourceButton = new Button(optionsGroup, SWT.CHECK | SWT.LEFT);
        sourceButton.setText(Messages.getString("JobScriptsExportWizardPage.sourceFiles")); //$NON-NLS-1$
        sourceButton.setSelection(true);
        sourceButton.setFont(font);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 1;
        sourceButton.setLayoutData(gd);

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
            sourceButton.setEnabled(false);
            sourceButton.setSelection(true);
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
    public List<ExportFileResource> getExportResources() {
        Map<ExportChoice, Boolean> exportChoiceMap = getExportChoiceMap();
        if (exportTypeCombo.getText().equals(EXPORTTYPE_POJO)) {
            return manager.getExportResources(process, exportChoiceMap, contextCombo.getText(), launcherCombo.getText(),
                    IProcessor.NO_STATISTICS, IProcessor.NO_TRACES);
        } else {
            return manager.getExportResources(process, exportChoiceMap, contextCombo.getText(), "all", //$NON-NLS-1$
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
        if (exportTypeCombo.getText().equals(EXPORTTYPE_WSWAR) || exportTypeCombo.getText().equals(EXPORTTYPE_WSZIP)) {
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
}
