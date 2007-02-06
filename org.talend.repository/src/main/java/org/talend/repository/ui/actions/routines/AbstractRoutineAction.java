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
package org.talend.repository.ui.actions.routines;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.PartInitException;
import org.talend.commons.exception.SystemException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.RoutineItem;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.repository.editor.RepositoryEditorInput;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.actions.AContextualAction;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public abstract class AbstractRoutineAction extends AContextualAction {

    /**
     * DOC smallet Comment method "openRoutineEditor".
     * 
     * @param routineItem
     * @throws SystemException
     * @throws PartInitException
     */
    protected void openRoutineEditor(RoutineItem routineItem) throws SystemException, PartInitException {
        ICodeGeneratorService service = (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(
                ICodeGeneratorService.class);
        IFile file = service.createPerlRoutineSynchronizer().syncRoutine(routineItem);
        RepositoryEditorInput input = new RepositoryEditorInput(file, routineItem);

        getActivePage().openEditor(input, "org.talend.designer.core.ui.editor.StandAloneTalendPerlEditor"); //$NON-NLS-1$
    }
}
