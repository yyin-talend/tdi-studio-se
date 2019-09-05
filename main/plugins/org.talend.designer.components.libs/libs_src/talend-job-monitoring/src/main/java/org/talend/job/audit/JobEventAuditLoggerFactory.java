package org.talend.job.audit;

import java.lang.reflect.Proxy;
import java.util.Properties;

import org.talend.logging.audit.AuditLoggerFactory;
import org.talend.logging.audit.EventAuditLogger;
import org.talend.logging.audit.impl.AbstractBackend;
import org.talend.logging.audit.impl.AuditConfiguration;
import org.talend.logging.audit.impl.AuditConfigurationMap;
import org.talend.logging.audit.impl.AuditLoggerBase;
import org.talend.logging.audit.impl.DefaultAuditLoggerBase;
import org.talend.logging.audit.impl.ProxyEventAuditLogger;

public class JobEventAuditLoggerFactory {

	public static JobAuditLogger createJobAuditLogger(final Properties properties) {
		final AuditConfigurationMap config = AuditConfiguration.loadFromProperties(properties);

		AbstractBackend logger = null;
		String loggerClass = "org.talend.logging.audit.logback.LogbackBackend";
		try {
            final Class<?> clz = Class.forName(loggerClass);
            logger = (AbstractBackend) clz.getConstructor(AuditConfigurationMap.class).newInstance(config);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to load backend " + loggerClass, e);
        }

		final DefaultAuditLoggerBase loggerBase = new DefaultAuditLoggerBase(logger, config);

		JobAuditLogger result = getEventAuditLogger(JobAuditLogger.class, loggerBase);
		return result;
	}

	// we can't access AuditLoggerFactory.getEventAuditLogger method now, should
	// adjust the daikon jar, now only do it here soon for POC
	private static <T extends EventAuditLogger> T getEventAuditLogger(Class<T> clz, AuditLoggerBase auditLoggerBase) {
		return (T) Proxy.newProxyInstance(AuditLoggerFactory.class.getClassLoader(), new Class<?>[] { clz },
				new ProxyEventAuditLogger(auditLoggerBase));
	}
}