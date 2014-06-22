// ============================================================================
package org.talend.designer.xmlmap;

import org.talend.core.model.process.INode;
import org.talend.core.service.IDesignerXMLMapperService;
import org.talend.designer.xmlmap.util.XMLMapperHelper;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public class DesignerXMLMapperService implements IDesignerXMLMapperService {


    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.IDesignerMapperSerivce#isVirtualComponent(org.talend.core.model.process.INode)
     */
    public boolean isVirtualComponent(INode node) {
        return XMLMapperHelper.isGeneratedAsVirtualComponent(node);
    }
}
