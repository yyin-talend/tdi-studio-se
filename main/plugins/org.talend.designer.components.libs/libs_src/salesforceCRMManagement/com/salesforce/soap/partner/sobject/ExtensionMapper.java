/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.6.2 Built on : Apr 17, 2012 (05:34:40 IST)
 */

package com.salesforce.soap.partner.sobject;

/**
 * ExtensionMapper class
 */
@SuppressWarnings({ "unchecked", "unused" })
public class ExtensionMapper {

    public static java.lang.Object getTypeObject(java.lang.String namespaceURI, java.lang.String typeName,
            javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeTab".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeTab.Factory.parse(reader);

        }

        if ("urn:sobject.partner.soap.sforce.com".equals(namespaceURI) && "sObject".equals(typeName)) {

            return com.salesforce.soap.partner.sobject.SObject.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "MergeRequest".equals(typeName)) {

            return com.salesforce.soap.partner.MergeRequest.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "ExecuteListViewResult".equals(typeName)) {

            return com.salesforce.soap.partner.ExecuteListViewResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "SetPasswordResult".equals(typeName)) {

            return com.salesforce.soap.partner.SetPasswordResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeThemeResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeThemeResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeSoftphoneLayoutCallType".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeSoftphoneLayoutCallType.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "UndeleteResult".equals(typeName)) {

            return com.salesforce.soap.partner.UndeleteResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "NameValuePair".equals(typeName)) {

            return com.salesforce.soap.partner.NameValuePair.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "QueryLocator".equals(typeName)) {

            return com.salesforce.soap.partner.QueryLocator.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "Error".equals(typeName)) {

            return com.salesforce.soap.partner.Error.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeLayoutRow".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeLayoutRow.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeCompactLayout".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeCompactLayout.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeQuickActionDefaultValue".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeQuickActionDefaultValue.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "RelatedListColumn".equals(typeName)) {

            return com.salesforce.soap.partner.RelatedListColumn.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DeleteResult".equals(typeName)) {

            return com.salesforce.soap.partner.DeleteResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "GetUpdatedResult".equals(typeName)) {

            return com.salesforce.soap.partner.GetUpdatedResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "SearchRecord".equals(typeName)) {

            return com.salesforce.soap.partner.SearchRecord.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "NamedLayoutInfo".equals(typeName)) {

            return com.salesforce.soap.partner.NamedLayoutInfo.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeQuickActionResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeQuickActionResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "PicklistEntry".equals(typeName)) {

            return com.salesforce.soap.partner.PicklistEntry.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeSoqlListViewResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeSoqlListViewResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "KnowledgeSettings".equals(typeName)) {

            return com.salesforce.soap.partner.KnowledgeSettings.Factory.parse(reader);

        }

        if ("urn:fault.partner.soap.sforce.com".equals(namespaceURI) && "InvalidIdFault".equals(typeName)) {

            return com.salesforce.soap.partner.fault.InvalidIdFault.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "ListViewRecord".equals(typeName)) {

            return com.salesforce.soap.partner.ListViewRecord.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "RecordTypeMapping".equals(typeName)) {

            return com.salesforce.soap.partner.RecordTypeMapping.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeGlobalSObjectResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeGlobalSObjectResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeLayout".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeLayout.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "TabOrderType".equals(typeName)) {

            return com.salesforce.soap.partner.TabOrderType.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "InvalidateSessionsResult".equals(typeName)) {

            return com.salesforce.soap.partner.InvalidateSessionsResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "WebLinkPosition".equals(typeName)) {

            return com.salesforce.soap.partner.WebLinkPosition.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "UpsertResult".equals(typeName)) {

            return com.salesforce.soap.partner.UpsertResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "soapType".equals(typeName)) {

            return com.salesforce.soap.partner.SoapType.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeSObjectResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeSObjectResult.Factory.parse(reader);

        }

