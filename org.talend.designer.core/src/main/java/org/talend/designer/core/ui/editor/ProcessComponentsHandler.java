// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.TComponentsHandler;

/**
 * DOC marvin class global comment. Detailled comment
 */
public class ProcessComponentsHandler implements TComponentsHandler {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.TComponentsHandler#filterComponents(java.util.Set)
     */
    @Override
    public List<IComponent> filterComponents(Set<IComponent> allComponents) {
        List<IComponent> filteredComponents = new ArrayList<IComponent>();
        if (allComponents != null && allComponents.size() > 0) {
            for (IComponent component : allComponents) {
                String compType = component.getType();
                if (compType == null) {
                    filteredComponents.add(component);
                } else if (compType != null && ComponentCategory.CATEGORY_4_DI.getName().equals(compType)) {
                    filteredComponents.add(component);
                }
            }
        }
        return filteredComponents;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.TComponentsHandler#sortComponents(java.util.List)
     */
    @Override
    public List<IComponent> sortComponents(List<IComponent> filteredComponents) {
        return filteredComponents;
    }

}
