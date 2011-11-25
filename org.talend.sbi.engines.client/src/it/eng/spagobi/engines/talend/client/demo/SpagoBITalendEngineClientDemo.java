/**
 * 
 * SpagoBI - The Business Intelligence Free Platform
 * 
 * Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 */
package it.eng.spagobi.engines.talend.client.demo;

import it.eng.spagobi.engines.talend.client.ISpagoBITalendEngineClient;
import it.eng.spagobi.engines.talend.client.JobDeploymentDescriptor;
import it.eng.spagobi.engines.talend.client.SpagoBITalendEngineClient;
import it.eng.spagobi.engines.talend.client.exception.AuthenticationFailedException;
import it.eng.spagobi.engines.talend.client.exception.EngineUnavailableException;
import it.eng.spagobi.engines.talend.client.exception.ServiceInvocationFailedException;
import it.eng.spagobi.engines.talend.client.exception.UnsupportedEngineVersionException;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;

/**
 * @author Andrea Gioia
 * 
 */
public class SpagoBITalendEngineClientDemo {

    private static void usage() {
        System.out.println("cmdName usr pwd host port context file"); //$NON-NLS-1$
    }

    public static void main(String[] args) throws ZipException, IOException {

        if (args.length < 6) {
            usage();
            System.exit(1);
        }

        String user = args[0];
        String password = args[1];
        String host = args[2];
        String port = args[3];
        String applicationContext = args[4];
        String deploymentFile = args[5];

        try {

            // create the client
            ISpagoBITalendEngineClient client = new SpagoBITalendEngineClient(user, password, host, port,
                    applicationContext);

            // get some informations about the engine instance referenced by the client
            System.out.println("Engine version: " + client.getEngineVersion()); //$NON-NLS-1$
            System.out.println("Engine fullname: " + client.getEngineName()); //$NON-NLS-1$

            // prepare parameters used during deployment
            JobDeploymentDescriptor jobDeploymentDescriptor = new JobDeploymentDescriptor("Test", "perl"); //$NON-NLS-1$ //$NON-NLS-2$
            File zipFile = new File(deploymentFile);

            // deploy job on engine runtime
            boolean result = client.deployJob(jobDeploymentDescriptor, zipFile);
            if (result)
                System.out.println("Jobs deployed succesfully"); //$NON-NLS-1$
            else
                System.out.println("Jobs not deployed"); //$NON-NLS-1$

        } catch (EngineUnavailableException e) {
            System.err.println("ERRORE: " + e.getMessage()); //$NON-NLS-1$
        } catch (AuthenticationFailedException e) {
            System.err.println("ERRORE: " + e.getMessage()); //$NON-NLS-1$
        } catch (UnsupportedEngineVersionException e) {
            System.err.println("ERROR: Unsupported engine version"); //$NON-NLS-1$
            System.err.println("You are using TalendEngineClientAPI version " //$NON-NLS-1$
                    + SpagoBITalendEngineClient.CLIENTAPI_VERSION_NUMBER + ". " //$NON-NLS-1$
                    + "The TalendEngine instance you are trying to connect to require TalendEngineClientAPI version " //$NON-NLS-1$
                    + e.getComplianceVersion() + " or grater."); //$NON-NLS-1$
        } catch (ServiceInvocationFailedException e) {
            System.err.println("ERRORE: " + e.getMessage()); //$NON-NLS-1$
            System.err.println("StatusLine: " + e.getStatusLine() + "responseBody: " + e.getResponseBody()); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

}