        if ("urn:fault.partner.soap.sforce.com".equals(namespaceURI) && "InvalidSObjectFault".equals(typeName)) {

            return com.salesforce.soap.partner.fault.InvalidSObjectFault.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeCompactLayoutsResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeCompactLayoutsResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "Field".equals(typeName)) {

            return com.salesforce.soap.partner.Field.Factory.parse(reader);

        }

        if ("urn:fault.partner.soap.sforce.com".equals(namespaceURI) && "InvalidFieldFault".equals(typeName)) {

            return com.salesforce.soap.partner.fault.InvalidFieldFault.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeAppMenuItem".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeAppMenuItem.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "PicklistForRecordType".equals(typeName)) {

            return com.salesforce.soap.partner.PicklistForRecordType.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeTabSetResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeTabSetResult.Factory.parse(reader);

        }

        if ("urn:fault.partner.soap.sforce.com".equals(namespaceURI) && "InvalidNewPasswordFault".equals(typeName)) {

            return com.salesforce.soap.partner.fault.InvalidNewPasswordFault.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "SendEmailResult".equals(typeName)) {

            return com.salesforce.soap.partner.SendEmailResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "Gender".equals(typeName)) {

            return com.salesforce.soap.partner.Gender.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "ListViewRecordColumn".equals(typeName)) {

            return com.salesforce.soap.partner.ListViewRecordColumn.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeLayoutSection".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeLayoutSection.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeLayoutResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeLayoutResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeGlobalResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeGlobalResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "GetDeletedResult".equals(typeName)) {

            return com.salesforce.soap.partner.GetDeletedResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeGlobalTheme".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeGlobalTheme.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "FilteredLookupInfo".equals(typeName)) {

            return com.salesforce.soap.partner.FilteredLookupInfo.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "orderByDirection".equals(typeName)) {

            return com.salesforce.soap.partner.OrderByDirection.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DataCategory".equals(typeName)) {

            return com.salesforce.soap.partner.DataCategory.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DataCategoryGroupSobjectTypePair".equals(typeName)) {

            return com.salesforce.soap.partner.DataCategoryGroupSobjectTypePair.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeDataCategoryGroupResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeDataCategoryGroupResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "EmailPriority".equals(typeName)) {

            return com.salesforce.soap.partner.EmailPriority.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "PackageVersion".equals(typeName)) {

            return com.salesforce.soap.partner.PackageVersion.Factory.parse(reader);

        }

