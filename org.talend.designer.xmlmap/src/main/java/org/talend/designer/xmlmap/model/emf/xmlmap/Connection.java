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
     * It is bidirectional and its opposite is '{@link org.talend.designer.xmlmap.model.emf.xmlmap.AbstractNode#getOutgoingConnections <em>Outgoing Connections</em>}'.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Source</em>' reference.
     * @see #setSource(AbstractNode)
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getConnection_Source()
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.AbstractNode#getOutgoingConnections
     * @model opposite="outgoingConnections"
     * @generated
     */
	AbstractNode getSource();

	/**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.Connection#getSource <em>Source</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source</em>' reference.
     * @see #getSource()
     * @generated
     */
    void setSource(AbstractNode value);

    /**
     * Returns the value of the '<em><b>Target</b></em>' reference.
     * It is bidirectional and its opposite is '{@link org.talend.designer.xmlmap.model.emf.xmlmap.AbstractNode#getIncomingConnections <em>Incoming Connections</em>}'.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Target</em>' reference.
     * @see #setTarget(AbstractNode)
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getConnection_Target()
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.AbstractNode#getIncomingConnections
     * @model opposite="incomingConnections"
     * @generated
     */
	AbstractNode getTarget();

	/**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.Connection#getTarget <em>Target</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target</em>' reference.
     * @see #getTarget()
     * @generated
     */
    void setTarget(AbstractNode value);

} // Connection
