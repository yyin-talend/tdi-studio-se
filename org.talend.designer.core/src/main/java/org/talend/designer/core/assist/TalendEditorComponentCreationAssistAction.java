package org.talend.designer.core.assist;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.editor.AbstractTalendEditor;
import org.talend.designer.core.ui.preferences.TalendDesignerPrefConstants;

public class TalendEditorComponentCreationAssistAction extends Action implements IPartListener {

    private IWorkbenchWindow window;

    public static final String ID = "org.talend.designer.core.assist.action";

    public static final String TEXT = "Enable Component Creation Assistant";

    private static TalendEditorComponentCreationAssistAction instance = new TalendEditorComponentCreationAssistAction();

    public static TalendEditorComponentCreationAssistAction getDefault() {
        return instance;
    }

    public TalendEditorComponentCreationAssistAction() {
        super(TEXT, IAction.AS_CHECK_BOX);
        this.window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        this.setId(ID);
        this.setDescription(TEXT);
        setChecked(getGlobalStore().getBoolean(TalendDesignerPrefConstants.COMPONENT_ASSIST));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        boolean isChecked = isChecked();
        getGlobalStore().setValue(TalendDesignerPrefConstants.COMPONENT_ASSIST, isChecked);
        IEditorReference[] editorParts = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getEditorReferences();
        for (IEditorReference reference : editorParts) {
            IEditorPart editorPart = reference.getEditor(true);
            if (editorPart != null && editorPart instanceof AbstractMultiPageTalendEditor) {
                AbstractMultiPageTalendEditor editor = (AbstractMultiPageTalendEditor) editorPart;
                AbstractTalendEditor talendEditor = editor.getTalendEditor();
                IAction action = talendEditor.getAction(ID);
                action.setChecked(isChecked);
                if (isChecked) {
                    window.getActivePage().addPartListener(this);
                    TalendEditorComponentCreationUtil.registerAssistListenerFromOpenedEditors();
                } else {
                    window.getActivePage().removePartListener(this);
                    TalendEditorComponentCreationUtil.removeAssistListenerOnOpenedEditors();
                }
            }
        }
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

    public IPreferenceStore getGlobalStore() {
        return DesignerPlugin.getDefault().getPreferenceStore();
    }

}
