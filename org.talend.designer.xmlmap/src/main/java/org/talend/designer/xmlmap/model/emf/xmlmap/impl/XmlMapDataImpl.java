/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.designer.xmlmap.model.emf.xmlmap.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.talend.designer.core.model.utils.emf.talendfile.impl.AbstractExternalDataImpl;

import org.talend.designer.xmlmap.model.emf.xmlmap.Connection;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Xml Map Data</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.impl.XmlMapDataImpl#getInputTrees <em>Input Trees</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.impl.XmlMapDataImpl#getOutputTrees <em>Output Trees</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class XmlMapDataImpl extends AbstractExternalDataImpl implements XmlMapData {
    /**
     * The cached value of the '{@link #getInputTrees() <em>Input Trees</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInputTrees()
     * @generated
     * @ordered
     */
    protected EList<InputXmlTree> inputTrees;

    /**
     * The cached value of the '{@link #getOutputTrees() <em>Output Trees</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputTrees()
     * @generated
     * @ordered
     */
    protected EList<OutputXmlTree> outputTrees;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected XmlMapDataImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return XmlmapPackage.Literals.XML_MAP_DATA;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<InputXmlTree> getInputTrees() {
        if (inputTrees == null) {
            inputTrees = new EObjectContainmentEList<InputXmlTree>(InputXmlTree.class, this, XmlmapPackage.XML_MAP_DATA__INPUT_TREES);
        }
        return inputTrees;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<OutputXmlTree> getOutputTrees() {
        if (outputTrees == null) {
            outputTrees = new EObjectContainmentEList<OutputXmlTree>(OutputXmlTree.class, this, XmlmapPackage.XML_MAP_DATA__OUTPUT_TREES);
        }
        return outputTrees;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case XmlmapPackage.XML_MAP_DATA__INPUT_TREES:
                return ((InternalEList<?>)getInputTrees()).basicRemove(otherEnd, msgs);
            case XmlmapPackage.XML_MAP_DATA__OUTPUT_TREES:
                return ((InternalEList<?>)getOutputTrees()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case XmlmapPackage.XML_MAP_DATA__INPUT_TREES:
                return getInputTrees();
            case XmlmapPackage.XML_MAP_DATA__OUTPUT_TREES:
                return getOutputTrees();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case XmlmapPackage.XML_MAP_DATA__INPUT_TREES:
                getInputTrees().clear();
                getInputTrees().addAll((Collection<? extends InputXmlTree>)newValue);
                return;
            case XmlmapPackage.XML_MAP_DATA__OUTPUT_TREES:
                getOutputTrees().clear();
                getOutputTrees().addAll((Collection<? extends OutputXmlTree>)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case XmlmapPackage.XML_MAP_DATA__INPUT_TREES:
                getInputTrees().clear();
                return;
            case XmlmapPackage.XML_MAP_DATA__OUTPUT_TREES:
                getOutputTrees().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case XmlmapPackage.XML_MAP_DATA__INPUT_TREES:
                return inputTrees != null && !inputTrees.isEmpty();
            case XmlmapPackage.XML_MAP_DATA__OUTPUT_TREES:
                return outputTrees != null && !outputTrees.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //XmlMapDataImpl
