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
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.component.core.constants.IElementParameterEventProperties;
import org.talend.components.api.NamedThing;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.properties.PresentationItem;
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

    private List<?> possibleValues;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private boolean isFirstCall;

    public GenericElementParameter(IElement element, ComponentProperties componentProperties, Widget widget,
            ComponentService componentService) {
        super(element);
        this.componentProperties = componentProperties;
        this.widget = widget;
        this.componentService = componentService;
        isFirstCall = true;

        NamedThing[] widgetProperties = widget.getProperties();
        NamedThing widgetProperty = widgetProperties[0];
        setName(widgetProperty.getName());
        setDisplayName(widgetProperty.getDisplayName());
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
            boolean calledValidate = callValidate();
            if (calledValidate) {
                fireValidateStatusEvent();
            }
            boolean calledAfter = callAfter();
            if (calledAfter) {
                fireValueChangedEvent();
            }
        }
        isFirstCall = false;
    }

    private void updateProperty(Object newValue) {
        NamedThing[] widgetProperties = widget.getProperties();
        NamedThing widgetProperty = widgetProperties[0];
        if (widgetProperty instanceof SchemaElement) {
            SchemaElement se = (SchemaElement) widgetProperty;
            componentProperties.setValue(se, newValue);
        } else if (widgetProperty instanceof PresentationItem) {
            PresentationItem pi = (PresentationItem) widgetProperty;
            Form formtoShow = pi.getFormtoShow();
            if (formtoShow != null) {
                fireShowDialogEvent(formtoShow);
            }
        }
    }

    private void fireValidateStatusEvent() {
        this.pcs.firePropertyChange(IElementParameterEventProperties.EVENT_VALIDATE_RESULT_UPDATE, null,
                componentProperties.getValidationResult());
    }

    private void fireValueChangedEvent() {
        List<Form> forms = componentProperties.getForms();
        for (Form form : forms) {
            if (form.isRefreshUI()) {
                this.pcs.firePropertyChange(IElementParameterEventProperties.EVENT_PROPERTY_VALUE_CHANGED, null, null);
                return;
            }
        }
    }

    private void fireShowDialogEvent(Form formToDisplay) {
        this.pcs.firePropertyChange(IElementParameterEventProperties.EVENT_SHOW_DIALOG, null, formToDisplay);
    }

    private boolean callBefore() {
        if (widget.isCallBefore()) {
            try {
                componentProperties = componentService.beforeProperty(getName(), componentProperties);
                return true;
            } catch (Throwable e) {
                ExceptionHandler.process(e);
            }
        }
        return false;
    }

    private boolean callValidate() {
        if (widget.isCallValidate()) {
            if (widget.isLongRunning()) {
                return new RunWithProgress(widget.getProperties()[0].getDisplayName()) {

                    @Override
                    protected void toDo() throws Throwable {
                        componentProperties = componentService.validateProperty(getName(), componentProperties);
                    };

                }.run();
            } else {
                try {
                    componentProperties = componentService.validateProperty(getName(), componentProperties);
                    return true;
                } catch (Throwable e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        return false;
    }

    private boolean callAfter() {
        if (widget.isCallAfter()) {
            try {
                componentProperties = componentService.afterProperty(getName(), componentProperties);
                return true;
            } catch (Throwable e) {
                ExceptionHandler.process(e);
            }
        }
        return false;
    }

    abstract class RunWithProgress {

        private String taskName;

        public RunWithProgress(String taskName) {
            this.taskName = taskName;
        }

        public boolean run() {
            final AtomicBoolean result = new AtomicBoolean();
            IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {

                @Override
                public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    monitor.beginTask(taskName, IProgressMonitor.UNKNOWN);
                    try {
                        toDo();
                    } catch (Throwable e) {
                        result.set(false);
                        throw new InvocationTargetException(e);
                    }
                    result.set(true);
                }
            };
            ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
            try {
                dialog.run(true, true, runnableWithProgress);
            } catch (Exception e) {
                result.set(false);
                ExceptionHandler.process(e);
            }
            return result.get();
        }

        protected abstract void toDo() throws Throwable;

    }

    public List<?> getPossibleValues() {
        return this.possibleValues;
    }

    public void setPossibleValues(List<?> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public Widget getWidget() {
        return this.widget;
    }

}
