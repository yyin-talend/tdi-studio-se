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
 * DOC dev  class global comment. Detailled comment
 * <br/>
 *
 * @author ftang
 *
 */
public interface ISQLEditor {

    /**
     * DOC dev Comment method "getSessionTreeNode".
     * @return
     */
    
    SessionTreeNode getSessionTreeNode();

    //void setMessage(String msg);

    /**
     * DOC dev Comment method "getShell".
     * @return
     */
    Shell getShell();

    boolean getIfLimit();
    String getMaxResult();
   
    void setSessionTreeNode(SessionTreeNode sessionTreeNode);

   // Object getEditorToolBar();

    String getSQLToBeExecuted();
    
    void setEditorContent(ConnectionParameters connParam);

    void doSaveAs();

    void clearText();

    void refresh(boolean b);

    String getRepositoryName();
    
    void setDefaultEditor(boolean isDefaultEditor);

    void setEditorContent(String string);
}
