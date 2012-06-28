package org.talend.repository.ui.wizards.exportjob.scriptsmanager.esb;

import java.net.URI;
import java.util.Collection;

import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.utils.EmfModelUtils;

// OSGi DataSource
public class DataSourceConfig {

	private String additionalJobBeanParams = "";
	private String additionalJobBundleConfig = "";

    public static boolean isDBConnectionJob(ProcessItem processItem) {
        return !getDBConnectionComponents(processItem).isEmpty();
    }

    private static Collection<NodeType> getDBConnectionComponents(ProcessItem processItem) {
    	return EmfModelUtils.getComponentsByName(processItem, "tJDBCConnection", "tMysqlConnection");
    }

	public DataSourceConfig(ProcessItem processItem) {
        Collection<NodeType> dbComponents = getDBConnectionComponents(processItem);
        if (!dbComponents.isEmpty()) {
        	additionalJobBeanParams +=
        		  "\n\t\t<property name=\"dataSources\">" 
        		+ "\n\t\t\t<map>";
        	
        	for (NodeType dbComponent : dbComponents) {
        		String id = EmfModelUtils.computeTextElementValue("UNIQUE_NAME", dbComponent);
        		String beanPool = id + "Pool";

        		additionalJobBeanParams +=
            		"\n\t\t\t\t<entry key=\"" + id + "\" value-ref=\"" + beanPool + "\" />";

        		String beanDataSource = id + "DataSource";
        		String url = EmfModelUtils.computeTextElementValue("URL", dbComponent);
				URI jdbcURI = URI.create(url.substring(6, url.length() - 1)); // remove jdbc: and quotes
				additionalJobBundleConfig +=
              		  "\n\t<bean id=\"" + beanDataSource + "\" class=\"org.apache.derby.jdbc.ClientConnectionPoolDataSource\">"
                      + "\n\t\t<property name=\"serverName\" value=\"" + jdbcURI.getHost() + "\"/>"
                      + "\n\t\t<property name=\"portNumber\" value=\"" + jdbcURI.getPort() + "\"/>"
                      + "\n\t\t<property name=\"databaseName\" value=\"" + jdbcURI.getPath() + "\"/>"
                      + "\n\t\t<property name=\"user\" value=" + EmfModelUtils.computeTextElementValue("USER", dbComponent) + "/>"
                      + "\n\t\t<property name=\"password\" value=" + EmfModelUtils.computeTextElementValue("PASS", dbComponent) + "/>"
                      + "\n\t</bean>"
                      + "\n\t<bean id=\"" + beanPool + "\" class=\"org.apache.commons.dbcp.datasources.SharedPoolDataSource\" destroy-method=\"close\">"
                      + "\n\t\t<property name=\"connectionPoolDataSource\" ref=\"" + beanDataSource + "\"/>"
                      + "\n\t</bean>";
			}
        	additionalJobBeanParams +=
        		  "\n\t\t\t</map>"
        		+ "\n\t\t</property>";
        }
//        <bean id="mysqlDataSource" class="com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource">
//        <property name="url" value="${datasource.url}"/>
//        <property name="user" value="${datasource.username}"/>
//        <property name="password" value="${datasource.password}"/>
//	</bean>

	}

	public String getAdditionalJobBeanParams() {
		return additionalJobBeanParams;
	}

	public String getAdditionalJobBundleConfig() {
		return additionalJobBundleConfig;
	}
}
