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
package org.talend.repository.ui.login.connections;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.dom4j.DocumentException;
import org.eclipse.core.runtime.adaptor.LocationManager;
import org.talend.core.model.general.ConnectionBean;

/**
 * DOC teileizeget class global comment. Detailled comment
 */
public class ConnectionUserPerReader {

    private static ConnectionUserPerReader con = new ConnectionUserPerReader();

    private String perfileName = "connection_user.properties";

    private String path = null;

    private File perfile = null;

    private Properties proper = null;

    private boolean isRead;

    private ConnectionUserPerReader() {
        proper = new Properties();
        isRead = false;
        String tmp = LocationManager.getConfigurationLocation().getURL().getPath();
        path = tmp.substring(tmp.indexOf("/") + 1, tmp.length());//$NON-NLS-1$
        perfile = new File(path + perfileName);
    }

    public static ConnectionUserPerReader getInstance() {
        synchronized (con) {
            if (con == null)
                con = new ConnectionUserPerReader();
            return con;
        }
    }

    /**
     * connetion.users=user1|user2|user3|user4 user1=local#Local#Default connection####false
     * 
     * 
     * DOC teileizeget Comment method "readConnections".
     * 
     * @return
     * @throws DocumentException
     */
    public List<ConnectionBean> readConnections() {
        if (!isRead)
            this.readProperties();
        String userString = proper.getProperty("connection.users");
        if (userString == null) {
            userString = "";//$NON-NLS-1$
            return new ArrayList<ConnectionBean>(0);
        } else {
            String[] users = userString.split("\\|");//$NON-NLS-1$
            List<ConnectionBean> toReturn = new ArrayList<ConnectionBean>(users.length);
            for (String usr : users) {
                ConnectionBean conBean = ConnectionBean.writeFromString(proper.getProperty(usr));
                toReturn.add(conBean);
            }
            return toReturn;
        }

    }

    private ConnectionUserPerReader readProperties() {
        try {
            proper.load(new FileInputStream(perfile));
            isRead = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String readLastConncetion() {
        if (!isRead)
            this.readProperties();
        String tmp = proper.getProperty("connection.lastConnection");
        if (tmp == null) {
            tmp = "";//$NON-NLS-1$
        }
        return tmp;
    }

    public String readLastWorkSpace() {
        if (!isRead)
            this.readProperties();
        String workSpace = proper.getProperty("connection.lastWorkSpace");

        if (workSpace == null) {
            return "";//$NON-NLS-1$
        }
        return workSpace;
    }

    // public boolean haveWorkSpace() {
    // return proper.containsKey("connection.lastWorkSpace");
    // }

    public void saveConnections(List<ConnectionBean> cons) {
        if (!isHaveUserPer())
            createPropertyFile();
        if (!isRead)
            this.readProperties();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (ConnectionBean currentConnection : cons) {
            String userName = currentConnection.getName();

            if (i != 0 && userName != null) {
                sb.append("|");//$NON-NLS-1$
            }
            if (userName != null)
                sb.append(userName);
            proper.setProperty(userName, currentConnection.readToString());

            i++;
        }
        proper.setProperty("connection.users", sb.toString());//$NON-NLS-1$
        try {

            FileOutputStream out = new FileOutputStream(perfile);
            proper.store(out, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveLastConnectionBean(ConnectionBean bean) {
        if (!isHaveUserPer())
            createPropertyFile();
        if (!isRead)
            this.readProperties();
        String userName = bean.getName();
        String workSpace = bean.getWorkSpace();
        proper.setProperty("connection.lastConnection", userName);//$NON-NLS-1$
        proper.setProperty("connection.lastWorkSpace", workSpace);//$NON-NLS-1$
        FileOutputStream out;
        try {
            out = new FileOutputStream(perfile);
            proper.store(out, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createPropertyFile() {
        File fatherFloder = new File(path);
        if (!fatherFloder.exists()) {
            fatherFloder.mkdirs();
        }
        try {
            if (!perfile.exists()) {
                perfile.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isHaveUserPer() {
        return perfile.exists();
    }
}
