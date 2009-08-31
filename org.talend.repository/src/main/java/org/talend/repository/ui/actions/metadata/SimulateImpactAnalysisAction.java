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
package org.talend.repository.ui.actions.metadata;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.PluginChecker;
import org.talend.repository.i18n.Messages;

/**
 * cli class global comment. Detailled comment
 */
public class SimulateImpactAnalysisAction extends DetecteViewImpactAction {

    public SimulateImpactAnalysisAction() {
        setOnlySimpleShow(true);
        String label = Messages.getString("SimulateImpactAnalysisAction.ImpactAnalysis"); //$NON-NLS-1$
        setText(label);
        setToolTipText(label);
        setImageDescriptor(ImageProvider.getImageDesc(EImage.REFRESH_ICON));
    }

    public void init(TreeViewer viewer, IStructuredSelection selection) {
        super.init(viewer, selection);
        setEnabled(isEnabled() && PluginChecker.isTIS());
    }

}
