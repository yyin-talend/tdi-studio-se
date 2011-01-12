/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.designer.xmlmap.model.emf.xmlmap.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.talend.designer.xmlmap.model.emf.xmlmap.Connection;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Output Tree Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.impl.OutputTreeNodeImpl#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.impl.OutputTreeNodeImpl#isGroup <em>Group</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.impl.OutputTreeNodeImpl#getIncomingConnections <em>Incoming Connections</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OutputTreeNodeImpl extends TreeNodeImpl implements OutputTreeNode {
    /**
     * The default value of the '{@link #getDefaultValue() <em>Default Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefaultValue()
     * @generated
     * @ordered
     */
    protected static final String DEFAULT_VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDefaultValue() <em>Default Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefaultValue()
     * @generated
     * @ordered
     */
    protected String defaultValue = DEFAULT_VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #isGroup() <em>Group</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGroup()
     * @generated
     * @ordered
     */
    protected static final boolean GROUP_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isGroup() <em>Group</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGroup()
     * @generated
     * @ordered
     */
    protected boolean group = GROUP_EDEFAULT;

    /**
     * The cached value of the '{@link #getIncomingConnections() <em>Incoming Connections</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIncomingConnections()
     * @generated
     * @ordered
     */
    protected EList<Connection> incomingConnections;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected OutputTreeNodeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return XmlmapPackage.Literals.OUTPUT_TREE_NODE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDefaultValue(String newDefaultValue) {
        String oldDefaultValue = defaultValue;
        defaultValue = newDefaultValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XmlmapPackage.OUTPUT_TREE_NODE__DEFAULT_VALUE, oldDefaultValue, defaultValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isGroup() {
        return group;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGroup(boolean newGroup) {
        boolean oldGroup = group;
        group = newGroup;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XmlmapPackage.OUTPUT_TREE_NODE__GROUP, oldGroup, group));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Connection> getIncomingConnections() {
        if (incomingConnections == null) {
            incomingConnections = new EObjectResolvingEList<Connection>(Connection.class, this, XmlmapPackage.OUTPUT_TREE_NODE__INCOMING_CONNECTIONS);
        }
        return incomingConnections;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case XmlmapPackage.OUTPUT_TREE_NODE__DEFAULT_VALUE:
                return getDefaultValue();
            case XmlmapPackage.OUTPUT_TREE_NODE__GROUP:
                return isGroup();
            case XmlmapPackage.OUTPUT_TREE_NODE__INCOMING_CONNECTIONS:
                return getIncomingConnections();
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
            case XmlmapPackage.OUTPUT_TREE_NODE__DEFAULT_VALUE:
                setDefaultValue((String)newValue);
                return;
            case XmlmapPackage.OUTPUT_TREE_NODE__GROUP:
                setGroup((Boolean)newValue);
                return;
            case XmlmapPackage.OUTPUT_TREE_NODE__INCOMING_CONNECTIONS:
                getIncomingConnections().clear();
                getIncomingConnections().addAll((Collection<? extends Connection>)newValue);
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
            case XmlmapPackage.OUTPUT_TREE_NODE__DEFAULT_VALUE:
                setDefaultValue(DEFAULT_VALUE_EDEFAULT);
                return;
            case XmlmapPackage.OUTPUT_TREE_NODE__GROUP:
                setGroup(GROUP_EDEFAULT);
                return;
            case XmlmapPackage.OUTPUT_TREE_NODE__INCOMING_CONNECTIONS:
                getIncomingConnections().clear();
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
            case XmlmapPackage.OUTPUT_TREE_NODE__DEFAULT_VALUE:
                return DEFAULT_VALUE_EDEFAULT == null ? defaultValue != null : !DEFAULT_VALUE_EDEFAULT.equals(defaultValue);
            case XmlmapPackage.OUTPUT_TREE_NODE__GROUP:
                return group != GROUP_EDEFAULT;
            case XmlmapPackage.OUTPUT_TREE_NODE__INCOMING_CONNECTIONS:
                return incomingConnections != null && !incomingConnections.isEmpty();
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
        result.append(" (defaultValue: ");
        result.append(defaultValue);
        result.append(", group: ");
        result.append(group);
        result.append(')');
        return result.toString();
    }

} //OutputTreeNodeImpl
