/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.designer.xmlmap.model.emf.xmlmap;

import org.eclipse.emf.common.util.EList;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Output Tree Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode#isGroup <em>Group</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode#getIncomingConnections <em>Incoming Connections</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getOutputTreeNode()
 * @model
 * @generated
 */
public interface OutputTreeNode extends TreeNode {
    /**
     * Returns the value of the '<em><b>Default Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Default Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Default Value</em>' attribute.
     * @see #setDefaultValue(String)
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getOutputTreeNode_DefaultValue()
     * @model
     * @generated
     */
    String getDefaultValue();

    /**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode#getDefaultValue <em>Default Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default Value</em>' attribute.
     * @see #getDefaultValue()
     * @generated
     */
    void setDefaultValue(String value);

    /**
     * Returns the value of the '<em><b>Group</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Group</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Group</em>' attribute.
     * @see #setGroup(boolean)
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getOutputTreeNode_Group()
     * @model
     * @generated
     */
    boolean isGroup();

    /**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode#isGroup <em>Group</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Group</em>' attribute.
     * @see #isGroup()
     * @generated
     */
    void setGroup(boolean value);

    /**
     * Returns the value of the '<em><b>Incoming Connections</b></em>' reference list.
     * The list contents are of type {@link org.talend.designer.xmlmap.model.emf.xmlmap.Connection}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Incoming Connections</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Incoming Connections</em>' reference list.
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getOutputTreeNode_IncomingConnections()
     * @model
     * @generated
     */
    EList<Connection> getIncomingConnections();

} // OutputTreeNode
