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
package org.talend.component.ui.wizard.provider.impl;

import java.util.Map;
import java.util.Set;

import org.talend.component.core.provider.IComponentServiceProvider;
import org.talend.component.core.provider.impl.ComponentServiceOsgiProvider;
import org.talend.component.ui.wizard.provider.IGenericWizardProvider;
import org.talend.components.api.wizard.ComponentWizardDefinition;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * created by ycbai on 2015年9月9日 Detailled comment
 *
 */
public class GenericWizardProvider implements IGenericWizardProvider {

    private IComponentServiceProvider serviceProvider;

    // private Map<ERepositoryObjectType, ComponentWizardDefinition> wizardsMap = new HashMap<>();

    public GenericWizardProvider() {
        this(new ComponentServiceOsgiProvider());
    }

    public GenericWizardProvider(IComponentServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public Map<ERepositoryObjectType, ComponentWizardDefinition> getWizardDefinition(ERepositoryObjectType type) {
        return null;
    }

    @Override
    public Set<ComponentWizardDefinition> getWizardDefinitions() {
        return serviceProvider.getComponentService().getTopLevelComponentWizards();
    }

}
