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
package org.talend.sbi.engines.client.ui.wizards;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.talend.core.model.properties.ProcessItem;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 */
public class JobJavaScriptsManager extends org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobJavaScriptsManager {

    @Override
    protected List<URL> getLauncher(boolean needLauncher, ProcessItem process, String contextName, String environment,
            int statisticPort, int tracePort, String... codeOptions) {
        String tmpFold = getTmpFolder();
        File fileTemp = new File(tmpFold);
        if (!fileTemp.exists()) {
            fileTemp.mkdir();
        }
        List<URL> toReturn = super.getLauncher(needLauncher, process, contextName, environment, statisticPort, tracePort,
                codeOptions);
        // File spagobi = new File(tmpFold + "/" + "spagobi.xml");
        try {
            toReturn.add(new GenerateSpagoBIXML(fileTemp, process, contextName).getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }

}
