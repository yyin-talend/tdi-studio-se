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
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#getXpath <em>Xpath</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#isLoop <em>Loop</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#getNodeType <em>Node Type</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#getPattern <em>Pattern</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#isKey <em>Key</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#isGroup <em>Group</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#isMain <em>Main</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#getLookupOutgoingConnections <em>Lookup Outgoing Connections</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#getLookupIncomingConnections <em>Lookup Incoming Connections</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#getDefaultValue <em>Default Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getTreeNode()
 * @model
 * @generated
 */
public interface TreeNode extends AbstractNode {
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
     * @model
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
     * @model
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

    /**
     * Returns the value of the '<em><b>Pattern</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Pattern</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Pattern</em>' attribute.
     * @see #setPattern(String)
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getTreeNode_Pattern()
     * @model
     * @generated
     */
    String getPattern();

    /**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#getPattern <em>Pattern</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Pattern</em>' attribute.
     * @see #getPattern()
     * @generated
     */
    void setPattern(String value);

    /**
     * Returns the value of the '<em><b>Key</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Key</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Key</em>' attribute.
     * @see #setKey(boolean)
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getTreeNode_Key()
     * @model
     * @generated
     */
    boolean isKey();

    /**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#isKey <em>Key</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Key</em>' attribute.
     * @see #isKey()
     * @generated
     */
    void setKey(boolean value);

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
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getTreeNode_Group()
     * @model
     * @generated
     */
    boolean isGroup();

    /**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#isGroup <em>Group</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Group</em>' attribute.
     * @see #isGroup()
     * @generated
     */
    void setGroup(boolean value);

    /**
     * Returns the value of the '<em><b>Main</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Main</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Main</em>' attribute.
     * @see #setMain(boolean)
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getTreeNode_Main()
     * @model
     * @generated
     */
    boolean isMain();

    /**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#isMain <em>Main</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Main</em>' attribute.
     * @see #isMain()
     * @generated
     */
    void setMain(boolean value);

    /**
     * Returns the value of the '<em><b>Lookup Outgoing Connections</b></em>' reference list.
     * The list contents are of type {@link org.talend.designer.xmlmap.model.emf.xmlmap.LookupConnection}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Lookup Outgoing Connections</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Lookup Outgoing Connections</em>' reference list.
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getTreeNode_LookupOutgoingConnections()
     * @model
     * @generated
     */
    EList<LookupConnection> getLookupOutgoingConnections();

    /**
     * Returns the value of the '<em><b>Lookup Incoming Connections</b></em>' reference list.
     * The list contents are of type {@link org.talend.designer.xmlmap.model.emf.xmlmap.LookupConnection}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Lookup Incoming Connections</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Lookup Incoming Connections</em>' reference list.
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getTreeNode_LookupIncomingConnections()
     * @model
     * @generated
     */
    EList<LookupConnection> getLookupIncomingConnections();

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
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getTreeNode_DefaultValue()
     * @model
     * @generated
     */
    String getDefaultValue();

    /**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode#getDefaultValue <em>Default Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default Value</em>' attribute.
     * @see #getDefaultValue()
     * @generated
     */
    void setDefaultValue(String value);

} // TreeNode
