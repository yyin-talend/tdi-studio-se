// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.maven.job.setting.repository.standalone;

import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.designer.maven.ui.setting.repository.tester.IRepositorySettingTester;
import org.talend.repository.model.IRepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class StandaloneJobRepositorySettingTester implements IRepositorySettingTester {

    @Override
    public boolean valid(IRepositoryNode node) {
        ERepositoryObjectType contentType = node.getContentType();
        if (contentType != null) {
            if (contentType.equals(ERepositoryObjectType.PROCESS)) {
                return true;
            }
            if (contentType.equals(ERepositoryObjectType.PROCESS_MR)) {
                return true;
            }
            if (contentType.equals(ERepositoryObjectType.PROCESS_STORM)) {
                return true;
            }
            if (contentType.equals(ERepositoryObjectType.PROCESS_SPARK)) {
                return true;
            }
            if (contentType.equals(ERepositoryObjectType.PROCESS_SPARKSTREAMING)) {
                return true;
            }
        }
        return false;
    }

}
