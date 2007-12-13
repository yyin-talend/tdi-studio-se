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
package org.talend.componentdesigner.ui.wizard;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.componentdesigner.PluginConstant;
import org.talend.componentdesigner.model.enumtype.JetFileStamp;

/**
 * @author rli
 * 
 */
public class WizardJetFilesChoosePage extends WizardPage {

    private Button useBeginButton;

    private Button useMainButton;

    private Button useEndButton;

    private Label imageDirectoryLabel;

    private Text imageDirectoryText;

    private Button viewImageButton;

    private Button addLibCheckButton;

    private Button viewLibButton;

    private Label libDirectoryLabel;

    private Text libDirectoryText;

    private Button browserImageButton;

    private PropertyChangeBean propertyChangeBean;

    /**
     * @param pageName
     */
    public WizardJetFilesChoosePage(String pageName) {
        super(pageName);
        propertyChangeBean = new PropertyChangeBean();
    }

    PropertyChangeBean getPropertyChangeBean() {
        return propertyChangeBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        Composite groupsComposite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        groupsComposite.setLayout(layout);
        groupsComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

        creatFilesSelectionGrp(groupsComposite);
        creatImageSelectionGrp(groupsComposite);
        creatLibSelectionGrp(groupsComposite);

        this.setControl(groupsComposite);

    }

