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
package org.talend.sqlbuilder.actions;

import java.util.List;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.dbstructure.SessionTreeNodeUtils;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;
import org.talend.sqlbuilder.ui.editor.ISQLEditor;

/**
 * This class is responsible for representing repository name as items into combo component. <br/>
 * 
 * @author ftang
 * 
 */
public class SQLEditorSessionSwitcher extends ControlContribution {

    private ISQLEditor editor;

    private Combo sessionCombo;

    /**
     * @param editor SQLEditor to which this session switcher belongs
     */
    public SQLEditorSessionSwitcher(ISQLEditor editor) {

        super("org.talend.sqlbuilder.sessionswitcher");

        this.editor = editor;
    }

    protected Control createControl(Composite parent) {

        sessionCombo = new Combo(parent, SWT.READ_ONLY);
        sessionCombo.setToolTipText(Messages.getString("SQLEditor.Actions.ChooseSession.ToolTip"));
        List<String> sessionNames = SessionTreeNodeUtils.getRepositoryNames();

        sessionCombo.setItems(sessionNames.toArray(new String[sessionNames.size()]));

        sessionCombo.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                String repoName = sessionCombo.getText();
                SessionTreeNode node = SessionTreeNodeUtils.getSessionTreeNode(repoName);
                editor.setSessionTreeNode(node);
                editor.refresh(true);
            }

        });

        return sessionCombo;
    }

    public void refreshSelectedRepository() {
        sessionCombo.setText(editor.getRepositoryName());
    }

}
