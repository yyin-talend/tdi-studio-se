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
package org.talend.repository.ui.wizards.exportjob.scriptsmanager.esb;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.talend.core.model.process.JobInfo;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.utils.EmfModelUtils;

/**
 * OSGi DataSource
 */
public class DataSourceConfig {

    private DataSourceConfig() {
    }

    public static Collection<String> getAliases(ProcessItem processItem) {
        return getAliases(processItem, new HashSet<String>());
    }

    private static Collection<String> getAliases(ProcessItem processItem, Collection<String> jobNameVersionChecked) {
        String jobName = processItem.getProperty().getLabel();
        String jobVersion = processItem.getProperty().getVersion();
        String id = jobName + '_' + jobVersion;
        if (!jobNameVersionChecked.add(id)) {
            return Collections.emptyList(); // no need to add more to the list, just return the empty list
        }
        Collection<String> aliases = new HashSet<String>();
        for (Object o : processItem.getProcess().getNode()) {
            if (o instanceof NodeType) {
                NodeType node = (NodeType) o;
                if (EmfModelUtils.isComponentActive(node)
                        && EmfModelUtils.computeCheckElementValue("SPECIFY_DATASOURCE_ALIAS", node)) { //$NON-NLS-1$
                    String alias = EmfModelUtils.computeTextElementValue("DATASOURCE_ALIAS", node); //$NON-NLS-1$
                    if (!alias.isEmpty()) {
                        aliases.add(alias);
                    }
                }
            }
        }

        for (JobInfo subjobInfo : ProcessorUtilities.getChildrenJobInfo(processItem)) {
            aliases.addAll(getAliases(subjobInfo.getProcessItem(), jobNameVersionChecked));
        }
        return aliases;
    }

}
