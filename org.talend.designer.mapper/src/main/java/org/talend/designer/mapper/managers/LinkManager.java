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
package org.talend.designer.mapper.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talend.designer.mapper.model.tableentry.ITableEntry;
import org.talend.designer.mapper.ui.visualmap.link.IMapperLink;
import org.talend.designer.mapper.ui.visualmap.link.LinkState;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class LinkManager {

    private static final Comparator<IMapperLink> COMPARATOR = new Comparator<IMapperLink>() {

        public int compare(IMapperLink link1, IMapperLink link2) {
            if (link1.getState() == link2.getState()) {
                return 0;
            }
            if (link1.getState() == LinkState.SELECTED) {
                return 1;
            }
            return -1;
        }

    };

    private List<IMapperLink> links = new ArrayList<IMapperLink>();

    private int currentNumberLinks = 0;

    private Map<ITableEntry, Set<ITableEntry>> targetToSources = new HashMap<ITableEntry, Set<ITableEntry>>();

    private Map<ITableEntry, Set<IMapperLink>> sourceTableEntryToLinks = new HashMap<ITableEntry, Set<IMapperLink>>();

    private Map<ITableEntry, Set<IMapperLink>> targetTableEntryToLinks = new HashMap<ITableEntry, Set<IMapperLink>>();

    public LinkManager() {
        super();
        currentNumberLinks = 0;
    }

    /**
     * DOC amaumont Comment method "addLink".
     * 
     * @param link
     */
    public void addLink(IMapperLink link) {
        currentNumberLinks++;
        // System.out.println(currentNumberLinks + " links");

        links.add(link);
        ITableEntry sourceITableEntry = link.getPointLinkDescriptor1().getTableEntry();
        ITableEntry targetITableEntry = link.getPointLinkDescriptor2().getTableEntry();
        Set<ITableEntry> targetDataMapTableEntries = getSourcesCollection(targetITableEntry);
        targetDataMapTableEntries.add(sourceITableEntry);
        Set<IMapperLink> targetGraphicalLinks = getGraphicalLinksFromTarget(targetITableEntry);
        targetGraphicalLinks.add(link);
        Set<IMapperLink> sourceGraphicalLinks = getGraphicalLinksFromSource(sourceITableEntry);
        sourceGraphicalLinks.add(link);
    }

    /**
     * DOC amaumont Comment method "getGraphicalLinks".
     * 
     * @param targetTableEntry
     * @return
     */
    private Set<IMapperLink> getGraphicalLinksFromTarget(ITableEntry dataMapTableEntry) {
        Set<IMapperLink> graphicalLinks = targetTableEntryToLinks.get(dataMapTableEntry);
        if (graphicalLinks == null) {
            graphicalLinks = new HashSet<IMapperLink>();
            targetTableEntryToLinks.put(dataMapTableEntry, graphicalLinks);
        }
        return graphicalLinks;
    }

    /**
     * DOC amaumont Comment method "getGraphicalLinks".
     * 
     * @param targetTableEntry
     * @return
     */
    public Set<IMapperLink> getLinksFromTarget(ITableEntry dataMapTableEntry) {
        return new HashSet<IMapperLink>(getGraphicalLinksFromTarget(dataMapTableEntry));
    }

    /**
     * DOC amaumont Comment method "getGraphicalLinks".
     * 
     * @param targetTableEntry
     * @return
     */
    private Set<IMapperLink> getGraphicalLinksFromSource(ITableEntry dataMapTableEntry) {
        Set<IMapperLink> graphicalLinks = sourceTableEntryToLinks.get(dataMapTableEntry);
        if (graphicalLinks == null) {
            graphicalLinks = new HashSet<IMapperLink>();
            sourceTableEntryToLinks.put(dataMapTableEntry, graphicalLinks);
        }
        return graphicalLinks;
    }

    public Set<IMapperLink> getLinksFromSource(ITableEntry dataMapTableEntry) {
        return new HashSet<IMapperLink>(getGraphicalLinksFromSource(dataMapTableEntry));
    }

    /**
     * DOC amaumont Comment method "getSourcesCollection".
     * 
     * @param targetITableEntry
     * @return
     */
    private Set<ITableEntry> getSourcesCollection(ITableEntry targetITableEntry) {
        Set<ITableEntry> targetDataMapTableEntries = targetToSources.get(targetITableEntry);
        if (targetDataMapTableEntries == null) {
            targetDataMapTableEntries = new HashSet<ITableEntry>();
            targetToSources.put(targetITableEntry, targetDataMapTableEntries);
        }
        return targetDataMapTableEntries;
    }

    /**
     * DOC amaumont Comment method "addLink".
     * 
     * @param link
     */
    public void removeLink(IMapperLink link) {
        currentNumberLinks--;

        links.remove(link);
        ITableEntry sourceITableEntry = link.getPointLinkDescriptor1().getTableEntry();
        ITableEntry targetITableEntry = link.getPointLinkDescriptor2().getTableEntry();
        Set<ITableEntry> targetDataMapTableEntries = getSourcesCollection(targetITableEntry);
        targetDataMapTableEntries.remove(sourceITableEntry);
        getGraphicalLinksFromSource(sourceITableEntry).remove(link);
        getGraphicalLinksFromTarget(targetITableEntry).remove(link);
    }

    /**
     * DOC amaumont Comment method "clearLinks".
     */
    public void clearLinks() {
        links.clear();
        targetToSources.clear();
    }

    /**
     * DOC amaumont Comment method "getLinks".
     * 
     * @return
     */
    public List<IMapperLink> getLinks() {
        return this.links;
    }

    /**
     * DOC amaumont Comment method "getSourcesForTarget".
     * 
     * @param dataMapTableEntry
     */
    public Set<ITableEntry> getSourcesForTarget(ITableEntry dataMapTableEntry) {
        return Collections.unmodifiableSet(getSourcesCollection(dataMapTableEntry));

    }

    /**
     * DOC amaumont Comment method "orderLinks".
     */
    public void orderLinks() {
        Collections.sort(links, COMPARATOR);
    }

    /**
     * Getter for currentNumberLinks.
     * 
     * @return the currentNumberLinks
     */
    public int getCurrentNumberLinks() {
        return this.currentNumberLinks;
    }
}
