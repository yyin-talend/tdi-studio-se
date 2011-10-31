// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.components.exchange.ui.htmlcontent;

import java.util.List;
import java.util.Properties;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroSite;
import org.eclipse.ui.intro.config.IIntroAction;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.designer.components.exchange.model.ComponentExtension;
import org.talend.designer.components.exchange.model.ExchangeFactory;
import org.talend.designer.components.exchange.model.VersionRevision;
import org.talend.designer.components.exchange.ui.actions.DeleteExtensionAction;
import org.talend.designer.components.exchange.ui.actions.InsertionExtensionAction;
import org.talend.designer.components.exchange.ui.actions.UploadRevisionAction;
import org.talend.designer.components.exchange.ui.views.ExchangeView;
import org.talend.json.JSONException;
import org.talend.json.JSONObject;

/**
 * DOC talend class global comment. Detailled comment
 */
public class MyExtensionAction implements IIntroAction {

    public void run(IIntroSite site, Properties params) {
        if (params != null) {
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            if (page != null) {
                IViewPart view = page.findView(ExchangeView.ID);
                if (view != null) {
                    ExchangeView exchangeView = (ExchangeView) view;
                    Object actionType = params.get(ContentConstants.KEY_TYPE);
                    if (ContentConstants.RETURN_TO_FIRST_PAGE.equals(actionType)) {
                        exchangeView.returnMyExtensionCompositeToFirstPage();
                    } else if (ContentConstants.UPLOAD_MY_EXTENSION.equals(actionType)) {
                        try {
                            String values = (String) params.get(ContentConstants.EXTENSIONVALUES);
                            if (values != null) {
                                List<VersionRevision> versionRevisions = exchangeView.getVersionRevisions();
                                ComponentExtension extension = ExchangeFactory.eINSTANCE.createComponentExtension();
                                setValuesForExension(extension, values, versionRevisions);
                                exchangeView.setSelectedExtension(extension);
                                InsertionExtensionAction uploadAction = new InsertionExtensionAction(extension);
                                uploadAction.run();
                            }
                        } catch (JSONException e) {
                            ExceptionHandler.process(e);
                        }

                    } else if (ContentConstants.UPDATE_MY_EXTENSION.equals(actionType)
                            || ContentConstants.MODIFY_MY_EXTENSION.equals(actionType)) {
                        try {
                            String values = (String) params.get(ContentConstants.EXTENSIONVALUES);
                            if (values != null) {
                                List<VersionRevision> versionRevisions = exchangeView.getVersionRevisions();
                                ComponentExtension selectedExtension = exchangeView.getSelectedExtension();
                                setValuesForExension(selectedExtension, values, versionRevisions);
                                UploadRevisionAction uploadAction = new UploadRevisionAction(selectedExtension);
                                if (uploadAction != null) {
                                    uploadAction.run();
                                }
                            }
                        } catch (JSONException e) {
                            ExceptionHandler.process(e);
                        }

                    } else if (ContentConstants.UPLOAD_NEW_VERSION_ACTION.equals(actionType)
                            || ContentConstants.MODIFY_ACTION.equals(actionType)) {
                        Object object = params.get(ContentConstants.KEY_EXTENSION_ID);
                        String key = null;
                        if (object instanceof String) {
                            key = (String) object;
                        }
                        ComponentExtension componentExtension = MyExchangeContentProvider.componentMap.get(key);
                        if (componentExtension != null) {
                            exchangeView.setSelectedExtension(componentExtension);
                            if (ContentConstants.UPLOAD_NEW_VERSION_ACTION.equals(actionType)) {
                                exchangeView.myExtensionModifyOrUploadVersion(0, componentExtension);
                            } else {
                                exchangeView.myExtensionModifyOrUploadVersion(1, componentExtension);
                            }

                        }
                    } else if (ContentConstants.DELETE_ACTION.equals(actionType)) {
                        Object object = params.get(ContentConstants.KEY_EXTENSION_ID);
                        String key = null;
                        if (object instanceof String) {
                            key = (String) object;
                        }
                        ComponentExtension componentExtension = MyExchangeContentProvider.componentMap.get(key);
                        exchangeView.setSelectedExtension(componentExtension);
                        if (componentExtension != null) {
                            final DeleteExtensionAction delAction = new DeleteExtensionAction(componentExtension);
                            if (delAction != null) {
                                delAction.run();
                            }
                        }

                    } else if (ContentConstants.ADD_NEW_EXTENSION.equals(actionType)) {
                        exchangeView.myExtensionAddNew();
                    }
                }

            }
        }

    }

