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
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.components.api.service.ComponentService;
import org.talend.components.api.wizard.ComponentWizardDefinition;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.runtime.services.IGenericWizardService;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;

/**
 * created by ycbai on 2015年9月9日 Detailled comment
 *
 */
public class GenericWizardService implements IGenericWizardService {

    @Override
    public List<RepositoryNode> createNodesFromComponentService(RepositoryNode curParentNode) {
        List<RepositoryNode> repNodes = new ArrayList<>();
        Set<ComponentWizardDefinition> wizardDefinitions = getWizardDefinitions();
        for (ComponentWizardDefinition wizardDefinition : wizardDefinitions) {
            String name = wizardDefinition.getName();
            String displayName = wizardDefinition.getDisplayName();
            String folder = "metadata/" + name; //$NON-NLS-1$
            int ordinal = 100;
            ERepositoryObjectType repositoryType = createRepositoryType(name, displayName, name, folder, ordinal);
            repNodes.add(createRepositoryNode(curParentNode, wizardDefinition.getDisplayName(), repositoryType,
                    ENodeType.SYSTEM_FOLDER));
        }
        return repNodes;
    }

    @Override
    public List<String> getGenericTypeNames() {
        List<String> typeNames = new ArrayList<>();
        Set<ComponentWizardDefinition> wizardDefinitions = getWizardDefinitions();
        for (ComponentWizardDefinition wizardDefinition : wizardDefinitions) {
            typeNames.add(wizardDefinition.getName());
        }
        return typeNames;
    }

    @Override
    public Image getNodeImage(String typeName) {
        InputStream imageStream = getComponentService().getWizardPngImage(typeName);
        ImageData id = new ImageData(imageStream);
        Image image = new Image(null, id);
        return image;
    }

    private Set<ComponentWizardDefinition> getWizardDefinitions() {
        return getComponentService().getTopLevelComponentWizards();
    }

    private RepositoryNode createRepositoryNode(RepositoryNode curParentNode, String label, ERepositoryObjectType type,
            ENodeType nodeType) {
        RepositoryNode dynamicNode = new RepositoryNode(null, (RepositoryNode) curParentNode.getRoot(), nodeType);
        dynamicNode.setProperties(EProperties.LABEL, label);
        dynamicNode.setProperties(EProperties.CONTENT_TYPE, type);
        curParentNode.getChildren().add(dynamicNode);
        return dynamicNode;
    }

    private ERepositoryObjectType createRepositoryType(String type, String label, String alias, String folder, int ordinal) {
        Constructor<ERepositoryObjectType> dynamicConstructor = getConstructor(ERepositoryObjectType.class, new Class[] {
                String.class, String.class, String.class, String.class, int.class, boolean.class, String.class, String[].class,
                boolean.class, String[].class, boolean[].class });
        ERepositoryObjectType typeObject = null;
        try {
            dynamicConstructor.setAccessible(true);
            typeObject = dynamicConstructor.newInstance(type, label, folder, type, ordinal, false, alias,
                    new String[] { ERepositoryObjectType.PROD_DI }, false, new String[0], new boolean[] { true });
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return typeObject;
    }

    private <E> Constructor<E> getConstructor(Class<E> clazz, Class<?>[] argTypes) {
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            try {
                return (Constructor<E>) c.getDeclaredConstructor(argTypes);
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }

    private ComponentService getComponentService() {
        ComponentService compService = null;
        BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        ServiceReference<ComponentService> compServiceRef = bundleContext.getServiceReference(ComponentService.class);
        if (compServiceRef != null) {
            compService = bundleContext.getService(compServiceRef);
        }
        return compService;
    }

}
