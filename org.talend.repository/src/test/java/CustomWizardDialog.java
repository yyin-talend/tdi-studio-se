// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

/**
 * Extends WizardDialog to customize the label Finish. <br/>
 * 
 * $Id$
 * 
 */
public class CustomWizardDialog extends WizardDialog {

    private String finishLabel = "Finish";

    /**
     * Constructor WizardDialog.
     * 
     * @param parentShell
     * @param newWizard
     */
    public CustomWizardDialog(Shell parentShell, IWizard newWizard, String finishLabel) {
        super(parentShell, newWizard);
        this.finishLabel = finishLabel;
    }

    public void createButtonsForButtonBar() {
        Button button = getButton(IDialogConstants.FINISH_ID);
        if (button != null) {
            button.setText(finishLabel);
        }
    }

}
