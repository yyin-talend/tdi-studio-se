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
 * A representation of the model object '<em><b>Input Xml Tree</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree#getNodes <em>Nodes</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree#getName <em>Name</em>}</li>
 *   <li>{@link org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree#isLookup <em>Lookup</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getInputXmlTree()
 * @model
 * @generated
 */
public interface InputXmlTree extends EObject {
    /**
     * Returns the value of the '<em><b>Nodes</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Nodes</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Nodes</em>' containment reference list.
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getInputXmlTree_Nodes()
     * @model containment="true"
     * @generated
     */
    EList<TreeNode> getNodes();

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
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getInputXmlTree_Name()
     * @model
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Lookup</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Lookup</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Lookup</em>' attribute.
     * @see #setLookup(boolean)
     * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage#getInputXmlTree_Lookup()
     * @model
     * @generated
     */
    boolean isLookup();

    /**
     * Sets the value of the '{@link org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree#isLookup <em>Lookup</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lookup</em>' attribute.
     * @see #isLookup()
     * @generated
     */
    void setLookup(boolean value);

} // InputXmlTree
