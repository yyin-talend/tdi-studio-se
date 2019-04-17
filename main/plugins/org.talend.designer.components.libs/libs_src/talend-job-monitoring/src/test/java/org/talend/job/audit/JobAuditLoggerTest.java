package org.talend.job.audit;

import org.talend.logging.audit.AuditLoggerFactory;
import org.talend.logging.audit.Context;

public class JobAuditLoggerTest {

	public static void main(String[] args) {
		final JobAuditLogger logger = AuditLoggerFactory.getEventAuditLogger(JobAuditLogger.class);
		Context context = JobContextBuilder.create().jobName("fetch_from_s3_every_day").jobId("jobid_123")
				.jobVersion("0.1").connectorType("tXMLMAP").connectorId("tXMLMap_1").parameters("{header : 1, file : a}").rows(10).build();

		logger.runjob(context);
		logger.runcomponent(context);
		logger.flow(context);
	}
}
