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
package org.talend.repository.ui.wizards.exportjob;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.properties.ProcessItem;

/**
 * 
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 * $Id: ExportFileResource.java 1 2006-09-29 17:06:40 +0000 (星期五, 29 九月 2006) nrousseau $
 * 
 */
/**
 * DOC   class global comment. Detailled comment
 * <br/>
 *
 * $Id: ExportFileResource.java 1 2007-1-30下午05:16:53 +0000  ylv $
 *
 */
public class ExportFileResource {

    private ProcessItem process;

    private List<URL> processResouces = new ArrayList<URL>();

    private String directoryName;

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public List<URL> getProcessResouces() {
        return processResouces;
    }

    public void setProcessResouces(List<URL> resouces) {
        this.processResouces = resouces;
    }

    
    public ProcessItem getProcess() {
        return process;
    }

    
    public void setProcess(ProcessItem process) {
        this.process = process;
    }

    public ExportFileResource(ProcessItem process, String directoryName) {
        super();
        this.process = process;
        this.directoryName = directoryName;
    }

}
