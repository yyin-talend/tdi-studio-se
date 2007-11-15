// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.core.ui.editor.cmd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.ui.PlatformUI;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.core.ui.editor.notes.Note;
import org.talend.designer.core.ui.editor.notes.NoteEditPart;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.process.ProcessPart;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 */
public class MultiplePasteCommand extends CompoundCommand {

    private List<EditPart> oldSelection;

    private NodesPasteCommand nodeCmd;

    private NotesPasteCommand noteCmd;

    public MultiplePasteCommand(List<NodePart> nodeParts, List<NoteEditPart> noteParts, Process process) {
        nodeCmd = new NodesPasteCommand(nodeParts, process);
        nodeCmd.setMultipleCommand(true);
        add(nodeCmd);
        noteCmd = new NotesPasteCommand(noteParts, process);
        noteCmd.setMultipleCommand(true);
        add(noteCmd);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.CompoundCommand#execute()
     */
    @Override
    public void execute() {
        MultiPageTalendEditor multiPageTalendEditor = (MultiPageTalendEditor) PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        GraphicalViewer viewer = multiPageTalendEditor.getTalendEditor().getViewer();
        oldSelection = new ArrayList<EditPart>();
        for (EditPart editPart : (List<EditPart>) viewer.getSelectedEditParts()) {
            oldSelection.add(editPart);
        }
        // remove the old selection
        viewer.deselectAll();

        super.execute();

        EditPart processPart = (EditPart) viewer.getRootEditPart().getChildren().get(0);
        if (processPart instanceof ProcessPart) { // can only be ProcessPart but still test
            for (EditPart editPart : (List<EditPart>) processPart.getChildren()) {
                if (editPart instanceof NodePart) {
                    Node currentNode = (Node) editPart.getModel();
                    if (nodeCmd.getNodeContainerList().contains(currentNode.getNodeContainer())) {
                        viewer.appendSelection(editPart);
                    }
                } else if (editPart instanceof NoteEditPart) {
                    Note currentNode = (Note) editPart.getModel();
                    if (noteCmd.getNoteList().contains(currentNode)) {
                        viewer.appendSelection(editPart);
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.CompoundCommand#undo()
     */
    @Override
    public void undo() {
        // remove the current selection
        MultiPageTalendEditor multiPageTalendEditor = (MultiPageTalendEditor) PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        GraphicalViewer viewer = multiPageTalendEditor.getTalendEditor().getViewer();
        viewer.deselectAll();

        super.undo();

        // set the old selection active
        for (EditPart editPart : oldSelection) {
            viewer.appendSelection(editPart);
        }
    }

}
