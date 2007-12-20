package org.talend.designer.components.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.talend.designer.components.ComponentsLocalProviderPlugin;

public class XsdValidationCacheManager {

    private Map<String, Long> alreadyCheckedXsd;

    private static XsdValidationCacheManager instance;

    private XsdValidationCacheManager() {
    }

    public synchronized static XsdValidationCacheManager getInstance() {
        if (instance == null) {
            instance = new XsdValidationCacheManager();
        }
        return instance;
    }

    public void load() {
        try {
            deserializeAlreadyChecked();
        } catch (Exception e) {
            IStatus status = new Status(IStatus.WARNING, ComponentsLocalProviderPlugin.PLUGIN_ID,
                    "unable to load xsd validation cache file", e);
            ComponentsLocalProviderPlugin.getDefault().getLog().log(status);
        }
    }

    public void save() {
        try {
            serializeAlreadyChecked();
        } catch (Exception e) {
            IStatus status = new Status(IStatus.WARNING, ComponentsLocalProviderPlugin.PLUGIN_ID,
                    "unable to save xsd validation cache file", e);
            ComponentsLocalProviderPlugin.getDefault().getLog().log(status);
        }
    }

    public boolean needCheck(java.io.File file) {
        long lastModified = file.lastModified();
        String path = file.getAbsolutePath();
        Long lastChecked = alreadyCheckedXsd.get(path);
        if (lastChecked == null) {
            return true;
        }
        return lastModified > lastChecked;
    }

    public void setChecked(File file) {
        String path = file.getAbsolutePath();
        alreadyCheckedXsd.put(path, new Date().getTime());
    }

    private File getSerializationFilePath() throws CoreException {
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(".JETEmitters");
        if (!project.exists()) {
            project.create(new NullProgressMonitor());
        }
        project.open(new NullProgressMonitor());
        IFile file = project.getFile("XsdValidationCache");
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
        alreadyCheckedXsd = new HashMap<String, Long>();

        File file = getSerializationFilePath();
        if (!file.exists()) {
            return;
        }

        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            ObjectInputStream objectIn = new ObjectInputStream(bufferedInputStream);
            alreadyCheckedXsd = (Map<String, Long>) objectIn.readObject();
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
