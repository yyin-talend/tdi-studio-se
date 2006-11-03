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
package org.talend.sqlbuilder.ui.proposal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.talend.commons.ui.swt.proposal.ContentProposalAdapterExtended;


/**
 * DOC dev  class global comment. Detailled comment
 * <br/>
 *
 * $Id: SQLEditorControlHelper.java,v 1.4 2006/11/03 07:18:21 qiang.zhang Exp $
 *
 */
public class SQLEditorControlHelper {

    private Map<Control, ControlProperties> controlToProp = new HashMap<Control, ControlProperties>();

    /**
     * DOC amaumont CheckSyntaxHelper constructor comment.
     */
    public SQLEditorControlHelper() {
        super();
    }

    private FocusListener focusListenerForCheckingError = new FocusListener() {

        public void focusGained(FocusEvent event) {
            focusGainedExecute((Control) event.widget);
        }

        public void focusLost(FocusEvent event) {
            if (!extendedProposal.isProposalOpened()) {
                Control control = (Control) event.widget;
                checkErrors(control, false);
            }
        }

    };

    private KeyListener keyListenerForCheckingError = new KeyListener() {

        public void keyPressed(KeyEvent event) {
            Control control = (Control) event.widget;
            resetErrorState(control);
        }

        public void keyReleased(KeyEvent e) {
        }

    };

    private ContentProposalAdapterExtended extendedProposal;

    /**
     * DOC dev Comment method "register".
     * @param control 
     * @param extendedProposal
     */
    public void register(Control control, ContentProposalAdapterExtended extendedProposal){
        control.addFocusListener(focusListenerForCheckingError);
        control.addKeyListener(keyListenerForCheckingError);
        this.extendedProposal = extendedProposal;
    }

    /**
     * DOC amaumont Comment method "unregister".
     * 
     * @param control
     */
    public void unregister(Control control) {
        control.removeFocusListener(focusListenerForCheckingError);
        control.removeKeyListener(keyListenerForCheckingError);
    }

    private void focusGainedExecute(Control control) {
        resetErrorState(control);
    }

    /**
     * DOC amaumont Comment method "checkSyntax".
     * 
     * @param control
     * @param modifying
     */
    public void checkErrors(final Control control, final boolean modifying) {

    }

    /**
     * DOC amaumont Comment method "resetErrorState".
     * 
     * @param control
     * @param previousProblem
     */
    private void resetErrorState(final Control control) {
        ControlProperties existingControlProperties = controlToProp.get(control);
        if (existingControlProperties != null) {
            control.setToolTipText(existingControlProperties.originalToolTip);
            control.setBackground(existingControlProperties.originalBgColor);
            control.setForeground(existingControlProperties.originalFgColor);
            controlToProp.remove(control);
        }
    }

    /**
     * 
     * Container of original properties of Control. <br/>
     * 
     * $Id: SQLEditorControlHelper.java,v 1.4 2006/11/03 07:18:21 qiang.zhang Exp $
     * 
     */
    class ControlProperties {

        public Color originalBgColor;

        public Color originalFgColor;

        public String originalToolTip;

        /**
         * DOC amaumont ControlProperties constructor comment.
         */
        public ControlProperties() {
            super();
        }

    }

}
