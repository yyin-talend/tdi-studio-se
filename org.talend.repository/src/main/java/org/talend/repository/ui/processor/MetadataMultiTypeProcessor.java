package org.talend.repository.ui.processor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.talend.core.model.metadata.Query;
import org.talend.core.model.param.ERepositoryCategoryType;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.RepositoryNode;

/**
 * 
 * DOC talend class global comment. Detailled comment
 */
public class MetadataMultiTypeProcessor extends MultiTypesProcessor {

    public MetadataMultiTypeProcessor(String[] repositoryTypes) {
        super(repositoryTypes);
    }

    @Override
    protected List<ERepositoryObjectType> getTypes() {
        List<ERepositoryObjectType> types = new ArrayList<ERepositoryObjectType>();

        String[] repositoryTypes = getRepositoryTypes();
        if (repositoryTypes != null) {
            for (String repositoryType : repositoryTypes) {
                if (ERepositoryCategoryType.XML.getName().equals(repositoryType)) {
                    types.add(ERepositoryObjectType.METADATA_FILE_XML);
                } else if (ERepositoryCategoryType.MDM.getName().equals(repositoryType)) {
                    types.add(ERepositoryObjectType.METADATA_MDMCONNECTION);
                }
            }
        }
        return types;
    }

    @Override
    protected boolean selectRepositoryNode(Viewer viewer, RepositoryNode parentNode, RepositoryNode node) {
        if (super.selectRepositoryNode(viewer, parentNode, node)) {
            IRepositoryViewObject object = node.getObject();
            if (object != null) {
                // query
                if (object instanceof Query) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String getDialogTitle() {
        return Messages.getString("RepositoryReviewDialog.metadataTitle"); //$NON-NLS-1$
    }

}