// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.swt.events.SelectionListener;
import org.talend.commons.utils.workbench.extensions.ExtensionImplementationProvider;
import org.talend.commons.utils.workbench.extensions.ExtensionPointLimiterImpl;
import org.talend.commons.utils.workbench.extensions.IExtensionPointLimiter;

/**
 * Provides, using extension points, implementation of many factories.
 * 
 * <ul>
 * <li>IProcessFactory</li>
 * </ul>
 * 
 * $Id$
 */
public class RepositoryFactoryProvider {

    private static List<IRepositoryFactory> list = null;

    public static final IExtensionPointLimiter REPOSITORY_PROVIDER = new ExtensionPointLimiterImpl(
            "org.talend.core.repository_provider", //$NON-NLS-1$
            "RepositoryFactory", 1, -1); //$NON-NLS-1$

    public static List<IRepositoryFactory> getAvailableRepositories() {
        if (list == null) {
            list = new ArrayList<IRepositoryFactory>();
            List<IConfigurationElement> extension = ExtensionImplementationProvider.getInstanceV2(REPOSITORY_PROVIDER);

            for (IConfigurationElement current : extension) {
                try {
                    IRepositoryFactory currentAction = (IRepositoryFactory) current.createExecutableExtension("class"); //$NON-NLS-1$
                    currentAction.setId(current.getAttribute("id")); //$NON-NLS-1$
                    currentAction.setName(current.getAttribute("name")); //$NON-NLS-1$
                    currentAction.setAuthenticationNeeded(new Boolean(current.getAttribute("authenticationNeeded"))); //$NON-NLS-1$
                    currentAction.setDisplayToUser(new Boolean(current.getAttribute("displayToUser")).booleanValue()); //$NON-NLS-1$

                    // Getting dynamic login fields:
                    for (IConfigurationElement currentLoginField : current.getChildren("loginField")) { //$NON-NLS-1$
                        DynamicFieldBean key = new DynamicFieldBean(currentLoginField.getAttribute("id"), //$NON-NLS-1$
                                currentLoginField.getAttribute("name"), //$NON-NLS-1$
                                new Boolean(currentLoginField.getAttribute("required")), //$NON-NLS-1$
                                new Boolean(currentLoginField.getAttribute("password"))); //$NON-NLS-1$
                        currentAction.getFields().add(key);
                    }

                    for (IConfigurationElement currentLoginField : current.getChildren("button")) { //$NON-NLS-1$
                        DynamicButtonBean key = new DynamicButtonBean(currentLoginField.getAttribute("id"), //$NON-NLS-1$
                                currentLoginField.getAttribute("name"), //$NON-NLS-1$
                                (SelectionListener) currentLoginField.createExecutableExtension("selectionListener")); //$NON-NLS-1$
                        currentAction.getButtons().add(key);
                    }

                    for (IConfigurationElement currentLoginField : current.getChildren("choiceField")) { //$NON-NLS-1$
                        DynamicChoiceBean key = new DynamicChoiceBean(currentLoginField.getAttribute("id"), //$NON-NLS-1$
                                currentLoginField.getAttribute("name")); //$NON-NLS-1$
                        for (IConfigurationElement currentChoice : currentLoginField.getChildren("choice")) { //$NON-NLS-1$
                            String value = currentChoice.getAttribute("value");
                            String label = currentChoice.getAttribute("label");
                            key.addChoice(value, label);
                        }
                        currentAction.getChoices().add(key);
                    }

                    list.add(currentAction);
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static IRepositoryFactory getRepositoriyById(String id) {
        for (IRepositoryFactory current : getAvailableRepositories()) {
            if (current.getId().equals(id)) {
                return current;
            }
        }
        return null;
    }
}
