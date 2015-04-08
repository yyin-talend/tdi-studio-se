
package com.netsuite.webservices.platform.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.netsuite.webservices.activities.scheduling.CalendarEventSearchRow;
import com.netsuite.webservices.activities.scheduling.PhoneCallSearchRow;
import com.netsuite.webservices.activities.scheduling.ProjectTaskSearchRow;
import com.netsuite.webservices.activities.scheduling.ResourceAllocationSearchRow;
import com.netsuite.webservices.activities.scheduling.TaskSearchRow;
import com.netsuite.webservices.documents.filecabinet.FileSearchRow;
import com.netsuite.webservices.documents.filecabinet.FolderSearchRow;
import com.netsuite.webservices.general.communication.MessageSearchRow;
import com.netsuite.webservices.general.communication.NoteSearchRow;
import com.netsuite.webservices.lists.accounting.AccountSearchRow;
import com.netsuite.webservices.lists.accounting.AccountingPeriodSearchRow;
import com.netsuite.webservices.lists.accounting.BillingScheduleSearchRow;
import com.netsuite.webservices.lists.accounting.BinSearchRow;
import com.netsuite.webservices.lists.accounting.ClassificationSearchRow;
import com.netsuite.webservices.lists.accounting.ContactCategorySearchRow;
import com.netsuite.webservices.lists.accounting.ContactRoleSearchRow;
import com.netsuite.webservices.lists.accounting.CurrencyRateSearchRow;
import com.netsuite.webservices.lists.accounting.CustomerCategorySearchRow;
import com.netsuite.webservices.lists.accounting.CustomerMessageSearchRow;
import com.netsuite.webservices.lists.accounting.DepartmentSearchRow;
import com.netsuite.webservices.lists.accounting.ExpenseCategorySearchRow;
import com.netsuite.webservices.lists.accounting.GiftCertificateSearchRow;
import com.netsuite.webservices.lists.accounting.GlobalAccountMappingSearchRow;
import com.netsuite.webservices.lists.accounting.InventoryNumberSearchRow;
import com.netsuite.webservices.lists.accounting.ItemAccountMappingSearchRow;
import com.netsuite.webservices.lists.accounting.ItemRevisionSearchRow;
import com.netsuite.webservices.lists.accounting.ItemSearchRow;
import com.netsuite.webservices.lists.accounting.LocationSearchRow;
import com.netsuite.webservices.lists.accounting.NexusSearchRow;
import com.netsuite.webservices.lists.accounting.NoteTypeSearchRow;
import com.netsuite.webservices.lists.accounting.OtherNameCategorySearchRow;
import com.netsuite.webservices.lists.accounting.PartnerCategorySearchRow;
import com.netsuite.webservices.lists.accounting.PaymentMethodSearchRow;
import com.netsuite.webservices.lists.accounting.PriceLevelSearchRow;
import com.netsuite.webservices.lists.accounting.PricingGroupSearchRow;
import com.netsuite.webservices.lists.accounting.RevRecScheduleSearchRow;
import com.netsuite.webservices.lists.accounting.RevRecTemplateSearchRow;
import com.netsuite.webservices.lists.accounting.SalesRoleSearchRow;
import com.netsuite.webservices.lists.accounting.SubsidiarySearchRow;
import com.netsuite.webservices.lists.accounting.TermSearchRow;
import com.netsuite.webservices.lists.accounting.UnitsTypeSearchRow;
import com.netsuite.webservices.lists.accounting.VendorCategorySearchRow;
import com.netsuite.webservices.lists.accounting.WinLossReasonSearchRow;
import com.netsuite.webservices.lists.employees.EmployeeSearchRow;
import com.netsuite.webservices.lists.employees.PayrollItemSearchRow;
import com.netsuite.webservices.lists.marketing.CampaignSearchRow;
import com.netsuite.webservices.lists.marketing.CouponCodeSearchRow;
import com.netsuite.webservices.lists.marketing.PromotionCodeSearchRow;
import com.netsuite.webservices.lists.relationships.ContactSearchRow;
import com.netsuite.webservices.lists.relationships.CustomerSearchRow;
import com.netsuite.webservices.lists.relationships.CustomerStatusSearchRow;
import com.netsuite.webservices.lists.relationships.EntityGroupSearchRow;
import com.netsuite.webservices.lists.relationships.JobSearchRow;
import com.netsuite.webservices.lists.relationships.JobStatusSearchRow;
import com.netsuite.webservices.lists.relationships.JobTypeSearchRow;
import com.netsuite.webservices.lists.relationships.OriginatingLeadSearchRow;
import com.netsuite.webservices.lists.relationships.PartnerSearchRow;
import com.netsuite.webservices.lists.relationships.VendorSearchRow;
import com.netsuite.webservices.lists.supplychain.ManufacturingCostTemplateSearchRow;
import com.netsuite.webservices.lists.supplychain.ManufacturingOperationTaskSearchRow;
import com.netsuite.webservices.lists.supplychain.ManufacturingRoutingSearchRow;
import com.netsuite.webservices.lists.support.IssueSearchRow;
import com.netsuite.webservices.lists.support.SolutionSearchRow;
import com.netsuite.webservices.lists.support.SupportCaseSearchRow;
import com.netsuite.webservices.lists.support.TopicSearchRow;
import com.netsuite.webservices.lists.website.SiteCategorySearchRow;
import com.netsuite.webservices.setup.customization.AppDefinitionSearchRow;
import com.netsuite.webservices.setup.customization.AppPackageSearchRow;
import com.netsuite.webservices.setup.customization.CustomListSearchRow;
import com.netsuite.webservices.setup.customization.CustomRecordSearchRow;
import com.netsuite.webservices.transactions.customers.ChargeSearchRow;
import com.netsuite.webservices.transactions.demandplanning.ItemDemandPlanSearchRow;
import com.netsuite.webservices.transactions.demandplanning.ItemSupplyPlanSearchRow;
import com.netsuite.webservices.transactions.employees.TimeBillSearchRow;
import com.netsuite.webservices.transactions.employees.TimeEntrySearchRow;
import com.netsuite.webservices.transactions.employees.TimeSheetSearchRow;
import com.netsuite.webservices.transactions.financial.BudgetSearchRow;
import com.netsuite.webservices.transactions.sales.AccountingTransactionSearchRow;
import com.netsuite.webservices.transactions.sales.OpportunitySearchRow;
import com.netsuite.webservices.transactions.sales.TransactionSearchRow;


