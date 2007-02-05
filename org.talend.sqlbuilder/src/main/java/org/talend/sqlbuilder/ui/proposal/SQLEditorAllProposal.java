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
package org.talend.sqlbuilder.ui.proposal;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.swt.graphics.Image;
import org.talend.sqlbuilder.util.ImageUtil;

/**
 * DOC dev class global comment. Detailled comment <br/>
 * 
 * $Id: SQLEditorAllProposal.java,v 1.9 2006/11/06 08:15:37 qiang.zhang Exp $
 * 
 */
public class SQLEditorAllProposal implements IContentProposal {

    private String content;

    private String label;

    private String description;

    private int position;

    private Image image;

    private String allString;
    /**
     * DOC dev SQLEditorAllProposal constructor comment.
     * 
     * @param hasString has input String
     * @param allString full String
     * @param position Cursor current positioon
     * @param contents text edited all content
     */
    public SQLEditorAllProposal(String hasString, String allString, int position, String[] contents, String dbType) {
        super();
        this.allString = allString;
        setImages();
        if (!dbType.equals("PostgreSQL")) { //$NON-NLS-1$
            hasString = initHasString(hasString);
            label = label.replaceAll("\"", ""); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            hasString = initHasStringForPostgres(hasString);
        }
        hasString = label.substring(0, hasString.length());
        label = label.replaceAll("\"", ""); //$NON-NLS-1$ //$NON-NLS-2$
        String tmp = initLabel(dbType);
        contents[0] = contents[0].substring(0, contents[0].length() - hasString.length()) + hasString;
        content = contents[0];
        content += tmp.substring(hasString.length());
        content += contents[1];
        description = allString;
        this.position = position + tmp.substring(hasString.length()).length();
    }

    private void setImages() {
        if (this.allString.indexOf(".") != -1) { //$NON-NLS-1$
            if (this.allString.indexOf(".") == this.allString.lastIndexOf(".")) { //$NON-NLS-1$ //$NON-NLS-2$
                image = ImageUtil.getImage("Images.TableIcon"); //$NON-NLS-1$
            } else {
                image = ImageUtil.getImage("Images.ColumnIcon"); //$NON-NLS-1$
            }
        }
        if (allString.indexOf("alias: ") != -1) { 
            image = ImageUtil.getImage("Image.sqlAliasIcon"); //$NON-NLS-1$
            this.allString = this.allString.substring(0, this.allString.indexOf("\n")); //$NON-NLS-1$
        }

    }

    private String initHasStringForPostgres(String hasString) {
        label = allString;
        int index = label.indexOf("."); //$NON-NLS-1$
        int index2 = label.lastIndexOf("."); //$NON-NLS-1$
        String qualityName = ""; //$NON-NLS-1$
        if (index > -1) {
            qualityName = label.substring(index + 1, label.length());
            if (index == index2) {
                label = qualityName;
            } else {
                hasString = getColumnHasString(hasString, qualityName);
                label = label.substring(index2 + 1, label.length());
            }
        }
        return hasString;
    }

    private String getColumnHasString(String hasString, String qualityName) {
        int index3;

        String newQualityName = qualityName.replaceAll("\"", ""); //$NON-NLS-1$ //$NON-NLS-2$
        int hasX = hasString.lastIndexOf("\""); //$NON-NLS-1$

        if (hasX != -1) {
            String newQualityName2 = qualityName.replaceFirst("\"", ""); //$NON-NLS-1$ //$NON-NLS-2$
            newQualityName2 = newQualityName2.replaceFirst("\"", ""); //$NON-NLS-1$ //$NON-NLS-2$
            if (hasString.indexOf("\"") == newQualityName2.indexOf("\"")) { //$NON-NLS-1$ //$NON-NLS-2$
                index3 = newQualityName2.indexOf("."); //$NON-NLS-1$
            } else {
                index3 = qualityName.indexOf("."); //$NON-NLS-1$
            }
        } else {
            index3 = newQualityName.indexOf("."); //$NON-NLS-1$
        }

        String newHasString = hasString.replaceAll("\"", ""); //$NON-NLS-1$ //$NON-NLS-2$
        if (!"".equals(hasString) //$NON-NLS-1$
                && (qualityName.toLowerCase().startsWith(hasString.toLowerCase()) || newQualityName.toLowerCase()
                        .startsWith(newHasString.toLowerCase()))) {
            if (hasString.indexOf(".") > -1) { //$NON-NLS-1$
                hasString = hasString.substring(index3 + 1);
            }
        }
        return hasString;
    }

    /**
     * DOC dev Comment method "initLabel".
     * 
     * @param dbType
     * @return
     */
    private String initLabel(String dbType) {
        String tmp = label;
        int index = allString.indexOf("."); //$NON-NLS-1$
        if (index > -1 && dbType.equals("PostgreSQL")) { //$NON-NLS-1$
            tmp = "\"" + label + "\""; //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            tmp = label;
        }
        return tmp;
    }

    /**
     * DOC dev Comment method "initHasString".
     * 
     * @param hasString
     * @param allString
     * @return
     */
    protected String initHasString(String hasString) {
        label = allString;
        int index = label.indexOf("."); //$NON-NLS-1$
        int index2 = label.lastIndexOf("."); //$NON-NLS-1$
        String qualityName = ""; //$NON-NLS-1$
        if (index > -1) {
            qualityName = label.substring(index + 1, label.length());
            // if (!dbType.equals("PostgreSQL")) {
            qualityName = qualityName.replaceAll("\"", ""); //$NON-NLS-1$ //$NON-NLS-2$
            // }

            if (index == index2) {
                label = qualityName;
            } else {
                int index3;
                String newHasString = hasString.replaceAll("\"", ""); //$NON-NLS-1$ //$NON-NLS-2$

                String newQualityName = qualityName.replaceAll("\"", ""); //$NON-NLS-1$ //$NON-NLS-2$
                index3 = qualityName.indexOf("."); //$NON-NLS-1$

                if (!"".equals(hasString) //$NON-NLS-1$
                        && (qualityName.toLowerCase().startsWith(hasString.toLowerCase()) || newQualityName
                                .toLowerCase().startsWith(newHasString.toLowerCase()))) {
                    if (hasString.indexOf(".") > -1) { //$NON-NLS-1$
                        hasString = hasString.substring(index3 + 1);
                    }
                }
                label = label.substring(index2 + 1, label.length());
            }

        }

        return hasString;
    }

    /**
     * Getter for image.
     * 
     * @return the image
     */
    public Image getImage() {
        return this.image;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposal#getContent()
     */
    /**
     * Get edit text Content.
     * 
     * @return the content String.
     */
    public String getContent() {
        return content;
    }

    /**
     * Get cursor position after insert accepted string.
     * 
     * @return the position int.
     */
    public int getCursorPosition() {
        return position;
    }

    /**
     * Get label's description information and show in InfoPopupDialog.
     * 
     * @return the description Information String.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get label and show in ContentProposalPopup.
     * 
     * @return the label String.
     */
    public String getLabel() {
        return label;
    }

}
