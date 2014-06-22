// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.salesforce.oauth.test;

import org.talend.salesforce.oauth.OAuthClient;
import org.talend.salesforce.oauth.Token;

import com.salesforce.soap.partner.sobject.SObject;

/**
 * created by bchen on Aug 28, 2013 Detailled comment
 * 
 */
public class OAuthClientTest {

    public static void main(String[] args) throws Exception {
        OAuthClient client = new OAuthClient();
        client.setBaseOAuthURL("https://login.salesforce.com/services/oauth2");
        client.setCallbackHost("192.168.30.123");
        client.setCallbackPort(8555);
        client.setClientID("3MVG99qusVZJwhsl6LQR3j8Kp74AoXCfza9O6Q09mfGq0Rk1uGZ1whOlV24dxQLFIrn1MSe7dm4JUbG8oy9fY");
        client.setClientSecret("8349684339611555474");
        Token token = client.getToken();
        System.out.println(token.getAccess_token());
        String refreshToken = token.getRefresh_token();
        token = client.refreshToken(refreshToken);
        System.out.println(token.getAccess_token());
        // rest bulk api
        org.talend.salesforceBulk.SalesforceBulkAPI sforceBulk = new org.talend.salesforceBulk.SalesforceBulkAPI();

        sforceBulk.login(token.getAccess_token(), client.getBulkEndpoint(token, "25.0"));

        sforceBulk.setConcurrencyMode("Parallel");
        sforceBulk.setAwaitTime(100 * 1000);

        sforceBulk.executeBulk("Account", "insert", "Id", "csv", "/home/bchen/Desktop/sforce_bulk.csv", 10 * 1024 * 1024, 10000);

        // soap api
        String endpoint = client.getSOAPEndpoint(token, "25.0");

        org.talend.salesforce.SforceManagement sfMgr = new org.talend.salesforce.SforceManagementImpl();

        boolean result = sfMgr.login(token.getAccess_token(), endpoint, 60000, false);
        if (!result) {
            throw new RuntimeException("Login failed! Please check the username,password and endpoint");
        }
        com.salesforce.soap.partner.QueryResult qr = sfMgr.query("select Id from Account", 250);
        SObject[] records = qr.getRecords();
        System.out.println(records.length);
    }
}
