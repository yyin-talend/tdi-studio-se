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
import org.talend.core.ui.branding.BrandingService;
import org.talend.repository.i18n.Messages;

/**
 * Wizard for the creation of a new project. <br/> $Id: RegisterWizard.java 1 2006-09-29 17:06:40 +0000 (ven., 29 sept.
 * 2006) nrousseau $
 */
public class RegisterWizard extends Wizard {

    /** Main page. */
    private RegisterWizardPage mainPage;

    private String email;

    private String country;

    private boolean proxyEnabled = false;

    private String proxyHost = "";

    private String proxyPort = "";

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
        setWindowTitle(Messages.getString("RegisterWizard.windowTitle", BrandingService.getInstance().getFullProductName()));
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        this.email = mainPage.getEmail();
        this.country = mainPage.getCountry();
        this.proxyEnabled = mainPage.getEnableHttpProxy().getEnabled();
        this.proxyHost = mainPage.getHttpProxyHostText().getText();
        this.proxyPort = mainPage.getHttpProxyPortText().getText();

        return true;
    }

    /**
     * Getter for country.
     * 
     * @return the country
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * Sets the country.
     * 
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Getter for email.
     * 
     * @return the email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Sets the email.
     * 
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the proxyEnabled
     */
    public boolean isProxyEnabled() {
        return proxyEnabled;
    }

    /**
     * @param proxyEnabled the proxyEnabled to set
     */
    public void setProxyEnabled(boolean proxyEnabled) {
        this.proxyEnabled = proxyEnabled;
    }

    /**
     * @return the proxyHost
     */
    public String getProxyHost() {
        return proxyHost;
    }

    /**
     * @param proxyHost the proxyHost to set
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /**
     * @return the proxyPort
     */
    public String getProxyPort() {
        return proxyPort;
    }

    /**
     * @param proxyPort the proxyPort to set
     */
    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

}
