// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards.exportjob.scriptsmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * This is a jar file builder. <br/>
 * 
 * $Id: MakeJarRunnable.java Mar 30, 200712:49:05 PM bqian $
 * 
 */
public class JarBuilder {

    String dir = null;

    String jarFile = null;

    String jarname = null;

    List<String> includeDirs = null;

    List<String> excludeDirs = null;

    List<File> excludeFiles = null;

    List<File> includeSystemRoutines = null;

    private static final String SYSTEM = "system";

    /**
     * Constructure.
     * 
     * @param root
     * @param jarFile
     * @param jarName
     * @param includeDirs
     */
    JarBuilder(String root, String jarFile) {
        this.dir = root;
        this.jarFile = jarFile;
        File file = new File(jarFile);
        this.jarname = file.getName();
    }

    public void setIncludeDir(List<String> includeDirs) {
        this.includeDirs = includeDirs;
    }

    public void setExcludeDir(List<String> excludeDirs) {
        this.excludeDirs = excludeDirs;
    }

    public void setExcludeFiles(List<File> excludeFiles) {
        this.excludeFiles = excludeFiles;
    }

    public void setIncludeSystemRoutines(List<File> includeSystemRoutines) {
        this.includeSystemRoutines = includeSystemRoutines;
    }

    /**
     * Gets the files to zip in jar.
     * 
     * @return
     */
    private List<File> getExportedFiles() {

        if (includeDirs == null) {
            includeDirs = new ArrayList<String>();
            includeDirs.add(""); //$NON-NLS-1$
        }
        List<File> includeFiles = getAllFiles(includeDirs);

        if (excludeDirs != null) {
            List<File> excludeFiles = getAllFiles(excludeDirs);
            includeFiles.removeAll(excludeFiles);
        }
        if (excludeFiles != null) {
            includeFiles.removeAll(excludeFiles);
        }
        if (includeSystemRoutines != null) {
            includeFiles.addAll(includeSystemRoutines);
        }

        return includeFiles;
    }

    private List<File> getAllFiles(List<String> subDirs) {
        final List<File> list = new ArrayList<File>();

        for (int i = 0; i < subDirs.size(); i++) {

            File subFile = new File(dir, subDirs.get(i));
            subFile.listFiles(new java.io.FilenameFilter() {

                public boolean accept(java.io.File dir, String name) {
                    File file = new java.io.File(dir, name);
                    if (file.isFile()) {
                        list.add(file);
                        return true;
                    } else {
                        file.listFiles(this);
                    }
                    return false;
                }
            });
        }
        return list;
    }

    private Manifest getManifest() throws IOException {

        Manifest manifest = new Manifest();
        Map<String, Attributes> m = manifest.getEntries();
        Attributes a = new Attributes();
        a.put(Attributes.Name.IMPLEMENTATION_VERSION, "1.0"); //$NON-NLS-1$
        a.put(Attributes.Name.IMPLEMENTATION_VENDOR, "Talend Open Studio"); //$NON-NLS-1$
        m.put(jarname, a);
        return manifest;
    }

    /**
     * Builds the jar file.
     * 
     * @throws Exception
     */
    public void buildJar() throws Exception {
        File root = new File(dir);
        final List<File> list = getExportedFiles();
        Manifest manifest = getManifest();
        exportJar(root, list, manifest);
    }

    /**
     * exports the jar to specific location.
     * 
     * @param root
     * @param list
     * @param manifest
     */
    private void exportJar(File root, List<File> list, Manifest manifest) throws Exception {
        JarOutputStream jarOut = null;
        try {
            jarOut = new JarOutputStream(new FileOutputStream(jarFile), manifest);

            for (int i = 0; i < list.size(); i++) {
                String filename = list.get(i).getAbsolutePath();
                filename = filename.substring(root.getAbsolutePath().length() + 1);
                JarEntry entry = new JarEntry(filename.replace('\\', '/'));
                jarOut.putNextEntry(entry);

                FileInputStream fin = new FileInputStream(list.get(i));
                byte[] buf = new byte[4096];
                int read;
                while ((read = fin.read(buf)) != -1) {
                    jarOut.write(buf, 0, read);
                }
                fin.close();

                jarOut.closeEntry();
                jarOut.flush();
            }
        } finally {

            if (jarOut != null) {
                try {
                    jarOut.close();
                } catch (Exception e) {
                    // do nothing
                }
            }
        }
    }
}
