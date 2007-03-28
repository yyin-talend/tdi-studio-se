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

import org.eclipse.jface.fieldassist.IContentProposal;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.designer.mapper.MapperMain;
import org.talend.designer.mapper.i18n.Messages;
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
        content = language.getLocation(entry.getParentName(), entry.getName()) + " "; //$NON-NLS-1$
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

        String separator = " - "; //$NON-NLS-1$
        if (entry instanceof InputColumnTableEntry) {

            InputColumnTableEntry inputEntry = (InputColumnTableEntry) entry;

            IMetadataColumn metadataColumn = inputEntry.getMetadataColumn();
            sb
                    .append(Messages.getString("EntryContentProposal.metadataColumn")).append(" '").append(metadataColumn.getLabel()) //$NON-NLS-1$ //$NON-NLS-2$
                    .append("' "); //$NON-NLS-1$
            sb.append(Messages.getString("EntryContentProposal.properties")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
            sb.append(separator)
                    .append(Messages.getString("EntryContentProposal.column")).append(metadataColumn.getLabel()); //$NON-NLS-1$
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
            sb.append(separator).append(Messages.getString("EntryContentProposal.key")).append(metadataColumn.isKey()); //$NON-NLS-1$
            if (!MapperMain.isStandAloneMode()) {
                sb.append(AbstractLanguage.CARRIAGE_RETURN);
                sb
                        .append(separator)
                        .append(Messages.getString("EntryContentProposal.type")).append(format(metadataColumn.getTalendType())); //$NON-NLS-1$
            }
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
            sb.append(separator).append(Messages.getString("EntryContentProposal.length")); //$NON-NLS-1$
            if (metadataColumn.getLength() != null && metadataColumn.getLength() > 0) {
                sb.append(format(metadataColumn.getLength()));
            }

            sb.append(AbstractLanguage.CARRIAGE_RETURN);
            sb.append(separator).append(Messages.getString("EntryContentProposal.precision")); //$NON-NLS-1$
            if (metadataColumn.getPrecision() != null && metadataColumn.getPrecision() > 0) {
                sb.append(format(metadataColumn.getPrecision()));
            }
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
            sb
                    .append(separator)
                    .append(Messages.getString("EntryContentProposal.default")).append(format(metadataColumn.getDefault())); //$NON-NLS-1$
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
            sb
                    .append(separator)
                    .append(Messages.getString("EntryContentProposal.comment")).append(format(metadataColumn.getComment())); //$NON-NLS-1$
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
            sb.append(separator).append(Messages.getString("EntryContentProposal.expressionKey")); //$NON-NLS-1$
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
            sb.append(format(entry.getExpression()));
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
        } else if (entry instanceof VarTableEntry) {
            sb
                    .append(Messages.getString("EntryContentProposal.variable")).append(" '").append(entry.getName()).append("' :"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
            sb.append(separator).append(Messages.getString("EntryContentProposal.expressionKey")); //$NON-NLS-1$
            sb.append(AbstractLanguage.CARRIAGE_RETURN);
            sb.append(format(entry.getExpression()));
        }
        return sb.toString();
    }

    public String format(Object object) {
        if (object == null) {
            return ""; //$NON-NLS-1$
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
