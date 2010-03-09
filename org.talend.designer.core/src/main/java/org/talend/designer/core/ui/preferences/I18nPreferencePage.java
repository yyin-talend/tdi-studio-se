// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.epic.core.preferences.LabelFieldEditor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.prefs.ui.CorePreferencePage;
import org.talend.core.prefs.ui.OneLineComboFieldEditor;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.views.RefreshView;
import org.talend.designer.core.utils.ZipFileUtils;

/**
 * DOC wzhang class global comment. Detailled comment
 */
public class I18nPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private OneLineComboFieldEditor languageSelectionEditor;

    private String fs = System.getProperties().getProperty("file.separator");

    private List<FieldEditor> fields = new ArrayList<FieldEditor>();

    private static Logger log = Logger.getLogger(CorePreferencePage.class);

    private static final Pattern VERSION_PATTERN = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)(\\.(RC|M)\\d+)?_r\\d+"); //$NON-NLS-1$

    private static final Pattern DEFAULT_PATTERN = Pattern.compile("(\\d+)\\.(\\d+)\\.*(\\d*)"); //$NON-NLS-1$

    private boolean updateCompleted;

    private boolean isBabiliButtonClicked = false;

    /**
     * Construct a new I18nPreferencePage.
     */
    public I18nPreferencePage() {
        super(GRID);

        // Set the preference store for the preference page.
        IPreferenceStore store = CorePlugin.getDefault().getPreferenceStore();
        setPreferenceStore(store);
    }

    public void init(IWorkbench workbench) {
        // nothing to do
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    @Override
    protected void createFieldEditors() {
        // Adds a combo for language selection.

        String spanish = "Espa\u00F1ol"; //$NON-NLS-1$
        byte[] utf8Bytes;
        try {
            utf8Bytes = spanish.getBytes("UTF8"); //$NON-NLS-1$
            spanish = new String(utf8Bytes, "UTF8"); //$NON-NLS-1$
        } catch (UnsupportedEncodingException e1) {
            // could be translated, but it's only in case of error when change UTF8 characters.
            spanish = "Spanish"; //$NON-NLS-1$
        }

        String russian = "\u0420\u0443\u0441\u0441\u043A\u0438\u0439"; //$NON-NLS-1$
        try {
            utf8Bytes = russian.getBytes("UTF8"); //$NON-NLS-1$
            russian = new String(utf8Bytes, "UTF8"); //$NON-NLS-1$
        } catch (UnsupportedEncodingException e1) {
            // could be translated, but it's only in case of error when change UTF8 characters.
            russian = "Russian"; //$NON-NLS-1$
        }

        String[][] entryNamesAndValues = { { Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH), Locale.ENGLISH.getLanguage() },
                { Locale.FRENCH.getDisplayLanguage(Locale.FRENCH), Locale.FRENCH.getLanguage() },
                { Locale.CHINESE.getDisplayLanguage(Locale.CHINESE), Locale.CHINESE.getLanguage() },
                { Locale.GERMAN.getDisplayLanguage(Locale.GERMAN), Locale.GERMAN.getLanguage() },
                { Locale.JAPANESE.getDisplayLanguage(Locale.JAPANESE), Locale.JAPANESE.getLanguage() },
                { Locale.ITALIAN.getDisplayLanguage(Locale.ITALIAN), Locale.ITALIAN.getLanguage() }, { "Brasil", "pt_BR" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                { spanish, "es" }, { russian, "ru" } }; //$NON-NLS-1$ //$NON-NLS-2$
        languageSelectionEditor = new OneLineComboFieldEditor(ITalendCorePrefConstants.LANGUAGE_SELECTOR, Messages
                .getString("I18nPreferencePage.needRestart"), entryNamesAndValues, getFieldEditorParent()); //$NON-NLS-1$
        addField(languageSelectionEditor);

        Composite composite = getFieldEditorParent();
        LabelFieldEditor importAll = new LabelFieldEditor(Messages.getString("I18nPreferencePage.translationInformation"), //$NON-NLS-1$
                composite);
        addField(importAll);

        Button allUpdate = new Button(composite, SWT.FLAT);
        allUpdate.setText("Import Translation from Babili"); //$NON-NLS-1$
        allUpdate.setLayoutData(new GridData());

        allUpdate.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                // import all update from Babili
                // select the .zip file
                FileDialog fd = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.OPEN);
                fd.setText("Open");
                fd.setFilterPath("C:" + fs);
                String[] filterExtensions = { "*.zip" };
                fd.setFilterExtensions(filterExtensions);
                String selected = fd.open();
                isBabiliButtonClicked = true;
                runProgressMonitorDialog(selected);
                if (MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Restart",
                        "You need restart studio to activate the change."))
                    PlatformUI.getWorkbench().restart();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // Nothing to do
            }
        });

        // added by nma
        Button restoredefault = new Button(composite, SWT.FLAT);
        restoredefault.setText("Restore Defaults"); //$NON-NLS-1$
        restoredefault.setLayoutData(new GridData());
        restoredefault.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
                // Nothing to do
            }

            public void widgetSelected(SelectionEvent e) {
                isBabiliButtonClicked = true;
                runProgressMonitorDialog("Restore default");
                if (MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Restart",
                        "You need restart studio to activate the change."))
                    PlatformUI.getWorkbench().restart();
            }
        });
    }

    /**
     * 
     * DOC wzhang Comment method "runProgressMonitorDialog".
     * 
     * @param validated
     */
    public void runProgressMonitorDialog(final String zipFileName) {
        updateCompleted = false;
        ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getFieldEditorParent().getShell());
        IRunnableWithProgress runnable = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) {
                try {
                    String jarFolderPath = System.getProperty("user.dir") + fs + "jarTemp";
                    String zipFolderPath = System.getProperty("user.dir") + fs + "zipTemp";
                    File jarFolderPathFile = new File(jarFolderPath);
                    File zipFolderPathFile = new File(zipFolderPath);
                    if (!jarFolderPathFile.exists()) {
                        jarFolderPathFile.mkdir();
                    }
                    if (!zipFolderPathFile.exists()) {
                        zipFolderPathFile.mkdir();
                    }
                    String pluginPath = System.getProperty("user.dir") + fs + "rrrr";
                    HashMap jarFileMap = new HashMap();
                    File file = new File(pluginPath);
                    if (file.isDirectory()) {
                        String[] fileNameList = file.list();
                        File[] fileList = file.listFiles();
                        for (File f : fileList) {
                            if (f.getName().equals("net.sourceforge.sqlexplorer.nl_3.2.3.r35442.jar")) {
                                jarFileMap.put("net.sourceforge.sqlexplorer.nl", f);
                            }
                            if (f.getName().endsWith(".jar") && f.getName().indexOf("nl") != -1
                                    && f.getName().indexOf("talend") != -1) {
                                String[] fileNameArr = f.getName().split("_");
                                jarFileMap.put(fileNameArr[0], f);
                            }
                        }
                    }
                    if (zipFileName.equals("Restore default")) {
                        Iterator iter = jarFileMap.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry entry = (Map.Entry) iter.next();
                            Object key = entry.getKey();
                            File currentFileToBak = (File) entry.getValue();
                            if (key.toString().endsWith(".nl")) {
                                ZipFileUtils.unZip(currentFileToBak, jarFolderPath + fs + currentFileToBak.getName());
                                File jarFiles = new File(jarFolderPath + fs + currentFileToBak.getName());
                                File[] jarSubFiles = jarFiles.listFiles();
                                for (File subJarf : jarSubFiles) {
                                    if (subJarf.getName().endsWith(".original")) {
                                        String subjarfPath = subJarf.getAbsolutePath().substring(0,
                                                subJarf.getAbsolutePath().indexOf(".original"));
                                        File subjarfPathFile = new File(subjarfPath);
                                        if (subjarfPathFile.exists())
                                            subjarfPathFile.delete();
                                        subJarf.renameTo(subjarfPathFile);
                                    }
                                }
                                String writeJarFileName = jarFolderPath + fs + currentFileToBak.getName();
                                currentFileToBak.delete();
                                ZipFileUtils.zip(writeJarFileName, currentFileToBak.getAbsolutePath(), false);
                            }
                        }
                    } else {
                        ZipFileUtils.unZip(zipFileName, zipFolderPath);
                        File zipFile = new File(zipFolderPath);
                        File[] zipFiles = zipFile.listFiles()[0].listFiles();
                        for (File f : zipFiles) {
                            if (f.getName().endsWith(".nl")) {
                                File writeJarFile = (File) jarFileMap.get(f.getName());
                                if (writeJarFile == null)
                                    continue;
                                String jarFilePath = writeJarFile.getAbsolutePath();
                                ZipFileUtils.unZip(writeJarFile, jarFolderPath + fs + writeJarFile.getName());
                                File[] zipSubFiles = f.listFiles();
                                File jarFiles = new File(jarFolderPath + fs + writeJarFile.getName());
                                File[] jarSubFiles = jarFiles.listFiles();
                                boolean bakBool = false;
                                for (File subJarf : jarSubFiles) {
                                    if (subJarf.getName().endsWith(".original")) {
                                        bakBool = true;
                                    }
                                }
                                for (File subf : zipSubFiles) {
                                    boolean subfNotExistBool = true;
                                    for (File subJarf : jarSubFiles) {
                                        if (subf.getName().equals(subJarf.getName())) {
                                            String subjarfPath = subJarf.getAbsolutePath();
                                            File newSubJarf = new File(subjarfPath + ".original");
                                            if (!newSubJarf.exists() && bakBool == false)
                                                subJarf.renameTo(newSubJarf);
                                            try {
                                                FileChannel srcChannel = new FileInputStream(subf.getAbsoluteFile()).getChannel();
                                                FileChannel dstChannel = new FileOutputStream(subJarf.getAbsolutePath())
                                                        .getChannel();
                                                dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                                                dstChannel.close();
                                                subfNotExistBool = false;
                                                break;
                                            } catch (IOException e) {
                                                ExceptionHandler.process(e);
                                            }
                                        }
                                    }
                                    if (subfNotExistBool == true) {
                                        try {
                                            FileChannel srcChannel = new FileInputStream(subf.getAbsoluteFile()).getChannel();
                                            FileChannel dstChannel = new FileOutputStream(jarFiles.getAbsolutePath() + fs
                                                    + subf.getName()).getChannel();
                                            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                                            dstChannel.close();
                                        } catch (IOException e) {
                                            ExceptionHandler.process(e);
                                        }
                                    }
                                }
                                String writeJarFileName = jarFolderPath + fs + writeJarFile.getName();
                                writeJarFile.delete();
                                ZipFileUtils.zip(writeJarFileName, jarFilePath, false);
                            }
                        }
                    }
                    updateCompleted = true;
                    jarFolderPathFile.delete();
                    zipFolderPathFile.delete();
                } catch (Exception e1) {
                    ExceptionHandler.process(e1);
                } finally {
                    monitor.done();
                }
            }
        };
        try {
            progressDialog.run(true, true, runnable);
        } catch (InvocationTargetException e1) {
            ExceptionHandler.process(e1);
        } catch (InterruptedException e1) {
            ExceptionHandler.process(e1);
        }

        if (updateCompleted) {
        } else {
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        boolean ok = super.performOk();
        saveLanguageType();
        CorePlugin.getDefault().savePluginPreferences();
        if (isBabiliButtonClicked)
            RefreshView.refreshAll();
        return ok;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performApply()
     */
    @Override
    protected void performApply() {
        saveLanguageType();
        CorePlugin.getDefault().savePluginPreferences();
    }

    /**
     * 
     * DOC wzhang Comment method "saveLanguageType".
     */
    private void saveLanguageType() {
        FileInputStream fin = null;
        FileOutputStream fout = null;
        try {
            URL url = Platform.getConfigurationLocation().getURL();
            log(url.getFile());
            Properties p = new Properties();
            // load the file configuration/config.ini
            File iniFile = new File(url.getFile(), "config.ini"); //$NON-NLS-1$
            fin = new FileInputStream(iniFile);
            p.load(fin);

            String languageType = CorePlugin.getDefault().getPluginPreferences().getString(
                    ITalendCorePrefConstants.LANGUAGE_SELECTOR);

            if (languageType.equals(p.getProperty(EclipseStarter.PROP_NL))) {
                return;
            }

            p.setProperty(EclipseStarter.PROP_NL, languageType);
            fout = new FileOutputStream(iniFile);
            p.store(fout, "#Configuration File"); //$NON-NLS-1$
            fout.flush();

        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (Exception e) {
                    // do nothing
                }

            }
            if (fout != null) {
                try {
                    fout.close();
                } catch (Exception e) {
                    // do nothing
                }

            }
        }
    }

    /**
     * 
     * DOC wzhang Comment method "getCurrentTosVersion".
     * 
     * @param normalize
     * @return
     */
    public static String getCurrentTosVersion(boolean normalize) {
        String version = (String) CorePlugin.getDefault().getBundle().getHeaders().get(
                org.osgi.framework.Constants.BUNDLE_VERSION);
        if (normalize) {
            version = normalizeVersion(version);
        }
        return version;
    }

    /**
     * 
     * DOC wzhang Comment method "normalizeVersion".
     * 
     * @param version
     * @return
     */
    public static String normalizeVersion(String version) {
        Matcher matcher = VERSION_PATTERN.matcher(version);
        if (matcher.matches()) {
            String str = version.substring(0, version.indexOf("_r")); //$NON-NLS-1$
            return str.replaceAll("\\.RC", "RC").replaceAll("\\.M", "M"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        } else {
            // try again, ignore M, RC
            matcher = DEFAULT_PATTERN.matcher(version);
            matcher.find();
            return matcher.group();
        }
    }

    private void log(String s) {
        log.log(Level.INFO, s);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#addField(org.eclipse.jface.preference.FieldEditor)
     */
    @Override
    protected void addField(FieldEditor editor) {
        super.addField(editor);
        fields.add(editor);
    }

}
