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
package org.talend.designer.core.ui.views.statsandlogs;

import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.projectsetting.ProjectSettingManager;
import org.talend.designer.core.ui.views.jobsettings.AbstractPreferenceComposite;
import org.talend.repository.ProjectManager;

/**
 * ftang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class StatsAndLogsComposite extends AbstractPreferenceComposite {

    /**
     * ftang StatAndLogsComposite constructor comment.
     * 
     * @param parentComposite
     * @param styles
     * @param section
     * @param element
     */
    public StatsAndLogsComposite(Composite parentComposite, int styles, EComponentCategory section, Element element) {
        super(parentComposite, styles, section, element, true);
        setDialogTitle(Messages.getString("StatsAndLogsComposite.StatsLogsSettings"));
        // achen modify to fix 0005993 change button's text
        isUsingProjectSetting = true;
    }

    @Override
    protected void onReloadPreference() {
        // StatsAndLogsViewHelper.reloadValuesFromPreferencePage(elem, StatsAndLogsComposite.this);
        // achen modify to fix 0005991
        ProjectSettingManager.reloadStatsAndLogFromProjectSettings(elem, ProjectManager.getInstance().getCurrentProject(),
                StatsAndLogsComposite.this);
    }

    @Override
    protected void onSavePreference() {
        // StatsAndLogsViewHelper.saveValuesToPreferencePage(elem, StatsAndLogsComposite.this);
        // achen modify to fix 0005991
        ProjectSettingManager.saveStatsAndLogToProjectSettings(elem, ProjectManager.getInstance().getCurrentProject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.views.jobsettings.AbstractPreferenceComposite#needApplyToChildren()
     */
    @Override
    protected boolean needApplyToChildren() {
        return true;
    }

}
