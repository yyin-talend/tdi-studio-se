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

import java.util.HashSet;
import java.util.Set;

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

    public static String getAdditionalJobBeanParams(ProcessItem processItem, boolean addOsgiPrefix) {
        Set<String> aliases = getAliases(processItem);
        String additionalJobBeanParams = ""; //$NON-NLS-1$
        if (!aliases.isEmpty()) {
            additionalJobBeanParams += "\n\t\t<property name=\"dataSources\">" //$NON-NLS-1$
                    + "\n\t\t\t<map>"; //$NON-NLS-1$
            for (String alias : aliases) {
                additionalJobBeanParams += "\n\t\t\t\t<entry key=\"" + alias + "\">" //$NON-NLS-1$
                        + "\n\t\t\t\t\t<" + (addOsgiPrefix ? "osgi:" : "") + "reference interface=\"javax.sql.DataSource\" filter=\"(osgi.jndi.service.name=" + alias + ")\"/>" //$NON-NLS-1$
                        + "\n\t\t\t\t</entry>"; //$NON-NLS-1$
            }
            additionalJobBeanParams += "\n\t\t\t</map>" //$NON-NLS-1$
                    + "\n\t\t</property>"; //$NON-NLS-1$
        }
        return additionalJobBeanParams;
    }

    private static Set<String> getAliases(ProcessItem processItem) {
        return getAliases(processItem, new HashSet<String>());
    }

    private static Set<String> getAliases(ProcessItem processItem, Set<String> jobNameVersionChecked) {
        Set<String> aliases = new HashSet<String>();
        String jobName = processItem.getProperty().getLabel();
        String jobVersion = processItem.getProperty().getVersion();
        String id = jobName + "_" + jobVersion; //$NON-NLS-1$
        if (jobNameVersionChecked.contains(id)) {
            return aliases; // no need to add more to the list, just return the empty list
        }
        jobNameVersionChecked.add(id);
        for (Object o : processItem.getProcess().getNode()) {
            if (o instanceof NodeType) {
                NodeType node = (NodeType) o;
                if (EmfModelUtils.isComponentActive(node)) {
                    String alias = EmfModelUtils.computeTextElementValue("DATASOURCE_ALIAS", node); //$NON-NLS-1$
                    if (!"".equals(alias)) {
                        aliases.add(alias);
                    }
                }
            }
        }
        Set<JobInfo> subjobInfos = ProcessorUtilities.getChildrenJobInfo(processItem);
        for (JobInfo subjobInfo : subjobInfos) {
            aliases.addAll(getAliases(subjobInfo.getProcessItem(), jobNameVersionChecked));
        }
        return aliases;
    }

}
