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
package org.talend.designer.core.ui.editor.properties.controllers.generator;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.BidiMap;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.designer.core.ui.MultiPageTalendEditor;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 */
public interface IDynamicProperty {

    public BidiMap getHashCurControls();

    public Element getElement();

    public MultiPageTalendEditor getPart();

    public EComponentCategory getSection();

    public Composite getComposite();

    public Map<String, IMetadataTable> getRepositoryTableMap();

    public void setCurRowSize(int i);

    public int getCurRowSize();

    public Map<String, ConnectionItem> getRepositoryConnectionItemMap();

    public Map<String, List<String>> getTablesMap();

    public Map<String, List<String>> getQueriesMap();

    public void updateRepositoryList();

    public Map<String, Query> getRepositoryQueryStoreMap();

    public Map<String, String> getTableIdAndDbTypeMap();

    public Map<String, String> getTableIdAndDbSchemaMap();

    public void refresh();
}
