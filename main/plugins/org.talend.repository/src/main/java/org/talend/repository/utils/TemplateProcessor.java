// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;

public class TemplateProcessor {

    public static boolean processTemplate(String logTag, Map<String, Object> contextParams,
            Writer outputWriter, Reader templateReader) throws IOException {

        VelocityEngine engine = new VelocityEngine();
//        engine.setProperty("resource.loader", "classpath"); //$NON-NLS-1$ //$NON-NLS-2$
//        engine.setProperty("classpath.resource.loader.class", //$NON-NLS-1$
//                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader"); //$NON-NLS-1$
        engine.setProperty("eventhandler.referenceinsertion.class", //$NON-NLS-1$
                "org.apache.velocity.app.event.implement.EscapeXmlReference"); //$NON-NLS-1$
        engine.init();

        VelocityContext context = new VelocityContext(contextParams);

      try {
            boolean result = engine.evaluate(context, outputWriter, logTag, templateReader);

            outputWriter.flush();

            return result;
      } catch (VelocityException ve) {
          // couldn't find the template
          // org.apache.velocity.exception.ResourceNotFoundException;
          // syntax error: problem parsing the template
          // org.apache.velocity.exception.ParseErrorException;
          // something invoked in the template threw an exception
          // org.apache.velocity.exception.MethodInvocationException;
          throw new IOException(ve);
      }
    }

    public static boolean processTemplate(String logTag, Map<String, Object> contextParams,
            File outputFile, Reader templateReader) throws IOException {

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(outputFile);
            return processTemplate(logTag, contextParams, fileWriter, templateReader);
        } finally {
            if (null != fileWriter) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    //LOG.warn(e.getLocalizedMessage(), e);
                }
            }
        }
    }
}
