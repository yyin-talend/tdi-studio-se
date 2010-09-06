/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.designer.mapper.model.emf.mapper.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.talend.designer.mapper.model.emf.mapper.InputTable;
import org.talend.designer.mapper.model.emf.mapper.MapperPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Input Table</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.designer.mapper.model.emf.mapper.impl.InputTableImpl#getMatchingMode <em>Matching Mode</em>}</li>
 *   <li>{@link org.talend.designer.mapper.model.emf.mapper.impl.InputTableImpl#getLookupMode <em>Lookup Mode</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InputTableImpl extends AbstractInOutTableImpl implements InputTable {
    /**
     * The default value of the '{@link #getMatchingMode() <em>Matching Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMatchingMode()
     * @generated
     * @ordered
     */
    protected static final String MATCHING_MODE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMatchingMode() <em>Matching Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMatchingMode()
     * @generated
     * @ordered
     */
    protected String matchingMode = MATCHING_MODE_EDEFAULT;

    /**
     * The default value of the '{@link #getLookupMode() <em>Lookup Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLookupMode()
     * @generated
     * @ordered
     */
    protected static final String LOOKUP_MODE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLookupMode() <em>Lookup Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLookupMode()
     * @generated
     * @ordered
     */
    protected String lookupMode = LOOKUP_MODE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected InputTableImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return MapperPackage.Literals.INPUT_TABLE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getMatchingMode() {
        return matchingMode;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMatchingMode(String newMatchingMode) {
        String oldMatchingMode = matchingMode;
        matchingMode = newMatchingMode;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, MapperPackage.INPUT_TABLE__MATCHING_MODE, oldMatchingMode, matchingMode));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLookupMode() {
        return lookupMode;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLookupMode(String newLookupMode) {
        String oldLookupMode = lookupMode;
        lookupMode = newLookupMode;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, MapperPackage.INPUT_TABLE__LOOKUP_MODE, oldLookupMode, lookupMode));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case MapperPackage.INPUT_TABLE__MATCHING_MODE:
                return getMatchingMode();
            case MapperPackage.INPUT_TABLE__LOOKUP_MODE:
                return getLookupMode();
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
            case MapperPackage.INPUT_TABLE__MATCHING_MODE:
                setMatchingMode((String)newValue);
                return;
            case MapperPackage.INPUT_TABLE__LOOKUP_MODE:
                setLookupMode((String)newValue);
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
            case MapperPackage.INPUT_TABLE__MATCHING_MODE:
                setMatchingMode(MATCHING_MODE_EDEFAULT);
                return;
            case MapperPackage.INPUT_TABLE__LOOKUP_MODE:
                setLookupMode(LOOKUP_MODE_EDEFAULT);
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
            case MapperPackage.INPUT_TABLE__MATCHING_MODE:
                return MATCHING_MODE_EDEFAULT == null ? matchingMode != null : !MATCHING_MODE_EDEFAULT.equals(matchingMode);
            case MapperPackage.INPUT_TABLE__LOOKUP_MODE:
                return LOOKUP_MODE_EDEFAULT == null ? lookupMode != null : !LOOKUP_MODE_EDEFAULT.equals(lookupMode);
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
        result.append(" (matchingMode: ");
        result.append(matchingMode);
        result.append(", lookupMode: ");
        result.append(lookupMode);
        result.append(')');
        return result.toString();
    }

} //InputTableImpl
