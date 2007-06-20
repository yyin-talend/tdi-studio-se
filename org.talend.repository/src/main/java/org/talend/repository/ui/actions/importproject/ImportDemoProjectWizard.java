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
package org.talend.repository.ui.actions.importproject;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 */
public class ImportDemoProjectWizard extends Wizard implements IImportWizard {

    private ImportDemoProjectPage demoProjectPage;

    private List<DemoProjectBean> demoProjectList;

    @Override
    public boolean performFinish() {
        return true;
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {

        setDefaultPageImageDescriptor(IDEWorkbenchPlugin.getIDEImageDescriptor("wizban/exportzip_wiz.png"));//$NON-NLS-1$
        setNeedsProgressMonitor(true);
    }

    public void addPages() {
        super.addPages();
        demoProjectPage = new ImportDemoProjectPage(null);
        demoProjectPage.setImportDemoProjectList(this.demoProjectList);
        super.addPage(demoProjectPage);
    }

    public ImportDemoProjectWizard(List<DemoProjectBean> demoProjectList) {
        this.demoProjectList = demoProjectList;
    }

    public int getSelectedDemoProjectIndex() {
        return demoProjectPage.getSelectedDemoProjectIndex();
    }
}