    private void setValuesForExension(ComponentExtension componentExtension, String values, List<VersionRevision> versionRevisions)
            throws JSONException {

        JSONObject jsObject = new JSONObject(values);
        if (jsObject.has(ContentConstants.EXTENSION_VALUE_LABEL)) {
            componentExtension.setLabel(jsObject.getString(ContentConstants.EXTENSION_VALUE_LABEL));
        }
        if (jsObject.has(ContentConstants.EXTENSION_VALUE_LASTVERSIONAVAILABLE)) {
            componentExtension.setLastVersionAvailable(jsObject.getString(ContentConstants.EXTENSION_VALUE_LASTVERSIONAVAILABLE));
        }
        if (jsObject.has(ContentConstants.EXTENSION_VALUE_DESCRIPTION)) {
            componentExtension.setDescription(jsObject.getString(ContentConstants.EXTENSION_VALUE_DESCRIPTION));
        }
        if (jsObject.has(ContentConstants.EXTENSION_VALUE_COMPATIBLES)) {
            String compatibles = jsObject.getString(ContentConstants.EXTENSION_VALUE_COMPATIBLES);

            String filter = null;
            if (jsObject.has(ContentConstants.EXTENSION_VALUE_Filter)) {
                filter = jsObject.getString(ContentConstants.EXTENSION_VALUE_Filter);
            }
            String revision = filterVersionRevisionsToString(compatibles, filter, versionRevisions);
            componentExtension.setListVersionCompatibles(revision);
        }
        if (jsObject.has(ContentConstants.EXTENSION_VALUE_FILENAME)) {
            componentExtension.setFilename(jsObject.getString(ContentConstants.EXTENSION_VALUE_FILENAME));
        }
    }

    private String filterVersionRevisionsToString(String status, String version, List<VersionRevision> fVersionRevisions) {
        if (fVersionRevisions == null || fVersionRevisions.isEmpty()) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        if (status.equals("all")) {
            for (VersionRevision ver : fVersionRevisions) {
                sb.append(ver.getVersionId());
                sb.append(",");
            }
            return sb.substring(0, sb.length() - 1);
        } else if (status.equals("older")) {
            for (VersionRevision ver : fVersionRevisions) {
                if (ver.getVersionName().equalsIgnoreCase(version)) {
                    break;
                }
                sb.append(ver.getVersionId());
                sb.append(",");
            }
            return sb.substring(0, sb.length() - 1);
        } else if (status.equals("newer")) {
            boolean newerb = false;
            for (VersionRevision ver : fVersionRevisions) {
                if (ver.getVersionName().equalsIgnoreCase(version)) {
                    newerb = true;
                }
                if (newerb) {
                    sb.append(ver.getVersionId());
                    sb.append(",");
                }

            }
            return sb.substring(0, sb.length() - 1);
        } else if (status.equals("except")) {
            for (VersionRevision ver : fVersionRevisions) {
                if (!ver.getVersionName().equalsIgnoreCase(version)) {
                    sb.append(ver.getVersionId());
                    sb.append(",");
                }
            }
            return sb.substring(0, sb.length() - 1);
        } else if (status.equals("only")) {
            for (VersionRevision ver : fVersionRevisions) {
                if (ver.getVersionName().equalsIgnoreCase(version)) {
                    sb.append(ver.getVersionId());
                    break;
                }
            }
            return sb.toString();
        }

        return "";
    }

}
