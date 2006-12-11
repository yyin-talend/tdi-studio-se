// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.help.perl;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.talend.help.perl.ui.PerlHelpComposite;

/**
 * PerlHelpDialog.java.
 *
 */
public class PerlHelpDialog extends TitleAreaDialog {

	public PerlHelpDialog(Shell parentShell) {
		super(parentShell);
        setShellStyle(SWT.DIALOG_TRIM  | getDefaultOrientation() | SWT.RESIZE);
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		createDialogAreaContent(composite);
		return composite;
	}

	private void createDialogAreaContent(Composite parent) {
		PerlHelpComposite perlHelpComposite = new PerlHelpComposite(parent, SWT.BORDER);
		perlHelpComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
	}
}
