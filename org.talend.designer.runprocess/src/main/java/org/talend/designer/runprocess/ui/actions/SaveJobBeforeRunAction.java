// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
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
package org.talend.designer.runprocess.ui.actions;

import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.designer.core.ui.MultiPageTalendEditor;

/**
 * Save the job if it is refered by a tRunJob. <br/>
 * 
 * 2007-07-16 17:06:40
 * 
 */
public class SaveJobBeforeRunAction extends Action {

    private IProcess activeProcess;

    /**
     * SaveJobBeforeRunAction constructor comment.
     * 
     * @param activeProcess
     */
    public SaveJobBeforeRunAction(IProcess activeProcess) {
        this.activeProcess = activeProcess;
    }

    public void run() {
        List<? extends INode> nodes = activeProcess.getGraphicalNodes();

        if (nodes.isEmpty()) {
            return;
        }
        IEditorReference[] references = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getEditorReferences();
        for (int i = 0; i < references.length; i++) {
            IEditorPart part = references[i].getEditor(false);
            if (!(part instanceof MultiPageTalendEditor)) {
                continue;
            }
            MultiPageTalendEditor editor = (MultiPageTalendEditor) part;
            editor.doSave(new NullProgressMonitor());
        }
    }
}
