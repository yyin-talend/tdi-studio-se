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
package org.talend.designer.core.ui;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.core.model.process.IProcess;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.views.problems.Problems;
import org.talend.designer.runprocess.IRunProcessService;

/**
 * Track the active Process being edited. <br/>
 * 
 * $Id$
 * 
 */
public class ActiveProcessTracker implements IPartListener {

    private static IProcess currentProcess;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPartListener#partActivated(org.eclipse.ui.IWorkbenchPart)
     */
    public void partActivated(final IWorkbenchPart part) {
        if (MultiPageTalendEditor.ID.equals(part.getSite().getId())) {
            MultiPageTalendEditor mpte = (MultiPageTalendEditor) part;
            mpte.setName();

            IProcess process = mpte.getTalendEditor().getProcess();
            if (process instanceof Process) {
                Process p = (Process) process;
                if (!p.isReadOnly() && p.isActivate()) {
                    if (p.checkDifferenceWithRepository()) {
                        mpte.getTalendEditor().setDirty(true);
                    }
                }
            }

//            if (updateContextSection()) {
//                mpte.getTalendEditor().setDirty(true);
//            }
        }
    }

//    private boolean updateContextSection() {
//        boolean modified = false;
//        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//        IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
//        PropertySheet sheet = (PropertySheet) view;
//        if (view instanceof TabbedPropertySheetPage) {
//            TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet.getCurrentPage();
//            if (tabbedPropertySheetPage.getCurrentTab() == null) {
//                return modified;
//            }
//            ISection[] sections = tabbedPropertySheetPage.getCurrentTab().getSections();
//            for (int i = 0; i < sections.length; i++) {
//                if (sections[i] instanceof ContextProcessSection2) {
//                    ContextProcessSection2 currentSection = (ContextProcessSection2) sections[i];
//                    modified = currentSection.updateContextView();
//                }
//            }
//        }
//        return modified;
//    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPartListener#partBroughtToTop(org.eclipse.ui.IWorkbenchPart)
     */
    public void partBroughtToTop(IWorkbenchPart part) {
        if (MultiPageTalendEditor.ID.equals(part.getSite().getId())) {
            MultiPageTalendEditor mpte = (MultiPageTalendEditor) part;
            mpte.setName();
            IProcess process = mpte.getTalendEditor().getProcess();
            currentProcess = process;

            Problems.setCurrentProcess(currentProcess);

            IRunProcessService service = DesignerPlugin.getDefault().getRunProcessService();
            service.setActiveProcess(process);

            Problems.setTitle("Job " + process.getProperty().getLabel()); //$NON-NLS-1$
            Problems.switchToCurProblemView();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPartListener#partClosed(org.eclipse.ui.IWorkbenchPart)
     */
    public void partClosed(IWorkbenchPart part) {
        if (MultiPageTalendEditor.ID.equals(part.getSite().getId())) {
            MultiPageTalendEditor mpte = (MultiPageTalendEditor) part;
            IProcess process = mpte.getTalendEditor().getProcess();
            Problems.removeProblemsByProcessId(process.getId());

            IRunProcessService service = DesignerPlugin.getDefault().getRunProcessService();
            service.removeProcess(process);

            if (currentProcess == process) {
                Problems.setTitle(""); //$NON-NLS-1$
                Problems.clearAll();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPartListener#partDeactivated(org.eclipse.ui.IWorkbenchPart)
     */
    public void partDeactivated(IWorkbenchPart part) {
        // Do nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPartListener#partOpened(org.eclipse.ui.IWorkbenchPart)
     */
    public void partOpened(IWorkbenchPart part) {
        // Do nothing
    }

}
