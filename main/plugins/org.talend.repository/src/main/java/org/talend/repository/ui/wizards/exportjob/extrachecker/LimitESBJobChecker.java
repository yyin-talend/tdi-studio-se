package org.talend.repository.ui.wizards.exportjob.extrachecker;

import org.apache.commons.lang.ArrayUtils;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.wizards.exportjob.JavaJobScriptsExportWSWizardPage.JobExportType;

/**
 * Limits job contains specific ESB components only build using [OSGI for ESB] build type.  
 * 
 * TODO better move to other bundle relative to this check.(some appropriate esb bundle)
 * 
 * @author GaoZone
 * @JIRA TESB-13867
 */
public class LimitESBJobChecker extends AbstractJobNodeChecker {

	/** The Constant ESB_JOB_NODES. */
	private static final String[] ESB_JOB_NODES = {"tESBConsumer", "tRESTRequest", "tRESTClient"};

	@Override
	String checkNode(JobExportType exportType, NodeType nodeType) {
		if (exportType == JobExportType.OSGI) {
			return null;
		}
		if (isESBJobNode(nodeType)) {
			return Messages.getString("JavaJobScriptsExportWSWizardPage.limitESBJob");
		}
		return null;
	}

	/**
	 * Checks if is esb job node.
	 *
	 * @param nodeType the node type
	 * @return true, if checks if is esb job node
	 */
	private boolean isESBJobNode(NodeType nodeType) {
		return ArrayUtils.contains(ESB_JOB_NODES, nodeType.getComponentName());
	}

}
