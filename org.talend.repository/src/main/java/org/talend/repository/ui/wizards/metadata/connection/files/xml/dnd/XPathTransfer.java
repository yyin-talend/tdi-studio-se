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
package org.talend.repository.ui.wizards.metadata.connection.files.xml.dnd;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.talend.repository.i18n.Messages;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: TableEntriesTransfer.java 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class XPathTransfer extends ByteArrayTransfer {

    private XmlToSchemaDraggedData draggedData;

    private static final String XML_NODE_TO_XPATH_TYPE_NAME = "XML_NODE_TO_XPATH_ENTRIES"; //$NON-NLS-1$

    private static final int XML_NODE_TO_XPATH_ENTRIES_ID = registerType(XML_NODE_TO_XPATH_TYPE_NAME);

    private static final XPathTransfer INSTANCE = new XPathTransfer();

    public static XPathTransfer getInstance() {
        return INSTANCE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.dnd.Transfer#getTypeIds()
     */
    @Override
    protected int[] getTypeIds() {
        return new int[] { XML_NODE_TO_XPATH_ENTRIES_ID };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.dnd.Transfer#getTypeNames()
     */
    @Override
    protected String[] getTypeNames() {
        return new String[] { XML_NODE_TO_XPATH_TYPE_NAME };
    }

    @Override
    protected void javaToNative(Object object, TransferData transferData) {
    }

    @Override
    protected Object nativeToJava(TransferData transferData) {
        return new byte[0];
    }

    protected XmlToSchemaDraggedData getDraggedData() {
        return draggedData;
    }

    protected void setDraggedData(XmlToSchemaDraggedData draggedData) {
        this.draggedData = draggedData;
    }

}
