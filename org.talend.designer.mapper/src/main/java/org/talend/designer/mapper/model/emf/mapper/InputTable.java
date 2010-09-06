/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.designer.mapper.model.emf.mapper;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Input Table</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.designer.mapper.model.emf.mapper.InputTable#getMatchingMode <em>Matching Mode</em>}</li>
 *   <li>{@link org.talend.designer.mapper.model.emf.mapper.InputTable#getLookupMode <em>Lookup Mode</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.designer.mapper.model.emf.mapper.MapperPackage#getInputTable()
 * @model
 * @generated
 */
public interface InputTable extends AbstractInOutTable {
    /**
     * Returns the value of the '<em><b>Matching Mode</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Matching Mode</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Matching Mode</em>' attribute.
     * @see #setMatchingMode(String)
     * @see org.talend.designer.mapper.model.emf.mapper.MapperPackage#getInputTable_MatchingMode()
     * @model
     * @generated
     */
    String getMatchingMode();

    /**
     * Sets the value of the '{@link org.talend.designer.mapper.model.emf.mapper.InputTable#getMatchingMode <em>Matching Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Matching Mode</em>' attribute.
     * @see #getMatchingMode()
     * @generated
     */
    void setMatchingMode(String value);

    /**
     * Returns the value of the '<em><b>Lookup Mode</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Lookup Mode</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Lookup Mode</em>' attribute.
     * @see #setLookupMode(String)
     * @see org.talend.designer.mapper.model.emf.mapper.MapperPackage#getInputTable_LookupMode()
     * @model
     * @generated
     */
    String getLookupMode();

    /**
     * Sets the value of the '{@link org.talend.designer.mapper.model.emf.mapper.InputTable#getLookupMode <em>Lookup Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lookup Mode</em>' attribute.
     * @see #getLookupMode()
     * @generated
     */
    void setLookupMode(String value);

} // InputTable
