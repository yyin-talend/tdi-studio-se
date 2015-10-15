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
package org.talend.component.ui.wizard.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.talend.component.core.constants.IElementParameterEventProperties;
import org.talend.component.core.model.GenericElementParameter;
import org.talend.component.core.utils.ComponentsUtils;
import org.talend.components.api.properties.ValidationResult;
import org.talend.components.api.properties.ValidationResult.Result;
import org.talend.components.api.properties.presentation.Form;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.ui.check.Checker;
import org.talend.core.ui.check.IChecker;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.ui.views.properties.MultipleThreadDynamicComposite;

/**
 * 
 * created by ycbai on 2015年9月24日 Detailled comment
 *
 */
public class DynamicComposite extends MultipleThreadDynamicComposite implements PropertyChangeListener {

    private Element element;

    private Form form;

    private IChecker checker;

    public DynamicComposite(Composite parentComposite, int styles, EComponentCategory section, Element element,
            boolean isCompactView, Color backgroundColor, Form form) {
        super(parentComposite, styles, section, element, isCompactView, backgroundColor);
        this.element = element;
        this.form = form;
        checker = new Checker();
    }

    public List<ElementParameter> resetParameters() {
        List<ElementParameter> parameters = ComponentsUtils.getParametersFromForm(element, null, form, null, null);
        for (ElementParameter parameter : parameters) {
            if (parameter instanceof GenericElementParameter) {
                ((GenericElementParameter) parameter).addPropertyChangeListener(this);
            }
        }
        parameters.add(getUpdateParameter());
        element.setElementParameters(parameters);
        return parameters;
    }

    private ElementParameter getUpdateParameter() {
        ElementParameter param = new ElementParameter(element);
        param.setName(EParameterName.UPDATE_COMPONENTS.getName());
        param.setValue(true);
        param.setDisplayName(EParameterName.UPDATE_COMPONENTS.getDisplayName());
        param.setFieldType(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setNumRow(1000);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(false);
        return param;
    }

    private void updateValidationStatus(ValidationResult validationResult) {
        String validationMessage = validationResult.getMessage();
        Result validationStatus = validationResult.getStatus();
        switch (validationStatus) {
        case WARNING:
            checker.updateStatus(IStatus.WARNING, validationMessage);
            break;
        case ERROR:
            checker.updateStatus(IStatus.ERROR, validationMessage);
            break;
        default:
            validationMessage = "Connection successful!";
            checker.updateStatus(IStatus.OK, validationMessage);
            break;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (IElementParameterEventProperties.EVENT_PROPERTY_VALUE_CHANGED.equals(event.getPropertyName())) {
            resetParameters();
            refresh();
        } else if (IElementParameterEventProperties.EVENT_VALIDATE_RESULT_UPDATE.equals(event.getPropertyName())) {
            Object newValue = event.getNewValue();
            if (newValue instanceof ValidationResult) {
                updateValidationStatus((ValidationResult) newValue);
            }
        } else if (IElementParameterEventProperties.EVENT_SHOW_DIALOG.equals(event.getPropertyName())) {
            Object newValue = event.getNewValue();
            if (newValue instanceof Form) {
                new GenericDialog(getShell(), (Form) newValue).open();
            }
        }
    }

    public IChecker getChecker() {
        return this.checker;
    }

}
