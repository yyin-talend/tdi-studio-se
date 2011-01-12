/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.designer.xmlmap.model.emf.xmlmap;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tree Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#getChildren <em>Children</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#getName <em>Name</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#getExpression <em>Expression</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#getType <em>Type</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#isNullable <em>Nullable</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#getXpath <em>Xpath</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#isLoop <em>Loop</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#getOutgoingConnections <em>Outgoing Connections</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#getNodeType <em>Node Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getTreeNode()
 * @model
 * @generated
 */
public interface TreeNode extends EObject {
    /**
     * Returns the value of the '<em><b>Children</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Children</em>' containment reference list.
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getTreeNode_Children()
     * @model containment="true"
     * @generated
     */
    EList<TreeNode> getChildren();

    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getTreeNode_Name()
     * @model
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Expression</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Expression</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Expression</em>' attribute.
     * @see #setExpression(String)
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getTreeNode_Expression()
     * @model
     * @generated
     */
    String getExpression();

    /**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#getExpression <em>Expression</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Expression</em>' attribute.
     * @see #getExpression()
     * @generated
     */
    void setExpression(String value);

    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' attribute.
     * @see #setType(String)
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getTreeNode_Type()
     * @model
     * @generated
     */
    String getType();

    /**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' attribute.
     * @see #getType()
     * @generated
     */
    void setType(String value);

    /**
     * Returns the value of the '<em><b>Nullable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Nullable</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Nullable</em>' attribute.
     * @see #setNullable(boolean)
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getTreeNode_Nullable()
     * @model
     * @generated
     */
    boolean isNullable();

    /**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#isNullable <em>Nullable</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Nullable</em>' attribute.
     * @see #isNullable()
     * @generated
     */
    void setNullable(boolean value);

    /**
     * Returns the value of the '<em><b>Xpath</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Xpath</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Xpath</em>' attribute.
     * @see #setXpath(String)
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getTreeNode_Xpath()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     * @generated
     */
    String getXpath();

    /**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#getXpath <em>Xpath</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Xpath</em>' attribute.
     * @see #getXpath()
     * @generated
     */
    void setXpath(String value);

    /**
     * Returns the value of the '<em><b>Loop</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Loop</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Loop</em>' attribute.
     * @see #setLoop(boolean)
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getTreeNode_Loop()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     * @generated
     */
    boolean isLoop();

    /**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#isLoop <em>Loop</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Loop</em>' attribute.
     * @see #isLoop()
     * @generated
     */
    void setLoop(boolean value);

    /**
     * Returns the value of the '<em><b>Outgoing Connections</b></em>' reference list.
     * The list contents are of type {@link org.talend.designer.xmlmap.model.emf.xmlmap.Connection}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Outgoing Connections</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Outgoing Connections</em>' reference list.
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getTreeNode_OutgoingConnections()
     * @model
     * @generated
     */
    EList<Connection> getOutgoingConnections();

    /**
     * Returns the value of the '<em><b>Node Type</b></em>' attribute.
     * The literals are from the enumeration {@link org.talend.designer.xmlmap.model.emf.xmlmap.NodeType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Node Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Node Type</em>' attribute.
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.NodeType
     * @see #setNodeType(NodeType)
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getTreeNode_NodeType()
     * @model
     * @generated
     */
    NodeType getNodeType();

    /**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#getNodeType <em>Node Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Node Type</em>' attribute.
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.NodeType
     * @see #getNodeType()
     * @generated
     */
    void setNodeType(NodeType value);

} // TreeNode
