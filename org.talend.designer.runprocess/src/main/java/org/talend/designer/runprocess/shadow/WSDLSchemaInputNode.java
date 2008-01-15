// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.runprocess.shadow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.repository.preview.WSDLSchemaBean;

/**
 * DOC qwei class global comment. Detailled comment
 */
public class WSDLSchemaInputNode extends FileInputNode {

    /**
     * DOC qwei WSDLSchemaInputNode constructor comment.
     * 
     * @param nodeType
     */
    private List<IMetadataTable> metadatas = null;

    private String encoding = "";

    /**
     * Constructs a new WSDLSchemaInputNode.
     * 
     * @param schemaBean
     * @param string
     */
    public WSDLSchemaInputNode(String fileName, String encoding, List<IMetadataTable> metadatas, WSDLSchemaBean schemaBean,
            ArrayList parameters) {
        super("tWebServiceInput"); //$NON-NLS-1$
        this.encoding = encoding;
        addParameters(schemaBean, parameters);
        setMetadataList(metadatas);
    }

    /**
     * DOC qwei Comment method "addParameters".
     * 
     * @param schemaBean
     * @param parameters
     */
    private void addParameters(WSDLSchemaBean schemaBean, ArrayList parameters) {
        IElementParameter param = new TextElementParameter("ENDPOINT", schemaBean.getWslUrl());
        addParameter(param);
        if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA)) {
            addJavaParameters(schemaBean);
        } else if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.PERL)) {
            addPerlParameters(schemaBean);
        }
        param = new TextElementParameter("METHOD", schemaBean.getMethod());
        addParameter(param);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Object string : parameters) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("VALUE", TalendTextUtils.addQuotes(string.toString()));
            list.add(map);
        }
        param = new ObjectElementParameter("PARAMS", list);
        param.setListItemsDisplayCodeName(new String[] { "VALUE" });
        addParameter(param);

    }

    private void addJavaParameters(WSDLSchemaBean schemaBean) {
        IElementParameter param = new TextElementParameter("NEED_AUTH", "" + schemaBean.isNeedAuth());
        addParameter(param);

        param = new TextElementParameter("AUTH_USERNAME", schemaBean.getUserName());
        addParameter(param);

        param = new TextElementParameter("AUTH_PASSWORD", schemaBean.getPassword());
        addParameter(param);

        param = new TextElementParameter("UES_PROXY", "" + schemaBean.isUseProxy());
        addParameter(param);

        param = new TextElementParameter("PROXY_HOST", schemaBean.getProxyHost());
        addParameter(param);

        param = new TextElementParameter("PROXY_PORT", schemaBean.getProxyPort());
        addParameter(param);

        param = new TextElementParameter("PROXY_USERNAME", schemaBean.getProxyUser());
        addParameter(param);

        param = new TextElementParameter("PROXY_PASSWORD", schemaBean.getProxyPassword());
        addParameter(param);
    }

    private void addPerlParameters(WSDLSchemaBean schemaBean) {
        IElementParameter param = new TextElementParameter("WSDL", schemaBean.getEndpointURI());
        addParameter(param);
        param = new TextElementParameter("ENCODING", this.encoding);
        addParameter(param);
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
