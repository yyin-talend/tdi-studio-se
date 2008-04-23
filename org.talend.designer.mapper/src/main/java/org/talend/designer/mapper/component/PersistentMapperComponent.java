// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.mapper.component;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.core.model.components.EComponentType;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IMultipleComponentManager;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.INodeReturn;
import org.talend.core.model.temp.ECodePart;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class PersistentMapperComponent implements IComponent {

    IComponent baseComponent;

    /**
     * Create a component based on the emf component. <br>
     * Only change should be the function getMultipleComponentManagers
     */
    public PersistentMapperComponent(IComponent component) {
        baseComponent = component;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getMultipleComponentManagers()
     */
    public List<IMultipleComponentManager> getMultipleComponentManagers() {

        // MultipleComponentManager mcm = new MultipleComponentManager("input", "output");

        return baseComponent.getMultipleComponentManagers();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#createConnectors()
     */
    public List<? extends INodeConnector> createConnectors() {
        return baseComponent.createConnectors();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#createElementParameters(org.talend.core.model.process.INode)
     */
    public List<? extends IElementParameter> createElementParameters(INode node) {
        return baseComponent.createElementParameters(node);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#createReturns()
     */
    public List<? extends INodeReturn> createReturns() {
        return baseComponent.createReturns();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getAvailableCodeParts()
     */
    public List<ECodePart> getAvailableCodeParts() {
        return baseComponent.getAvailableCodeParts();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getComponentType()
     */
    public EComponentType getComponentType() {
        return baseComponent.getComponentType();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getFamily()
     */
    public String getFamily() {
        return baseComponent.getFamily();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getIcon16()
     */
    public ImageDescriptor getIcon16() {
        return baseComponent.getIcon16();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getIcon24()
     */
    public ImageDescriptor getIcon24() {
        return baseComponent.getIcon24();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getIcon32()
     */
    public ImageDescriptor getIcon32() {
        return baseComponent.getIcon32();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getLongName()
     */
    public String getLongName() {
        return baseComponent.getLongName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getModulesNeeded()
     */
    public List<ModuleNeeded> getModulesNeeded() {
        return baseComponent.getModulesNeeded();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getName()
     */
    public String getName() {
        return baseComponent.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getPathSource()
     */
    public String getPathSource() {
        return baseComponent.getPathSource();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getPluginDependencies()
     */
    public List<String> getPluginDependencies() {
        return baseComponent.getPluginDependencies();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getPluginFullName()
     */
    public String getPluginFullName() {
        return baseComponent.getPluginFullName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getTranslatedName()
     */
    public String getTranslatedName() {
        return baseComponent.getTranslatedName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getVersion()
     */
    public String getVersion() {
        return baseComponent.getVersion();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#hasConditionalOutputs()
     */
    public Boolean hasConditionalOutputs() {
        return baseComponent.hasConditionalOutputs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#isDataAutoPropagated()
     */
    public boolean isDataAutoPropagated() {
        return baseComponent.isDataAutoPropagated();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#isHashComponent()
     */
    public boolean isHashComponent() {
        return baseComponent.isHashComponent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#isLoaded()
     */
    public boolean isLoaded() {
        return baseComponent.isLoaded();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#isMultipleOutput()
     */
    public boolean isMultipleOutput() {
        return baseComponent.isMultipleOutput();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#isMultiplyingOutputs()
     */
    public Boolean isMultiplyingOutputs() {
        return baseComponent.isMultiplyingOutputs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#isSchemaAutoPropagated()
     */
    public boolean isSchemaAutoPropagated() {
        return baseComponent.isSchemaAutoPropagated();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#isVisible()
     */
    public boolean isVisible() {
        return baseComponent.isVisible();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#setIcon16(org.eclipse.jface.resource.ImageDescriptor)
     */
    public void setIcon16(ImageDescriptor icon16) {
        baseComponent.setIcon16(icon16);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#setIcon24(org.eclipse.jface.resource.ImageDescriptor)
     */
    public void setIcon24(ImageDescriptor icon24) {
        baseComponent.setIcon24(icon24);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#setIcon32(org.eclipse.jface.resource.ImageDescriptor)
     */
    public void setIcon32(ImageDescriptor icon32) {
        baseComponent.setIcon32(icon32);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#useImport()
     */
    public boolean useImport() {
        return baseComponent.useImport();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#useLookup()
     */
    public boolean useLookup() {
        return baseComponent.useLookup();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#useMerge()
     */
    public boolean useMerge() {
        return baseComponent.useMerge();
    }

    public IComponent getBaseComponent() {
        return this.baseComponent;
    }

}
