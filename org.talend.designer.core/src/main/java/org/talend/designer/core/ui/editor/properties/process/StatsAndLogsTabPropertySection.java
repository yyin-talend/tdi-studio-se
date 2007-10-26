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
package org.talend.designer.core.ui.editor.properties.process;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.core.model.process.EComponentCategory;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.process.ProcessPart;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC Administrator class global comment. Detailed comment <br/>
 * 
 */
public class StatsAndLogsTabPropertySection extends DynamicTabbedPropertySection {

    public StatsAndLogsTabPropertySection() {
        super(EComponentCategory.STATSANDLOGS);
    }

    public void setInput(final IWorkbenchPart workbenchPart, final ISelection selection) {
        Object input = ((IStructuredSelection) selection).getFirstElement();
        if (input instanceof RepositoryNode) {
            // This is the only RepositoryNode that displays the Job.
            Process process = StatsAndLogsSectionFilter.getProcessPartByRepositoryNode((RepositoryNode) input);
            if (process == null) {
                return;
            }
            // make a mock processPart here for super.setInput();
            ProcessPart part = new ProcessPart();
            part.setModel(process);

            StructuredSelection sel = new StructuredSelection(part);
            super.setInput(workbenchPart, sel);
        } else {
            super.setInput(workbenchPart, selection);
        }
    }

    IWorkbenchPart oldPart;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection#refresh()
     */
    @Override
    public void refresh() {
        addComponents(false);
        super.refresh();
    }
}
