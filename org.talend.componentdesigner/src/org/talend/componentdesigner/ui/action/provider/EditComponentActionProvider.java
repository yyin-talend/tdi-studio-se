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
package org.talend.componentdesigner.ui.action.provider;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.talend.componentdesigner.ImageLib;
import org.talend.componentdesigner.ui.wizard.creatcomponent.CreateComponentWizard;

/**
 * DOC rli  class global comment. Detailled comment
 */
public class EditComponentActionProvider extends CommonActionProvider {

	/**
	 * DOC rli EditComponentActionProvider constructor comment.
	 */
	public EditComponentActionProvider() {
		// TODO Auto-generated constructor stub
	}
	
	private IAction editComponentAction;

	private String selectedFolderName;

	public void init(ICommonActionExtensionSite anExtensionSite) {

		if (anExtensionSite.getViewSite() instanceof ICommonViewerWorkbenchSite) {
			editComponentAction = new EditComponentAction();
		}
	}

	/**
	 * Adds a submenu to the given menu with the name "New Component".
	 */
	public void fillContextMenu(IMenuManager menu) {
		menu.insertBefore("group.edit", editComponentAction);
		Object obj = ((TreeSelection) this.getContext().getSelection())
				.getFirstElement();
		if (obj instanceof IFolder) {
			selectedFolderName = ((IFolder) obj).getName();
		}
	}

	/**
	 * @author rli
	 * 
	 */
	class EditComponentAction extends Action {

		public EditComponentAction() {
			super("Edit This Component");
			setImageDescriptor(ImageLib
					.getImageDescriptor(ImageLib.EDITCOMPONENT_ACTION));
		}

		/*
		 * (non-Javadoc) Method declared on IAction.
		 */
		public void run() {
			CreateComponentWizard wizard = new CreateComponentWizard(selectedFolderName);
			 wizard.init(PlatformUI.getWorkbench(), null);
			WizardDialog dialog = new WizardDialog(null, wizard);
			dialog.open();
		}
	}

}
