/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.designer.dbmap.model.emf.dbmap.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.talend.designer.dbmap.model.emf.dbmap.DbmapPackage;
import org.talend.designer.dbmap.model.emf.dbmap.InputTable;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Input Table</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.designer.dbmap.model.emf.dbmap.impl.InputTableImpl#getJoinType <em>Join Type</em>}</li>
 *   <li>{@link org.talend.designer.dbmap.model.emf.dbmap.impl.InputTableImpl#getAlias <em>Alias</em>}</li>
 *   <li>{@link org.talend.designer.dbmap.model.emf.dbmap.impl.InputTableImpl#getTableName <em>Table Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InputTableImpl extends AbstaceDBInOutTableImpl implements InputTable {
    /**
     * The default value of the '{@link #getJoinType() <em>Join Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getJoinType()
     * @generated
     * @ordered
     */
    protected static final String JOIN_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getJoinType() <em>Join Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getJoinType()
     * @generated
     * @ordered
     */
    protected String joinType = JOIN_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getAlias() <em>Alias</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAlias()
     * @generated
     * @ordered
     */
    protected static final String ALIAS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAlias() <em>Alias</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAlias()
     * @generated
     * @ordered
     */
    protected String alias = ALIAS_EDEFAULT;

    /**
     * The default value of the '{@link #getTableName() <em>Table Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTableName()
     * @generated
     * @ordered
     */
    protected static final String TABLE_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTableName() <em>Table Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTableName()
     * @generated
     * @ordered
     */
    protected String tableName = TABLE_NAME_EDEFAULT;

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
        return DbmapPackage.Literals.INPUT_TABLE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getJoinType() {
        return joinType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setJoinType(String newJoinType) {
        String oldJoinType = joinType;
        joinType = newJoinType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, DbmapPackage.INPUT_TABLE__JOIN_TYPE, oldJoinType, joinType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getAlias() {
        return alias;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAlias(String newAlias) {
        String oldAlias = alias;
        alias = newAlias;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, DbmapPackage.INPUT_TABLE__ALIAS, oldAlias, alias));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTableName(String newTableName) {
        String oldTableName = tableName;
        tableName = newTableName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, DbmapPackage.INPUT_TABLE__TABLE_NAME, oldTableName, tableName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case DbmapPackage.INPUT_TABLE__JOIN_TYPE:
                return getJoinType();
            case DbmapPackage.INPUT_TABLE__ALIAS:
                return getAlias();
            case DbmapPackage.INPUT_TABLE__TABLE_NAME:
                return getTableName();
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
            case DbmapPackage.INPUT_TABLE__JOIN_TYPE:
                setJoinType((String)newValue);
                return;
            case DbmapPackage.INPUT_TABLE__ALIAS:
                setAlias((String)newValue);
                return;
            case DbmapPackage.INPUT_TABLE__TABLE_NAME:
                setTableName((String)newValue);
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
            case DbmapPackage.INPUT_TABLE__JOIN_TYPE:
                setJoinType(JOIN_TYPE_EDEFAULT);
                return;
            case DbmapPackage.INPUT_TABLE__ALIAS:
                setAlias(ALIAS_EDEFAULT);
                return;
            case DbmapPackage.INPUT_TABLE__TABLE_NAME:
                setTableName(TABLE_NAME_EDEFAULT);
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
            case DbmapPackage.INPUT_TABLE__JOIN_TYPE:
                return JOIN_TYPE_EDEFAULT == null ? joinType != null : !JOIN_TYPE_EDEFAULT.equals(joinType);
            case DbmapPackage.INPUT_TABLE__ALIAS:
                return ALIAS_EDEFAULT == null ? alias != null : !ALIAS_EDEFAULT.equals(alias);
            case DbmapPackage.INPUT_TABLE__TABLE_NAME:
                return TABLE_NAME_EDEFAULT == null ? tableName != null : !TABLE_NAME_EDEFAULT.equals(tableName);
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
        result.append(" (joinType: ");
        result.append(joinType);
        result.append(", alias: ");
        result.append(alias);
        result.append(", tableName: ");
        result.append(tableName);
        result.append(')');
        return result.toString();
    }

} //InputTableImpl
