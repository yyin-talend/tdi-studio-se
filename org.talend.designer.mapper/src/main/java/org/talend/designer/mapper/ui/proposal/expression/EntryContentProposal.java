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
package org.talend.designer.mapper.ui.proposal.expression;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.designer.mapper.MapperMain;
import org.talend.designer.mapper.language.AbstractLanguage;
import org.talend.designer.mapper.language.ILanguage;
import org.talend.designer.mapper.model.tableentry.ITableEntry;
import org.talend.designer.mapper.model.tableentry.InputColumnTableEntry;
import org.talend.designer.mapper.model.tableentry.VarTableEntry;

/**
 * Content proposal based on a IContextParameter. <br/>
 * 
 * $Id$
 * 
 */
public class EntryContentProposal implements IContentProposal {

    private ITableEntry entry;

    private ILanguage language;

    private String content;

    /**
     * Constructs a new ContextParameterProposal.
     * 
     * @param language
     * @param control
     */
    public EntryContentProposal(ITableEntry entry, ILanguage language) {
        super();
        this.entry = entry;
        this.language = language;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposal#getContent()
     */
    public String getContent() {
        content = language.getLocation(entry.getParentName(), entry.getName()) + " ";
        return content;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposal#getCursorPosition()
     */
    public int getCursorPosition() {
        return content.length() + 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposal#getDescription()
     */
    public String getDescription() {

        StringBuilder sb = new StringBuilder();

        if (entry instanceof InputColumnTableEntry) {

            InputColumnTableEntry inputEntry = (InputColumnTableEntry) entry;

            IMetadataColumn metadataColumn = inputEntry.getMetadataColumn();
            sb.append("Metadata column '").append(metadataColumn.getLabel()).append("' properties :");
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
            sb.append(" - Column :").append(metadataColumn.getLabel());
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
            sb.append(" - Key :").append(metadataColumn.isKey());
            if (!MapperMain.isStandAloneMode()) {
                sb.append(AbstractLanguage.CARRIAGE_RETURN);
                sb.append(" - Type :").append(format(metadataColumn.getTalendType()));
            }
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
            sb.append(" - Length :").append(format(metadataColumn.getLength()));
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
            sb.append(" - Precision :").append(format(metadataColumn.getPrecision()));
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
            sb.append(" - Default :").append(format(metadataColumn.getDefault()));
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
            sb.append(" - Comment :").append(format(metadataColumn.getComment()));
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
            sb.append(" - Expression key :");
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
            sb.append(format(entry.getExpression()));
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
        } else if (entry instanceof VarTableEntry) {
            sb.append("Variable '").append(entry.getName()).append("' :");
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
            sb.append(" - Expression key :");
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
            sb.append(format(entry.getExpression()));
        }
        return sb.toString();
    }

    public String format(Object object) {
        if (object == null) {
            return "";
        }
        return String.valueOf(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposal#getLabel()
     */
    public String getLabel() {
        return language.getLocation(entry.getParentName(), entry.getName());
    }

}
