// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
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
import org.talend.commons.utils.workbench.extensions.ExtensionImplementationProviders;
import org.talend.commons.utils.workbench.extensions.ExtensionPointImpl;
import org.talend.commons.utils.workbench.extensions.ISimpleExtensionPoint;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ActionsHelper {

    private static final Comparator COMP = new ActionsLevelComparator();

    @SuppressWarnings("unchecked")
    public static List<ITreeContextualAction> getActions() {
        List<ITreeContextualAction> toReturn = new ArrayList<ITreeContextualAction>();
        ISimpleExtensionPoint actionExtensionPoint = new ExtensionPointImpl("org.talend.core.repositoryContextualsActions",
                "Action", -1, -1);
        List<IConfigurationElement> extension = ExtensionImplementationProviders.getInstanceV2(actionExtensionPoint);

        for (IConfigurationElement current : extension) {
            try {
                ITreeContextualAction currentAction = (ITreeContextualAction) current.createExecutableExtension("class");
                try {
                    int level = Integer.parseInt(current.getAttribute("level"));
                    currentAction.setLevel(level);
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
