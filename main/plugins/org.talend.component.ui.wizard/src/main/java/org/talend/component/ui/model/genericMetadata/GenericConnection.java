/**
 */
package org.talend.component.ui.model.genericMetadata;

import org.talend.core.model.metadata.builder.connection.Connection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Generic Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.component.ui.model.genericMetadata.GenericConnection#getParameters <em>Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.component.ui.model.genericMetadata.GenericMetadataPackage#getGenericConnection()
 * @model
 * @generated
 */
public interface GenericConnection extends Connection {
    /**
     * Returns the value of the '<em><b>Parameters</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Parameters</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parameters</em>' attribute.
     * @see #setParameters(String)
     * @see org.talend.component.ui.model.genericMetadata.GenericMetadataPackage#getGenericConnection_Parameters()
     * @model
     * @generated
     */
    String getParameters();

    /**
     * Sets the value of the '{@link org.talend.component.ui.model.genericMetadata.GenericConnection#getParameters <em>Parameters</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Parameters</em>' attribute.
     * @see #getParameters()
     * @generated
     */
    void setParameters(String value);

} // GenericConnection
