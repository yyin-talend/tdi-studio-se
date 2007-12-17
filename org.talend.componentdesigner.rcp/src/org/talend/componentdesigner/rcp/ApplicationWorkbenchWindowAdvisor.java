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
package org.talend.componentdesigner.rcp;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.talend.componentdesigner.ComponentDesigenerPlugin;
import org.talend.componentdesigner.manager.ComponentProjectManager;
import org.talend.componentdesigner.ui.ProjectSelectionDialog;

/**
 * DOC rli  class global comment. Detailled comment
 * <br/>
 *
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 *
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(1000, 750));
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(false);
		configurer.setTitle("RCP Application");
		chooseProjectLocation();
	}
	
	private void chooseProjectLocation() {
		boolean isOK = true;
		if (!ComponentDesigenerPlugin.getDefault().isUsed()) {
			isOK = popSelectionDiaLog(PlatformUI.getWorkbench().getDisplay()
					.getActiveShell());
		}
		if (!isOK) {
			Platform.endSplash();
			System.exit(0);
		}
	}

	private boolean popSelectionDiaLog(Shell activeShell) {
        ProjectSelectionDialog dialog = new ProjectSelectionDialog(activeShell);
        return IDialogConstants.OK_ID == dialog.open();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#postWindowOpen()
	 */
	@Override
	public void postWindowOpen() {
		ComponentDesigenerPlugin.getDefault().checkProject();
	}
}
