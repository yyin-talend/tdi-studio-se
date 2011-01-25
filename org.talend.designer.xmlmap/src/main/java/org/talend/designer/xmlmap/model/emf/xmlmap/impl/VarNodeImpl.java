/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.designer.xmlmap.model.emf.xmlmap.impl;

import org.eclipse.emf.common.notify.Notification;
import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.talend.designer.xmlmap.model.emf.xmlmap.Connection;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Var Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.impl.VarNodeImpl#getVariable <em>Variable</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VarNodeImpl extends AbstractNodeImpl implements VarNode {
    /**
     * The default value of the '{@link #getVariable() <em>Variable</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVariable()
     * @generated
     * @ordered
     */
    protected static final String VARIABLE_EDEFAULT = null;
    /**
     * The cached value of the '{@link #getVariable() <em>Variable</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVariable()
     * @generated
     * @ordered
     */
    protected String variable = VARIABLE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected VarNodeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return XmlmapPackage.Literals.VAR_NODE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getVariable() {
        return variable;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setVariable(String newVariable) {
        String oldVariable = variable;
        variable = newVariable;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XmlmapPackage.VAR_NODE__VARIABLE, oldVariable, variable));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case XmlmapPackage.VAR_NODE__VARIABLE:
                return getVariable();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case XmlmapPackage.VAR_NODE__VARIABLE:
                setVariable((String)newValue);
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
            case XmlmapPackage.VAR_NODE__VARIABLE:
                setVariable(VARIABLE_EDEFAULT);
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
            case XmlmapPackage.VAR_NODE__VARIABLE:
                return VARIABLE_EDEFAULT == null ? variable != null : !VARIABLE_EDEFAULT.equals(variable);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (variable: ");
        result.append(variable);
        result.append(')');
        return result.toString();
    }

} //VarNodeImpl
