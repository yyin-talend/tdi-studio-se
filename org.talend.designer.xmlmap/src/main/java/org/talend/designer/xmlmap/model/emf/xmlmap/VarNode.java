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
 * A representation of the model object '<em><b>Var Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.VarNode#getVariable <em>Variable</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getVarNode()
 * @model
 * @generated
 */
public interface VarNode extends AbstractNode {

    /**
     * Returns the value of the '<em><b>Variable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Variable</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Variable</em>' attribute.
     * @see #setVariable(String)
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getVarNode_Variable()
     * @model
     * @generated
     */
    String getVariable();

    /**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.VarNode#getVariable <em>Variable</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Variable</em>' attribute.
     * @see #getVariable()
     * @generated
     */
    void setVariable(String value);

} // VarNode
