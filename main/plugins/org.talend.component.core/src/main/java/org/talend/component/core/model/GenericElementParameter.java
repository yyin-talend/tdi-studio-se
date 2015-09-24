// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.component.core.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.component.core.constants.IElementParameterEventProperties;
import org.talend.components.api.NamedThing;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.properties.presentation.Form;
import org.talend.components.api.properties.presentation.Widget;
import org.talend.components.api.schema.SchemaElement;
import org.talend.components.api.service.ComponentService;
import org.talend.core.model.process.IElement;
import org.talend.designer.core.model.components.ElementParameter;

/**
 * created by ycbai on 2015年9月24日 Detailled comment
 *
 */
public class GenericElementParameter extends ElementParameter {

    private ComponentProperties componentProperties;

    private Widget widget;

    private ComponentService componentService;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private boolean isFirstCall;

    public GenericElementParameter(IElement element, ComponentProperties componentProperties, Widget widget,
            ComponentService componentService) {
        super(element);
        this.componentProperties = componentProperties;
        this.widget = widget;
        this.componentService = componentService;
        isFirstCall = true;
        callBefore();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    @Override
    public void setValue(Object o) {
        super.setValue(o);
        if (!isFirstCall) {
            updateProperty(o);
            callValidate();
            callAfter();
            fireChangedIfNeeded();
        }
        isFirstCall = false;
    }

    private void updateProperty(Object newValue) {
        NamedThing[] widgetProperties = widget.getProperties();
        NamedThing widgetProperty = widgetProperties[0];
        if (widgetProperty instanceof SchemaElement) {
            SchemaElement se = (SchemaElement) widgetProperty;
            componentProperties.setValue(se, newValue);
        }
    }

    private void fireChangedIfNeeded() {
        List<Form> forms = componentProperties.getForms();
        for (Form form : forms) {
            if (form.isRefreshUI()) {
                this.pcs.firePropertyChange(IElementParameterEventProperties.EVENT_PROPERTY_VALUE_CHANGED, null, null);
                return;
            }
        }
    }

    private void callBefore() {
        if (widget.isCallBefore()) {
            try {
                componentProperties = componentService.beforeProperty(getName(), componentProperties);
            } catch (Throwable e) {
                ExceptionHandler.process(e);
            }
        }
    }

    private void callValidate() {
        if (widget.isCallValidate()) {
            try {
                componentProperties = componentService.validateProperty(getName(), componentProperties);
            } catch (Throwable e) {
                ExceptionHandler.process(e);
            }
        }
    }

    private void callAfter() {
        if (widget.isCallAfter()) {
            try {
                componentProperties = componentService.afterProperty(getName(), componentProperties);
            } catch (Throwable e) {
                ExceptionHandler.process(e);
            }
        }
    }

}
