/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.designer.xmlmap.model.emf.xmlmap;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage
 * @generated
 */
public interface XmlmapFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    XmlmapFactory eINSTANCE = org.talend.designer.xmlmap.model.emf.xmlmap.impl.XmlmapFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Xml Map Data</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Xml Map Data</em>'.
     * @generated
     */
    XmlMapData createXmlMapData();

    /**
     * Returns a new object of class '<em>Input Xml Tree</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Input Xml Tree</em>'.
     * @generated
     */
    InputXmlTree createInputXmlTree();

    /**
     * Returns a new object of class '<em>Output Xml Tree</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Output Xml Tree</em>'.
     * @generated
     */
    OutputXmlTree createOutputXmlTree();

    /**
     * Returns a new object of class '<em>Tree Node</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Tree Node</em>'.
     * @generated
     */
    TreeNode createTreeNode();

    /**
     * Returns a new object of class '<em>Output Tree Node</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Output Tree Node</em>'.
     * @generated
     */
    OutputTreeNode createOutputTreeNode();

    /**
     * Returns a new object of class '<em>Connection</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Connection</em>'.
     * @generated
     */
	Connection createConnection();

				/**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    XmlmapPackage getXmlmapPackage();

} //XmlmapFactory
