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
package org.talend.repository.ui.actions.metadata.database.wizard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.ui.actions.metadata.database.ConnectionDBTableHelper;
import org.talend.repository.ui.actions.metadata.database.DBProcessRecords;
import org.talend.repository.ui.actions.metadata.database.DBTableForDelimitedBean;
import org.talend.repository.ui.actions.metadata.database.DBProcessRecords.ProcessType;
import org.talend.repository.ui.actions.metadata.database.DBProcessRecords.RecordsType;
import org.talend.repository.ui.wizards.RepositoryWizard;

/**
 * ggu class global comment. Detailled comment <br/>
 * 
 */
public class ImportDBTableWizard extends RepositoryWizard implements IImportWizard {

    private static Logger log = Logger.getLogger(ImportDBTableWizard.class);

    private static final IProxyRepositoryFactory FACTORY = ProxyRepositoryFactory.getInstance();

    private ImportDBTableWizardPage importWizardPage;

    private DBProcessRecords processRecords = new DBProcessRecords();

    private static int rejectedNum = 0;

    /**
     * ggu ImportDBTableWizard constructor comment.
     */
    public ImportDBTableWizard(IWorkbench workbench, ISelection selection) {
        super(workbench, true);
        this.selection = selection;
        setNeedsProgressMonitor(true);
        initSetting();

    }

    private void initSetting() {
        if (selection == null) {
            pathToSave = new Path(""); //$NON-NLS-1$
            return;
        }

        Object userSelection = ((IStructuredSelection) selection).getFirstElement();
        if (userSelection instanceof RepositoryNode) {
            switch (((RepositoryNode) userSelection).getType()) {
            case SIMPLE_FOLDER:
                pathToSave = RepositoryNodeUtilities.getPath((RepositoryNode) userSelection);
                break;
            case SYSTEM_FOLDER:
                pathToSave = new Path(""); //$NON-NLS-1$
                break;
            default:
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        super.setWorkbench(workbench);
        this.selection = selection;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        setWindowTitle(Messages.getString("ImportDBTableWizard.WizardTitle")); //$NON-NLS-1$
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.METADATA_CONNECTION_WIZ));
        importWizardPage = new ImportDBTableWizardPage();
        importWizardPage.setTitle(Messages.getString("ImportDBTableWizard.Title")); //$NON-NLS-1$
        importWizardPage.setDescription(Messages.getString("ImportDBTableWizard.Description")); //$NON-NLS-1$
        importWizardPage.setPageComplete(false);
        addPage(importWizardPage);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        if (importWizardPage.isPageComplete()) {

            File file = importWizardPage.getFormSetting().getImportFile();
            if (file == null) {
                return false;
            }
            process(file);

            progressDialog();
            return true;
        }
        return false;
    }

