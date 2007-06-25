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
package org.talend.designer.core.model.process.statsandlogs;

import java.util.List;

import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.designer.core.model.components.ElementParameter;


/**
 * This class will create a virtual component that will create the logs for the job. It's not used at all in the
 * designer, only during the code generation. <br/>
 * 
 */
public class JobLogsComponent extends AbstractStatsLogsComponent {

    public JobLogsComponent(boolean useFile, boolean useConsole, String dbComponent) {
        if (dbComponent != null) {
            this.useDb = true;
            this.dbComponent = dbComponent;
        }
        this.useFile = useFile;
        this.useConsole = useConsole;
        this.componentId = "LOGS"; //$NON-NLS-1$
        this.subComponent = "tLogCatcher"; //$NON-NLS-1$

        loadMultipleComponentManager();
    }

    public String getVersion() {
        return "0.1"; //$NON-NLS-1$
    }
    
    /* (non-Javadoc)
     * @see org.talend.designer.core.model.process.statsandlogs.AbstractStatsLogsComponent#createElementParameters(org.talend.core.model.process.INode)
     */
    @Override
    public List<? extends IElementParameter> createElementParameters(INode node) {
        List<IElementParameter> paramList = (List<IElementParameter>) super.createElementParameters(node);
        
        IElementParameter newParam = new ElementParameter(node);
        newParam.setName("CATCH_RUNTIME_ERRORS"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.TEXT);
        newParam.setValue(Boolean.TRUE);
        paramList.add(newParam);
        
        newParam = new ElementParameter(node);
        newParam.setName("CATCH_USER_ERRORS"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.TEXT);
        newParam.setValue(Boolean.TRUE);
        paramList.add(newParam);
        
        newParam = new ElementParameter(node);
        newParam.setName("CATCH_USER_WARNING"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.TEXT);
        newParam.setValue(Boolean.FALSE);
        paramList.add(newParam);
        
        return paramList;
    }
    

    /* (non-Javadoc)
     * @see org.talend.designer.core.model.process.statsandlogs.AbstractStatsLogsComponent#createMultipleComponentsParameters()
     */
    @Override
    protected void createMultipleComponentsParameters() {
        super.createMultipleComponentsParameters();
        
        multipleComponentManager.addParam("self.CATCH_RUNTIME_ERRORS", "LOGS.CATCH_JAVA_EXCEPTION"); //$NON-NLS-1$ //$NON-NLS-2$
        multipleComponentManager.addParam("self.CATCH_RUNTIME_ERRORS", "LOGS.CATCH_PERL_DIE"); //$NON-NLS-1$ //$NON-NLS-2$
        multipleComponentManager.addParam("self.CATCH_USER_ERRORS", "LOGS.CATCH_TDIE"); //$NON-NLS-1$ //$NON-NLS-2$
        multipleComponentManager.addParam("self.CATCH_USER_WARNING", "LOGS.CATCH_TWARN"); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
