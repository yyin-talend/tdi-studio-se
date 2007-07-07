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
package org.talend.sqlbuilder.ui;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.CorePlugin;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.RepositoryElementDelta;
import org.talend.repository.model.RepositoryNode;
import org.talend.sqlbuilder.util.ConnectionParameters;

/**
 * DOC qianbing class global comment. An interface provides some method for the composites in SQLBuilderDialog to
 * interactive. <br/>
 * 
 * $Id: talend-code-templates.xml,v 1.3 2006/11/01 05:38:28 nicolas Exp $
 * 
 */
public interface ISQLBuilderDialog {

    /**
     * DOC qianbing Comment method "openEditor".
     * 
     * @param node RepositoryNode with the DatabaseConnection
     * @param repositoryName all the repositories' name
     * @param connParam ConnectionParameters
     * @param isDefaultEditor whether is the DefaultEditor
     */
    public void openEditor(RepositoryNode node, List<String> repositoryName, ConnectionParameters connParam,
            boolean isDefaultEditor);

    public void openEditor(RepositoryNode node, List<String> repositoryName, ConnectionParameters connParam,
            boolean isDefaultEditor, List<RepositoryNode> nodeSel);

    public ConnectionParameters getConnParameters();

    public void refreshNode(RepositoryNode node);

    public Shell getShell();

    public IProgressMonitor getProgressMonitor();

    public void notifySQLBuilder(IRepositoryObject o);
}
