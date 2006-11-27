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
 * DOC dev  class global comment. Detailled comment
 * <br/>
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
    /**
     * DOC dev SQLEditorAllProposal constructor comment.
     * @param hasString has input String
     * @param allString full String
     * @param position Cursor current positioon
     * @param contents text edited all content
     */
    public SQLEditorAllProposal(String hasString, String allString, int position, String[] contents , String dbType) {
        super();
        hasString = initHasString(hasString, allString, dbType);
        if (!dbType.equals("PostgreSQL")) {
        	label = label.replaceAll("\"", "");
        }
        hasString = label.substring(0, hasString.length());
        label = label.replaceAll("\"", "");
        String tmp = initLabel(dbType, allString);
        contents[0] = contents[0].substring(0, contents[0].length() - hasString.length()) + hasString;
        content = contents[0];
        content += tmp.substring(hasString.length());
        content += contents[1];
        description = allString;
        this.position = position + tmp.substring(hasString.length()).length();
    }

	/**
	 * DOC dev Comment method "initLabel".
	 * @param dbType
	 * @return
	 */
	private String initLabel(String dbType, String allString) {
		String tmp = label;
		int index = allString.indexOf(".");
        if (index > -1 && dbType.equals("PostgreSQL")) {
        	tmp = "\"" + label + "\"";
        } else {
        	tmp = label;
        }
		return tmp;
	}

	/**
	 * DOC dev Comment method "initHasString".
	 * @param hasString
	 * @param allString
	 * @return
	 */
	private String initHasString(String hasString, String allString, String dbType) {
		label = allString;
        int index = label.indexOf(".");
        int index2 = label.lastIndexOf(".");
        String qualityName = "";
        if (index > -1) {
        	qualityName = label.substring(index + 1, label.length());
        	if (!dbType.equals("PostgreSQL")) {
        		qualityName = qualityName.replaceAll("\"", "");
        	}
        	
        	if (index == index2) {
        		label = qualityName;
                image = ImageUtil.getDescriptor("Images.TableIcon").createImage();
        	} else {
        		int index3 = qualityName.indexOf(".");
        		String newHasString = hasString.replaceAll("\"", "");
        		String newQualityName = qualityName.replaceAll("\"", "");
        		if (!"".equals(hasString) && (qualityName.toLowerCase().startsWith(hasString.toLowerCase())
        				|| newQualityName.toLowerCase().startsWith(newHasString.toLowerCase()))) {
        			hasString = hasString.substring(index3 + 1);
        		}
        		label = label.substring(index2 + 1, label.length());
        		image = ImageUtil.getDescriptor("Images.ColumnIcon").createImage();
        	}
            
        }
		return hasString;
	}

    /**
     * Getter for image.
     * @return the image
     */
    public Image getImage() {
        return this.image;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.fieldassist.IContentProposal#getContent()
     */
    /**
     * Get edit text Content.
     * @return the content String.
     */
    public String getContent() {
        return content;
    }
    /**
     * Get cursor position after insert accepted string.
     * @return the position int. 
     */
    public int getCursorPosition() {
        return position;
    }

    /**
     * Get label's description information and show in InfoPopupDialog.
     * @return the description Information String.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get label and show in ContentProposalPopup.
     * @return the label String. 
     */
    public String getLabel() {
        return label;
    }

}
