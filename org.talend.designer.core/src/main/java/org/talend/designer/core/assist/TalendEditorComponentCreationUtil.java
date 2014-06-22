package org.talend.designer.core.assist;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.process.IProcess2;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.DummyComponent;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.editor.AbstractTalendEditor;
import org.talend.designer.core.ui.preferences.TalendDesignerPrefConstants;
import org.talend.repository.model.ComponentsFactoryProvider;

public class TalendEditorComponentCreationUtil {

    private static final String COMPONENT_CREATION_ASSIST_LISTENER = "COMPONENT_CREATION_ASSIST_LISTENER";

    public static void addComponentCreationAssist(AbstractTalendEditor talendEditor) {

        final GraphicalViewer graphicalViewer = (GraphicalViewer) talendEditor.getAdapter(GraphicalViewer.class);
        final CommandStack commandStack = (CommandStack) talendEditor.getAdapter(CommandStack.class);
        final String categoryName = talendEditor.getComponenentsHandler().extractComponentsCategory().getName();
        final IProcess2 process = talendEditor.getProcess();

        KeyListener listener = new KeyListener() {

            public void keyReleased(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (Character.isISOControl(e.character) || Character.isSpaceChar(e.character)) {
                    return;
                }
                TalendEditorComponentCreationAssist assist = new TalendEditorComponentCreationAssist(categoryName,
                        graphicalViewer, commandStack, process);
                assist.showComponentCreationAssist(e.character);
            }
        };
        graphicalViewer.getControl().addKeyListener(listener);
        graphicalViewer.getControl().setData(COMPONENT_CREATION_ASSIST_LISTENER, listener);

    }

    public static void removeComponentCreationAssist(AbstractTalendEditor talendEditor) {
        GraphicalViewer graphicalViewer = (GraphicalViewer) talendEditor.getAdapter(GraphicalViewer.class);
        Object data = graphicalViewer.getControl().getData(COMPONENT_CREATION_ASSIST_LISTENER);
        if (data != null && data instanceof KeyListener) {
            graphicalViewer.getControl().removeKeyListener((KeyListener) data);
            graphicalViewer.getControl().update();
        }

    }

    public static void removeAssistListenerOnOpenedEditors() {
        IEditorReference[] editorReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getEditorReferences();
        for (IEditorReference er : editorReferences) {
            if (er == null) {
                continue;
            }
            IEditorPart editor = er.getEditor(true);
            if (editor == null || !(editor instanceof AbstractMultiPageTalendEditor)) {
                continue;
            }
            AbstractTalendEditor talendEditor = ((AbstractMultiPageTalendEditor) editor).getTalendEditor();
            removeComponentCreationAssist(talendEditor);
        }
    }

    public static void registerAssistListenerFromOpenedEditors() {
        IEditorReference[] editorReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getEditorReferences();
        for (IEditorReference er : editorReferences) {
            if (er == null) {
                continue;
            }
            IEditorPart editor = er.getEditor(true);
            if (editor == null || !(editor instanceof AbstractMultiPageTalendEditor)) {
                continue;
            }
            AbstractTalendEditor talendEditor = ((AbstractMultiPageTalendEditor) editor).getTalendEditor();
            addComponentCreationAssist(talendEditor);
        }
    }

    public static void updateAssistListener() {
        boolean isEnable = DesignerPlugin.getDefault().getPreferenceStore()
                .getBoolean(TalendDesignerPrefConstants.COMPONENT_ASSIST);
        if (isEnable) {
            registerAssistListenerFromOpenedEditors();
        } else {
            removeAssistListenerOnOpenedEditors();
        }
    }

    public static void updateAssistListener(AbstractTalendEditor talendEditor) {
        boolean isEnable = DesignerPlugin.getDefault().getPreferenceStore()
                .getBoolean(TalendDesignerPrefConstants.COMPONENT_ASSIST);
        if (isEnable) {
            addComponentCreationAssist(talendEditor);
        } else {
            removeComponentCreationAssist(talendEditor);
        }
    }

    /**
     * read all components belongs to some category (DI, CAMEL etc.) then store it into a map which can be reused
     * 
     * @param categoryName
     * @param entries
     */
    /*
     * TODO this can be improved after refactoring org.talend.core.model.components.IComponentsHandler implementation in
     * each editor
     */
    private static void readComponentsInCategory(String categoryName, Map<String, IComponent> entries) {
        IComponentsFactory componentsFactory = ComponentsFactoryProvider.getInstance();
        Set<IComponent> allComponents = componentsFactory.getComponents();
        for (IComponent component : allComponents) {
            String compType = component.getPaletteType();
            if (!component.isTechnical() && compType != null && categoryName.equals(compType)) {
                entries.put(component.getName(), component);
            }
        }
        DummyComponent noteComponent = new DummyComponent("Note");
        noteComponent.setIcon16(ImageProvider.getImageDesc(ECoreImage.CODE_ICON));
        noteComponent.setOriginalFamilyName("Misc");
        entries.put("Note", noteComponent);
    }

    private static Map<String, Map<String, IComponent>> entries = new HashMap<String, Map<String, IComponent>>();

    /**
     * get all components belongs to some category
     * 
     * @param categoryName
     * @return
     */
    public static Map<String, IComponent> getComponentsInCategory(String categoryName) {
        Map<String, IComponent> map = entries.get(categoryName);
        if (map == null) {
            map = new HashMap<String, IComponent>();
            entries.put(categoryName, map);
            readComponentsInCategory(categoryName, map);
        }
        return map;
    }
}