        if ("urn:fault.partner.soap.sforce.com".equals(namespaceURI) && "MalformedSearchFault".equals(typeName)) {

            return com.salesforce.soap.partner.fault.MalformedSearchFault.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "listViewIsSoqlCompatible".equals(typeName)) {

            return com.salesforce.soap.partner.ListViewIsSoqlCompatible.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeSoqlListView".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeSoqlListView.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "ID".equals(typeName)) {

            return com.salesforce.soap.partner.ID.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeSoftphoneScreenPopOption".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeSoftphoneScreenPopOption.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeIcon".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeIcon.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "SearchResult".equals(typeName)) {

            return com.salesforce.soap.partner.SearchResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "GrammaticalNumber".equals(typeName)) {

            return com.salesforce.soap.partner.GrammaticalNumber.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DeletedRecord".equals(typeName)) {

            return com.salesforce.soap.partner.DeletedRecord.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "QuickActionTemplateResult".equals(typeName)) {

            return com.salesforce.soap.partner.QuickActionTemplateResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeQuickActionListItemResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeQuickActionListItemResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "layoutComponentType".equals(typeName)) {

            return com.salesforce.soap.partner.LayoutComponentType.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "ProcessRequest".equals(typeName)) {

            return com.salesforce.soap.partner.ProcessRequest.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "LeadConvert".equals(typeName)) {

            return com.salesforce.soap.partner.LeadConvert.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeSearchLayoutResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeSearchLayoutResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeThemeItem".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeThemeItem.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeFlexiPageRegion".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeFlexiPageRegion.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "CaseType".equals(typeName)) {

            return com.salesforce.soap.partner.CaseType.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "StatusCode".equals(typeName)) {

            return com.salesforce.soap.partner.StatusCode.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeComponentInstanceProperty".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeComponentInstanceProperty.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "WebLinkWindowType".equals(typeName)) {

            return com.salesforce.soap.partner.WebLinkWindowType.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "GetServerTimestampResult".equals(typeName)) {

            return com.salesforce.soap.partner.GetServerTimestampResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "RelatedList".equals(typeName)) {

            return com.salesforce.soap.partner.RelatedList.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "FeedLayoutFilterType".equals(typeName)) {

            return com.salesforce.soap.partner.FeedLayoutFilterType.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeLayoutButtonSection".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeLayoutButtonSection.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeQuickActionListResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeQuickActionListResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "MergeResult".equals(typeName)) {

            return com.salesforce.soap.partner.MergeResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "RelatedListSort".equals(typeName)) {

            return com.salesforce.soap.partner.RelatedListSort.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeAvailableQuickActionResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeAvailableQuickActionResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "Article".equals(typeName)) {

            return com.salesforce.soap.partner.Article.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "StartsWith".equals(typeName)) {

            return com.salesforce.soap.partner.StartsWith.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "RecordTypeCompactLayoutMapping".equals(typeName)) {

            return com.salesforce.soap.partner.RecordTypeCompactLayoutMapping.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "fieldType".equals(typeName)) {

            return com.salesforce.soap.partner.FieldType.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "ActionOverride".equals(typeName)) {

            return com.salesforce.soap.partner.ActionOverride.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DebugLevel".equals(typeName)) {

            return com.salesforce.soap.partner.DebugLevel.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "ShareAccessLevel".equals(typeName)) {

            return com.salesforce.soap.partner.ShareAccessLevel.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "SaveResult".equals(typeName)) {

            return com.salesforce.soap.partner.SaveResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeLayoutItem".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeLayoutItem.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "ListViewColumn".equals(typeName)) {

            return com.salesforce.soap.partner.ListViewColumn.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "WebLinkType".equals(typeName)) {

            return com.salesforce.soap.partner.WebLinkType.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "ListViewOrderBy".equals(typeName)) {

            return com.salesforce.soap.partner.ListViewOrderBy.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeLayoutButton".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeLayoutButton.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "orderByNullsPosition".equals(typeName)) {

            return com.salesforce.soap.partner.OrderByNullsPosition.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "SendEmailError".equals(typeName)) {

            return com.salesforce.soap.partner.SendEmailError.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeFlexiPageResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeFlexiPageResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeLayoutFeedFilter".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeLayoutFeedFilter.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeSoftphoneLayoutInfoField".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeSoftphoneLayoutInfoField.Factory.parse(reader);

        }

        if ("urn:fault.partner.soap.sforce.com".equals(namespaceURI) && "UnexpectedErrorFault".equals(typeName)) {

            return com.salesforce.soap.partner.fault.UnexpectedErrorFault.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeDataCategoryGroupStructureResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeDataCategoryGroupStructureResult.Factory.parse(reader);

        }

        if ("urn:fault.partner.soap.sforce.com".equals(namespaceURI) && "InvalidQueryLocatorFault".equals(typeName)) {

            return com.salesforce.soap.partner.fault.InvalidQueryLocatorFault.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "SoqlWhereCondition".equals(typeName)) {

            return com.salesforce.soap.partner.SoqlWhereCondition.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeSearchScopeOrderResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeSearchScopeOrderResult.Factory.parse(reader);

        }

        if ("urn:fault.partner.soap.sforce.com".equals(namespaceURI) && "MalformedQueryFault".equals(typeName)) {

            return com.salesforce.soap.partner.fault.MalformedQueryFault.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "AppMenuType".equals(typeName)) {

            return com.salesforce.soap.partner.AppMenuType.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "QueryResult".equals(typeName)) {

            return com.salesforce.soap.partner.QueryResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeLayoutFeedView".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeLayoutFeedView.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "LeadConvertResult".equals(typeName)) {

            return com.salesforce.soap.partner.LeadConvertResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeLayoutComponent".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeLayoutComponent.Factory.parse(reader);

        }

