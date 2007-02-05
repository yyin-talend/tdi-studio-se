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
package org.talend.repository.ui.wizards.exportjob;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.wizards.datatransfer.DataTransferMessages;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.properties.ProcessItem;

/**
 * Operation for exporting a resource and its children to a new .zip or .tar.gz file.
 * 
 * @since 3.1
 */
public class ArchiveFileExportOperationFullPath implements IRunnableWithProgress {

    private static final String SEPARATOR = "/";

    private static final String JOB_SOURCE_FOLDER_NAME = "src";

    private IFileExporterFullPath exporter;

    private String destinationFilename;

    private IProgressMonitor monitor;

    private List errorTable = new ArrayList(1); // IStatus

    private boolean useCompression = true;

    private boolean useTarFormat = false;

    private boolean createLeadupStructure = true;

    private String rootName;

    private String regEx = ".*.pl$|.*.pm$|.*.bat$|.*.sh$";

    private String jobSourceNameRegEx = ".*.properties$|.*.item$";

    private List<ExportFileResource> resourcesListToExport;

    /**
     * Create an instance of this class. Use this constructor if you wish to recursively export a single resource.
     * 
     * @param res org.eclipse.core.resources.IResource;
     * @param filename java.lang.String
     */
    private ArchiveFileExportOperationFullPath(String filename) {
        super();
        destinationFilename = filename;
    }

    /**
     * Create an instance of this class. Use this constructor if you wish to export specific resources with a common
     * parent resource (affects container directory creation)
     * 
     * @param res org.eclipse.core.resources.IResource
     * @param resources java.util.Vector
     * @param filename java.lang.String
     */

    public ArchiveFileExportOperationFullPath(List<ExportFileResource> resourcesListToExport, String destinationValue) {
        this(destinationValue);
        this.resourcesListToExport = resourcesListToExport;
    }

    /**
     * Add a new entry to the error table with the passed information.
     */
    protected void addError(String message, Throwable e) {
        ExceptionHandler.process(e);
        errorTable.add(new Status(IStatus.ERROR, IDEWorkbenchPlugin.IDE_WORKBENCH, 0, message, e));
    }

    /**
     * Answer the total number of file resources that exist at or below self in the resources hierarchy.
     * 
     * @return int
     * @param checkResource org.eclipse.core.resources.IResource
     */
    protected int countChildrenOf(String checkResource) throws CoreException {

        File file = new File(checkResource);

        if (file.isFile()) {
            return 1;
        }

        int count = 0;

        if (file.isDirectory()) {
            File[] children = file.listFiles();
            for (int i = 0; i < children.length; i++) {
                count += countChildrenOf(children[i].getPath());
            }
        }
        return count;
    }

    /**
     * Answer a boolean indicating the number of file resources that were specified for export.
     * 
     * @return int
     */
    protected int countSelectedResources() throws CoreException {
        int result = 0;
        for (ExportFileResource fileList : resourcesListToExport) {
            for (URL filePath : fileList.getProcessResouces()) {
                result += countChildrenOf(filePath.getPath());
            }
        }

        return result;
    }

    /**
     * Export the passed resource to the destination .zip. Export with no path leadup
     * 
     * @param exportResource org.eclipse.core.resources.IResource
     */
    protected void exportResource(String exportResource) throws InterruptedException {
        exportResource("", exportResource, 1);
    }

    /**
     * Export the passed resource to the destination .zip.
     * 
     * @param exportResource org.eclipse.core.resources.IResource
     * @param leadupDepth the number of resource levels to be included in the path including the resourse itself.
     */
    protected void exportResource(String directory, String exportResource, int leadupDepth) throws InterruptedException {
        // if (!exportResource.isAccessible()) {
        // return;
        // }

        File file = new File(exportResource);
        if (file.isFile()) {

            String destinationName = file.getName();
            if (!"".equals(directory)) {
                destinationName = directory + file.getName();
            }

            if (createLeadupStructure) {
                if (rootName != null && !"".equals(destinationName)) {
                    destinationName = rootName + SEPARATOR + destinationName;
                }

            }

            monitor.subTask(destinationName);

            try {
                exporter.write(exportResource, destinationName);
            } catch (IOException e) {
                addError(NLS.bind(DataTransferMessages.DataTransfer_errorExporting, exportResource, e.getMessage()), e);
            } catch (CoreException e) {
                addError(NLS.bind(DataTransferMessages.DataTransfer_errorExporting, exportResource, e.getMessage()), e);
            }

            monitor.worked(1);
            ModalContext.checkCanceled(monitor);
        } else if (file.isDirectory()) {
            File[] children = null;

            try {
                children = file.listFiles(new FileFilter() {

                    public boolean accept(File pathname) {

                        boolean result = true;
                        if (pathname != null && pathname.isFile()) {
                            try {
                                result = Pattern.compile(regEx).matcher(pathname.getName()).find();
                            } catch (PatternSyntaxException e) {
                                // here do nothing
                            }
                        }
                        return result;
                    }
                });
            } catch (Exception e) {
                // this should never happen because an #isAccessible check is done before #members is invoked
                addError(NLS.bind(DataTransferMessages.DataTransfer_errorExporting, exportResource), e);
            }

            for (int i = 0; i < children.length; i++) {
                exportResource(directory + file.getName() + SEPARATOR, children[i].getPath(), leadupDepth + 1);
            }

        }
    }

