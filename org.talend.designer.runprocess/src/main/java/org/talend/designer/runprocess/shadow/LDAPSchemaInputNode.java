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
package org.talend.designer.runprocess.shadow;

import java.util.List;

import org.talend.core.model.metadata.IMetadataTable;
import org.talend.repository.preview.LDAPSchemaBean;

/**
 * The class is used for LDAP schema on Repository View. <br/>
 * @author ftang, 18/09/2007
 * 
 */
public class LDAPSchemaInputNode extends FileInputNode {

    private List<IMetadataTable> metadatas = null;

    /**
     * Constructs a new LDAPSchemaInputNode.
     * 
     * @param schemaBean
     * @param string
     */
    public LDAPSchemaInputNode(String fileName, List<IMetadataTable> metadatas, String encoding,
            LDAPSchemaBean schemaBean) {
        super("tLDAPInput"); //$NON-NLS-1$
        int limit = 50;
        this.setColumnNumber(limit);

        String[] paramNames = new String[] { "FILENAME", "ENCODING", "HOST", "PORT", "PROTOCOL", "AUTHENTIFICATION",
                "USER", "PASSWD", "FILTER", "BASEDN",
                "MULTI_VALUE_SEPARATOR", "LIMIT", "TIMEOUT", "ALIASES", "REFERRALS" }; //$NON-NLS-1$
        String[] paramValues = new String[] { fileName, encoding, schemaBean.getHost(), schemaBean.getPort(),
                schemaBean.getEncryMethod(), schemaBean.isAuthen() + "", schemaBean.getUser(), schemaBean.getPasswd(),
                schemaBean.getFilter(), schemaBean.getBaseDN(), schemaBean.getMultiValueSeparator(),
                schemaBean.getCountLimit(), schemaBean.getTimeOutLimit(), schemaBean.getAliasDereferenring(),
                schemaBean.getReferrals() };

        for (int i = 0; i < paramNames.length; i++) {
            if (paramValues[i] != null) {
                TextElementParameter param = new TextElementParameter(paramNames[i], paramValues[i]);
                addParameter(param);
            }
        }
        setMetadataList(metadatas);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.shadow.ShadowNode#getMetadataList()
     */
    @Override
    public List<IMetadataTable> getMetadataList() {
        return metadatas;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.shadow.ShadowNode#setMetadataList(java.util.List)
     */
    @Override
    public void setMetadataList(List<IMetadataTable> metadataList) {
        this.metadatas = metadataList;
    }
}
