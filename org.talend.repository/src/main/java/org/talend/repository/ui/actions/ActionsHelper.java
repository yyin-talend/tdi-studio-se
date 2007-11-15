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
package org.talend.repository.ui.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.talend.commons.ui.swt.actions.ITreeContextualAction;
import org.talend.commons.utils.workbench.extensions.ExtensionImplementationProvider;
import org.talend.commons.utils.workbench.extensions.ExtensionPointLimiterImpl;
import org.talend.commons.utils.workbench.extensions.IExtensionPointLimiter;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ActionsHelper {

    private static final Comparator COMP = new ActionsLevelComparator();

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public static List<ITreeContextualAction> getRepositoryContextualsActions() {
        List<ITreeContextualAction> toReturn = new ArrayList<ITreeContextualAction>();
        IExtensionPointLimiter actionExtensionPoint = new ExtensionPointLimiterImpl(
                "org.talend.core.repositoryContextualsActions", //$NON-NLS-1$
                "Action"); //$NON-NLS-1$
        List<IConfigurationElement> extension = ExtensionImplementationProvider.getInstanceV2(actionExtensionPoint);

        for (IConfigurationElement current : extension) {
            try {
                ITreeContextualAction currentAction = (ITreeContextualAction) current
                        .createExecutableExtension("class"); //$NON-NLS-1$
                try {
                    int level = Integer.parseInt(current.getAttribute("level")); //$NON-NLS-1$
                    currentAction.setLevel(level);
                    currentAction.setReadAction("true".equals(current.getAttribute("isReadAction"))); //$NON-NLS-1$
                    currentAction.setEditAction("true".equals(current.getAttribute("isEditAction"))); //$NON-NLS-1$
                    currentAction.setPropertiesAction("true".equals(current.getAttribute("isPropertiesAction"))); //$NON-NLS-1$
                } catch (NumberFormatException e) {
                    currentAction.setLevel(1000);
                }
                toReturn.add(currentAction);
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(toReturn, COMP);

        return toReturn;
    }

    /**
     * 
     * DOC smallet ActionsHelper class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    private static class ActionsLevelComparator implements Comparator<ITreeContextualAction> {

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(ITreeContextualAction o1, ITreeContextualAction o2) {
            Integer l1 = o1.getLevel();
            Integer l2 = o2.getLevel();
            return l1.compareTo(l2);
        }

    }
}
