// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.RetargetAction;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.designer.core.assist.TalendEditorComponentCreationAssistAction;
import org.talend.designer.core.ui.preferences.TalendDesignerPrefConstants;

/**
 * DOC PLV class global comment. Detailled comment
 */
public class ComponentAssistRetargetAction extends RetargetAction {

    public ComponentAssistRetargetAction() {
        super(TalendEditorComponentCreationAssistAction.ID, TalendEditorComponentCreationAssistAction.TEXT, IAction.AS_CHECK_BOX);
        setToolTipText(TalendEditorComponentCreationAssistAction.TEXT);
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        setChecked(TalendEditorComponentCreationAssistAction.getDefault().getGlobalStore()
                .getBoolean(TalendDesignerPrefConstants.COMPONENT_ASSIST));
        // TODO Provides appropriate hover and disabled images
        setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.COMPONMENT_ASSIST));
        // setDisabledImageDescriptor(ImageProvider.getImageDesc(ECoreImage.TOGGLE_SUBJOB_DISABLED));
    }

}
