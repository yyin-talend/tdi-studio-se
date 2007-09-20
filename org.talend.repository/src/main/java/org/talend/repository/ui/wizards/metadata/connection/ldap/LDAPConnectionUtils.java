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
package org.talend.repository.ui.wizards.metadata.connection.ldap;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.ldap.InitialLdapContext;

import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.core.ldap.AdvancedSocketFactory;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.repository.model.EAuthenticationMethod;
import org.talend.repository.model.EEncryptionMethod;

/**
 * This class is used for getting connection and retrieving data for LDAP. <br/>
 * 
 * @author ftang, 18/09/2007
 * 
 */
public class LDAPConnectionUtils {

    private static java.util.Hashtable<String, String> env = null;

    private static final String LDAP_URL_PREFIX = "ldap://";

    private static final String LDAPS_URL_PREFIX = "ldaps://";

    private static final String CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

    private static InitialLdapContext ctx = null;

    /**
     * Comment method "getAttributes".
     * 
     * @return
     */
    public static List<Attribute> getAttributes(LDAPSchemaConnection connection) {
        if (ctx == null) {
            return null;
        }

        List<Attribute> attributeList = new ArrayList<Attribute>();
        String baseDN = connection.getSelectedDN();
        EList baseDNs = connection.getBaseDNs();
        String searchFilter = connection.getFilter();
        int timeLimit = (Integer.valueOf(connection.getTimeOutLimit()).intValue());
        EList returnAttributeList = connection.getReturnAttributes();

        int size = returnAttributeList.size();

        String[] returnAttributes = (String[]) returnAttributeList.toArray(new String[size]);

        String filter = null;

        try {
            javax.naming.directory.SearchControls searchCtls = new javax.naming.directory.SearchControls();
            searchCtls.setSearchScope(javax.naming.directory.SearchControls.SUBTREE_SCOPE);

            javax.naming.directory.Attributes namingContexts = ctx.getAttributes("", new String[] { "namingContexts" });
            javax.naming.directory.Attribute namingContextValue = namingContexts.get("namingContexts");
            if (namingContextValue == null) {
                namingContextValue = namingContexts.get("namingcontexts");
            }
            String searchBase = null;
            if (baseDN == null) {
                searchBase = ((namingContextValue == null) || ((namingContextValue != null)
                        && (namingContextValue.get().toString() != null)
                        && (namingContextValue.get().toString().length() > 0) && (Character
                        .isIdentifierIgnorable(namingContextValue.get().toString().charAt(0))))) ? ""
                        : namingContextValue.get().toString();
            } else {
                searchBase = baseDN;
            }

            if (searchFilter == null) {
                searchFilter = "(&(objectClass=*))";
            } else {
                filter = searchFilter;
            }

            // initialize counter to total the group members
            int totalResults = 0;

            // Specify the attributes to return.
            if (returnAttributes != null && returnAttributes.length > 0) {
                searchCtls.setReturningAttributes(returnAttributes);
            }

            // specify the value of time out.
            if (timeLimit != 0) {
                searchCtls.setTimeLimit(timeLimit);
            }

            boolean isGetBaseDNsFromRoot = connection.isGetBaseDNsFromRoot();

            if (isGetBaseDNsFromRoot) {
                EList baseDNList = baseDNs;
                for (Object tempBaseDN : baseDNList) {
                    attributeList.addAll(getAttributeList(searchFilter, searchCtls, (String) tempBaseDN));
                }
            } else {
                attributeList.addAll(getAttributeList(searchFilter, searchCtls, searchBase));
            }
            // System.out.println("Total attrs: " + totalResults);
        } catch (Exception e) {
            MessageBoxExceptionHandler.process(e);
        }

        return attributeList;

    }

