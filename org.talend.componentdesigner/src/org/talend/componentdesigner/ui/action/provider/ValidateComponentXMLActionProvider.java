// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.componentdesigner.ui.action.provider;

import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.talend.componentdesigner.i18n.internal.Messages;
import org.talend.core.model.ModelPlugin;

/**
 * DOC gke class global comment. Detailled comment
 */
public class ValidateComponentXMLActionProvider extends CommonActionProvider {

    private IAction validateAction;

    private IFile selectedFile;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.actions.ActionGroup#fillContextMenu(org.eclipse.jface.action.IMenuManager)
     */
    public void fillContextMenu(IMenuManager menu) {
        Object obj = ((TreeSelection) this.getContext().getSelection()).getFirstElement();
        if (obj instanceof IFile) {
            selectedFile = (IFile) obj;
        }
        if (((String) selectedFile.getFileExtension()).equalsIgnoreCase("xml")) { //$NON-NLS-1$
            menu.insertBefore("common.new.menu", validateAction);//$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.CommonActionProvider#init(org.eclipse.ui.navigator.ICommonActionExtensionSite)
     */
    public void init(ICommonActionExtensionSite site) {
        if (site.getViewSite() instanceof ICommonViewerWorkbenchSite) {
            validateAction = new ValidateComponentXMLAction();
        }
    }

    /**
     * DOC gke ValidateComponentXMLActionProvider class global comment. Detailled comment
     */
    class ValidateComponentXMLAction extends Action {

        public ValidateComponentXMLAction() {
            super(Messages.getString("ValidateComponentXMLActionProvider.validate")); //$NON-NLS-1$
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.action.Action#run()
         */
        public void run() {
            //
            try {
                URL url = ModelPlugin.getDefault().getBundle().getResource("/model/Component.xsd"); //$NON-NLS-1$
                url = FileLocator.toFileURL(url);
                String componentXSDFile = url.getFile();
                //
                javax.xml.validation.SchemaFactory factorytXSDValidator = javax.xml.validation.SchemaFactory
                        .newInstance("http://www.w3.org/2001/XMLSchema"); //$NON-NLS-1$
                java.io.File schemaLocationtXSDValidator = new java.io.File(componentXSDFile);
                String message = null;

                TalendErrorHandler errorHandler = new TalendErrorHandler();

                try {
                    javax.xml.validation.Schema schematXSDValidator = factorytXSDValidator
                            .newSchema(schemaLocationtXSDValidator);
                    javax.xml.validation.Validator validatortXSDValidator = schematXSDValidator.newValidator();
                    java.io.File xmlfiletXSDValidator = new java.io.File(selectedFile.getLocationURI());
                    javax.xml.transform.Source sourcetXSDValidator = new javax.xml.transform.stream.StreamSource(
                            xmlfiletXSDValidator);

                    validatortXSDValidator.setErrorHandler(errorHandler);

                    validatortXSDValidator.validate(sourcetXSDValidator);

                } catch (org.xml.sax.SAXParseException extXSDValidator) {
                    errorHandler.error(extXSDValidator);
                }
                message = errorHandler.returnMessage();
                if (message.length() > 0) {
                    message = Messages.getString("ValidateComponentXMLActionProvider.invalid") + message; //$NON-NLS-1$
                } else {
                    message = Messages.getString("ValidateComponentXMLActionProvider.valid"); //$NON-NLS-1$
                }

                // show the dialog for the validate result.
                MessageDialog messageDialog = new MessageDialog(
                        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                        Messages.getString("ValidateComponentXMLActionProvider.result"), null, message, MessageDialog.INFORMATION, new String[] { Messages.getString("ValidateComponentXMLActionProvider.ok") }, 0); //$NON-NLS-1$ //$NON-NLS-2$
                messageDialog.open();

            } catch (Exception e) {
                org.talend.componentdesigner.exception.ExceptionHandler.process(e);
            }

        }

    }

    /**
     * DOC gke ValidateWithComponentXSDActionProvider class global comment. Detailled comment
     */
    class TalendErrorHandler implements org.xml.sax.ErrorHandler {

        String errorMessage = ""; //$NON-NLS-1$

        public void warning(org.xml.sax.SAXParseException ex) throws org.xml.sax.SAXException {
            errorMessage = errorMessage
                    + Messages.getString("ValidateComponentXMLActionProvider.warning", String.valueOf(ex.getLineNumber())) + " : " //$NON-NLS-1$//$NON-NLS-2$
                    + ex.getMessage();
        }

        public void error(org.xml.sax.SAXParseException ex) throws org.xml.sax.SAXException {
            errorMessage = errorMessage
                    + Messages.getString("ValidateComponentXMLActionProvider.error", String.valueOf(ex.getLineNumber())) + " : " //$NON-NLS-1$//$NON-NLS-2$
                    + ex.getMessage();
        }

        public void fatalError(org.xml.sax.SAXParseException ex) throws org.xml.sax.SAXException {

            errorMessage = errorMessage
                    + Messages.getString("ValidateComponentXMLActionProvider.fatalerror", String.valueOf(ex.getLineNumber())) + " : " //$NON-NLS-1$//$NON-NLS-2$
                    + ex.getMessage();

        }

        private String returnMessage() {
            return errorMessage == null ? "" : errorMessage; //$NON-NLS-1$
        }

    }

}