    private void process(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {

                DBTableForDelimitedBean bean = ConnectionDBTableHelper.getRowData(line);
                if (bean != null) { // the line is suitable format.

                    ConnectionItem connItem = null;
                    try {
                        connItem = ConnectionDBTableHelper.setConnectionItemData(bean);
                    } catch (PersistenceException e) {
                        e.printStackTrace();
                        writeRejects(line, bean);
                        continue;
                    } catch (BusinessException e) {
                        e.printStackTrace();
                        writeRejects(line, bean);
                        continue;
                    }
                    if (connItem == null) {
                        writeRejects(line, bean);
                        continue;
                    }
                    try {
                        if (ConnectionDBTableHelper.isConnectionCreated()) {
                            connItem.getProperty().setId(FACTORY.getNextId());
                            FACTORY.create(connItem, pathToSave);
                        } else {
                            FACTORY.save(connItem);
                        }
                    } catch (PersistenceException e) {
                        e.printStackTrace();
                        writeRejects(line, bean);

                        continue;
                    }
                    addRecords(ProcessType.IMPORT, bean);
                } else { // this line isn't right format. record it in ".log" and ".rejects"
                    // bean = null
                    writeRejects(line, null);

                }
            }
            // TODO write the .log
            writeLogs();
        } catch (FileNotFoundException e) {
            MessageBoxExceptionHandler.process(e, getShell());
            e.printStackTrace();
        } catch (IOException e) {
            MessageBoxExceptionHandler.process(e, getShell());
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
                reader = null;
            }
        }

    }

    private void progressDialog() {
        // ProgressDialog progressDialog = new ProgressDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell()) {
        //        
        // private IProgressMonitor monitorWrap;
        //        
        // @Override
        // public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        // monitorWrap = new EventLoopProgressMonitor(monitor);
        //        
        // try {
        // DemoProjectBean demoProjectBean = demoProjectList.get(selectedDemoProjectIndex);
        // String techName = demoProjectBean.getProjectName();
        //        
        // if (checkProjectIsExisting(techName)) {
        // boolean reImportFlag = MessageDialog.openQuestion(shell, Messages
        // .getString("ImportDemoProjectAction.alertDialog.messageTitle"), Messages
        // .getString("ImportDemoProjectAction.alertDialog.message"));
        // if (!reImportFlag) {
        // return;
        // }
        // }
        //        
        // String demoFilePath = demoProjectBean.getDemoProjectFilePath();
        // EDemoProjectFileType demoProjectFileType = demoProjectBean.getDemoProjectFileType();
        // Bundle bundle = Platform.getBundle(ResourcesPlugin.PLUGIN_ID);
        //        
        // URL url = FileLocator.resolve(bundle.getEntry(demoFilePath));
        //        
        // String filePath = new Path(url.getFile()).toOSString();
        //        
        // if (demoProjectFileType.getName().equalsIgnoreCase("folder")) {
        // ImportProjectsUtilities.importProjectAs(shell, techName, techName, filePath, monitorWrap);
        // } else {// type.equalsIgnoreCase("archive")
        // ImportProjectsUtilities.importArchiveProject(shell, techName, filePath, monitorWrap);
        //        
        // }
        // lastImportedName = techName;
        //        
        // } catch (IOException e) {
        // throw new InvocationTargetException(e);
        // } catch (TarException e) {
        // throw new InvocationTargetException(e);
        // }
        //        
        // monitorWrap.done();
        // MessageDialog.openInformation(shell,
        // Messages.getString("ImportDemoProjectAction.messageDialogTitle.demoProject"), //$NON-NLS-1$
        // Messages.getString("ImportDemoProjectAction.messageDialogContent.demoProjectImportedSuccessfully"));
        // //$NON-NLS-1$
        // }
        // };
        //        
        // try {
        // progressDialog.executeProcess();
        // } catch (InvocationTargetException e) {
        // MessageBoxExceptionHandler.process(e.getTargetException(), shell);
        // } catch (InterruptedException e) {
        // // Nothing to do
        // }
    }

    private void writeLogs() {
        StringBuffer sb = new StringBuffer();
        sb.append("---------Log start---------\n");

        System.out.println("---------Log start---------");
        int conNum = processRecords.getRecord(ProcessType.IMPORT, RecordsType.CONNECTION);
        int tableNum = processRecords.getRecord(ProcessType.IMPORT, RecordsType.TABLE);
        int fieldNum = processRecords.getRecord(ProcessType.IMPORT, RecordsType.FIELD);

        System.out.println("Imported:\r\n" + "   " + conNum + " connections, " + tableNum + " tables, " + fieldNum + " fields");

        sb.append("Imported:\r\n" + "   " + conNum + " connections, " + tableNum + " tables, " + fieldNum + " fields \n");

        conNum = processRecords.getRecord(ProcessType.REJECT, RecordsType.CONNECTION);
        tableNum = processRecords.getRecord(ProcessType.REJECT, RecordsType.TABLE);
        fieldNum = processRecords.getRecord(ProcessType.REJECT, RecordsType.FIELD);
        System.out.println("Rejected:\r\n" + "   " + conNum + " connections, " + tableNum + " tables, " + fieldNum + " fields");

        sb.append("Rejected:\r\n" + "   " + conNum + " connections, " + tableNum + " tables, " + fieldNum + " fields\n");

        System.out.println("---------Log End---------");
        sb.append("---------Log End---------");
        log.trace(sb.toString());
    }

    private void writeRejects(String line, DBTableForDelimitedBean bean) {
        // TODO write .rejects
        try {

            PrintWriter pw = new PrintWriter(new FileWriter("d:/.rejects", true), true);
            pw.println(line);
            pw.flush();
            pw.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

        if (bean == null) {
            rejectedNum++;
        } else {
            addRecords(ProcessType.REJECT, bean);
        }
    }

    private void addRecords(ProcessType rType, DBTableForDelimitedBean bean) {
        processRecords.addRecord(rType, RecordsType.CONNECTION, bean.getName());
        processRecords.addRecord(rType, RecordsType.TABLE, bean.getTableName());
        processRecords.addRecord(rType, RecordsType.FIELD, bean.getLabel());
    }
}
