// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
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
import java.util.HashSet;
import java.util.Set;

import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.utils.EmfModelUtils;

/**
 * OSGi DataSource
 */
public class DataSourceConfig {

	private String additionalJobBeanParams = ""; //$NON-NLS-1$

    public static boolean isDBConnectionJob(ProcessItem processItem) {
        return !getDBConnectionComponents(processItem).isEmpty();
    }

    private static Collection<NodeType> getDBConnectionComponents(ProcessItem processItem) {
    	return EmfModelUtils.getComponentsByName(processItem,
    			"tJDBCConnection",
    			"tJDBCInput",
    			"tMysqlConnection",
    			"tMysqlInput");
    }

	public DataSourceConfig(ProcessItem processItem) {
        Collection<NodeType> dbComponents = getDBConnectionComponents(processItem);
        if (!dbComponents.isEmpty()) {
        	Set<String> aliases = new HashSet<String>();
        	for (NodeType dbComponent : dbComponents) {
        		aliases.add(EmfModelUtils.computeTextElementValue("DATASOURCE_ALIAS", dbComponent));
			}
        	additionalJobBeanParams +=
        		  "\n\t\t<property name=\"dataSources\">" 
        		+ "\n\t\t\t<map>";
        	for (String alias : aliases) {
        		additionalJobBeanParams +=
                		  "\n\t\t\t\t<entry key=\"" + alias + "\">"
                		+ "\n\t\t\t\t\t<reference interface=\"javax.sql.DataSource\" filter=\"(osgi.jndi.service.name=" + alias + ")\"/>"
		      			+ "\n\t\t\t\t</entry>";
        	}
        	additionalJobBeanParams +=
        		  "\n\t\t\t</map>"
        		+ "\n\t\t</property>";
        }
	}

	public String getAdditionalJobBeanParams() {
		return additionalJobBeanParams;
	}

}