    /**
     * Export the resources contained in the previously-defined resourcesToExport collection.
     */
    protected void exportSpecifiedResources() throws InterruptedException {
        for (ExportFileResource fileResource : resourcesListToExport) {
            this.rootName = fileResource.getDirectoryName();
            for (URL url : fileResource.getProcessResouces()) {
                String currentResource = url.getPath();
                if (Pattern.matches(jobSourceNameRegEx, currentResource)) {
                    exportResource(JOB_SOURCE_FOLDER_NAME + SEPARATOR, currentResource, 1);
                } else {
                    exportResource(currentResource);
                }
            }
        }
    }

    /**
     * Answer the error table.
     * 
     * @return Vector of IStatus
     */
    public List getResult() {
        return errorTable;
    }

    /**
     * Returns the status of the operation. If there were any errors, the result is a status object containing
     * individual status objects for each error. If there were no errors, the result is a status object with error code
     * <code>OK</code>.
     * 
     * @return the status
     */
    public IStatus getStatus() {
        IStatus[] errors = new IStatus[errorTable.size()];
        errorTable.toArray(errors);
        return new MultiStatus(IDEWorkbenchPlugin.IDE_WORKBENCH, IStatus.OK, errors,
                DataTransferMessages.FileSystemExportOperation_problemsExporting, null);
    }

    /**
     * Initialize this operation.
     * 
     * @exception java.io.IOException
     */
    protected void initialize() throws IOException {
        if (useTarFormat) {
            exporter = new TarFileExporterFullPath(destinationFilename, useCompression);
        } else {
            exporter = new ZipFileExporterFullPath(destinationFilename, useCompression);
        }
    }

    /**
     * Answer a boolean indicating whether the passed child is a descendent of one or more members of the passed.
     * resources collection
     * 
     * @return boolean
     * @param resources java.util.Vector
     * @param child org.eclipse.core.resources.IResource
     */
    protected boolean isDescendent(List resources, IResource child) {
        if (child.getType() == IResource.PROJECT) {
            return false;
        }

        IResource parent = child.getParent();
        if (resources.contains(parent)) {
            return true;
        }

        return isDescendent(resources, parent);
    }

    /**
     * Export the resources that were previously specified for export (or if a single resource was specified then
     * export. it recursively)
     */
    public void run(IProgressMonitor progressMonitor) throws InvocationTargetException, InterruptedException {
        this.monitor = progressMonitor;

        try {
            initialize();
        } catch (IOException e) {
            throw new InvocationTargetException(e, NLS.bind(DataTransferMessages.ZipExport_cannotOpen, e.getMessage()));
        }

        try {
            // ie.- a single resource for recursive export was specified
            int totalWork = IProgressMonitor.UNKNOWN;
            try {
                if (resourcesListToExport == null) {
                    // FIXME here nerver happen
                    // totalWork = countChildrenOf(resource);
                } else {
                    totalWork = countSelectedResources();
                }
            } catch (CoreException e) {
                // Should not happen
            }
            monitor.beginTask(DataTransferMessages.DataTransfer_exportingTitle, totalWork);
            if (resourcesListToExport == null) {
                // FIXME here nerver happen
                // exportResource(resource);
            } else {
                // ie.- a list of specific resources to export was specified
                exportSpecifiedResources();
            }

            try {
                exporter.finished();
            } catch (IOException e) {
                throw new InvocationTargetException(e, NLS.bind(DataTransferMessages.ZipExport_cannotClose, e
                        .getMessage()));
            }
        } finally {
            monitor.done();
        }
    }

    /**
     * Set this boolean indicating whether each exported resource's path should include containment hierarchies as.
     * dictated by its parents
     * 
     * @param value boolean
     */
    public void setCreateLeadupStructure(boolean value) {
        createLeadupStructure = value;
    }

    /**
     * Set this boolean indicating whether exported resources should be compressed (as opposed to simply being stored).
     * 
     * @param value boolean
     */
    public void setUseCompression(boolean value) {
        useCompression = value;
    }

    /**
     * Set this boolean indicating whether the file should be output in tar.gz format rather than .zip format.
     * 
     * @param value boolean
     */
    public void setUseTarFormat(boolean value) {
        useTarFormat = value;
    }
}
