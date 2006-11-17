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

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.dbstructure.RepositoryNodeType;
import org.talend.sqlbuilder.ui.ISQLBuilderDialog;
import org.talend.sqlbuilder.util.ConnectionParameters;

/**
 * DOC qianbing class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml,v 1.3 2006/11/01 05:38:28 nicolas Exp $
 * 
 */
public class OpenQueryAction extends OpenNewEditorAction {

    private ISQLBuilderDialog dialog;

    /**
     * DOC qianbing OpenQueryAction constructor comment.
     */
    public OpenQueryAction(ISelectionProvider selectionProvider, ISQLBuilderDialog dialog,
            ConnectionParameters connParam) {
        super(selectionProvider, dialog, connParam, false);
        setText(Messages.getString("DBStructureComposite.OpenQuery")); //$NON-NLS-1$
        setToolTipText(Messages.getString("DBStructureComposite.OpenQuery"));//$NON-NLS-1$
    }

    @Override
    public void run() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.sqlbuilder.actions.OpenNewEditorAction#selectionChanged(org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    public void selectionChanged(IStructuredSelection selection) {
    }

}
