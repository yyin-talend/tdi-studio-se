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
package org.talend.designer.core.ui.projectsetting;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.core.model.properties.ImplicitContextSettings;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.process.jobsettings.JobSettingsConstants;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.ui.views.properties.MultipleThreadDynamicComposite;
import org.talend.designer.core.ui.views.properties.WidgetFactory;
import org.talend.repository.preference.ProjectSettingPage;

/**
 * DOC aimingchen class global comment. Detailled comment
 */
public class ImplicitContextLoadProjectSettingPage extends ProjectSettingPage {

    private MultipleThreadDynamicComposite mComposite;

    private Element elem;

    WidgetFactory widgetFactory = new WidgetFactory();

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent) {
        Composite composite = widgetFactory.createComposite(parent, SWT.NONE);
        composite.setLayout(new FormLayout());
        FormData data = createFormData();
        composite.setLayoutData(data);
        if (pro.getEmfProject().getImplicitContextSettings() == null) {
            // display one message box to tell the user the settings is get from old preference page.
            MessageDialog.openInformation(getShell(), getTitle(), Messages.getString("ImplicitContextLoad.LoadOldPreferences")); //$NON-NLS-1$
        }
        elem = ProjectSettingManager.createImplicitContextLoadElement(pro);
        ImplicitContextSettings implicit = pro.getEmfProject().getImplicitContextSettings();
        ElementParameter2ParameterType.loadElementParameters(elem, implicit.getParameters(), JobSettingsConstants
                .getExtraParameterName(EParameterName.PROPERTY_TYPE.getName())
                + ":" + EParameterName.PROPERTY_TYPE.getName());
        // create implicitContextLoad Control base on the ImplicitContextLoadElement
        mComposite = new MultipleThreadDynamicComposite(composite, SWT.V_SCROLL | SWT.BORDER, EComponentCategory.EXTRA, elem,
                true);
        mComposite.setLayoutData(createFormData());
        return composite;
    }

    private FormData createFormData() {
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        return data;
    }

    @Override
    public void dispose() {
        if (widgetFactory != null)
            widgetFactory.dispose();
        super.dispose();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk() {

        ImplicitContextSettings implicit = null;
        ParametersType parameters = null;
        if (mComposite != null) {
            // save the Element's parameters to EMF model
            Element elem = pro.getInitialContextLoad();
            implicit = pro.getEmfProject().getImplicitContextSettings();
            if (implicit != null) {
                parameters = implicit.getParameters();
                if (parameters != null && !"".equals(parameters)) {
                    // save to the memory
                    ElementParameter2ParameterType.saveElementParameters(elem, parameters);
                }
            }
            ProjectSettingManager.saveProject();
        }
        // if (parameters != null) {
        // ElementParameter2ParameterType.loadProjectsettingsParameters(parameters);
        // }
        return super.performOk();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performApply()
     */
    @Override
    protected void performApply() {
        performOk();
        super.performApply();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.preference.ProjectSettingPage#refresh()
     */
    @Override
    public void refresh() {
        if (mComposite != null) {
            ImplicitContextSettings implicit = pro.getEmfProject().getImplicitContextSettings();
            ElementParameter2ParameterType.loadElementParameters(elem, implicit.getParameters(), JobSettingsConstants
                    .getExtraParameterName(EParameterName.PROPERTY_TYPE.getName())
                    + ":" + EParameterName.PROPERTY_TYPE.getName());
            mComposite.refresh();
        }

    }

}
