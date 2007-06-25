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
package org.talend.sqlbuilder.actions;

import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.repository.model.RepositoryNode;
import org.talend.sqlbuilder.dbstructure.SqlBuilderRepositoryObject;
import org.talend.sqlbuilder.util.ConnectionParameters;
import org.talend.sqlbuilder.util.ImageUtil;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 */
public class SaveSQLAction extends AbstractEditorAction {

    private ImageDescriptor image = ImageUtil.getDescriptor("Images.SaveSQLIcon");

    private RepositoryNode repositoryNode;

    private Query query;

    /**
     * qzhang SaveSQLAction constructor comment.
     */
    public SaveSQLAction(RepositoryNode repositoryNodeInput, ConnectionParameters query) {
        this.query = query.getQueryObject();
        this.repositoryNode = repositoryNodeInput;
        SqlBuilderRepositoryObject o = (SqlBuilderRepositoryObject) repositoryNode.getObject();
        boolean isBuildin = o.isBuildIn() || query.isNodeReadOnly();
        setEnabled(!isBuildin);
        this.setImageDescriptor(image);
        this.setToolTipText(getText());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.sqlbuilder.actions.AbstractEditorAction#getText()
     */
    @Override
    public String getText() {
        
        return "Save";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.sqlbuilder.actions.AbstractEditorAction#run()
     */
    @Override
    public void run() {
        
        Query query2 = editor.doSaveSQL(query, false);
        if (query2 != null) {
            query = query2;
        }
    }

    
    public void setQuery(Query query) {
        this.query = query;
    }

}
