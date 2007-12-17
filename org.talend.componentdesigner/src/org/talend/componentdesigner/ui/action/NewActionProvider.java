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
package org.talend.componentdesigner.ui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.talend.componentdesigner.ImageLib;
import org.talend.componentdesigner.ui.wizard.creatcomponent.CreateComponentWizard;

/**
 * Provides the new (artifact creation) menu options for a context menu.
 * 
 * 
 */
public class NewActionProvider extends CommonActionProvider {
	private static final String NEW_MENU_NAME = "common.new.menu"; //$NON-NLS-1$

	private IAction newProjectAction;

	public void init(ICommonActionExtensionSite anExtensionSite) {

		if (anExtensionSite.getViewSite() instanceof ICommonViewerWorkbenchSite) {
			newProjectAction = new NewComponentAction();
		}
	}

	/**
	 * Adds a submenu to the given menu with the name "New Component".
	 */
	public void fillContextMenu(IMenuManager menu) {
		IMenuManager submenu = new MenuManager("New", NEW_MENU_NAME);
		submenu.add(newProjectAction);

		// append the submenu after the GROUP_NEW group.
		menu.insertAfter(ICommonMenuConstants.GROUP_NEW, submenu);
	}
	
	/**
	 * @author rli
	 *
	 */
	class NewComponentAction extends Action {

		public NewComponentAction() {
			super("New Component");
	        ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
	        setImageDescriptor(images
	                .getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));
		}

		/*
		 * (non-Javadoc) Method declared on IAction.
		 */
		public void run() {
			CreateComponentWizard wizard = new CreateComponentWizard();
			WizardDialog dialog = new WizardDialog(null, wizard);
			dialog.open();
		}
	}


}
