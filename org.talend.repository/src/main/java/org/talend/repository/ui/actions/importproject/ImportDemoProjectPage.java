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
package org.talend.repository.ui.actions.importproject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.pde.internal.ui.elements.ListContentProvider;
import org.eclipse.pde.internal.ui.wizards.ListUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.internal.wizards.datatransfer.WizardFileSystemResourceExportPage1;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.repository.i18n.Messages;
import org.talend.core.language.ECodeLanguage;
import org.talend.repository.RepositoryPlugin;
import org.talend.resources.ResourcesPlugin;

/**
 * This class is used for creating a page for importing demo project.<br/>
 * 
 * @author ftang
 * 
 */
public class ImportDemoProjectPage extends WizardFileSystemResourceExportPage1 implements ISelectionChangedListener {

    private TableViewer wizardSelectionViewer;

    private Browser descriptionBrowser;

    private List<DemoProjectBean> demoProjectList;

    private int selectedDemoProjectIndex = Integer.MAX_VALUE;

    /**
     * ImportDemoProjectPage constructor.
     * 
     * @param selection
     */
    public ImportDemoProjectPage(IStructuredSelection selection) {
        super(selection);
        this.setMessage(Messages.getString("ImportDemoProjectPage.message"));
        this.setTitle(Messages.getString("ImportDemoProjectPage.title"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.wizards.datatransfer.WizardFileSystemResourceExportPage1#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.verticalSpacing = 10;
        container.setLayout(layout);
        container.setLayoutData(new GridData(GridData.FILL_BOTH));

        // createAbove(container, 1);
        Label label = new Label(container, SWT.NONE);
        label.setText(Messages.getString("ImportDemoProjectPage.avaiableProjectsPrompt"));
        GridData gd = new GridData();
        label.setLayoutData(gd);

        SashForm sashForm = new SashForm(container, SWT.HORIZONTAL);
        gd = new GridData(GridData.FILL_BOTH);
        gd.widthHint = 300;
        sashForm.setLayoutData(gd);

        wizardSelectionViewer = new TableViewer(sashForm, SWT.BORDER);
        wizardSelectionViewer.setContentProvider(new ListContentProvider());
        wizardSelectionViewer.setLabelProvider(ListUtil.TABLE_LABEL_PROVIDER);
        wizardSelectionViewer.setSorter(ListUtil.NAME_SORTER);
        createDescriptionIn(sashForm);
        initializeViewer();
        wizardSelectionViewer.addSelectionChangedListener(this);
        Dialog.applyDialogFont(container);
        setControl(container);
    }

    /**
     * DOC Administrator Comment method "createDescriptionIn".
     * 
     * @param composite
     */
    public void createDescriptionIn(Composite composite) {
        descriptionBrowser = new Browser(composite, SWT.BORDER);
        descriptionBrowser.setText(""); //$NON-NLS-1$
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.widthHint = 200;
        descriptionBrowser.setLayoutData(gd);
    }

    /**
     * initializeViewer.
     */
    private void initializeViewer() {
        for (int i = 0; i < this.demoProjectList.size(); i++) {
            DemoProjectBean demoProject = this.demoProjectList.get(i);

            TableItem tableItem = new TableItem(wizardSelectionViewer.getTable(), i);
            tableItem.setText(demoProject.getProjectName());
            tableItem.setImage(getImageForDemoProject(demoProject.getLanguage()));
        }
    }

    /**
     * Gets images.
     * 
     * @param language
     * @return
     */
    private Image[] getImageForDemoProject(ECodeLanguage language) {

        String languageName = language.getName();
        Image[] image = getFullImagePath(languageName);
        return image;
    }

    /**
     * getFullImagePath.
     * 
     * @param languageName
     * @return
     */
    private Image[] getFullImagePath(String languageName) {
        String relatedImagePath = null;
        if (languageName.equalsIgnoreCase(ECodeLanguage.getCodeLanguage("java").getName())) {
            relatedImagePath = "icons/java.gif";
        } else if (languageName.equalsIgnoreCase(ECodeLanguage.getCodeLanguage("perl").getName())) {
            relatedImagePath = "icons/perl.gif";
        } else {
            relatedImagePath = "icons/perl.gif";
        }

        Bundle bundle = Platform.getBundle(RepositoryPlugin.PLUGIN_ID);
        URL url = null;
        String pluginPath = null;
        try {
            url = FileLocator.resolve(bundle.getEntry(relatedImagePath));
            pluginPath = new Path(url.getFile()).toOSString();
        } catch (IOException e1) {
            ExceptionHandler.process(e1);
        }

        return new Image[] { new Image(null, pluginPath) };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    public void selectionChanged(SelectionChangedEvent event) {

        selectedDemoProjectIndex = ((TableViewer) event.getSource()).getTable().getSelectionIndex();

        Bundle bundle = Platform.getBundle(ResourcesPlugin.PLUGIN_ID);
        URL url;
        try {
            url = FileLocator.resolve(bundle.getEntry(this.demoProjectList.get(selectedDemoProjectIndex)
                    .getDescriptionFilePath()));

            String descriptionFilePath = new Path(url.getFile()).toOSString();

            descriptionBrowser.setUrl(descriptionFilePath);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }
    }

    /**
     * Sets import demo project list.
     * 
     * @param demoProjectList
     */
    public void setImportDemoProjectList(List<DemoProjectBean> demoProjectList) {
        this.demoProjectList = demoProjectList;
    }

    /**
     * Gets the index of selected demo project.
     * 
     * @return
     */
    public int getSelectedDemoProjectIndex() {
        return selectedDemoProjectIndex;
    }
}
