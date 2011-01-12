/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.designer.xmlmap.model.emf.xmlmap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.Connection#getSource <em>Source</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.Connection#getTarget <em>Target</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getConnection()
 * @model
 * @generated
 */
public interface Connection extends EObject {
	/**
     * Returns the value of the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Source</em>' reference.
     * @see #setSource(TreeNode)
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getConnection_Source()
     * @model
     * @generated
     */
	TreeNode getSource();

	/**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.Connection#getSource <em>Source</em>}' reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source</em>' reference.
     * @see #getSource()
     * @generated
     */
	void setSource(TreeNode value);

	/**
     * Returns the value of the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Target</em>' reference.
     * @see #setTarget(OutputTreeNode)
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getConnection_Target()
     * @model
     * @generated
     */
	OutputTreeNode getTarget();

	/**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.Connection#getTarget <em>Target</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target</em>' reference.
     * @see #getTarget()
     * @generated
     */
    void setTarget(OutputTreeNode value);

} // Connection
