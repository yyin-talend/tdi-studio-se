// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.sdk.component.studio.ui.composite.problemmanager;

import java.util.Optional;

import org.eclipse.swt.widgets.Display;
import org.talend.core.model.process.IElementParameter;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class WizardProblemManager extends ColorfulProblemManager {

    private Optional<IWizardHandler> wizardHandler = Optional.ofNullable(null);

    public void setWizardHandler(IWizardHandler wizardHandler) {
        this.wizardHandler = Optional.ofNullable(wizardHandler);
    }

    @Override
    public void setError(IElementParameter ep, String errMsg) {
        super.setError(ep, errMsg);
        this.wizardHandler.ifPresent(c -> Display.getDefault().asyncExec(() -> c.showError(errMsg)));
    }

    public interface IWizardHandler {

        public void showError(String error);
    }
}
