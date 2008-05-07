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
package org.talend.repository.ui.actions;

import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.repository.i18n.Messages;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public class TranscriptAction extends DuplicateAction {

    public TranscriptAction() {
        super();
        this.setText(Messages.getString("TranscriptAction.title")); //$NON-NLS-1$
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.COPY_ICON));
    }

    @Override
    protected boolean resetProcessVersion() {
        return true;
    }
}
