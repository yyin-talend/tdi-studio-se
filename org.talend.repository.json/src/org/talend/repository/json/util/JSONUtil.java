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
package org.talend.repository.json.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IProject;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.model.general.Project;
import org.talend.repository.ProjectManager;

/**
 * DOC wanghong class global comment. Detailled comment
 */
public class JSONUtil {

    public static final String TMP_JSON_FILE = "tempJSONFile" + '.' + "xml"; //$NON-NLS-1$

    public static final String JSON_FILE = '.' + "xml"; //$NON-NLS-1$

    public static boolean validateLabelForJSON(String label) {
        if (label == null) {
            return false;
        }
        if (label.length() < 1) {
            return false;
        }
        char firstChar = label.charAt(0);
        // see bug 10359,support begin with "_".
        if (!Character.isLetter(firstChar) && !('_' == firstChar)) {
            return false;
        }
        //        if (label.toLowerCase().startsWith("xml")) { //$NON-NLS-1$
        // return false;
        // }
        char[] array = label.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (Character.isSpaceChar(array[i]) || Character.isWhitespace(array[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean validateLabelForFixedValue(String label) {
        if (label == null) {
            return false;
        }
        if (label.length() < 1) {
            return false;
        }
        if (label.toLowerCase().startsWith(JSON_FILE)) { //$NON-NLS-1$
            return false;
        }
        // char[] array = label.toCharArray();
        // for (int i = 0; i < array.length; i++) {
        // if (Character.isSpaceChar(array[i]) || Character.isWhitespace(array[i])) {
        // return false;
        // }
        // }
        return true;
    }

    public static boolean validateLabelForNameSpace(String label) {
        if (label == null) {
            return false;
        }
        if (label.toLowerCase().startsWith(JSON_FILE)) { //$NON-NLS-1$
            return false;
        }
        if (label.contains(".")) { //$NON-NLS-1$
            return false;
        }
        if (!("".equals(label)) && !("".equals(label.trim()))) { //$NON-NLS-1$ //$NON-NLS-2$
            char firstChar = label.charAt(0);
            if (!Character.isLetter(firstChar)) {
                return false;
            }
            char[] array = label.toCharArray();
            for (int i = 0; i < array.length; i++) {
                if (Character.isSpaceChar(array[i]) || Character.isWhitespace(array[i])) {
                    return false;
                }
            }

        }
        return true;
    }

    public static String changeJsonToXml(String jsonPath) {
        Project project = ProjectManager.getInstance().getCurrentProject();
        IProject fsProject = null;
        try {
            fsProject = org.talend.core.repository.model.ResourceModelUtils.getProject(project);
        } catch (PersistenceException e2) {
            ExceptionHandler.process(e2);
        }
        if (fsProject == null) {
            return jsonPath;
        }
        String temPath = fsProject.getLocationURI().getPath() + File.separator + "temp" + File.separator + "jsonwizard"
                + File.separator;

        ConvertJSONString convertJSON = new ConvertJSONString();
        de.odysseus.staxon.json.JsonXMLConfig jsonConfig = new de.odysseus.staxon.json.JsonXMLConfigBuilder().multiplePI(false)
                .build();
        de.odysseus.staxon.json.JsonXMLInputFactory jsonXMLInputFactory = new de.odysseus.staxon.json.JsonXMLInputFactory(
                jsonConfig);
        javax.xml.stream.XMLOutputFactory xmlOutputFactory = javax.xml.stream.XMLOutputFactory.newInstance();

        java.io.ByteArrayOutputStream outStream = new java.io.ByteArrayOutputStream();
        java.io.ByteArrayInputStream inStream = null;
        File file = new File(jsonPath);
        String filename = file.getName().replaceAll("\\.", "_");
        try {
            FileInputStream input = new FileInputStream(file);
            String jsonStr = IOUtils.toString(input);

            convertJSON.setJsonString(jsonStr);

            convertJSON.generate();
            jsonStr = convertJSON.getJsonString4XML();
            inStream = new ByteArrayInputStream(jsonStr.getBytes());
            javax.xml.stream.XMLEventReader xmlEventReader = jsonXMLInputFactory.createXMLEventReader(inStream);
            javax.xml.stream.XMLEventWriter xmLEventWriter = xmlOutputFactory.createXMLEventWriter(outStream);
            xmLEventWriter.add(xmlEventReader);
            String xmlStr = outStream.toString();

            File xmlFolder = new File(temPath);
            if (!xmlFolder.exists()) {
                xmlFolder.mkdirs();
            }
            temPath = temPath + filename + ".xml";
            FileWriter writer = new FileWriter(temPath);
            writer.write(xmlStr);
            writer.flush();
            writer.close();

            xmLEventWriter.close();
            xmlEventReader.close();

        } catch (FileNotFoundException e1) {
            ExceptionHandler.process(e1);
        } catch (IOException e1) {
            ExceptionHandler.process(e1);
        } catch (java.lang.Exception e) {
            ExceptionHandler.process(e);
        } finally {
            try {
                outStream.close();
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException e) {
                ExceptionHandler.process(e);
            }

        }
        return temPath;
    }

    public static void deleteWizardTempFiles() {
        Project project = ProjectManager.getInstance().getCurrentProject();
        IProject fsProject = null;
        try {
            fsProject = org.talend.core.repository.model.ResourceModelUtils.getProject(project);
        } catch (PersistenceException e2) {
            ExceptionHandler.process(e2);
        }
        if (fsProject == null) {
            return;
        }
        String tempPath = fsProject.getLocationURI().getPath() + File.separator + "temp" + File.separator + "wizard";
        File tempWizardDir = new File(tempPath);
        tempWizardDir.delete();
        String tempjsonPath = fsProject.getLocationURI().getPath() + File.separator + "temp" + File.separator + "jsonwizard";
        File tempjsonWizardDir = new File(tempjsonPath);
        if (tempjsonWizardDir.exists()) {
            tempjsonWizardDir.delete();
        }
    }
}
