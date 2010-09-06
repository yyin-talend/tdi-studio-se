/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.designer.mapper.model.emf.mapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>SIZE STATE</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.talend.designer.mapper.model.emf.mapper.MapperPackage#getSIZE_STATE()
 * @model
 * @generated
 */
public enum SIZE_STATE implements Enumerator {
    /**
     * The '<em><b>MINIMIZED</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MINIMIZED_VALUE
     * @generated
     * @ordered
     */
    MINIMIZED(0, "MINIMIZED", "MINIMIZED"),

    /**
     * The '<em><b>INTERMEDIATE</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #INTERMEDIATE_VALUE
     * @generated
     * @ordered
     */
    INTERMEDIATE(1, "INTERMEDIATE", "INTERMEDIATE"),

    /**
     * The '<em><b>MAXIMIZED</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MAXIMIZED_VALUE
     * @generated
     * @ordered
     */
    MAXIMIZED(2, "MAXIMIZED", "MAXIMIZED");

    /**
     * The '<em><b>MINIMIZED</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>MINIMIZED</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #MINIMIZED
     * @model
     * @generated
     * @ordered
     */
    public static final int MINIMIZED_VALUE = 0;

    /**
     * The '<em><b>INTERMEDIATE</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>INTERMEDIATE</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #INTERMEDIATE
     * @model
     * @generated
     * @ordered
     */
    public static final int INTERMEDIATE_VALUE = 1;

    /**
     * The '<em><b>MAXIMIZED</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>MAXIMIZED</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #MAXIMIZED
     * @model
     * @generated
     * @ordered
     */
    public static final int MAXIMIZED_VALUE = 2;

    /**
     * An array of all the '<em><b>SIZE STATE</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final SIZE_STATE[] VALUES_ARRAY =
        new SIZE_STATE[] {
            MINIMIZED,
            INTERMEDIATE,
            MAXIMIZED,
        };

    /**
     * A public read-only list of all the '<em><b>SIZE STATE</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<SIZE_STATE> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>SIZE STATE</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static SIZE_STATE get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            SIZE_STATE result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>SIZE STATE</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static SIZE_STATE getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            SIZE_STATE result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>SIZE STATE</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static SIZE_STATE get(int value) {
        switch (value) {
            case MINIMIZED_VALUE: return MINIMIZED;
            case INTERMEDIATE_VALUE: return INTERMEDIATE;
            case MAXIMIZED_VALUE: return MAXIMIZED;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final int value;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String name;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String literal;

    /**
     * Only this class can construct instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private SIZE_STATE(int value, String name, String literal) {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getValue() {
      return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
      return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLiteral() {
      return literal;
    }

    /**
     * Returns the literal value of the enumerator, which is its string representation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        return literal;
    }
    
} //SIZE_STATE
