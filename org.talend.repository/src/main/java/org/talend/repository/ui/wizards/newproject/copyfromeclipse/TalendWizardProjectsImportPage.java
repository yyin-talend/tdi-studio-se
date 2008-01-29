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
package org.talend.repository.ui.wizards.newproject.copyfromeclipse;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.internal.wizards.datatransfer.TarEntry;
import org.eclipse.ui.internal.wizards.datatransfer.TarFile;
import org.eclipse.ui.internal.wizards.datatransfer.WizardProjectsImportPage;
import org.talend.core.CorePlugin;

/**
 * DOC zhangchao.wang class global comment. Detailled comment
 */
public class TalendWizardProjectsImportPage extends WizardProjectsImportPage {

    private static final String ZIP = "zip";

    private static final String TAR = "tar";

    public TalendWizardProjectsImportPage() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.wizards.datatransfer.WizardProjectsImportPage#updateProjectsList(java.lang.String)
     */
    @Override
    public void updateProjectsList(String sourcePath) {
        String destinationJavaPath = null;
        String destinationPerlPath = null;
        List<String> javaTextList = null;
        List<String> perlTextList = null;
        List<String> javaAnotherList = null;
        List<String> perlAnotherList = null;

        try {
            if (!("".equals(sourcePath))) {
                javaTextList = new ArrayList<String>();
                perlTextList = new ArrayList<String>();
                javaAnotherList = new ArrayList<String>();
                perlAnotherList = new ArrayList<String>();

                destinationJavaPath = CorePlugin.getDefault().getLibrariesService().getJavaLibrariesPath();
                destinationPerlPath = CorePlugin.getDefault().getLibrariesService().getPerlLibrariesPath();

                Pattern pattern0 = Pattern.compile("(.*)\\.zip$");
                Matcher matcher0 = pattern0.matcher(sourcePath);
                Pattern pattern00 = Pattern.compile("(.*)\\.tar$");
                Matcher matcher00 = pattern00.matcher(sourcePath);

                if (matcher0.find()) {
                    ZipFile zipFile = new ZipFile(sourcePath);
                    Enumeration<? extends ZipEntry> enumerationzip = zipFile.entries();
                    Pattern pattern = Pattern.compile("^(.*)java(.*)$");
                    Pattern pattern1 = Pattern.compile("^(.*)perl(.*)$");
                    while (enumerationzip.hasMoreElements()) {
                        ZipEntry zipEntry = enumerationzip.nextElement();
                        Matcher matcher = pattern.matcher(zipEntry.getName());
                        Matcher matcher1 = pattern1.matcher(zipEntry.getName());
                        if (matcher.find()) {
                            String name = zipEntry.getName();
                            File file = new File(destinationJavaPath
                                    + name.substring(name.lastIndexOf("java") + 4, name.length()));
                            writeLibrary(TalendWizardProjectsImportPage.ZIP, zipEntry, zipFile, file, null, null);
                            javaAnotherList.add(((matcher.group(2)).replace(java.io.File.separatorChar, '/')).substring(1,
                                    ((matcher.group(2)).replace(java.io.File.separatorChar, '/')).length()));
                        } else if (matcher1.find()) {
                            String name = zipEntry.getName();
                            File file = new File(destinationPerlPath
                                    + name.substring(name.lastIndexOf("perl") + 4, name.length()));
                            writeLibrary(TalendWizardProjectsImportPage.ZIP, zipEntry, zipFile, file, null, null);
                            perlAnotherList.add(((matcher1.group(2)).replace(java.io.File.separatorChar, '/')).substring(1,
                                    ((matcher1.group(2)).replace(java.io.File.separatorChar, '/')).length()));
                        }
                    }
                    zipFile.close();
                } else if (matcher00.find()) {
                    TarFile tarFile = new TarFile(sourcePath);
                    Enumeration<? extends TarEntry> enumerationtar = tarFile.entries();
                    Pattern pattern = Pattern.compile("^(.*)java(.*)$");
                    Pattern pattern1 = Pattern.compile("^(.*)perl(.*)$");
                    while (enumerationtar.hasMoreElements()) {
                        TarEntry tarEntry = enumerationtar.nextElement();
                        Matcher matcher = pattern.matcher(tarEntry.getName());
                        Matcher matcher1 = pattern1.matcher(tarEntry.getName());
                        if (matcher.find()) {
                            String name = tarEntry.getName();
                            File file = new File(destinationJavaPath
                                    + name.substring(name.lastIndexOf("java") + 4, name.length()));
                            writeLibrary(TalendWizardProjectsImportPage.TAR, null, null, file, tarEntry, tarFile);
                            javaAnotherList.add(((matcher.group(2)).replace(java.io.File.separatorChar, '/')).substring(1,
                                    ((matcher.group(2)).replace(java.io.File.separatorChar, '/')).length()));
                        } else if (matcher1.find()) {
                            String name = tarEntry.getName();
                            File file = new File(destinationPerlPath
                                    + name.substring(name.lastIndexOf("perl") + 4, name.length()));
                            writeLibrary(TalendWizardProjectsImportPage.TAR, null, null, file, tarEntry, tarFile);
                            perlAnotherList.add(((matcher1.group(2)).replace(java.io.File.separatorChar, '/')).substring(1,
                                    ((matcher1.group(2)).replace(java.io.File.separatorChar, '/')).length()));
                        }
                    }
                    tarFile.close();
                }

                javaTextList = this.readData("java", javaTextList);
                perlTextList = this.readData("perl", perlTextList);
                javaAnotherList = this.compareName(javaTextList, javaAnotherList);
                perlAnotherList = this.compareName(perlTextList, perlAnotherList);
                this.preferenceStore("java", javaAnotherList);
                this.preferenceStore("perl", perlAnotherList);

                super.updateProjectsList(sourcePath);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private List<String> compareName(List<String> textList, List<String> anotherList) {
        List<String> anotherNameList = new ArrayList<String>();

        anotherNameList.addAll(anotherList);
        for (String another : anotherList) {
            for (String text : textList) {
                if (another.equals(text)) {
                    anotherNameList.remove(another);
                }
            }
        }
        return anotherNameList;
    }

    private void writeLibrary(String zipOrTar, ZipEntry zipEntry, ZipFile zipFile, File file, TarEntry tarEntry, TarFile tarFile) {
        InputStream is = null;
        OutputStream os = null;
        int len = 0;
        byte[] b = new byte[1024];

        try {
            if (zipOrTar != null && file != null) {
                if (TalendWizardProjectsImportPage.ZIP.equals(zipOrTar)) {
                    if (zipEntry != null && zipFile != null) {
                        is = zipFile.getInputStream(zipEntry);
                    }
                } else if (TalendWizardProjectsImportPage.TAR.equals(zipOrTar)) {
                    if (tarEntry != null && tarFile != null) {
                        is = tarFile.getInputStream(tarEntry);
                    }
                }

                if (!file.exists()) {
                    os = new BufferedOutputStream(new FileOutputStream(file));
                    while ((len = is.read(b)) != -1) {
                        os.write(b);
                    }
                    is.close();
                    os.close();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private List<String> readData(String name, List<String> textList) {
        IPreferenceStore preferenceStore = CorePlugin.getDefault().getPreferenceStore();
        String[] oldName = null;

        if (preferenceStore.contains(name)) {
            String string = preferenceStore.getString(name);
            if (null == string || "".equals(string)) {
                return textList;
            } else {
                oldName = string.split(":");
                for (String s : oldName) {
                    textList.add(s);
                }
                return textList;
            }
        } else {
            return textList;
        }
    }

    private void preferenceStore(String name, List<String> anotherList) {
        IPreferenceStore preferenceStore = CorePlugin.getDefault().getPreferenceStore();
        String preferenceName = null;
        StringBuffer sb = null;
        if (preferenceStore.contains(name)) {
            String string = preferenceStore.getString(name);
            if (null == string || "".equals(string)) {
                sb = new StringBuffer();
                preferenceName = this.addName(anotherList, sb);
                preferenceStore.setValue(name, preferenceName);
            } else {
                sb = new StringBuffer();
                sb.append(string);
                preferenceName = this.addName(anotherList, sb);
                preferenceStore.setValue(name, preferenceName);
            }
        } else {
            sb = new StringBuffer();
            preferenceName = this.addName(anotherList, sb);
            preferenceStore.setValue(name, preferenceName);
        }
    }

    private String addName(List<String> anotherList, StringBuffer sb) {
        for (String str : anotherList) {
            sb.append(str + ":");
        }
        return sb.toString();
    }
}
