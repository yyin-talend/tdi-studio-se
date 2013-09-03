package org.talend.designer.core.assist;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.editor.AbstractTalendEditor;

public class TalendEditorComponentCreationUtil {

	private static final String COMPONENT_CREATION_ASSIST_LISTENER = "COMPONENT_CREATION_ASSIST_LISTENER";

	public static void addComponentCreationAssist(
			AbstractTalendEditor talendEditor) {

		final GraphicalViewer graphicalViewer = (GraphicalViewer) talendEditor
				.getAdapter(GraphicalViewer.class);
		final CommandStack commandStack = (CommandStack) talendEditor
				.getAdapter(CommandStack.class);
		final String categoryName = talendEditor.getComponenentsHandler()
				.extractComponentsCategory().getName();

		KeyListener listener = new KeyListener() {

			public void keyReleased(KeyEvent e) {
				if (Character.isISOControl(e.character)
						|| Character.isSpaceChar(e.character)) {
					return;
				}
				TalendEditorComponentCreationAssist assist = new TalendEditorComponentCreationAssist(
						categoryName, graphicalViewer, commandStack);
				assist.showComponentCreationAssist(e.character);
			}

			public void keyPressed(KeyEvent e) {
			}
		};
		graphicalViewer.getControl().addKeyListener(listener);
		graphicalViewer.getControl().setData(
				COMPONENT_CREATION_ASSIST_LISTENER, listener);

	}

	public static void removeComponentCreationAssist(
			AbstractTalendEditor talendEditor) {
		GraphicalViewer graphicalViewer = (GraphicalViewer) talendEditor
				.getAdapter(GraphicalViewer.class);
		Object data = graphicalViewer.getControl().getData(
				COMPONENT_CREATION_ASSIST_LISTENER);
		if (data != null && data instanceof KeyListener) {
			graphicalViewer.getControl().removeKeyListener((KeyListener) data);
		}

	}

	public static void removeAssistListenerOnOpenedEditors() {
		IEditorReference[] editorReferences = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences();
		for (IEditorReference er : editorReferences) {
			if (er == null) {
				continue;
			}
			IEditorPart editor = er.getEditor(true);
			if (editor == null
					|| !(editor instanceof AbstractMultiPageTalendEditor)) {
				continue;
			}
			AbstractTalendEditor talendEditor = ((AbstractMultiPageTalendEditor) editor)
					.getTalendEditor();
			removeComponentCreationAssist(talendEditor);
		}
	}

	public static void registerAssistListenerFromOpenedEditors() {
		IEditorReference[] editorReferences = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences();
		for (IEditorReference er : editorReferences) {
			if (er == null) {
				continue;
			}
			IEditorPart editor = er.getEditor(true);
			if (editor == null
					|| !(editor instanceof AbstractMultiPageTalendEditor)) {
				continue;
			}
			AbstractTalendEditor talendEditor = ((AbstractMultiPageTalendEditor) editor)
					.getTalendEditor();
			addComponentCreationAssist(talendEditor);
		}
	}
}
