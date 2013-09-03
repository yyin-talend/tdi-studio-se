package org.talend.designer.core.assist;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.editor.AbstractTalendEditor;

public class TalendEditorComponentCreationAssistAction extends Action implements IWorkbenchWindowActionDelegate, IPartListener {

    private IWorkbenchWindow window;

    public TalendEditorComponentCreationAssistAction() {
        // System.out.println("instantiate talend assist");
    }

    @Override
    public void run(IAction action) {
        boolean isChecked = action.isChecked();
        if (isChecked) {
            window.getActivePage().addPartListener(this);
            TalendEditorComponentCreationUtil.registerAssistListenerFromOpenedEditors();
        } else {
            window.getActivePage().removePartListener(this);
            TalendEditorComponentCreationUtil.removeAssistListenerOnOpenedEditors();
        }
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
    }

    @Override
    public void dispose() {
    }

    @Override
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    @Override
    public void partActivated(IWorkbenchPart part) {
    }

    @Override
    public void partBroughtToTop(IWorkbenchPart part) {
    }

    @Override
    public void partClosed(IWorkbenchPart part) {
    }

    @Override
    public void partDeactivated(IWorkbenchPart part) {
    }

    @Override
    public void partOpened(IWorkbenchPart part) {
        if (part == null || !(part instanceof AbstractMultiPageTalendEditor)) {
            return;
        }
        AbstractTalendEditor talendEditor = ((AbstractMultiPageTalendEditor) part).getTalendEditor();
        TalendEditorComponentCreationUtil.addComponentCreationAssist(talendEditor);

    }

}