/**
 * <p>Java class for SearchRow complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SearchRow">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchRow")
@XmlSeeAlso({
    ManufacturingOperationTaskSearchRow.class,
    ManufacturingCostTemplateSearchRow.class,
    ManufacturingRoutingSearchRow.class,
    ProjectTaskSearchRow.class,
    ResourceAllocationSearchRow.class,
    CalendarEventSearchRow.class,
    TaskSearchRow.class,
    PhoneCallSearchRow.class,
    CampaignSearchRow.class,
    CouponCodeSearchRow.class,
    PromotionCodeSearchRow.class,
    IssueSearchRow.class,
    SolutionSearchRow.class,
    SupportCaseSearchRow.class,
    TopicSearchRow.class,
    ItemSupplyPlanSearchRow.class,
    ItemDemandPlanSearchRow.class,
    PayrollItemSearchRow.class,
    EmployeeSearchRow.class,
    CustomRecordSearchRow.class,
    AppPackageSearchRow.class,
    CustomListSearchRow.class,
    AppDefinitionSearchRow.class,
    BudgetSearchRow.class,
    SearchRowBasic.class,
    SiteCategorySearchRow.class,
    FolderSearchRow.class,
    FileSearchRow.class,
    ChargeSearchRow.class,
    AccountingTransactionSearchRow.class,
    TransactionSearchRow.class,
    OpportunitySearchRow.class,
    MessageSearchRow.class,
    NoteSearchRow.class,
    TimeSheetSearchRow.class,
    TimeEntrySearchRow.class,
    TimeBillSearchRow.class,
    SalesRoleSearchRow.class,
    AccountSearchRow.class,
    PaymentMethodSearchRow.class,
    CustomerMessageSearchRow.class,
    CurrencyRateSearchRow.class,
    TermSearchRow.class,
    WinLossReasonSearchRow.class,
    OtherNameCategorySearchRow.class,
    PriceLevelSearchRow.class,
    UnitsTypeSearchRow.class,
    NoteTypeSearchRow.class,
    PartnerCategorySearchRow.class,
    PricingGroupSearchRow.class,
    SubsidiarySearchRow.class,
    NexusSearchRow.class,
    ClassificationSearchRow.class,
    GlobalAccountMappingSearchRow.class,
    ExpenseCategorySearchRow.class,
    CustomerCategorySearchRow.class,
    ItemSearchRow.class,
    BillingScheduleSearchRow.class,
    ItemRevisionSearchRow.class,
    BinSearchRow.class,
    LocationSearchRow.class,
    VendorCategorySearchRow.class,
    ItemAccountMappingSearchRow.class,
    ContactCategorySearchRow.class,
    ContactRoleSearchRow.class,
    RevRecTemplateSearchRow.class,
    GiftCertificateSearchRow.class,
    DepartmentSearchRow.class,
    AccountingPeriodSearchRow.class,
    RevRecScheduleSearchRow.class,
    InventoryNumberSearchRow.class,
    JobStatusSearchRow.class,
    CustomerStatusSearchRow.class,
    ContactSearchRow.class,
    VendorSearchRow.class,
    EntityGroupSearchRow.class,
    OriginatingLeadSearchRow.class,
    JobSearchRow.class,
    CustomerSearchRow.class,
    JobTypeSearchRow.class,
    PartnerSearchRow.class
})
public abstract class SearchRow {


}