    private void creatFilesSelectionGrp(Composite groupsComposite) {
        Group filesSelectionGrp = new Group(groupsComposite, SWT.NONE);
        filesSelectionGrp.setText("Choose which jet files are needed"); //$NON-NLS-1$
        // GridDataFactory.fillDefaults().span(1, 1).grab(true, false).applyTo(
        // filesSelectionGrp);
        filesSelectionGrp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout groupLayout = new GridLayout(1, false);
        filesSelectionGrp.setLayout(groupLayout);

        // create the language selection check button
        useBeginButton = new Button(filesSelectionGrp, SWT.CHECK | SWT.RIGHT);
        useBeginButton.setText("Begin File");
        useBeginButton.setSelection(true);
        useMainButton = new Button(filesSelectionGrp, SWT.CHECK | SWT.RIGHT);
        useMainButton.setText("Main File");
        useEndButton = new Button(filesSelectionGrp, SWT.CHECK | SWT.RIGHT);
        useEndButton.setText("End File");

        SelectionListener listener = new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                setPageComplete(validPage());
                List<JetFileStamp> jetFileTypes = JetFileStamp.findFileStamps(useBeginButton.getSelection(), useMainButton
                        .getSelection(), useEndButton.getSelection());
                propertyChangeBean.firePropertyChange(PluginConstant.JETFILETYPE_PROPERTY, null, jetFileTypes);
            }
        };
        useBeginButton.addSelectionListener(listener);
        useMainButton.addSelectionListener(listener);
        useEndButton.addSelectionListener(listener);
        List<JetFileStamp> jetFileTypes = JetFileStamp.findFileStamps(true, false, false);
        propertyChangeBean.firePropertyChange(PluginConstant.JETFILETYPE_PROPERTY, null, jetFileTypes);
    }

    private void creatImageSelectionGrp(Composite groupsComposite) {
        Group imageSelectionGrp = new Group(groupsComposite, SWT.NONE);
        imageSelectionGrp.setText("Choose Image"); //$NON-NLS-1$
        // GridDataFactory.fillDefaults().span(1, 1).grab(true, false).applyTo(
        // filesSelectionGrp);
        imageSelectionGrp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout groupLayout = new GridLayout(9, false);
        imageSelectionGrp.setLayout(groupLayout);

        // create the image selection check button
        final Button defaultImageButton = new Button(imageSelectionGrp, SWT.RADIO | SWT.RIGHT);
        GridData imageGridData = new GridData();
        imageGridData.horizontalSpan = 9;
        defaultImageButton.setLayoutData(imageGridData);
        defaultImageButton.setText("Use the default image");
        defaultImageButton.setSelection(true);
        browserImageButton = new Button(imageSelectionGrp, SWT.RADIO | SWT.RIGHT);
        browserImageButton.setText("Use the image from exist directory");
        imageGridData = new GridData();
        imageGridData.horizontalSpan = 9;
        browserImageButton.setLayoutData(imageGridData);

        SelectionListener listener = new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                setImageDirectoryEnable(browserImageButton.getSelection());
                setPageComplete(validPage());
            }
        };
        defaultImageButton.addSelectionListener(listener);
        browserImageButton.addSelectionListener(listener);

        imageDirectoryLabel = new Label(imageSelectionGrp, SWT.NONE);
        GridData labelGridData = new GridData();
        labelGridData.horizontalSpan = 2;
        imageDirectoryLabel.setLayoutData(labelGridData);
        imageDirectoryLabel.setText("Directory:");

        imageDirectoryText = new Text(imageSelectionGrp, SWT.BORDER);
        GridData textGridData = new GridData(GridData.FILL_HORIZONTAL);
        textGridData.horizontalSpan = 6;
        imageDirectoryText.setLayoutData(textGridData);

        imageDirectoryText.addModifyListener(new ModifyListener() {
            
            /* (non-Javadoc)
             * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
             */
            public void modifyText(ModifyEvent e) {
                setPageComplete(validPage());
                propertyChangeBean.firePropertyChange(PluginConstant.IMAGE_PROPERTY, null, imageDirectoryText.getText());
            }

        });

        viewImageButton = new Button(imageSelectionGrp, SWT.UP);
        GridData buttonGridData = new GridData();
        buttonGridData.horizontalSpan = 1;
        viewImageButton.setLayoutData(buttonGridData);
        viewImageButton.setText("Browser..");
        viewImageButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                imageDirectoryText.setText(getPathFromDialog(new String[] { "*.*", "*.jpg", "*.gif" }));
                setPageComplete(validPage());
            }
        });

        setImageDirectoryEnable(browserImageButton.getSelection());
    }

    private void creatLibSelectionGrp(Composite groupsComposite) {
        Group libSelectionGrp = new Group(groupsComposite, SWT.NONE);
        libSelectionGrp.setText("Add library"); //$NON-NLS-1$
        // GridDataFactory.fillDefaults().span(1, 1).grab(true, false).applyTo(
        // filesSelectionGrp);
        libSelectionGrp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout groupLayout = new GridLayout(9, false);
        libSelectionGrp.setLayout(groupLayout);

        addLibCheckButton = new Button(libSelectionGrp, SWT.CHECK | SWT.RIGHT);
        GridData imageGridData = new GridData();
        imageGridData.horizontalSpan = 9;
        addLibCheckButton.setLayoutData(imageGridData);
        addLibCheckButton.setText("Add library from exist resource");
        SelectionListener listener = new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                setLibDirectoryEnable(addLibCheckButton.getSelection());
                setPageComplete(validPage());
            }
        };
        addLibCheckButton.addSelectionListener(listener);

        libDirectoryLabel = new Label(libSelectionGrp, SWT.NONE);
        GridData labelGridData = new GridData();
        labelGridData.horizontalSpan = 2;
        libDirectoryLabel.setLayoutData(labelGridData);
        libDirectoryLabel.setText("Directory:");

        libDirectoryText = new Text(libSelectionGrp, SWT.BORDER);
        GridData textGridData = new GridData(GridData.FILL_HORIZONTAL);
        textGridData.horizontalSpan = 6;
        libDirectoryText.setLayoutData(textGridData);

        libDirectoryText.addModifyListener(new ModifyListener() {

            /* (non-Javadoc)
             * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
             */
            public void modifyText(ModifyEvent e) {
                setPageComplete(validPage());
                propertyChangeBean.firePropertyChange(PluginConstant.LIBRARY_PROPERTY, null, libDirectoryText.getText());
            }

        });

        viewLibButton = new Button(libSelectionGrp, SWT.UP);
        GridData buttonGridData = new GridData();
        buttonGridData.horizontalSpan = 1;
        viewLibButton.setLayoutData(buttonGridData);
        viewLibButton.setText("Browser..");
        viewLibButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                libDirectoryText.setText(getPathFromDialog(new String[] { "*.*", "*.zip", "*.jar", "*.pm" }));
            }
        });
        setLibDirectoryEnable(false);
    }

    private String getPathFromDialog(String[] extensions) {
        FileDialog fileDialog = new FileDialog(this.getShell(), SWT.OPEN);
        fileDialog.setFilterPath(null);
        fileDialog.setFilterExtensions(extensions); //$NON-NLS-1$ //$NON-NLS-2$
        String filePath = fileDialog.open();
        if (filePath == null) {
            return ""; //$NON-NLS-1$
        }
        return filePath;
    }

    private void setImageDirectoryEnable(boolean enabled) {
        viewImageButton.setEnabled(enabled);
        imageDirectoryText.setEnabled(enabled);
        imageDirectoryLabel.setEnabled(enabled);
    }

    private void setLibDirectoryEnable(boolean enabled) {
        viewLibButton.setEnabled(enabled);
        libDirectoryText.setEnabled(enabled);
        libDirectoryLabel.setEnabled(enabled);
    }

    protected boolean validPage() {
        if (!(useBeginButton.getSelection() || useMainButton.getSelection() || useEndButton.getSelection())) {
            this.setErrorMessage("Need choose which jet files to create");
            return false;
        }
        if (browserImageButton.getSelection() && imageDirectoryText.getText().equals(PluginConstant.EMPTYSTRING)) {
            this.setErrorMessage("The image of current component haven't assigned");
            return false;
        }
        if (addLibCheckButton.getSelection() && libDirectoryText.getText().equals(PluginConstant.EMPTYSTRING)) {
            this.setErrorMessage("The lib of current component haven't assigned");
            return false;
        }
        this.setErrorMessage(null);
        return true;
    }
}
