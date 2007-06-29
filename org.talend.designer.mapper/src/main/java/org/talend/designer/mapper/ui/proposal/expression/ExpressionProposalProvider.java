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
package org.talend.designer.mapper.ui.proposal.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.talend.designer.abstractmap.model.table.IDataMapTable;
import org.talend.designer.abstractmap.model.tableentry.IColumnEntry;
import org.talend.designer.abstractmap.model.tableentry.ITableEntry;
import org.talend.designer.mapper.language.ILanguage;
import org.talend.designer.mapper.language.LanguageProvider;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.model.table.AbstractDataMapTable;
import org.talend.designer.mapper.model.table.VarsTable;
import org.talend.designer.mapper.model.tableentry.ExpressionFilterEntry;
import org.talend.designer.mapper.model.tableentry.TableEntryLocation;
import org.talend.designer.mapper.ui.visualmap.zone.Zone;

/**
 * ContentProposalProvider which initialize valid locations of Mapper. <br/>
 * 
 * $Id$
 * 
 */
public class ExpressionProposalProvider implements IContentProposalProvider {

    private MapperManager mapperManager;

    private List<AbstractDataMapTable> tables;

    private ILanguage currentLanguage;

    private IContentProposalProvider[] otherContentProposalProviders;

    private ITableEntry currentModifiedEntry;

    /**
     * Constructs a new ProcessProposalProvider.
     * 
     * @param tables
     * @param control
     */
    public ExpressionProposalProvider(MapperManager mapperManager,
            IContentProposalProvider[] otherContentProposalProviders) {
        super();
        this.mapperManager = mapperManager;
        this.currentLanguage = LanguageProvider.getCurrentLanguage();
        this.otherContentProposalProviders = otherContentProposalProviders;
    }

    public void init(IDataMapTable currentTable, Zone[] zones, ITableEntry currentEntry) {

        tables = new ArrayList<AbstractDataMapTable>();
        for (int i = 0; i < zones.length; i++) {
            if (zones[i] == Zone.INPUTS) {
                tables.addAll(mapperManager.getInputTables());
            } else if (zones[i] == Zone.OUTPUTS) {
                tables.addAll(mapperManager.getOutputTables());
            } else if (zones[i] == Zone.VARS) {
                tables.addAll(mapperManager.getVarsTables());
            }
        }
//        if (!(currentTable instanceof VarsTable) && !(currentEntry instanceof ExpressionFilterEntry)) {
//            tables.remove(currentTable);
//        }
        this.currentModifiedEntry = currentEntry;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposalProvider#getProposals(java.lang.String, int)
     */
    public IContentProposal[] getProposals(String contents, int position) {
        List<IContentProposal> proposals = new ArrayList<IContentProposal>();

        TableEntryLocation sourceEntryLocation = new TableEntryLocation();

        for (IDataMapTable table : this.tables) {
            List<IColumnEntry> dataMapTableEntries = table.getColumnEntries();
            for (IColumnEntry entrySource : dataMapTableEntries) {
                sourceEntryLocation.tableName = entrySource.getParentName();
                sourceEntryLocation.columnName = entrySource.getName();
                if (mapperManager.getUiManager().checkSourceLocationIsValid(entrySource, currentModifiedEntry)) {
                    proposals.add(new EntryContentProposal(entrySource, this.currentLanguage));
                }
            }
        }

        for (IContentProposalProvider contentProposalProvider : otherContentProposalProviders) {
            proposals.addAll(Arrays.asList(contentProposalProvider.getProposals(contents, position)));
        }
        IContentProposal[] res = new IContentProposal[proposals.size()];
        res = proposals.toArray(res);
        return res;
    }

}
