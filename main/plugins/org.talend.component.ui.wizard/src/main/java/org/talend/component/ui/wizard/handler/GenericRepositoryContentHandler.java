package org.talend.component.ui.wizard.handler;

import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.component.ui.model.genericMetadata.GenericConnection;
import org.talend.component.ui.model.genericMetadata.GenericConnectionItem;
import org.talend.component.ui.model.genericMetadata.GenericMetadataFactory;
import org.talend.component.ui.model.genericMetadata.GenericMetadataPackage;
import org.talend.component.ui.wizard.ui.GenericConnWizard;
import org.talend.component.ui.wizard.util.GenericWizardServiceFactory;
import org.talend.core.model.metadata.MetadataManager;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.AbstractRepositoryContentHandler;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.utils.RepositoryNodeManager;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SubItemHelper;
import org.talend.repository.model.RepositoryNode;

/**
 * 
 * created by ycbai on 2015年10月15日 Detailled comment
 *
 */
public class GenericRepositoryContentHandler extends AbstractRepositoryContentHandler {

    @Override
    public boolean isProcess(Item item) {
        if (item.eClass() == GenericMetadataPackage.Literals.GENERIC_CONNECTION_ITEM) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isRepObjType(ERepositoryObjectType type) {
        return GenericWizardServiceFactory.getGenericWizardService().isGenericType(type);
    }

    @Override
    public ERepositoryObjectType getProcessType() {
        return null;
    }

    @Override
    public Resource create(IProject project, Item item, int classifierID, IPath path) throws PersistenceException {
        Resource itemResource = null;
        if (isProcess(item)) {
            itemResource = create(project, (GenericConnectionItem) item, path);
        }

        return itemResource;
    }

    private Resource create(IProject project, GenericConnectionItem item, IPath path) throws PersistenceException {
        XmiResourceManager xmiResourceManager = ProxyRepositoryFactory.getInstance().getRepositoryFactoryFromProvider()
                .getResourceManager();
        ERepositoryObjectType repObjType = getRepositoryObjectType(item);
        Resource itemResource = null;
        if (repObjType != null) {
            itemResource = xmiResourceManager.createItemResource(project, item, path, repObjType, false);
            MetadataManager.addContents(item, itemResource);
            itemResource.getContents().add(item.getConnection());
        }

        return itemResource;
    }

    @Override
    public Resource save(Item item) throws PersistenceException {
        Resource itemResource = null;
        if (isProcess(item)) {
            itemResource = save((GenericConnectionItem) item);
        }

        return itemResource;
    }

    private Resource save(GenericConnectionItem item) {
        XmiResourceManager xmiResourceManager = ProxyRepositoryFactory.getInstance().getRepositoryFactoryFromProvider()
                .getResourceManager();
        Resource itemResource = xmiResourceManager.getItemResource(item);
        itemResource.getContents().clear();
        MetadataManager.addContents(item, itemResource);

        return itemResource;
    }

    @Override
    public IImage getIcon(ERepositoryObjectType type) {
        return null;
    }

    @Override
    public Item createNewItem(ERepositoryObjectType type) {
        Item item = null;
        if (isRepObjType(type)) {
            item = GenericMetadataFactory.eINSTANCE.createGenericConnectionItem();
        }

        return item;
    }

    @Override
    public ERepositoryObjectType getRepositoryObjectType(Item item) {
        ERepositoryObjectType type = null;
        if (isProcess(item)) {
            GenericConnectionItem connectionItem = (GenericConnectionItem) item;
            type = ERepositoryObjectType.valueOf(connectionItem.getTypeName());
        }

        return type;
    }

    @Override
    public boolean hasSchemas() {
        return true;
    }

    @Override
    public void addNode(ERepositoryObjectType type, RepositoryNode recBinNode, IRepositoryViewObject repositoryObject,
            RepositoryNode node) {
        if (isRepObjType(type)) {
            GenericConnection connection = (GenericConnection) ((GenericConnectionItem) repositoryObject.getProperty().getItem())
                    .getConnection();
            Set<MetadataTable> tableset = ConnectionHelper.getTables(connection);
            for (MetadataTable metadataTable : tableset) {
                if (!SubItemHelper.isDeleted(metadataTable)) {
                    RepositoryNode tableNode = RepositoryNodeManager.createMetatableNode(node, repositoryObject, metadataTable);
                    node.getChildren().add(tableNode);
                    if (metadataTable.getColumns().size() > 0) {
                        RepositoryNodeManager.createColumns(tableNode, repositoryObject, metadataTable);
                    }
                }
            }
        }
    }

    @Override
    public IWizard newWizard(IWorkbench workbench, boolean creation, RepositoryNode node, String[] existingNames) {
        if (node == null) {
            return null;
        }
        IWorkbench wb = workbench;
        if (wb == null) {
            wb = PlatformUI.getWorkbench();
        }

        return new GenericConnWizard(wb, creation, node, existingNames);
    }

}
