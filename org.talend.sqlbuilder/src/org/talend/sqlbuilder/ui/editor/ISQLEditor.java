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
package org.talend.sqlbuilder.ui.editor;

import org.eclipse.swt.widgets.Shell;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;
import org.talend.sqlbuilder.util.ConnectionParameters;

/**
 * This interface is responsible for defining methods for SQLBuilderEditorComposite class.
 * 
 * @author ftang
 * 
 */
public interface ISQLEditor {

    /**
     * Gets SessionTreeNode.
     * 
     * @return an instance of SessionTreeNode.
     */
    SessionTreeNode getSessionTreeNode();

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
     * Sets sessionTreeNode.
     * 
     * @param sessionTreeNode
     */
    void setSessionTreeNode(SessionTreeNode sessionTreeNode);

    /**
     * 
     * Gets sql query.
     * 
     * @return
     */
    String getSQLToBeExecuted();

    /**
     * 
     * Sets query text into editor.
     */
    void setEditorContent(ConnectionParameters connParam);

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
     * Sets the flag for indicating current editor whether is the default one.
     * 
     * @param isDefaultEditor
     */
    void setDefaultEditor(boolean isDefaultEditor);

    /**
     * Sets the content of editor.
     */
    void setEditorContent(String string);
}
