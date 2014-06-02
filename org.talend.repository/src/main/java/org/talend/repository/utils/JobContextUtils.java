package org.talend.repository.utils;

import java.util.HashMap;
import java.util.Iterator;
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
			EList context = process.getContext();
			if (context != null) {
				Iterator iterator = context.iterator();
				while (iterator.hasNext()) {
					Object next = iterator.next();
					if (!(next instanceof ContextType)) {
						continue;
					}
					ContextType ct = (ContextType) next;
					String name = ct.getName();
					HashMap<String, String> contextParams = new HashMap<String, String>();
					contextValues.put(name, contextParams);
					EList<ContextParameterType> params = ct.getContextParameter();
					for (ContextParameterType param : params) {
						contextParams.put(param.getName(), param.getValue());
					}
				}
			}
		}
		return contextValues;
	}
}
