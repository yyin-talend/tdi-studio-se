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
package org.talend.designer.codegen;

import org.eclipse.core.resources.IFile;
import org.talend.commons.exception.SystemException;
import org.talend.core.model.properties.RoutineItem;
import org.talend.designer.codegen.i18n.Messages;

import com.sun.org.apache.xml.internal.utils.UnImplNode;

/**
 * Routine synchronizer of java project.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: JavaRoutineSynchronizer.java JavaRoutineSynchronizer 2007-2-2 下午03:29:12 +0000 (下午03:29:12, 2007-2-2 2007)
 * yzhang $
 * 
 */
public class JavaRoutineSynchronizer implements IRoutineSynchronizer {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.codegen.IRoutineSynchronizer#syncAllRoutines()
     */
    public void syncAllRoutines() throws SystemException {
        // TODO operation need to be done as perl done.
        throw new UnsupportedOperationException(
                Messages.getString("JavaRoutineSynchronizer.UnsupportedOperation.Exception1")); //$NON-NLS-1$

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.codegen.IRoutineSynchronizer#syncRoutine(org.talend.core.model.properties.RoutineItem)
     */
    public IFile syncRoutine(RoutineItem routineItem) throws SystemException {
        // TODO operation need to be done as perl done.
        throw new UnsupportedOperationException(
                Messages.getString("JavaRoutineSynchronizer.UnsupportedOperation.Exception2")); //$NON-NLS-1$

        // return null;
    }
}
