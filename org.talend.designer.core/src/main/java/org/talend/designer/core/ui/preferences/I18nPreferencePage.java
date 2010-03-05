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
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.epic.core.preferences.LabelFieldEditor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.i18n.BabiliInfo;
import org.talend.commons.i18n.BabiliTool;
import org.talend.commons.i18n.BabiliUpdateUtil;
import org.talend.commons.i18n.ImportBabiliCancelException;
import org.talend.core.CorePlugin;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.prefs.ui.CorePreferencePage;
import org.talend.core.prefs.ui.OneLineComboFieldEditor;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.views.RefreshView;

/**
 * DOC wzhang class global comment. Detailled comment
 */
public class I18nPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private OneLineComboFieldEditor languageSelectionEditor;

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
        allUpdate.setText(Messages.getString("I18nPreferencePage.allTranslation")); //$NON-NLS-1$
        allUpdate.setLayoutData(new GridData());

        Button validatedUpdate = new Button(composite, SWT.NONE);
        validatedUpdate.setText(Messages.getString("I18nPreferencePage.validateTranslation")); //$NON-NLS-1$
        validatedUpdate.setLayoutData(new GridData());

        allUpdate.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                // import all update from Babili
                isBabiliButtonClicked = true;
                runProgressMonitorDialog(false);
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // Nothing to do
            }
        });

        validatedUpdate.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                // import validated update from Babili
                isBabiliButtonClicked = true;
                runProgressMonitorDialog(true);
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // Nothing to do
            }

        });

    }

    /**
     * 
     * DOC wzhang Comment method "runProgressMonitorDialog".
     * 
     * @param validated
     */
    public void runProgressMonitorDialog(final boolean validated) {
        updateCompleted = false;
        BabiliTool.clear();
        ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getFieldEditorParent().getShell());
        IRunnableWithProgress runnable = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) {
                try {
                    monitor.beginTask(Messages.getString("I18nPreferencePage.loading"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$
                    String language = languageSelectionEditor.getFieldValue();
                    String version = getCurrentTosVersion(true);
                    // get list from Babili
                    List<BabiliInfo> bList = BabiliUpdateUtil.getBabiliList(language, validated, version, monitor);
                    for (BabiliInfo info : bList) {
                        BabiliUpdateUtil.checkProcessCancel(monitor);
                        // store to memory
                        String pluginId = info.getFilepath();
                        // for components
                        if (pluginId.startsWith("components/")) { //$NON-NLS-1$
                            String regexp = "((\\w)+)_messages(_.+?)?\\.properties"; //$NON-NLS-1$
                            Pattern p = Pattern.compile(regexp);
                            Matcher m = p.matcher(pluginId);
                            if (m.find()) {
                                pluginId = m.group(1);
                            }
                        } else {
                            int pos = pluginId.indexOf("/"); //$NON-NLS-1$
                            if (pos != -1) {
                                pluginId = pluginId.substring(0, pos);
                                if (pluginId.endsWith(".nl")) { //$NON-NLS-1$
                                    pluginId = pluginId.replace(".nl", ""); //$NON-NLS-1$ //$NON-NLS-2$
                                }
                            }
                        }
                        BabiliTool.storeBabiliTranslation(info.getKey(), pluginId, info.getLabel());
                    }

                    updateCompleted = true;

                } catch (ImportBabiliCancelException e) {
                    updateCompleted = false;
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
            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    MessageDialog.openInformation(Display.getDefault().getActiveShell(), Messages
                            .getString("I18nPreferencePage.title"), //$NON-NLS-1$
                            Messages.getString("I18nPreferencePage.completeInfo")); //$NON-NLS-1$
                }
            });
        } else {
            BabiliTool.clear();
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
