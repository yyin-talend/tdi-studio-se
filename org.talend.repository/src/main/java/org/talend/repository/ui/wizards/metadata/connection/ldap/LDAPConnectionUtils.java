// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards.metadata.connection.ldap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.ldap.InitialLdapContext;

import org.apache.directory.studio.ldapbrowser.core.model.schema.ObjectClassDescription;
import org.apache.directory.studio.ldapbrowser.core.model.schema.Schema;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.ExceptionHandler;
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

    private static final String LDAP_URL_PREFIX = "ldap://"; //$NON-NLS-1$

    private static final String LDAPS_URL_PREFIX = "ldaps://"; //$NON-NLS-1$

    private static final String CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory"; //$NON-NLS-1$

    private static InitialLdapContext ctx = null;

    // Used for customed schema attributes.
    private static List<String> allAttributes;

    /**
     * Comment method "getAttributes".
     * 
     * @return
     */
    // public static List<Attribute> getAttributes(LDAPSchemaConnection connection) {
    public static Object[] getAttributes(LDAPSchemaConnection connection) {
        if (ctx == null) {
            return null;
        }

        String baseDN = connection.getSelectedDN();
        EList baseDNs = connection.getBaseDNs();
        String searchFilter = connection.getFilter();
        int timeLimit = (Integer.valueOf(connection.getTimeOutLimit()).intValue());
        EList returnAttributeList = connection.getReturnAttributes();
        List<String> allObjectClassList = new ArrayList<String>();

        int size = returnAttributeList.size();

        String[] returnAttributes = (String[]) returnAttributeList.toArray(new String[size]);

        try {
            javax.naming.directory.SearchControls searchCtls = new javax.naming.directory.SearchControls();
            searchCtls.setSearchScope(javax.naming.directory.SearchControls.SUBTREE_SCOPE);

            javax.naming.directory.Attributes namingContexts = ctx.getAttributes("", new String[] { "namingContexts" }); //$NON-NLS-1$ //$NON-NLS-2$
            javax.naming.directory.Attribute namingContextValue = namingContexts.get("namingContexts"); //$NON-NLS-1$
            if (namingContextValue == null) {
                namingContextValue = namingContexts.get("namingcontexts"); //$NON-NLS-1$
            }
            String searchBase = null;
            if (baseDN == null) {
                searchBase = ((namingContextValue == null) || ((namingContextValue != null)
                        && (namingContextValue.get().toString() != null) && (namingContextValue.get().toString().length() > 0) && (Character
                        .isIdentifierIgnorable(namingContextValue.get().toString().charAt(0))))) ? "" : namingContextValue.get() //$NON-NLS-1$
                        .toString();
            } else {
                searchBase = baseDN;
            }

            if (searchFilter == null) {

                searchFilter = ConnectionUIConstants.DEFAULT_FILTER;
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
                    allObjectClassList.addAll(getAttributeList(searchFilter, searchCtls, (String) tempBaseDN));
                }
            } else {
                allObjectClassList.addAll(getAttributeList(searchFilter, searchCtls, searchBase));
            }
            // System.out.println("Total attrs: " + totalResults);
        } catch (Exception e) {
            MessageBoxExceptionHandler.process(e);
            connection.setFilter(ConnectionUIConstants.DEFAULT_FILTER);
            return null;
        }

        Schema defaultSchema = new Schema().DEFAULT_SCHEMA;

        Set<String> attributeSet = new TreeSet<String>();

        for (String objectClassName : allObjectClassList) {
            ObjectClassDescription objectClassDescription = defaultSchema.getObjectClassDescription(objectClassName);

            String[] mustAttributeTypeDescriptionNames = objectClassDescription.getMustAttributeTypeDescriptionNames();
            String[] mayAttributeTypeDescriptionNames = objectClassDescription.getMayAttributeTypeDescriptionNames();

            for (String string : mustAttributeTypeDescriptionNames) {
                attributeSet.add(string);
            }
            for (String string2 : mayAttributeTypeDescriptionNames) {
                attributeSet.add(string2);
            }
        }

        for (String attribute : allAttributes) {
            if (!attributeSet.contains(attribute)) {
                attributeSet.add(attribute);
            }
        }

        Object[] array = (Object[]) attributeSet.toArray();

        return array;
        // return attributeList;

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
    private static List<String> getAttributeList(String searchFilter, javax.naming.directory.SearchControls searchCtls,
            String searchBase) throws NamingException {

        allAttributes = new ArrayList<String>();

        List<String> objectClassList = new ArrayList<String>();
        javax.naming.NamingEnumeration answer = ctx.search(searchBase, searchFilter, searchCtls);

        // Loop through the search results
        while (answer.hasMoreElements()) {
            javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) answer.next();

            Attributes attrs = sr.getAttributes();

            if (attrs != null) {
                for (NamingEnumeration ae = attrs.getAll(); ae.hasMore();) {
                    Attribute attr = (Attribute) ae.next();
                    String id = attr.getID();
                    if (id.equals("objectClass")) { //$NON-NLS-1$
                        NamingEnumeration<?> all = attr.getAll();
                        while (all.hasMore()) {
                            String next = (String) all.next();
                            if (!objectClassList.contains(next)) {
                                objectClassList.add(next);
                            }
                        }
                    } else {
                        allAttributes.add(id);
                    }
                }
            }
        }
        return objectClassList;
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
            namingContexts = ctx.getAttributes("", new String[] { "namingContexts" }); //$NON-NLS-1$ //$NON-NLS-2$

            javax.naming.directory.Attribute namingContextValue = namingContexts.get("namingContexts"); //$NON-NLS-1$

            if (namingContextValue == null) {
                namingContextValue = namingContexts.get("namingcontexts"); //$NON-NLS-1$
            }

            NamingEnumeration<Object> namingEnumeration = (NamingEnumeration<Object>) namingContextValue.getAll();

            while (namingEnumeration.hasMore()) {
                list.add(namingEnumeration.next().toString());
            }

            return list;

        } catch (NamingException e) {
            MessageBoxExceptionHandler.process(e);
            return null;
        }
    }

    /**
     * Administrator Comment method "checkParam".
     * 
     * @param connection
     * @return
     */
    public static boolean checkParam(LDAPSchemaConnection connection, boolean isStep1Check) {
        String hostName = connection.getHost();
        String port = connection.getPort();
        String protocol = connection.getProtocol();

        if (isStep1Check) {
            protocol = EAuthenticationMethod.SIMPLE.getName();
        }

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
            env.put(javax.naming.Context.SECURITY_AUTHENTICATION, "simple");//  //$NON-NLS-1$
        } else {
            env.put(javax.naming.Context.SECURITY_AUTHENTICATION, "none"); //$NON-NLS-1$
        }

        if (isAuthUsed && userOrBindId != null && userOrBindId.length() > 0) {
            env.put(javax.naming.Context.SECURITY_PRINCIPAL, userOrBindId);
        }
        if (isAuthUsed && password != null && password.length() > 0) {
            env.put(javax.naming.Context.SECURITY_CREDENTIALS, password);
        }

        if (aliasesDereference != null && aliasesDereference.length() > 0) {
            env.put("java.naming.ldap.derefAliases", aliasesDereference); //$NON-NLS-1$
        }

        if (referral != null && referral.length() > 0) {
            env.put(javax.naming.Context.REFERRAL, referral);
        }

        String hostUrl = ""; //$NON-NLS-1$
        if (encryptionMethod.equals(EEncryptionMethod.SSL_ENCRYPTION_METHOD.getName())
                || (encryptionMethod.equals(EEncryptionMethod.STARTTSL_EXTENSION_METHOD.getName()))) {
            hostUrl = LDAPS_URL_PREFIX + hostName + ":" + port; //$NON-NLS-1$
            env.put(javax.naming.Context.SECURITY_PROTOCOL, "ssl"); //$NON-NLS-1$
            env.put("java.naming.ldap.factory.socket", "org.talend.core.ldap.AdvancedSocketFactory"); //$NON-NLS-1$ //$NON-NLS-2$
        } else if (encryptionMethod.equals(EEncryptionMethod.NO_ENCRYPTION_METHOD.getName())) {
            hostUrl = LDAP_URL_PREFIX + hostName + ":" + port; //$NON-NLS-1$
            env.remove(javax.naming.Context.SECURITY_PROTOCOL);
        }
        env.put(javax.naming.Context.PROVIDER_URL, hostUrl);

        try {
            ctx = new javax.naming.ldap.InitialLdapContext(env, null);
            if (encryptionMethod.equals(EEncryptionMethod.STARTTSL_EXTENSION_METHOD)) {
                javax.naming.ldap.StartTlsRequest tldsReq = new javax.naming.ldap.StartTlsRequest();
                javax.naming.ldap.StartTlsResponse tls = (javax.naming.ldap.StartTlsResponse) ctx.extendedOperation(tldsReq);
                javax.net.ssl.SSLSession session = tls.negotiate((javax.net.ssl.SSLSocketFactory) AdvancedSocketFactory
                        .getDefault());
            }

            return true;
        } catch (Exception e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
            return false;
        }
    }

}
