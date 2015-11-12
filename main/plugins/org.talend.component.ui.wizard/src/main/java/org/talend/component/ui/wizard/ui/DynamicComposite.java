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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.talend.component.core.constants.IElementParameterEventProperties;
import org.talend.component.core.model.GenericElementParameter;
import org.talend.component.core.utils.ComponentsUtils;
import org.talend.component.ui.wizard.i18n.Messages;
import org.talend.component.ui.wizard.model.FakeElement;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.properties.ValidationResult;
import org.talend.components.api.properties.ValidationResult.Result;
import org.talend.components.api.properties.presentation.Form;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
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
                GenericElementParameter genericElementParameter = (GenericElementParameter) parameter;
                genericElementParameter.callBefore();
                genericElementParameter.addPropertyChangeListener(this);
            }
        }
        parameters.add(getUpdateParameter());
        element.setElementParameters(parameters);
        return parameters;
    }

    public List<ElementParameter> resetElementParameters() {
        List<ElementParameter> oldParameters = (List<ElementParameter>) element.getElementParameters();
        final List<ElementParameter> newParameters = new ArrayList<ElementParameter>();
        ComponentProperties props = null;
        if (element instanceof INode) {
            INode node = (INode) element;
            if (node.getComponentProperties() != null) {
                props = node.getComponentProperties();
            }
        }
        List<ElementParameter> parameters = ComponentsUtils.getParametersFromForm(element, section, props, form, null, null);
        for (ElementParameter parameter : parameters) {
            if (parameter instanceof GenericElementParameter) {
                ((GenericElementParameter) parameter).addPropertyChangeListener(this);
            }
        }
        for (ElementParameter oldParameter : oldParameters) {
            if (EParameterName.UPDATE_COMPONENTS.getName().equals(oldParameter.getName())) {
                oldParameter.setValue(true);
            }
            boolean added = false;
            for (ElementParameter parameter : parameters) {
                if (oldParameter.getCategory() != null && oldParameter.getCategory().equals(parameter.getCategory())
                        && oldParameter.getName() != null && oldParameter.getName().equals(parameter.getName())) {
                    if (EParameterFieldType.SCHEMA_TYPE.equals(parameter.getFieldType())) {
                        if (parameter.getChildParameters().size() == 0) {
                            parameter.getChildParameters().putAll(oldParameter.getChildParameters());
                        }
                    }
                    newParameters.add(parameter);
                    added = true;
                }
            }
            if (!added) {
                newParameters.add(oldParameter);
            }
        }
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                if (newParameters.size() > 0) {
                    element.setElementParameters(newParameters);
                }
            }
        });

        return newParameters;
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
            checker.updateStatus(IStatus.WARNING, null);
            MessageDialog.openWarning(getShell(), Messages.getString("DynamicComposite.connectionTest.title"), validationMessage); //$NON-NLS-1$
            break;
        case ERROR:
            checker.updateStatus(IStatus.ERROR, null);
            MessageDialog.openError(getShell(), Messages.getString("DynamicComposite.connectionTest.title"), validationMessage); //$NON-NLS-1$
            break;
        default:
            checker.updateStatus(IStatus.OK, null);
            MessageDialog.openInformation(getShell(), Messages.getString("DynamicComposite.connectionTest.title"), //$NON-NLS-1$
                    Messages.getString("DynamicComposite.connectionTest.msg.success")); //$NON-NLS-1$
            break;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (IElementParameterEventProperties.EVENT_PROPERTY_VALUE_CHANGED.equals(event.getPropertyName())) {
            if (element instanceof FakeElement) {
                resetParameters();
            } else {
                resetElementParameters();
            }
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    refresh();
                }
            });
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

    @Override
    public synchronized void dispose() {
        List<? extends IElementParameter> elementParameters = element.getElementParameters();
        for (IElementParameter elementParameter : elementParameters) {
            if (elementParameter instanceof GenericElementParameter) {
                ((GenericElementParameter) elementParameter).removePropertyChangeListener(this);
            }
        }
        super.dispose();
    }

}
