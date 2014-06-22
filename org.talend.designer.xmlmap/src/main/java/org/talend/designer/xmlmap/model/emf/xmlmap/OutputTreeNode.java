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
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode#isAggregate <em>Aggregate</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getOutputTreeNode()
 * @model
 * @generated
 */
public interface OutputTreeNode extends TreeNode {

    /**
     * Returns the value of the '<em><b>Aggregate</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Aggregate</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Aggregate</em>' attribute.
     * @see #setAggregate(boolean)
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getOutputTreeNode_Aggregate()
     * @model
     * @generated
     */
    boolean isAggregate();

    /**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode#isAggregate <em>Aggregate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Aggregate</em>' attribute.
     * @see #isAggregate()
     * @generated
     */
    void setAggregate(boolean value);

} // OutputTreeNode
