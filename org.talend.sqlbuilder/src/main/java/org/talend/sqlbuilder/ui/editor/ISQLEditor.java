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
package org.talend.sqlbuilder.ui.editor;

import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.repository.model.RepositoryNode;
import org.talend.sqlbuilder.editors.MultiPageSqlBuilderEditor;

/**
 * This interface is responsible for defining methods for SQLBuilderEditorComposite class.
 * 
 * @author ftang
 * 
 */
public interface ISQLEditor {

    /**
     * Gets Shell.
     * 
     * @return an instance of Shell.
     */
    Shell getShell();

    /**
     * Checks if sql result length is limited.
     */
    boolean getIfLimit();

    /**
     * Gets the allowed max result length .
     * 
     * @return
     */
    String getMaxResult();

    /**
     * Gets repository node.
     * 
     * @return an instance of RepositoryNode
     */
    RepositoryNode getRepositoryNode();

    /**
     * 
     * Gets sql query.
     * 
     * @return
     */
    String getSQLToBeExecuted();

    /**
     * Save current editor's text as a file.
     */
    void doSaveAs();

    /**
     * Clear current editor's text.
     */
    void clearText();

    /**
     * 
     * Refresh actions availability on the toolbar.
     */
    void refresh(boolean b);

    /**
     * Gets repository name.
     * 
     * @return a string
     */
    String getRepositoryName();

    /**
     * Gets the flag for indicating current editor whether is the default one.
     */
    boolean getDefaultEditor();

    /**
     * Sets the content of editor.
     */
    void setEditorContent(String string);

    /**
     * Saves current editor's sql text into dbstructure composite.
     */
    Query doSaveSQL(Query query2, boolean as);

    /**
     * Sets repository node.
     * 
     * @param node
     */
    void setRepositoryNode(RepositoryNode node);
    
    MultiPageSqlBuilderEditor getMultiPageEditor();
    
}