        if ("urn:fault.partner.soap.sforce.com".equals(namespaceURI) && "ApiQueryFault".equals(typeName)) {

            return com.salesforce.soap.partner.fault.ApiQueryFault.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "LimitInfo".equals(typeName)) {

            return com.salesforce.soap.partner.LimitInfo.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeRelatedContentItem".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeRelatedContentItem.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "ChildRelationship".equals(typeName)) {

            return com.salesforce.soap.partner.ChildRelationship.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "GetUserInfoResult".equals(typeName)) {

            return com.salesforce.soap.partner.GetUserInfoResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "Email".equals(typeName)) {

            return com.salesforce.soap.partner.Email.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeComponentInstance".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeComponentInstance.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "EmptyRecycleBinResult".equals(typeName)) {

            return com.salesforce.soap.partner.EmptyRecycleBinResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "RelatedContent".equals(typeName)) {

            return com.salesforce.soap.partner.RelatedContent.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "KnowledgeLanguageItem".equals(typeName)) {

            return com.salesforce.soap.partner.KnowledgeLanguageItem.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "RecordTypeInfo".equals(typeName)) {

            return com.salesforce.soap.partner.RecordTypeInfo.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeColumn".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeColumn.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "SearchSnippet".equals(typeName)) {

            return com.salesforce.soap.partner.SearchSnippet.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeApprovalLayoutResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeApprovalLayoutResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeColor".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeColor.Factory.parse(reader);

        }

        if ("urn:fault.partner.soap.sforce.com".equals(namespaceURI) && "ApiFault".equals(typeName)) {

            return com.salesforce.soap.partner.fault.ApiFault.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "ResetPasswordResult".equals(typeName)) {

            return com.salesforce.soap.partner.ResetPasswordResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "PerformQuickActionResult".equals(typeName)) {

            return com.salesforce.soap.partner.PerformQuickActionResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeSoqlListViewParams".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeSoqlListViewParams.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "PerformQuickActionRequest".equals(typeName)) {

            return com.salesforce.soap.partner.PerformQuickActionRequest.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "ExecuteListViewRequest".equals(typeName)) {

            return com.salesforce.soap.partner.ExecuteListViewRequest.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "Possessive".equals(typeName)) {

            return com.salesforce.soap.partner.Possessive.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeSoftphoneLayoutSection".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeSoftphoneLayoutSection.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeAppMenuResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeAppMenuResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeSoftphoneLayoutItem".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeSoftphoneLayoutItem.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeSoqlListViewsRequest".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeSoqlListViewsRequest.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "ProcessResult".equals(typeName)) {

            return com.salesforce.soap.partner.ProcessResult.Factory.parse(reader);

        }

        if ("urn:fault.partner.soap.sforce.com".equals(namespaceURI) && "ExceptionCode".equals(typeName)) {

            return com.salesforce.soap.partner.fault.ExceptionCode.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "LoginResult".equals(typeName)) {

            return com.salesforce.soap.partner.LoginResult.Factory.parse(reader);

        }

        if ("urn:fault.partner.soap.sforce.com".equals(namespaceURI) && "LoginFault".equals(typeName)) {

            return com.salesforce.soap.partner.fault.LoginFault.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "NameCaseValue".equals(typeName)) {

            return com.salesforce.soap.partner.NameCaseValue.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeSoftphoneLayoutResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeSoftphoneLayoutResult.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeApprovalLayout".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeApprovalLayout.Factory.parse(reader);

        }

        if ("urn:partner.soap.sforce.com".equals(namespaceURI) && "DescribeNounResult".equals(typeName)) {

            return com.salesforce.soap.partner.DescribeNounResult.Factory.parse(reader);

        }

        throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
    }

}
