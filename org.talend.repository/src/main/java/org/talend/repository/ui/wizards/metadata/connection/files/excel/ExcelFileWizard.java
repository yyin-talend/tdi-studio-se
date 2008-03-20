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
package org.talend.repository.ui.wizards.metadata.connection.files.excel;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.FileExcelConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.ui.wizards.PropertiesWizardPage;
import org.talend.repository.ui.wizards.RepositoryWizard;
import org.talend.repository.ui.wizards.metadata.connection.Step0WizardPage;

/**
 * DOC yexiaowei class global comment. Detailled comment
 */
public class ExcelFileWizard extends RepositoryWizard implements INewWizard {

    private static Logger log = Logger.getLogger(ExcelFileWizard.class);

    private PropertiesWizardPage excelFileWizardPage0;

    private ExcelFileWizardPage page1 = null;

    private ExcelFileWizardPage page2 = null;

    private FileExcelConnection connection;

    private Property connectionProperty;

    private ConnectionItem connectionItem;

    public ExcelFileWizard(IWorkbench workbench, boolean creation, ISelection selection, String[] existingNames) {
        super(workbench, creation);

        this.selection = selection;
        this.existingNames = existingNames;
        setNeedsProgressMonitor(true);
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.METADATA_FILE_EXCEL_WIZ));

        Object obj = ((IStructuredSelection) selection).getFirstElement();
        RepositoryNode node = (RepositoryNode) obj;
        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case REPOSITORY_ELEMENT:
            pathToSave = RepositoryNodeUtilities.getPath(node);
            break;
        case SYSTEM_FOLDER:
            pathToSave = new Path(""); //$NON-NLS-1$
            break;
        }

        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            connection = ConnectionFactory.eINSTANCE.createFileExcelConnection();
            MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            metadataTable.setId(factory.getNextId());
            connection.getTables().add(metadataTable);
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty
                    .setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                            .getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = PropertiesFactory.eINSTANCE.createExcelFileConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = (FileExcelConnection) ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
            connectionProperty = node.getObject().getProperty();
            connectionItem = (ConnectionItem) node.getObject().getProperty().getItem();
            setRepositoryObject(node.getObject());
            isRepositoryObjectEditable();
            initLockStrategy();
            break;
        }
    }

    public ExcelFileWizard(IWorkbench workbench, boolean creation, RepositoryNode node, String[] existingNames) {
        super(workbench, creation);

        this.selection = selection;
        this.existingNames = existingNames;
        setNeedsProgressMonitor(true);
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.METADATA_FILE_LDIF_WIZ));
        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case REPOSITORY_ELEMENT:
            pathToSave = RepositoryNodeUtilities.getPath(node);
            break;
        case SYSTEM_FOLDER:
            pathToSave = new Path(""); //$NON-NLS-1$
            break;
        }

        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            connection = ConnectionFactory.eINSTANCE.createFileExcelConnection();
            MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            metadataTable.setId(factory.getNextId());
            connection.getTables().add(metadataTable);
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty
                    .setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                            .getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = PropertiesFactory.eINSTANCE.createExcelFileConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = (FileExcelConnection) ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
            connectionProperty = node.getObject().getProperty();
            connectionItem = (ConnectionItem) node.getObject().getProperty().getItem();
            setRepositoryObject(node.getObject());
            isRepositoryObjectEditable();
            initLockStrategy();
            break;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {

        boolean formIsPerformed = false;
        if (page2 == null) {
            formIsPerformed = page1.isPageComplete();
        } else {
            formIsPerformed = page2.isPageComplete();
        }

        if (formIsPerformed) {
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                if (creation) {
                    String nextId = factory.getNextId();
                    connectionProperty.setId(nextId);
                    factory.create(connectionItem, excelFileWizardPage0.getDestinationPath());
                } else {
                    factory.save(connectionItem);
                    closeLockStrategy();
                }
            } catch (PersistenceException e) {
                String detailError = e.toString();
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("CommonWizard.persistenceException"), //$NON-NLS-1$
                        detailError);
                log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
    }

    @Override
    public void addPages() {

        excelFileWizardPage0 = new Step0WizardPage(connectionProperty, pathToSave, ERepositoryObjectType.METADATA_FILE_EXCEL,
                !isRepositoryObjectEditable(), creation);

        excelFileWizardPage0.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 1 " //$NON-NLS-1$ //$NON-NLS-2$
                + Messages.getString("FileWizardPage.of") + " 3 "); //$NON-NLS-1$ //$NON-NLS-2$
        excelFileWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0")); //$NON-NLS-1$

        addPage(excelFileWizardPage0);

        if (creation) {
            setWindowTitle(Messages.getString("ExcelFileWizard.windowTitleCreate")); //$NON-NLS-1$
        } else {
            setWindowTitle(Messages.getString("ExcelFileWizard.windowTitleUpdate")); //$NON-NLS-1$
        }

        page1 = new ExcelFileWizardPage(1, connectionItem, isRepositoryObjectEditable(), existingNames);

        page1.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 2 " //$NON-NLS-1$ //$NON-NLS-2$
                + Messages.getString("FileWizardPage.of") + " 3"); //$NON-NLS-1$ //$NON-NLS-2$
        page1.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep1")); //$NON-NLS-1$

        addPage(page1);

        page2 = new ExcelFileWizardPage(2, connectionItem, isRepositoryObjectEditable(), existingNames);

        page2.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 3 " //$NON-NLS-1$ //$NON-NLS-2$
                + Messages.getString("FileWizardPage.of") + " 3 "); //$NON-NLS-1$ //$NON-NLS-2$
        page2.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep3")); //$NON-NLS-1$

        addPage(page2);

        page1.setPageComplete(false);
        page2.setPageComplete(false);

    }
}
