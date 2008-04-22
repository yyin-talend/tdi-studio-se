// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.codegen.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;
import org.talend.core.model.components.ComponentCompilations;
import org.talend.designer.codegen.CodeGeneratorActivator;
import org.talend.designer.codegen.config.JetSkeleton;

/**
 * DOC xtan
 * <p>
 * the same as XsdValidationCacheManager.java, used for checking the Skeleton files whether change or not.
 * </p>
 * <p>
 * if there is one Skeleton file have changed, there will generate all jet--->java again.
 * </p>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class JetSkeletonManager {

    private Map<String, Long> alreadyCheckedSkeleton;

    private static JetSkeletonManager instance;

    private final boolean forceSkeletonAlreadyChecked = ComponentCompilations.getMarkers();

    private JetSkeletonManager() {
    }

    private synchronized static JetSkeletonManager getInstance() {
        if (instance == null) {
            instance = new JetSkeletonManager();
        }
        return instance;
    }

    private void load() {
        try {
            deserializeAlreadyChecked();
        } catch (Exception e) {
            IStatus status = new Status(IStatus.WARNING, CodeGeneratorActivator.PLUGIN_ID,
                    "unable to load skeleton update cache file", e);
            CodeGeneratorActivator.getDefault().getLog().log(status);
        }
    }

    private void save() {
        try {
            serializeAlreadyChecked();
        } catch (Exception e) {
            IStatus status = new Status(IStatus.WARNING, CodeGeneratorActivator.PLUGIN_ID,
                    "unable to save skeleton update cache file", e);
            CodeGeneratorActivator.getDefault().getLog().log(status);
        }
    }

    private boolean needCheck(java.io.File file) {
        String path = file.getAbsolutePath();
        if (forceSkeletonAlreadyChecked) {
            alreadyCheckedSkeleton.put(path, System.currentTimeMillis());
            return false;
        } else {
            Long lastChecked = alreadyCheckedSkeleton.get(path);
            long lastModified = file.lastModified();
            if (lastChecked == null) {
                return true;
            }
            return lastModified > lastChecked;
        }
    }

    private void setChecked(File file) {
        String path = file.getAbsolutePath();
        alreadyCheckedSkeleton.put(path, new Date().getTime());
    }

    private File getSerializationFilePath() throws CoreException {
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(".JETEmitters");
        if (!project.exists()) {
            project.create(new NullProgressMonitor());
        }
        project.open(new NullProgressMonitor());
        IFile file = project.getFile("SkeletonUpdateCache");
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
            objectOut.writeObject(alreadyCheckedSkeleton);
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
        alreadyCheckedSkeleton = new HashMap<String, Long>();

        File file = getSerializationFilePath();
        if (!file.exists()) {
            return;
        }

        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            ObjectInputStream objectIn = new ObjectInputStream(bufferedInputStream);
            alreadyCheckedSkeleton = (Map<String, Long>) objectIn.readObject();
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

    /**
     * DOC xtan
     * <p>
     * check the skeleton file whether changed or not, and save the SkeletonUpdateCache file again.
     * </p>
     * 
     * @return true if there is one skeleton file changed,
     */
    public static boolean updateSkeletonPersistenceData() {

        boolean doUpdate = false;

        JetSkeletonManager instance = JetSkeletonManager.getInstance();
        instance.load();

        JetSkeleton[] values = JetSkeleton.values();

        for (JetSkeleton jetSkeleton : values) {
            try {

                String skeletonLocation = jetSkeleton.getSkeletonLocation();
                Bundle b = Platform.getBundle(jetSkeleton.getPluginId());
                URL skeletonUrl = FileLocator.toFileURL(FileLocator.find(b, new Path(skeletonLocation), null));
                File file = new File(skeletonUrl.toURI());
                if (instance.needCheck(file)) {
                    instance.setChecked(file);
                    doUpdate = true;
                }
            } catch (Exception e) {
                IStatus status = new Status(IStatus.WARNING, CodeGeneratorActivator.PLUGIN_ID,
                        "when update skeleton persistence data, there have some problems", e);
                CodeGeneratorActivator.getDefault().getLog().log(status);
                instance.save();
                return true;
            }
        }

        instance.save();

        return doUpdate;
    }

}
