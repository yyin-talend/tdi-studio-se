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
package org.talend.designer.core.ui.views.contexts;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 */
public class ContextsView extends ViewPart {

    public static final String ID = "org.talend.designer.core.ui.views.ContextsView";

    private MultiPageTalendEditor part;

    private JobContextViewComposite composite;

    protected CommandStack getCommandStack() {
        return (CommandStack) part.getTalendEditor().getAdapter(CommandStack.class);
    }

    public ContextsView() {
    }

    private Composite parent;

    @Override
    public void createPartControl(Composite parent) {
        this.parent = parent;
        parent.setLayout(new GridLayout());
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = SWT.FILL;
        gridData.horizontalAlignment = SWT.FILL;
        parent.setLayoutData(gridData);
        // final Label label = new Label(parent, SWT.NONE);
        // label.setText("This is not available");
        getPart();
        if (part == null) {
            composite = new JobContextViewComposite(parent);
            GridLayout gridLayout = new GridLayout();
            gridLayout.marginWidth = ITabbedPropertyConstants.HSPACE + 2;
            gridLayout.marginHeight = ITabbedPropertyConstants.VSPACE;
            composite.setLayout(gridLayout);

            GridData gridData2 = new GridData();
            gridData2.grabExcessHorizontalSpace = true;
            gridData2.grabExcessVerticalSpace = true;
            gridData2.verticalAlignment = SWT.FILL;
            gridData2.horizontalAlignment = SWT.FILL;
            composite.setLayoutData(gridData2);
            updateContextView();
        } else {
            createJobComposite();
        }
    }

    /**
     * qzhang Comment method "createJobComposite".
     * 
     * @param parent
     */
    private void createJobComposite() {
        // for (Control child : parent.getChildren()) {
        // if (child != null && !child.isDisposed()) {
        // child.dispose();
        // }
        // }
        getPart();
        composite = new JobContextViewComposite(parent, part);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = ITabbedPropertyConstants.HSPACE + 2;
        gridLayout.marginHeight = ITabbedPropertyConstants.VSPACE;
        composite.setLayout(gridLayout);

        GridData gridData2 = new GridData();
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.grabExcessVerticalSpace = true;
        gridData2.verticalAlignment = SWT.FILL;
        gridData2.horizontalAlignment = SWT.FILL;
        composite.setLayoutData(gridData2);

        initialContents();
    }

    public void updateContextView() {
        composite.disposeAllComponents();
        getPart();
        if (part != null) {
            boolean modified = composite.updateContextFromRepository();
            if (modified) {
                part.getTalendEditor().setDirty(true);
            }
        }
        composite.addComponents();
        composite.layout();
        composite.refresh();
    }

    public void updateContextView(boolean isBuildIn) {
        composite.disposeAllComponents();
        getPart();
        if (part != null) {
            boolean modified = composite.updateContextFromRepository();
            if (modified) {
                part.getTalendEditor().setDirty(true);
            }
        }
        composite.setReadOnly(!isBuildIn);
        composite.addComponents();
        composite.refresh();
        composite.layout();
    }

    public void updateContextView(boolean isBuildIn, boolean isDisposeAll) {
        if (isDisposeAll) {
            composite.disposeAllComponents();
        }
        getPart();
        if (part != null) {
            boolean modified = composite.updateContextFromRepository();
            if (modified) {
                part.getTalendEditor().setDirty(true);
            }
        }
        composite.setReadOnly(!isBuildIn);
        // composite.addComponents();
        composite.refresh();
        composite.layout();
    }

    public void refresh() {
        if (composite != null) {
            getPart();
            if (part != null) {
                composite.setPart(part);
                initialContents();
            }
        } else {
            createJobComposite();
        }
    }

    private Process process;

    /**
     * qzhang Comment method "initialContents".
     */
    private void initialContents() {
        if (part != null) {
            process = part.getTalendEditor().getProcess();
            composite.setContextManager(process.getContextManager());
            composite.setProcess(process);
            updateContextView();
        } else {
            composite.setContextManager(null);
            composite.setReadOnly(true);
            composite.setProcess(null);
            updateContextView();
        }
    }

    @Override
    public void setFocus() {
        updateContextView(true);
    }

    public void setPartName(String title) {
        String viewName = "Contexts"; //$NON-NLS-1$

        if (!title.equals("")) { //$NON-NLS-1$
            viewName = viewName + "(" + title + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        super.setPartName(viewName);
    }

    private void getPart() {
        final IEditorPart activeEditor = getSite().getPage().getActiveEditor();
        if (activeEditor instanceof MultiPageTalendEditor) {
            part = (MultiPageTalendEditor) activeEditor;
        } else {
            part = null;
        }
    }
}
