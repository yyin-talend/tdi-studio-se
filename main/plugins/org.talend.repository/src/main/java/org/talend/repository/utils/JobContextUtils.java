package org.talend.repository.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

public class JobContextUtils {

	public static Map<String, Map<String, String>> getContextsMap(ProcessItem processItem) {
		Map<String, Map<String, String>> contextValues = new HashMap<String, Map<String, String>>();
		ProcessType process = processItem.getProcess();
		if (process != null) {
			EList<?> context = process.getContext();
			if (context != null) {
				for (Object next : context) {
					ContextType ct = (ContextType) next;
					HashMap<String, String> contextParams = new HashMap<String, String>();
					contextValues.put(ct.getName(), contextParams);
					EList<ContextParameterType> params = ct.getContextParameter();
					for (ContextParameterType param : params) {
						contextParams.put(param.getName(), param.getValue());
					}
				}
			}
		}
		return contextValues;
	}

    public static Map<String, String> getDefaultContextMap(ProcessItem processItem) {
        ProcessType process = processItem.getProcess();
        if (process != null) {
            String defaultContext = process.getDefaultContext();
            EList<?> context = process.getContext();
            if (context != null) {
                for (Object next : context) {
                    ContextType ct = (ContextType) next;
                    if (ct.getName().equals(defaultContext)) {
                        Map<String, String> contextParams = new HashMap<String, String>();
                        EList<ContextParameterType> params = ct.getContextParameter();
                        for (ContextParameterType param : params) {
                            contextParams.put(param.getName(), param.getValue());
                        }
                        return contextParams;
                    }
                }
            }
        }
        return Collections.emptyMap();
    }
}
