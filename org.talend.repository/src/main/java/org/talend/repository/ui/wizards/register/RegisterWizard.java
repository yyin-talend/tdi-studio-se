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
package org.talend.repository.ui.wizards.register;

import org.eclipse.jface.wizard.Wizard;
import org.talend.repository.i18n.Messages;

/**
 * Wizard for the creation of a new project. <br/>
 * 
 * $Id$
 * 
 */
public class RegisterWizard extends Wizard {

    /** Main page. */
    private RegisterWizardPage mainPage;
    
    private String email;
    private String country;

    /**
     * Constructs a new RegisterWizard.
     * 
     * @param author Project author.
     * @param server
     * @param password
     * @param port2
     */
    public RegisterWizard() {
        super();
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        mainPage = new RegisterWizardPage();
        addPage(mainPage);
        setWindowTitle(Messages.getString("RegisterWizard.windowTitle"));
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        this.email = mainPage.getEmail();
        this.country = mainPage.getCountry();
        return true;
    }
    
    /**
     * Getter for country.
     * @return the country
     */
    public String getCountry() {
        return this.country;
    }
    
    /**
     * Sets the country.
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }
    
    /**
     * Getter for email.
     * @return the email
     */
    public String getEmail() {
        return this.email;
    }
    
    /**
     * Sets the email.
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
