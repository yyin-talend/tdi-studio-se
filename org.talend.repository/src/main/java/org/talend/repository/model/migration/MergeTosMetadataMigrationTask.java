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
package org.talend.repository.model.migration;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.migration.AbstractItemMigrationTask;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.model.migration.TosMetadataMigrationFrom400to410;
import org.talend.repository.ProjectManager;
import org.talend.repository.constants.FileConstants;
import org.talend.repository.model.URIHelper;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class MergeTosMetadataMigrationTask extends AbstractItemMigrationTask {

    TosMetadataMigrationFrom400to410 metadata400to410 = new TosMetadataMigrationFrom400to410();

    static final HashMap XML_SAVE_OTIONS = new HashMap(2);
    static {
        XML_SAVE_OTIONS.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
        XML_SAVE_OTIONS.put(XMLResource.OPTION_XML_VERSION, "1.1"); //$NON-NLS-1$
    }

    public ResourceSet resourceSet = new ResourceSetImpl();

    @Override
    public ExecutionResult execute(Item item) {
        if (item instanceof ConnectionItem) {
            try {
                URI itemResourceURI = getItemResourceURI(getItemURI(item));
                Resource migratedResource = metadata400to410.migrate(itemResourceURI.toString(), new NullProgressMonitor());
                if (migratedResource != null) {
                    OutputStream outputStream = migratedResource.getResourceSet().getURIConverter().createOutputStream(
                            itemResourceURI, XML_SAVE_OTIONS);
                    try {
                        migratedResource.save(outputStream, XML_SAVE_OTIONS);
                    } finally {
                        outputStream.close();
                    }
                    // ProxyRepositoryFactory.getInstance().save(item, true);
                }
                return ExecutionResult.SUCCESS_NO_ALERT;
            } catch (ATLCoreException e) {
                ExceptionHandler.process(e);
                return ExecutionResult.SUCCESS_NO_ALERT;
            } catch (IOException e) {
                ExceptionHandler.process(e);
                return ExecutionResult.FAILURE;
            }
        }
        return ExecutionResult.NOTHING_TO_DO;
    }

    private URI getItemURI(Item item) {
        ProjectManager pManager = ProjectManager.getInstance();
        org.talend.core.model.general.Project project = new org.talend.core.model.general.Project(pManager.getProject(item));
        // referenced item
        if (project != null && !project.equals(pManager.getCurrentProject())) {
            String folder = ERepositoryObjectType.getFolderName(ERepositoryObjectType.getItemType(item));

            if (folder != null) {
                IPath path = new Path(project.getTechnicalLabel());
                path = path.append(folder);
                path = path.append(item.getState().getPath());
                Property property = item.getProperty();
                String itemStr = property.getLabel() + "_" + property.getVersion() + "." + FileConstants.PROPERTIES_EXTENSION; //$NON-NLS-1$ //$NON-NLS-2$
                path = path.append(itemStr);
                return URIHelper.convert(path);
            }
        }
        return item.eResource().getURI();
    }

    private URI getItemResourceURI(URI propertyResourceURI) {
        return propertyResourceURI.trimFileExtension().appendFileExtension(FileConstants.ITEM_EXTENSION);
    }

    //
    // private Resource getItemResource(Item item) {
    // URI itemResourceURI = getItemResourceURI(getItemURI(item));
    // Resource itemResource = resourceSet.getResource(itemResourceURI, false);
    //
    // if (itemResource == null) {
    // if (item instanceof FileItem) {
    // itemResource = new ByteArrayResource(itemResourceURI);
    // resourceSet.getResources().add(itemResource);
    // }
    // itemResource = resourceSet.getResource(itemResourceURI, true);
    // }
    //
    // return itemResource;
    // }

    public Date getOrder() {
        // TODO Auto-generated method stub
        GregorianCalendar gc = new GregorianCalendar(2010, 6, 29, 10, 20, 0);
        return gc.getTime();
    }
}
