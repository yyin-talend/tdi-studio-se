// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend – www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.designer.business.diagram.custom.util;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class EmfPropertyHelper {

    /**
     * DOC mhelleboid Comment method "getItemPropertyDescriptor".
     * 
     * @param itemPropertySource
     * @param eObject TODO
     * @param structuralFeature TODO
     * @return
     */
    public static IItemPropertyDescriptor getItemPropertyDescriptor(IItemPropertySource itemPropertySource,
            EObject eObject, EStructuralFeature structuralFeature) {
        IItemPropertyDescriptor itemPropertyDescriptor = null;

        List propertyDescriptors = itemPropertySource.getPropertyDescriptors(eObject);
        for (Iterator iter = propertyDescriptors.iterator(); iter.hasNext();) {
            IItemPropertyDescriptor tempItemPropertyDescriptor = (IItemPropertyDescriptor) iter.next();
            if (tempItemPropertyDescriptor.getFeature(eObject).equals(structuralFeature)) {
                itemPropertyDescriptor = tempItemPropertyDescriptor;
            }
        }
        return itemPropertyDescriptor;
    }

    /**
     * DOC mhelleboid Comment method "getItemPropertySource".
     * 
     * @param adapterFactory TODO
     * @param eObject TODO
     * 
     * @return
     */
    public static IItemPropertySource getItemPropertySource(AdapterFactory adapterFactory, EObject eObject) {
        return (IItemPropertySource) adapterFactory.adapt(eObject, IItemPropertySource.class);
    }

    /**
     * DOC mhelleboid Comment method "getValue".
     * 
     * @param itemPropertyDescriptor TODO
     * @param eObject TODO
     * @return
     */
    public static String getValue(IItemPropertyDescriptor itemPropertyDescriptor, Object eObject) {
        String value = ""; //$NON-NLS-1$
        Object propertyValue = itemPropertyDescriptor.getPropertyValue(eObject);
        if (propertyValue instanceof IItemPropertySource) {
            IItemPropertySource propertyValueWrapper = (IItemPropertySource) propertyValue;
            value = propertyValueWrapper.getEditableValue(eObject).toString();
        }
        return value;
    }

}
