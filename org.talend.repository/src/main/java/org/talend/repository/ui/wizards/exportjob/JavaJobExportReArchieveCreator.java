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
package org.talend.repository.ui.wizards.exportjob;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.repository.ui.utils.ZipToFile;

/**
 * This class use to reAchieve the export job script file after the jobscript zip file generated, put all the jar files
 * in 'classpath.jar' to fix bug 0006108
 */
public class JavaJobExportReArchieveCreator {

    private String zipFile; // created zip filename

    private String jobFolerName;

    private File jobFolder;

    private File libFolder;

    private File batFile;

    private File shFile;

    private static final String CLASSPATH_JAR = "classpath.jar"; // the output new jar filename

    private static final String LIB = "lib"; // lib folder

    public JavaJobExportReArchieveCreator(String zipFile, String jobFolderName) {
        this.zipFile = zipFile;
        this.jobFolerName = jobFolderName;
    }

    public void buildNewJar() {
        try {
            String tmpFoler = getTmpFolder();
            // ZipToFile.unZipFile(zipFile, tmpFoler);
            // init jobFolder File
            initJobFolder();
            if (jobFolder == null) {
                return;
            }
            String newJarPath = jobFolder.getAbsolutePath() + "/" + CLASSPATH_JAR;
            NewJarBuilder jarBuilder = new NewJarBuilder(tmpFoler, newJarPath);
            jarBuilder.buildJar();

            // change the .bat file & .sh file
            if (batFile != null) {
                changeScriptFile(batFile);
            }
            if (shFile != null) {
                changeScriptFile(shFile);
            }

            // delete non used jar files
            // deleteNonUsedJar();

        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    /**
     * 
     * DOC aiming Comment method "getJobFolerName".
     * 
     * @return
     */
    public String getJobFolerName() {
        return this.jobFolerName;
    }

    /**
     * 
     * DOC aiming Comment method "setJobFolerName".
     * 
     * @param jobFolerName
     */
    public void setJobFolerName(String jobFolerName) {
        this.jobFolerName = jobFolerName;
    }

    /**
     * 
     * change .sh & .bat file's contents
     * 
     * @throws FileNotFoundException
     */
    private void changeScriptFile(File file) {
        // change batFile
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            // get java command line
            String line1 = br.readLine().trim();
            String[] strs = line1.split("\\s");
            int pos = -1;
            for (int i = 0; i < strs.length; i++) {
                if ("-cp".equalsIgnoreCase(strs[i]) || "-classpath".equalsIgnoreCase(strs[i])) {
                    pos = i;
                }
            }
            if (pos != -1) {
                if (file.getName().endsWith(".sh")) {
                    strs[pos + 1] = CLASSPATH_JAR + ":";
                }
                if (file.getName().endsWith(".bat")) {
                    strs[pos + 1] = CLASSPATH_JAR + ";";
                }
            }
            StringBuffer changedContent = new StringBuffer();
            for (String s : strs) {
                changedContent.append(s).append(" ");
            }
            // rewrite the changed content to file
            bw = new BufferedWriter(new FileWriter(file));
            bw.write(line + "\n");
            bw.write(changedContent.toString());
            bw.flush();
            // br.close();
            // bw.close();
        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    ExceptionHandler.process(e);
                }
            }
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    ExceptionHandler.process(e);
                }
            }
        }
    }

    private String[] getLibJarFilenames() {
        File[] files = libFolder.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jar") //$NON-NLS-1$ //$NON-NLS-2$
                        || name.toLowerCase().endsWith(".zip") ? true : false; //$NON-NLS-1$
            }
        });
        String[] filenames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            filenames[i] = files[i].getName();
        }
        return filenames;
    }

    private String[] getJobFolderJarFilenames() {
        File[] files = jobFolder.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jar") //$NON-NLS-1$ //$NON-NLS-2$
                        || name.toLowerCase().endsWith(".zip") ? true : false; //$NON-NLS-1$
            }
        });
        String[] filenames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            filenames[i] = files[i].getName();
        }
        return filenames;
    }

    /**
     * 
     * get jobFolder, libFolder, batFile, shFile
     */
    private void initJobFolder() {
        String tmpFoler = getTmpFolder();
        File tmpFile = new File(tmpFoler);
        File[] files = tmpFile.listFiles();
        File[] fs = files[0].listFiles();
        for (int i = 0; fs != null && i < fs.length; i++) {
            if (fs[i].getName().equals(jobFolerName)) {
                jobFolder = fs[i];
                File[] fs1 = jobFolder.listFiles();
                for (File f : fs1) {
                    if (f.getName().toLowerCase().endsWith(".bat")) {
                        batFile = f;
                    }
                    if (f.getName().toLowerCase().endsWith(".sh")) {
                        shFile = f;
                    }
                }
            }
            if (fs[i].getName().equals(LIB)) {
                libFolder = fs[i];
            }
        }
    }

    private String getClasspath() {
        StringBuffer sb = new StringBuffer();
        String[] fs = getJobFolderJarFilenames();
        for (int i = 0; i < fs.length; i++) {
            sb.append(fs[i] + " ");
        }
        String[] fn = getLibJarFilenames();
        for (int i = 0; i < fn.length; i++) {
            sb.append("../" + LIB + "/" + fn[i] + " ");
        }
        // sb.append("\n");
        return sb.toString();
    }

    public static String getTmpFolder() {
        String tmp = System.getProperty("user.dir") + "/newjarFolder";
        tmp = tmp.replace('\\', '/');
        File f = new File(tmp);
        if (!f.exists()) {
            f.mkdir();
        }
        return tmp;
    }

    /**
     * Deletes the temporary files.
     */
    public static void deleteTempFiles() {
        String tmpFold = getTmpFolder();
        File file = new File(tmpFold);
        if (!file.exists() && !file.isDirectory()) {
            return;
        }
        ZipToFile.deleteDirectory(file);
    }

    /**
     * 
     * only build the 'manifest.mf' to the classpath.jar
     */
    private class NewJarBuilder {

        String dir = null;

        String jarFile = null;

        String jarname = null;

        /**
         * Constructure.
         * 
         * @param root
         * @param jarFile
         * @param jarName
         * @param includeDirs
         */
        NewJarBuilder(String root, String jarFile) {
            this.dir = root;
            this.jarFile = jarFile;
            File file = new File(jarFile);
            this.jarname = file.getName();
        }

        private Manifest getManifest() throws IOException {
            Manifest manifest = new Manifest();
            Attributes a = manifest.getMainAttributes();
            a.put(Attributes.Name.MANIFEST_VERSION, "1.0");
            a.put(Attributes.Name.IMPLEMENTATION_VENDOR, "Talend Open Studio"); //$NON-NLS-1$
            a.put(Attributes.Name.CLASS_PATH, getClasspath());
            return manifest;
        }

        /**
         * Builds the jar file.
         * 
         * @throws Exception
         */
        public void buildJar() throws Exception {
            Manifest manifest = getManifest();
            // File root = new File(dir);
            // final List<File> list = getExportedFiles();
            // exportJar(root, list, manifest);
            exportJar(manifest);
        }

        private void exportJar(Manifest manifest) throws FileNotFoundException, IOException {
            JarOutputStream jarOut = null;
            try {
                jarOut = new JarOutputStream(new FileOutputStream(jarFile), manifest);
                jarOut.flush();
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
}
