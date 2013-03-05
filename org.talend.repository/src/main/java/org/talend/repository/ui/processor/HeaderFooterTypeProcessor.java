package org.talend.repository.ui.processor;

import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * DOC zli class global comment. Detailled comment
 */
public class HeaderFooterTypeProcessor extends SingleTypeProcessor {

    /**
     * DOC zli HeaderFooterTypeProcessor constructor comment.
     * 
     * @param repositoryType
     */
    public HeaderFooterTypeProcessor(String repositoryType) {
        super(repositoryType);
    }

    @Override
    protected ERepositoryObjectType getType() {
        return ERepositoryObjectType.METADATA_HEADER_FOOTER;
    }

}