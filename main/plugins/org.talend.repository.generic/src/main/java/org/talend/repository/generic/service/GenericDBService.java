// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.generic.service;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.talend.components.api.wizard.ComponentWizard;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.runtime.services.IGenericDBService;
import org.talend.daikon.properties.presentation.Form;
import org.talend.designer.core.generic.model.GenericElementParameter;
import org.talend.designer.core.generic.utils.ComponentsUtils;
import org.talend.designer.core.model.FakeElement;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.repository.generic.internal.IGenericWizardInternalService;
import org.talend.repository.generic.internal.service.GenericWizardInternalService;
import org.talend.repository.generic.model.genericMetadata.GenericConnectionItem;
import org.talend.repository.generic.model.genericMetadata.GenericMetadataFactory;
import org.talend.repository.generic.ui.DynamicComposite;
import org.talend.repository.generic.ui.context.ContextComposite;
import org.talend.repository.generic.ui.context.handler.GenericContextHandler;

/**
 * DOC hwang  class global comment. Detailled comment
 */
public class GenericDBService implements IGenericDBService{
    
    @Override
    public Composite creatDBDynamicComposite(Composite composite, EComponentCategory sectionCategory, boolean isCompactView,
            Property property, String typeName) {
        IGenericWizardInternalService internalService = new GenericWizardInternalService();
        ComponentWizard componentWizard = internalService.getComponentWizard(typeName, property.getId());
        List<Form> forms = componentWizard.getForms();
        Element baseElement = new FakeElement(forms.get(0).getName());
        DynamicComposite dynamicComposite = new DynamicComposite(composite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_FOCUS, EComponentCategory.BASIC,
                baseElement, true, composite.getBackground(), forms.get(0), false);
        dynamicComposite.setLayoutData(createMainFormData(true));
//        dynamicComposite.setWizardPropertyChangeListener(this);
        dynamicComposite.setConnectionItem((ConnectionItem)property.getItem());
//        addCheckListener(dynamicComposite.getChecker());

        Composite contextParentComp = new Composite(composite, SWT.NONE);
        contextParentComp.setLayoutData(createFooterFormData(dynamicComposite));
        contextParentComp.setLayout(new GridLayout());
        
        GenericContextHandler contextHandler = new GenericContextHandler();
        contextHandler.setParameters(getContextParameters(baseElement));
        ContextComposite contextComp = new ContextComposite(contextParentComp, (ConnectionItem)property.getItem(), true,
                contextHandler);
        
        contextComp.addPropertyChangeListener(dynamicComposite);
        contextComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        return dynamicComposite;
    }
    
    private List<IElementParameter> getContextParameters(Element element) {
        List<IElementParameter> contextParameters = new ArrayList<>();
        for (IElementParameter parameter : element.getElementParameters()) {
            if (parameter instanceof GenericElementParameter) {
                GenericElementParameter genericElementParameter = (GenericElementParameter) parameter;
                if (genericElementParameter.isSupportContext()) {
                    contextParameters.add(parameter);
                }
                List<ElementParameter> relatedParameters = ComponentsUtils.getRelatedParameters(genericElementParameter);
                for (ElementParameter relatedParameter : relatedParameters) {
                    if (relatedParameter instanceof GenericElementParameter
                            && ((GenericElementParameter) relatedParameter).isSupportContext()) {
                        contextParameters.add(relatedParameter);
                    }
                }
            }
        }
        return contextParameters;
    }
    
    private FormData createMainFormData(boolean addContextSupport) {
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        if (addContextSupport) {
            data.bottom = new FormAttachment(85, 0);
        } else {
            data.bottom = new FormAttachment(100, 0);
        }
        return data;
    }

    private FormData createFooterFormData(Composite topComposite) {
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(topComposite, 0);
        data.bottom = new FormAttachment(100, 0);
        return data;
    }
    
    @Override
    public Connection createGenericConnection() {
        return GenericMetadataFactory.eINSTANCE.createGenericConnection();
    }

    @Override
    public ConnectionItem createGenericConnectionItem() {
        return GenericMetadataFactory.eINSTANCE.createGenericConnectionItem();
    }

    @Override
    public String getGenericConnectionType(Item item) {
        if(item instanceof GenericConnectionItem){
            return ((GenericConnectionItem)item).getTypeName();
        }
        return null;
    }

    @Override
    public void setGenericConnectionType(String type, Item item) {
        if(item instanceof GenericConnectionItem){
            ((GenericConnectionItem)item).setTypeName(type);
        }
    }

}
