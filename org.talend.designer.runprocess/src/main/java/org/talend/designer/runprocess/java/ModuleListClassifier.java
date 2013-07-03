// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.runprocess.java;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.utils.json.JSONException;
import org.talend.utils.json.JSONObject;

/**
 * Classifier classify need jars from path optional with path and/or parameters
 * to plain jar name. After add parameters to jobModule, need to retrieve plain
 * name first.
 * 
 * @see <a href="https://jira.talendforge.org/browse/TESB-9875">[TESB-9875]</a>
 */
public class ModuleListClassifier {



	/** Indicate whether refresh from studio storage when run job */
	private static final String PARAM_REFRESH_WHEN_RUN = "refreshWhenRun";
	/** Indicate whether refetch from source file to libs storage */
	private static final String PARAM_REFETCH_WHEN_RUN = "refetchWhenRun";
	
	/** source file path of external module*/
	private static final String PARAM_SOURCE_FILE = "sourceFile";

	private Set<String> plainModuleNamesSet = new HashSet<String>();
	private Set<String> modulesRefreshWhenRun = new HashSet<String>();
	private Set<String> modulesNeedRefetch = new HashSet<String>();

	/**
	 * Instantiates a new module list classifier. JobModule may contains path
	 * and/or parameters.
	 * 
	 * @param jobModuleList
	 *            the job module list
	 */
	public ModuleListClassifier(Set<String> jobModuleList) {
		for (String module : jobModuleList) {
			int paramIndex = module.indexOf('?');
			if (paramIndex > 0) {
				String params = module.substring(paramIndex + 1);
				File moduleFile = new File(module.substring(0, paramIndex));
				module = moduleFile.getName();
				
				try {
					JSONObject paramsJson = new JSONObject(params);
					if(paramsJson.has(PARAM_REFRESH_WHEN_RUN) 
						&&paramsJson.getBoolean(PARAM_REFRESH_WHEN_RUN)) {
							modulesRefreshWhenRun.add(module);
					}
					if(paramsJson.has(PARAM_REFETCH_WHEN_RUN)
						&&paramsJson.getBoolean(PARAM_REFETCH_WHEN_RUN)) {
						modulesNeedRefetch.add(paramsJson.getString(PARAM_SOURCE_FILE));
					}
				} catch (JSONException e) {
					ExceptionHandler.process(e);
				}
			}
			plainModuleNamesSet.add(module);
		}

	}

	/**
	 * Gets the plain module names set.
	 * 
	 * @return the plain module names set
	 */
	public Set<String> getPlainModuleNamesSet() {
		return plainModuleNamesSet;
	}

	/**
	 * Gets the modules refresh when run. In plain jar name.
	 * 
	 * @return the modules refresh when run
	 */
	public Set<String> getModulesRefreshWhenRun() {
		return modulesRefreshWhenRun;
	}

	/**
	 * Gets the modules need refetch when run.
	 * Each item is a path of source file.
	 * @return the modules need refetch
	 */
	public Set<String> getModulesNeedRefetchWhenRun() {
		return modulesNeedRefetch;
	}
}
