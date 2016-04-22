// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.generic.model.migration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.talend.designer.core.generic.utils.ParameterUtilTool;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;

/**
 * created by hcyi on Apr 7, 2016 Detailled comment
 *
 */
public class NewSalesforceMigrationTask extends NewComponentFrameworkMigrationTask {

    @Override
    protected Properties getPropertiesFromFile() {
        Properties props = new Properties();
        InputStream in = getClass().getResourceAsStream("NewSalesforceMigrationTask.properties");//$NON-NLS-1$
        try {
            props.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }
    
    @Override
    protected ElementParameterType getParameterType(NodeType node, String paramName) {
    	ElementParameterType paramType = ParameterUtilTool.findParameterType(node, paramName);
    	if(node != null && paramType != null){
    		Object value = ParameterUtilTool.convertParameterValue(paramType);
    		if("MODULENAME".equals(paramName) && "CustomModule".equals(value)){
        		paramName =  "CUSTOM_MODULE";
        		paramType = ParameterUtilTool.findParameterType(node, paramName);
            }else if("CONNECTION".equals(paramName) && value!=null && !"".equals(value)){
        		ElementParameterType useConnection = ParameterUtilTool.findParameterType(node, "USE_EXISTING_CONNECTION");
        		if(useConnection!=null && Boolean.valueOf(String.valueOf(ParameterUtilTool.convertParameterValue(useConnection)))){
        			return paramType;
        		}else{
        			return null;
        		}
        	}else if("API".equals(paramName)){
        		if("soap".equals(value)){
        			paramType.setValue("Query");
        		}else{
        			paramType.setValue("Bulk");
        		}
            } else if ("LOGIN_TYPE".equals(paramName)) {
            	if("BASIC".equals(value)) {
            		paramType.setValue("Basic");
            	} else if("OAUTH".equals(value)) {
            		paramType.setValue("OAuth");
            	}
            }
    	}
    	return paramType;
    }
}
