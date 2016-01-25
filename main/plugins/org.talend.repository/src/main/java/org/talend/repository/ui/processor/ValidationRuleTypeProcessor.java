// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.processor;

import org.eclipse.jface.viewers.Viewer;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class ValidationRuleTypeProcessor extends SingleTypeProcessor {

    /**
     * DOC ycbai ValidationRuleTypeProcessor constructor comment.
     * 
     * @param repositoryType
     */
    public ValidationRuleTypeProcessor(String repositoryType) {
        super(repositoryType);
    }

    @Override
    protected ERepositoryObjectType getType() {
        return ERepositoryObjectType.METADATA_VALIDATION_RULES;
    }

    @Override
    protected boolean selectRepositoryNode(Viewer viewer, RepositoryNode parentNode, RepositoryNode node) {
        if (node.getContentType() == ERepositoryObjectType.REFERENCED_PROJECTS) {
            return true;
        }
        ProjectManager pManager = ProjectManager.getInstance();
        if (!pManager.isInCurrentMainProject(node)) {
            if (node.getType() == ENodeType.STABLE_SYSTEM_FOLDER) {
                return false;
            }
            if (node.getType() == ENodeType.SYSTEM_FOLDER) {
                return true;
            }
        }

        if (node.getType() == ENodeType.SYSTEM_FOLDER) {
            return true;
        }

        if (node.getObject() == null || node.getObject().getProperty().getItem() == null) {
            return false;
        }
        if (node.getObject() instanceof MetadataTable) {
            return false;
        }
        Item item = node.getObject().getProperty().getItem();
        if (item instanceof FolderItem) {
            return true;
        }
        if (node.getObjectType() == getType()) {
            return true;
        }
        return false;

    }

}