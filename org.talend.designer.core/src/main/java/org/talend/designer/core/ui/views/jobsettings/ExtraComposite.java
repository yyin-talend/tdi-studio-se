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
    }

    @Override
    protected void onReloadPreference() {
        ImplicitContextLoadHelper.reloadValuesFromPreferencePage(elem, ExtraComposite.this);
    }

    @Override
    protected void onSavePreference() {
        ImplicitContextLoadHelper.saveValuesToPreferencePage(elem, ExtraComposite.this);
    }
}
