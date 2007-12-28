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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.talend.core.model.process.INode2;

/**
 */
public class JobletNodeMoveCommand extends Command {

    private INode2 note;

    private Point location;

    private Point oldPos;

    /**
     * DOC qzhang JobletInputNodeCreateCommand constructor comment.
     * 
     * @param model
     * @param newObject
     * @param constraint
     */
    public JobletNodeMoveCommand(INode2 model, Point newPos) {
        this.note = model;
        location = newPos;
        setLabel("JobletNodeMoveCommand");
    }

    @Override
    public void execute() {
        if (location != null) {
            oldPos = note.getLocation();
            note.setLocation(location);
        }
    }

    @Override
    public void undo() {
        note.setLocation(oldPos);
    }
}
