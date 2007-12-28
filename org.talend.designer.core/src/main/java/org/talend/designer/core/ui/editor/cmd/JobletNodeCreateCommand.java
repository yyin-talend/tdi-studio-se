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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.INode2;
import org.talend.designer.core.ui.editor.process.Process;

/**
 */
public class JobletNodeCreateCommand extends CreateCommand {

    private INode2 note;

    private Dimension size;

    /**
     * DOC qzhang JobletInputNodeCreateCommand constructor comment.
     * 
     * @param model
     * @param newObject
     * @param constraint
     */
    public JobletNodeCreateCommand(Process model, INode2 newObject, Rectangle constraint) {
        super("JobletInput", model, constraint.getLocation());
        this.note = newObject;
        size = constraint.getSize();
    }

    @Override
    public void execute() {
        if (location != null) {
            note.setLocation(this.location);
        }
        process.addJoblet((Element) note);
    }

    @Override
    public void undo() {
        process.removeJoblet((Element) note);
    }
}
