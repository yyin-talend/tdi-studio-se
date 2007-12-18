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

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.talend.componentdesigner.ImageLib;
import org.talend.componentdesigner.ui.dialog.CopyComponentDialog;
import org.talend.componentdesigner.ui.dialog.CopyComponentValidator;

/**
 * @author rli
 * 
 */
public class CopyComponentActionProvider extends CommonActionProvider {

	private IAction copyProjectAction;

	private String selectedFolderName;

	public void init(ICommonActionExtensionSite anExtensionSite) {

		if (anExtensionSite.getViewSite() instanceof ICommonViewerWorkbenchSite) {
			copyProjectAction = new CopyComponentAction();
		}
	}

	/**
	 * Adds a submenu to the given menu with the name "New Component".
	 */
	public void fillContextMenu(IMenuManager menu) {
		menu.insertBefore("group.edit", copyProjectAction);
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
	class CopyComponentAction extends Action {

		public CopyComponentAction() {
			super("Copy This Component");
			setImageDescriptor(ImageLib
					.getImageDescriptor(ImageLib.COPYCOMPONENT));
		}

		/*
		 * (non-Javadoc) Method declared on IAction.
		 */
		public void run() {
			CopyComponentValidator validator = new CopyComponentValidator();
			CopyComponentDialog dialog = new CopyComponentDialog(PlatformUI
					.getWorkbench().getActiveWorkbenchWindow().getShell(),
					"Copy Component", "Input a new component name for '"
							+ selectedFolderName + "'", selectedFolderName,
					validator);
			dialog.open();
		}
	}
}
