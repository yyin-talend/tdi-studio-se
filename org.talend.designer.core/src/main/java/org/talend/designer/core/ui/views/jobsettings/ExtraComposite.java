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

import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.designer.core.i18n.Messages;
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
