package org.talend.repository.ui.wizards.exportjob.extrachecker;

import org.eclipse.emf.common.util.EList;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.wizards.exportjob.JavaJobScriptsExportWSWizardPage.JobExportType;

public class EndpointPortJobChecker extends AbstractJobNodeChecker {

	@Override
	String checkNode(JobExportType exportType, NodeType node) {
		if("OSGI".equals(exportType)) {
			EList elementParameter = node.getElementParameter();
			boolean wrongPort = false;
			for (Object obj : elementParameter) {
				if (!(obj instanceof ElementParameterType)) {
					continue;
				}
				ElementParameterType param = (ElementParameterType) obj;
				String name = param.getName();		
				if ("REST_ENDPOINT".equals(name)) {				
					String pVal = param.getValue();
					if (pVal != null && !pVal.trim().isEmpty() && pVal.contains("://")) {
						if (!pVal.contains("8040")) ;
						wrongPort = true;
						break;
					}
				}
			}
			if(wrongPort) {
				return Messages.getString("JavaJobScriptsExportWSWizardPage.banServiceJob");
			}
	    }
		return null;
	}
}