    /**
     * Administrator Comment method "getAttributeDetails".
     * 
     * @param searchFilter
     * @param attributeNameList
     * @param searchCtls
     * @param searchBase
     * @return
     * @throws NamingException
     */
    private static List<Attribute> getAttributeList(String searchFilter,
            javax.naming.directory.SearchControls searchCtls, String searchBase) throws NamingException {

        List<Attribute> attributeList = new ArrayList<Attribute>();
        // Search for objects using the filter
        javax.naming.NamingEnumeration answer = ctx.search(searchBase, searchFilter, searchCtls);
        // Loop through the search results
        while (answer.hasMoreElements()) {
            javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) answer.next();

            // System.out.println(">>>" + sr.getName());

            // Print out the groups

            Attributes attrs = sr.getAttributes();
            if (attrs != null) {

                try {
                    for (NamingEnumeration ae = attrs.getAll(); ae.hasMore();) {
                        Attribute attr = (Attribute) ae.next();
                        // System.out.println("Attribute: " + attr.getID());
                        if (attributeList.contains(attr) == false) {
                            attributeList.add(attr);
                        }
                        // int totalResults = 1;
                        // for (javax.naming.NamingEnumeration e = attr.getAll(); e.hasMore(); totalResults++) {
                        // System.out.println(" " + totalResults + ". " + e.next());
                        // }
                    }

                } catch (NamingException e) {
                    MessageBoxExceptionHandler.process(e);
                }
            }
        }
        return attributeList;
    }

    /**
     * Comment method "fetchBaseDNs".
     * 
     * @return
     */
    public static List<String> fetchBaseDNs() {
        if (ctx == null) {
            return null;
        }

        String searchBase = null;
        List<String> list = new ArrayList<String>();

        javax.naming.directory.Attributes namingContexts;
        try {
            namingContexts = ctx.getAttributes("", new String[] { "namingContexts" });

            javax.naming.directory.Attribute namingContextValue = namingContexts.get("namingContexts");

            if (namingContextValue == null) {
                namingContextValue = namingContexts.get("namingcontexts");
            }

            NamingEnumeration<Object> namingEnumeration = (NamingEnumeration<Object>) namingContextValue.getAll();

            while (namingEnumeration.hasMore()) {
                list.add(namingEnumeration.next().toString());
            }

            return list;

        } catch (NamingException e) {
            // MessageBoxExceptionHandler.process(e);
            return null;
        }
    }

    /**
     * Administrator Comment method "checkParam".
     * 
     * @param connection
     * @return
     */
    public static boolean checkParam(LDAPSchemaConnection connection) {
        String hostName = connection.getHost();
        String port = connection.getPort();
        String protocol = connection.getProtocol();

        String encryptionMethod = connection.getEncryptionMethodName();
        String userOrBindId = connection.getBindPrincipal();
        String password = connection.getBindPassword();

        String aliasesDereference = connection.getAliases();
        String referral = connection.getReferrals();

        boolean isAuthUsed = connection.isUseAuthen();

        env = new java.util.Hashtable<String, String>();
        env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, CONTEXT_FACTORY);
        if (protocol.equals(EAuthenticationMethod.SIMPLE.getName())) {
            // env.put(javax.naming.Context.SECURITY_AUTHENTICATION, protocol.equals(EAuthenticationMethod.SIMPLE
            // .getName()) ? "none" : "simple");// "none","simple","strong"
            env.put(javax.naming.Context.SECURITY_AUTHENTICATION, "simple");// 
        } else {
            env.put(javax.naming.Context.SECURITY_AUTHENTICATION, "none");
        }

        if (isAuthUsed && userOrBindId != null && userOrBindId.length() > 0) {
            env.put(javax.naming.Context.SECURITY_PRINCIPAL, userOrBindId);
        }
        if (isAuthUsed && password != null && password.length() > 0) {
            env.put(javax.naming.Context.SECURITY_CREDENTIALS, password);
        }

        if (aliasesDereference != null && aliasesDereference.length() > 0) {
            env.put("java.naming.ldap.derefAliases", aliasesDereference);
        }

        if (referral != null && referral.length() > 0) {
            env.put(javax.naming.Context.REFERRAL, referral);
        }

        String hostUrl = "";
        if (encryptionMethod.equals(EEncryptionMethod.SSL_ENCRYPTION_METHOD.getName())
                || (encryptionMethod.equals(EEncryptionMethod.STARTTSL_EXTENSION_METHOD.getName()))) {
            hostUrl = LDAPS_URL_PREFIX + hostName + ":" + port;
            env.put(javax.naming.Context.SECURITY_PROTOCOL, "ssl");
            env.put("java.naming.ldap.factory.socket", "org.talend.core.ldap.AdvancedSocketFactory");
        } else if (encryptionMethod.equals(EEncryptionMethod.NO_ENCRYPTION_METHOD.getName())) {
            hostUrl = LDAP_URL_PREFIX + hostName + ":" + port;
            env.remove(javax.naming.Context.SECURITY_PROTOCOL);
        }
        env.put(javax.naming.Context.PROVIDER_URL, hostUrl);

        try {
            ctx = new javax.naming.ldap.InitialLdapContext(env, null);
            if (encryptionMethod.equals(EEncryptionMethod.STARTTSL_EXTENSION_METHOD)) {
                javax.naming.ldap.StartTlsRequest tldsReq = new javax.naming.ldap.StartTlsRequest();
                javax.naming.ldap.StartTlsResponse tls = (javax.naming.ldap.StartTlsResponse) ctx
                        .extendedOperation(tldsReq);
                javax.net.ssl.SSLSession session = tls.negotiate((javax.net.ssl.SSLSocketFactory) AdvancedSocketFactory
                        .getDefault());
            }
            return true;
        } catch (Exception e) {
            // e.printStackTrace();
            return false;
        }
    }

}
