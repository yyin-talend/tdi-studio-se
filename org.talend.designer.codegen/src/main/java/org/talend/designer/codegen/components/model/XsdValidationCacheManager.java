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
package org.talend.designer.codegen.components.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.talend.designer.codegen.CodeGeneratorActivator;
import org.talend.designer.codegen.i18n.Messages;

/**
 * tfeng class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class XsdValidationCacheManager {

    private Map<String, String> alreadyCheckedXsd = new HashMap<String, String>();

    private static XsdValidationCacheManager instance;

    private XsdValidationCacheManager() {
    }

    public synchronized static XsdValidationCacheManager getInstance() {
        if (instance == null) {
            instance = new XsdValidationCacheManager();
            instance.load();
        }
        return instance;
    }

    private void load() {
        try {
            IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(".JETEmitters"); //$NON-NLS-1$
            if (project.exists()) {
                deserializeAlreadyChecked();
            }
        } catch (Exception e) {
            IStatus status = new Status(IStatus.WARNING, CodeGeneratorActivator.PLUGIN_ID,
                    Messages.getString("XsdValidationCacheManager.unableLoadxsd"), e); //$NON-NLS-1$
            CodeGeneratorActivator.getDefault().getLog().log(status);
        }
    }

    public void save() {
        try {
            serializeAlreadyChecked();
        } catch (Exception e) {
            IStatus status = new Status(IStatus.WARNING, CodeGeneratorActivator.PLUGIN_ID,
                    Messages.getString("XsdValidationCacheManager.unableSavexsd"), e); //$NON-NLS-1$
            CodeGeneratorActivator.getDefault().getLog().log(status);
        }
    }

    public boolean needCheck(java.io.File file, String sha1) {
        String name = file.getName();
        String oldName = alreadyCheckedXsd.get(sha1);
        if (oldName == null) {
            return true;
        }

        boolean isChanged = !StringUtils.equals(name, oldName);
        return isChanged;
    }

    public void setChecked(File file, String sha1) {
        String name = file.getName();
        alreadyCheckedXsd.put(sha1, name);
    }

    private File getSerializationFilePath() throws CoreException {
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(".JETEmitters"); //$NON-NLS-1$
        if (!project.exists()) {
            project.create(new NullProgressMonitor());
        }
        if (!project.isOpen()) {
            project.open(new NullProgressMonitor());
        }
        IFile file = project.getFile("XsdValidationCache"); //$NON-NLS-1$
        if (!file.exists()) {
            file.create(null, true, new NullProgressMonitor());
        }
        return file.getLocation().toFile();
    }

    private void serializeAlreadyChecked() throws IOException, CoreException {
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(getSerializationFilePath()));
            ObjectOutputStream objectOut = new ObjectOutputStream(bufferedOutputStream);
            objectOut.writeObject(alreadyCheckedXsd);
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                bufferedOutputStream.close();
            } catch (Exception e) {
                // ignore me even if i'm null
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void deserializeAlreadyChecked() throws Exception {
        alreadyCheckedXsd = new HashMap<String, String>();

        File file = getSerializationFilePath();
        if (!file.exists()) {
            return;
        }

        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            ObjectInputStream objectIn = new ObjectInputStream(bufferedInputStream);
            Object object = objectIn.readObject();
            if (object instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) (object);
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    String key = entry.getKey().toString();
                    Object value = entry.getValue();
                    if (value instanceof String) {
                        alreadyCheckedXsd.put(key, (String) value);
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                bufferedInputStream.close();
            } catch (Exception e) {
                // ignore me even if i'm null
            }
        }
    }

}
