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
package org.talend.designer.abstractmap.managers;

import java.util.List;
import java.util.Set;

import org.talend.designer.abstractmap.model.tableentry.ITableEntry;
import org.talend.designer.abstractmap.ui.visualmap.link.IMapperLink;


/**
 * DOC amaumont  class global comment. Detailled comment
 * <br/>
 *
 */
public interface ILinkManager {

    /**
     * DOC amaumont Comment method "addLink".
     * 
     * @param link
     */
    public void addLink(IMapperLink link);

    /**
     * DOC amaumont Comment method "addLink".
     * 
     * @param link
     */
    public void removeLink(IMapperLink link);

    /**
     * DOC amaumont Comment method "getGraphicalLinks".
     * 
     * @param targetTableEntry
     * @return
     */
    public Set<IMapperLink> getLinksFromTarget(ITableEntry dataMapTableEntry);

    public Set<IMapperLink> getLinksFromSource(ITableEntry dataMapTableEntry);

    /**
     * DOC amaumont Comment method "clearLinks".
     */
    public void clearLinks();

    /**
     * DOC amaumont Comment method "getLinks".
     * 
     * @return
     */
    public List<IMapperLink> getLinks();

    /**
     * DOC amaumont Comment method "getSourcesForTarget".
     * 
     * @param dataMapTableEntry
     */
    public Set<ITableEntry> getSourcesForTarget(ITableEntry dataMapTableEntry);

    /**
     * DOC amaumont Comment method "orderLinks".
     */
    public void orderLinks();

    /**
     * Getter for currentNumberLinks.
     * 
     * @return the currentNumberLinks
     */
    public int getCurrentNumberLinks();

    /**
     * Get the count of inputs levels.
     * 
     * @return the count of inputs levels
     */
    public int getCountOfInputLevels();

}
