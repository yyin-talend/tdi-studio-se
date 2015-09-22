// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.component.ui.wizard.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.talend.component.ui.wizard.internal.IGenericWizardInternalService;
import org.talend.component.ui.wizard.internal.service.GenericWizardInternalService;
import org.talend.components.api.wizard.ComponentWizardDefinition;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.runtime.services.IGenericWizardService;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;

/**
 * created by ycbai on 2015年9月9日 Detailled comment
 *
 */
public class GenericWizardService implements IGenericWizardService {

    private IGenericWizardInternalService internalService = null;

    public GenericWizardService() {
        internalService = new GenericWizardInternalService();
    }

    @Override
    public List<RepositoryNode> createNodesFromComponentService(RepositoryNode curParentNode) {
        List<RepositoryNode> repNodes = new ArrayList<>();
        Set<ComponentWizardDefinition> wizardDefinitions = internalService.getComponentService().getTopLevelComponentWizards();
        for (ComponentWizardDefinition wizardDefinition : wizardDefinitions) {
            String name = wizardDefinition.getName();
            String displayName = wizardDefinition.getDisplayName();
            String folder = "metadata/" + name; //$NON-NLS-1$
            int ordinal = 100;
            ERepositoryObjectType repositoryType = internalService.createRepositoryType(name, displayName, name, folder, ordinal);
            repNodes.add(internalService.createRepositoryNode(curParentNode, wizardDefinition.getDisplayName(), repositoryType,
                    ENodeType.SYSTEM_FOLDER));
        }
        return repNodes;
    }

    @Override
    public List<String> getGenericTypeNames() {
        List<String> typeNames = new ArrayList<>();
        Set<ComponentWizardDefinition> wizardDefinitions = internalService.getComponentService().getTopLevelComponentWizards();
        for (ComponentWizardDefinition wizardDefinition : wizardDefinitions) {
            typeNames.add(wizardDefinition.getName());
        }
        return typeNames;
    }

    @Override
    public Image getNodeImage(String typeName) {
        InputStream imageStream = internalService.getComponentService().getWizardPngImage(typeName);// TODO: use other
        // node image
        ImageData id = new ImageData(imageStream);
        Image image = new Image(null, id);
        return image;
    }

    @Override
    public Image getWiardImage(String typeName) {
        InputStream imageStream = internalService.getComponentService().getWizardPngImage(typeName);
        ImageData id = new ImageData(imageStream);
        Image image = new Image(null, id);
        return image;
    }

}
