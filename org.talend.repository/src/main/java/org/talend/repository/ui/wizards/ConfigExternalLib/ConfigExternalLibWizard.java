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
package org.talend.repository.ui.wizards.ConfigExternalLib;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.repository.i18n.Messages;

/**
 * Job scripts export wizard. <br/>
 * 
 * $Id: JobScriptsExportWizard.java 1 2006-12-13 下午03:13:18 bqian
 * 
 */
public class ConfigExternalLibWizard extends Wizard {

    private IStructuredSelection selection;

    private ConfigExternalLibPage mainPage;

    /**
     * Creates a wizard for importing the external library for java and perl.
     */
    public ConfigExternalLibWizard() {
        this.setWindowTitle(Messages.getString("ImportExternalLibWizard.windows.title")); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc) Method declared on IWizard.
     */
    public void addPages() {
        super.addPages();

        switch (((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY)).getProject()
                .getLanguage()) {
        case JAVA:
            mainPage = new ConfigExternalJarPage(selection);
            break;
        case PERL:
            mainPage = new ConfigExternalPerlModulePage(selection);
            break;
        }

        addPage(mainPage);
    }

    /*
     * (non-Javadoc) Method declared on IWorkbenchWizard.
     */
    public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
        this.selection = currentSelection;

        setDefaultPageImageDescriptor(IDEWorkbenchPlugin.getIDEImageDescriptor("wizban/exportzip_wiz.png"));//$NON-NLS-1$
        setNeedsProgressMonitor(true);
    }

    /*
     * (non-Javadoc) Method declared on IWizard.
     */
    public boolean performFinish() {
        boolean flag = mainPage.finish();
        List<String> textList = new ArrayList<String>();
        List<String> anotherRoutineNameList = new ArrayList<String>();
        List<String> routineNameList = this.getRoutineName();
        String path = CorePlugin.getDefault().getLibrariesService().getLibrariesPath();
        List<String> anotherList = new ArrayList<String>();

        try {
            Pattern pttn = Pattern.compile("(.*)java$");
            Matcher mtcr = pttn.matcher(path);
            Pattern pten = Pattern.compile("(.*)perl$");
            Matcher mter = pten.matcher(path);
            if (mtcr.find()) {
                textList = this.readData("java", textList);
                anotherList = this.compareName(textList, routineNameList, anotherRoutineNameList, anotherList);
                this.preferenceStore("java", anotherList);
            }
            // } else if (mter.find()) {
            // textList = this.readData("perl", textList);
            // anotherList = this.compareName(textList, routineNameList, anotherRoutineNameList, anotherList);
            // this.preferenceStore("perl", anotherList);
            // }

            return flag;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    private List<String> compareName(List<String> textList, List<String> routineNameList, List<String> anotherRoutineNameList,
            List<String> anotherList) {
        anotherRoutineNameList.addAll(routineNameList);
        for (String routineName : routineNameList) {
            for (String textString : textList) {
                if (routineName.equals(textString)) {
                    anotherRoutineNameList.remove(routineName);
                }
            }
        }

        for (String routineName : anotherRoutineNameList) {
            boolean same = false;
            boolean other = false;
            for (String newName : anotherList) {
                if (routineName.equals(newName)) {
                    other = true;
                }
            }
            if (other) {
                continue;
            }
            for (String name : anotherRoutineNameList) {
                if (routineName.equals(name)) {
                    if (!same) {
                        anotherList.add(routineName);
                        same = true;
                    }
                }
            }
        }
        return anotherList;
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

    private List<String> getRoutineName() {
        List<String> moduleNameList = new ArrayList<String>();

        List<ModuleNeeded> moduleNeededList = ModulesNeededProvider.getModulesNeededForRoutines();
        for (ModuleNeeded moduleNeeded : moduleNeededList) {
            moduleNameList.add(moduleNeeded.getModuleName());
        }
        return moduleNameList;
    }
}
