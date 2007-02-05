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
package org.talend.repository.model;

import java.io.IOException;

import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Status;
import org.talend.core.model.properties.helper.StatusHelper;
import org.talend.repository.i18n.Messages;

/**
 * DOC tguiu class global comment. Detailled comment Implementation for local repository remote implementation will
 * save/load content using methods on RepositoryFactory <br/>
 * 
 * $Id$
 * 
 */
public class RepositoryPreferenceStore implements IPersistentPreferenceStore {

    private String initialTechStatusList, techStatusList, defaultTechnicalStatusList;

    private String initialDocStatusList, docStatusList, defaultDocumentationStatusList;

    private final IProxyRepositoryFactory factory;

    /**
     * DOC tguiu RepositoryPreferenceStore constructor comment.
     */
    public RepositoryPreferenceStore(IProxyRepositoryFactory factory) {
        super();
        this.factory = factory;
    }

    public void save() throws IOException {
        try {
            factory.setDocumentationStatus(StatusHelper.parse(docStatusList));
            factory.setTechnicalStatus(StatusHelper.parse(techStatusList));
        } catch (PersistenceException e) {
            // TODO Auto-generated catch block
            throw new IOException(e.getMessage());
        }
    }

    public void load() throws PersistenceException {
        initialTechStatusList = StatusHelper.flat(factory.getTechnicalStatus());
        techStatusList = initialTechStatusList;
        defaultTechnicalStatusList = "DEV development;TEST testing;PROD production"; //$NON-NLS-1$
        initialDocStatusList = StatusHelper.flat(factory.getDocumentationStatus());
        docStatusList = initialDocStatusList;
        defaultDocumentationStatusList = "1ER 1er lecture;2EM 2eme lecture;VAL validée"; //$NON-NLS-1$
    }

    public void setValue(String name, String value) {
        if (Status.TECHNICAL_STATUS.equals(name)) {
            techStatusList = value;
        } else if (Status.DOCUMENTATION_STATUS.equals(name)) {
            docStatusList = value;
        }
    }

    public String getString(String name) {
        if (Status.TECHNICAL_STATUS.equals(name)) {
            return techStatusList;
        }
        if (Status.DOCUMENTATION_STATUS.equals(name)) {
            return docStatusList;
        }
        return null;
    }

    public void addPropertyChangeListener(IPropertyChangeListener listener) {
    }

    public boolean contains(String name) {
        return false;
    }

    public void firePropertyChangeEvent(String name, Object oldValue, Object newValue) {
    }

    public boolean getBoolean(String name) {
        return false;
    }

    public boolean getDefaultBoolean(String name) {
        return false;
    }

    public double getDefaultDouble(String name) {
        return 0;
    }

    public float getDefaultFloat(String name) {
        return 0;
    }

    public int getDefaultInt(String name) {
        return 0;
    }

    public long getDefaultLong(String name) {
        return 0;
    }

    public String getDefaultString(String name) {
        if (Status.TECHNICAL_STATUS.equals(name)) {
            return defaultTechnicalStatusList;
        } else if (Status.DOCUMENTATION_STATUS.equals(name)) {
            return defaultDocumentationStatusList;
        }
        return null;
    }

    public double getDouble(String name) {
        return 0;
    }

    public float getFloat(String name) {
        return 0;
    }

    public int getInt(String name) {
        return 0;
    }

    public long getLong(String name) {
        return 0;
    }

    public boolean isDefault(String name) {

        return false;
    }

    public boolean needsSaving() {
        return !(docStatusList.equals(initialDocStatusList) && techStatusList.equals(initialTechStatusList));
    }

    public void putValue(String name, String value) {
    }

    public void removePropertyChangeListener(IPropertyChangeListener listener) {
    }

    public void setDefault(String name, double value) {
    }

    public void setDefault(String name, float value) {
    }

    public void setDefault(String name, int value) {
    }

    public void setDefault(String name, long value) {
    }

    public void setDefault(String name, String value) {
        if (Status.TECHNICAL_STATUS.equals(name)) {
            defaultTechnicalStatusList = value;
        } else if (Status.DOCUMENTATION_STATUS.equals(name)) {
            defaultDocumentationStatusList = value;
        }
    }

    public void setDefault(String name, boolean value) {
    }

    public void setToDefault(String name) {
        if (Status.TECHNICAL_STATUS.equals(name)) {
            techStatusList = defaultTechnicalStatusList;
        } else if (Status.DOCUMENTATION_STATUS.equals(name)) {
            docStatusList = defaultDocumentationStatusList;
        }
    }

    public void setValue(String name, double value) {
    }

    public void setValue(String name, float value) {
    }

    public void setValue(String name, int value) {
    }

    public void setValue(String name, long value) {
    }

    public void setValue(String name, boolean value) {
    }

}
