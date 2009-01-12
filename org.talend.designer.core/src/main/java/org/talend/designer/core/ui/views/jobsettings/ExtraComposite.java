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
package org.talend.designer.core.ui.views.jobsettings;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
import org.talend.designer.core.ui.projectsetting.ElementParameter2ParameterType;
import org.talend.designer.core.ui.projectsetting.ProjectSettingManager;
import org.talend.repository.ProjectManager;

/**
 * DOC chuang class global comment. Detailled comment
 */
public class ExtraComposite extends AbstractPreferenceComposite {

    /**
     * DOC chuang ExtraComposite constructor comment.
     * 
     * @param parentComposite
     * @param styles
     * @param section
     * @param element
     * @param isCompactView
     */
    public ExtraComposite(Composite parentComposite, int styles, EComponentCategory section, Element element,
            boolean isCompactView) {
        super(parentComposite, styles, section, element, isCompactView);
        setDialogTitle(Messages.getString("ExtraComposite.ImplicitContextSettings"));

        // achen modify to fix 0005993
        isUsingProjectSetting = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.views.properties.MultipleThreadDynamicComposite#addComponents(boolean)
     */
    @Override
    public void addComponents(boolean forceRedraw, boolean reInitialize, int height) {
        // TODO Auto-generated method stub
        super.addComponents(forceRedraw, reInitialize, height);
        // achen add to fix 0005991 & 0005993 when reload
        Object value = ElementParameter2ParameterType.getParameterValue(elem, EParameterName.IMPLICITCONTEXT_USE_PROJECT_SETTINGS
                .getName());
        if (value != null && value instanceof Boolean) {
            Boolean v = (Boolean) value;
            useProjectSetting.setSelection(v.booleanValue());
            setMainCompositeEnable(!v.booleanValue());
            topComposite.setEnabled(true);
            // if (v.booleanValue()) {
            // onReloadPreference();
            // }
        }
        if (useProjectSetting != null) {
            useProjectSetting.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    boolean flag = useProjectSetting.getSelection();
                    setMainCompositeEnable(!flag);
                    topComposite.setEnabled(true);
                    // ElementParameter2ParameterType.setParameterValue(elem,
                    // EParameterName.USE_PROJECT_SETTINGS.getName(), Boolean
                    // .valueOf(flag));
                    PropertyChangeCommand cmd = new PropertyChangeCommand(elem,
                            EParameterName.IMPLICITCONTEXT_USE_PROJECT_SETTINGS.getName(), Boolean.valueOf(flag));
                    getCommandStack().execute(cmd);
                    if (flag) {
                        useProjectSetting();
                    }
                }
            });
        }
    }

    @Override
    protected void onReloadPreference() {
        // ImplicitContextLoadHelper.reloadValuesFromPreferencePage(elem, ExtraComposite.this);
        // achen modify to fix 0005993
        ProjectSettingManager.reloadImplicitValuesFromProjectSettings(elem, ProjectManager.getInstance().getCurrentProject(),
                ExtraComposite.this);
    }

    @Override
    protected void onSavePreference() {
        // ImplicitContextLoadHelper.saveValuesToPreferencePage(elem, ExtraComposite.this);
        // achen modify to fix 0005993
        ProjectSettingManager.saveImplicitValuesToProjectSettings(elem, ProjectManager.getInstance().getCurrentProject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.views.jobsettings.AbstractPreferenceComposite#needApplyToChildren()
     */
    @Override
    protected boolean needApplyToChildren() {
        return false;
    }
}
