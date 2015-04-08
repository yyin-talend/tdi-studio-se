
package com.netsuite.webservices.platform.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.netsuite.webservices.activities.scheduling.CalendarEvent;
import com.netsuite.webservices.activities.scheduling.PhoneCall;
import com.netsuite.webservices.activities.scheduling.ProjectTask;
import com.netsuite.webservices.activities.scheduling.ResourceAllocation;
import com.netsuite.webservices.activities.scheduling.Task;
import com.netsuite.webservices.documents.filecabinet.File;
import com.netsuite.webservices.documents.filecabinet.Folder;
import com.netsuite.webservices.general.communication.Message;
import com.netsuite.webservices.general.communication.Note;
import com.netsuite.webservices.lists.accounting.Account;
import com.netsuite.webservices.lists.accounting.AccountingPeriod;
import com.netsuite.webservices.lists.accounting.AssemblyItem;
import com.netsuite.webservices.lists.accounting.BillingSchedule;
import com.netsuite.webservices.lists.accounting.Bin;
import com.netsuite.webservices.lists.accounting.BudgetCategory;
import com.netsuite.webservices.lists.accounting.Classification;
import com.netsuite.webservices.lists.accounting.ContactCategory;
import com.netsuite.webservices.lists.accounting.ContactRole;
import com.netsuite.webservices.lists.accounting.CostCategory;
import com.netsuite.webservices.lists.accounting.Currency;
import com.netsuite.webservices.lists.accounting.CurrencyRate;
import com.netsuite.webservices.lists.accounting.CustomerCategory;
import com.netsuite.webservices.lists.accounting.CustomerMessage;
import com.netsuite.webservices.lists.accounting.Department;
import com.netsuite.webservices.lists.accounting.DescriptionItem;
import com.netsuite.webservices.lists.accounting.DiscountItem;
import com.netsuite.webservices.lists.accounting.DownloadItem;
import com.netsuite.webservices.lists.accounting.ExpenseCategory;
import com.netsuite.webservices.lists.accounting.GiftCertificate;
import com.netsuite.webservices.lists.accounting.GiftCertificateItem;
import com.netsuite.webservices.lists.accounting.GlobalAccountMapping;
import com.netsuite.webservices.lists.accounting.InventoryItem;
import com.netsuite.webservices.lists.accounting.InventoryNumber;
import com.netsuite.webservices.lists.accounting.ItemAccountMapping;
import com.netsuite.webservices.lists.accounting.ItemGroup;
import com.netsuite.webservices.lists.accounting.ItemRevision;
import com.netsuite.webservices.lists.accounting.KitItem;
import com.netsuite.webservices.lists.accounting.LeadSource;
import com.netsuite.webservices.lists.accounting.Location;
import com.netsuite.webservices.lists.accounting.LotNumberedAssemblyItem;
import com.netsuite.webservices.lists.accounting.LotNumberedInventoryItem;
import com.netsuite.webservices.lists.accounting.MarkupItem;
import com.netsuite.webservices.lists.accounting.Nexus;
import com.netsuite.webservices.lists.accounting.NonInventoryPurchaseItem;
import com.netsuite.webservices.lists.accounting.NonInventoryResaleItem;
import com.netsuite.webservices.lists.accounting.NonInventorySaleItem;
import com.netsuite.webservices.lists.accounting.NoteType;
import com.netsuite.webservices.lists.accounting.OtherChargePurchaseItem;
import com.netsuite.webservices.lists.accounting.OtherChargeResaleItem;
import com.netsuite.webservices.lists.accounting.OtherChargeSaleItem;
import com.netsuite.webservices.lists.accounting.OtherNameCategory;
import com.netsuite.webservices.lists.accounting.PartnerCategory;
import com.netsuite.webservices.lists.accounting.PaymentItem;
import com.netsuite.webservices.lists.accounting.PaymentMethod;
import com.netsuite.webservices.lists.accounting.PriceLevel;
import com.netsuite.webservices.lists.accounting.PricingGroup;
import com.netsuite.webservices.lists.accounting.RevRecSchedule;
import com.netsuite.webservices.lists.accounting.RevRecTemplate;
import com.netsuite.webservices.lists.accounting.SalesRole;
import com.netsuite.webservices.lists.accounting.SalesTaxItem;
import com.netsuite.webservices.lists.accounting.SerializedAssemblyItem;
import com.netsuite.webservices.lists.accounting.SerializedInventoryItem;
import com.netsuite.webservices.lists.accounting.ServicePurchaseItem;
import com.netsuite.webservices.lists.accounting.ServiceResaleItem;
import com.netsuite.webservices.lists.accounting.ServiceSaleItem;
import com.netsuite.webservices.lists.accounting.State;
import com.netsuite.webservices.lists.accounting.Subsidiary;
import com.netsuite.webservices.lists.accounting.SubtotalItem;
import com.netsuite.webservices.lists.accounting.TaxAcct;
import com.netsuite.webservices.lists.accounting.TaxGroup;
import com.netsuite.webservices.lists.accounting.TaxType;
import com.netsuite.webservices.lists.accounting.Term;
import com.netsuite.webservices.lists.accounting.UnitsType;
import com.netsuite.webservices.lists.accounting.VendorCategory;
import com.netsuite.webservices.lists.accounting.WinLossReason;
import com.netsuite.webservices.lists.employees.Employee;
import com.netsuite.webservices.lists.employees.PayrollItem;
import com.netsuite.webservices.lists.marketing.Campaign;
import com.netsuite.webservices.lists.marketing.CampaignAudience;
import com.netsuite.webservices.lists.marketing.CampaignCategory;
import com.netsuite.webservices.lists.marketing.CampaignChannel;
import com.netsuite.webservices.lists.marketing.CampaignFamily;
import com.netsuite.webservices.lists.marketing.CampaignOffer;
import com.netsuite.webservices.lists.marketing.CampaignResponse;
import com.netsuite.webservices.lists.marketing.CampaignSearchEngine;
import com.netsuite.webservices.lists.marketing.CampaignSubscription;
import com.netsuite.webservices.lists.marketing.CampaignVertical;
import com.netsuite.webservices.lists.marketing.CouponCode;
import com.netsuite.webservices.lists.marketing.PromotionCode;
import com.netsuite.webservices.lists.relationships.Contact;
import com.netsuite.webservices.lists.relationships.Customer;
import com.netsuite.webservices.lists.relationships.CustomerStatus;
import com.netsuite.webservices.lists.relationships.EntityGroup;
import com.netsuite.webservices.lists.relationships.Job;
import com.netsuite.webservices.lists.relationships.JobStatus;
import com.netsuite.webservices.lists.relationships.JobType;
import com.netsuite.webservices.lists.relationships.Partner;
import com.netsuite.webservices.lists.relationships.Vendor;
import com.netsuite.webservices.lists.supplychain.ManufacturingCostTemplate;
import com.netsuite.webservices.lists.supplychain.ManufacturingOperationTask;
import com.netsuite.webservices.lists.supplychain.ManufacturingRouting;
import com.netsuite.webservices.lists.support.Issue;
import com.netsuite.webservices.lists.support.Solution;
import com.netsuite.webservices.lists.support.SupportCase;
import com.netsuite.webservices.lists.support.SupportCaseIssue;
import com.netsuite.webservices.lists.support.SupportCaseOrigin;
import com.netsuite.webservices.lists.support.SupportCasePriority;
import com.netsuite.webservices.lists.support.SupportCaseStatus;
import com.netsuite.webservices.lists.support.SupportCaseType;
import com.netsuite.webservices.lists.support.Topic;
import com.netsuite.webservices.lists.website.SiteCategory;
import com.netsuite.webservices.platform.common.Address;
import com.netsuite.webservices.platform.common.InventoryDetail;
import com.netsuite.webservices.platform.common.LandedCost;
import com.netsuite.webservices.setup.customization.AppDefinition;
import com.netsuite.webservices.setup.customization.AppPackage;
import com.netsuite.webservices.setup.customization.CustomFieldType;
import com.netsuite.webservices.setup.customization.CustomList;
import com.netsuite.webservices.setup.customization.CustomRecord;
import com.netsuite.webservices.setup.customization.CustomRecordType;
import com.netsuite.webservices.transactions.bank.Check;
import com.netsuite.webservices.transactions.bank.Deposit;
import com.netsuite.webservices.transactions.customers.CashRefund;
import com.netsuite.webservices.transactions.customers.Charge;
import com.netsuite.webservices.transactions.customers.CreditMemo;
import com.netsuite.webservices.transactions.customers.CustomerDeposit;
import com.netsuite.webservices.transactions.customers.CustomerPayment;
import com.netsuite.webservices.transactions.customers.CustomerRefund;
import com.netsuite.webservices.transactions.customers.DepositApplication;
import com.netsuite.webservices.transactions.customers.ReturnAuthorization;
import com.netsuite.webservices.transactions.demandplanning.ItemDemandPlan;
import com.netsuite.webservices.transactions.demandplanning.ItemSupplyPlan;
import com.netsuite.webservices.transactions.employees.ExpenseReport;
import com.netsuite.webservices.transactions.employees.PaycheckJournal;
import com.netsuite.webservices.transactions.employees.TimeBill;
import com.netsuite.webservices.transactions.employees.TimeEntry;
import com.netsuite.webservices.transactions.employees.TimeSheet;
import com.netsuite.webservices.transactions.financial.Budget;
import com.netsuite.webservices.transactions.general.InterCompanyJournalEntry;
import com.netsuite.webservices.transactions.general.JournalEntry;
import com.netsuite.webservices.transactions.general.StatisticalJournalEntry;
import com.netsuite.webservices.transactions.inventory.AssemblyBuild;
import com.netsuite.webservices.transactions.inventory.AssemblyUnbuild;
import com.netsuite.webservices.transactions.inventory.BinTransfer;
import com.netsuite.webservices.transactions.inventory.BinWorksheet;
import com.netsuite.webservices.transactions.inventory.InterCompanyTransferOrder;
import com.netsuite.webservices.transactions.inventory.InventoryAdjustment;
import com.netsuite.webservices.transactions.inventory.InventoryCostRevaluation;
import com.netsuite.webservices.transactions.inventory.InventoryTransfer;
import com.netsuite.webservices.transactions.inventory.TransferOrder;
import com.netsuite.webservices.transactions.inventory.WorkOrder;
import com.netsuite.webservices.transactions.inventory.WorkOrderClose;
import com.netsuite.webservices.transactions.inventory.WorkOrderCompletion;
import com.netsuite.webservices.transactions.inventory.WorkOrderIssue;
import com.netsuite.webservices.transactions.purchases.ItemReceipt;
import com.netsuite.webservices.transactions.purchases.PurchaseOrder;
import com.netsuite.webservices.transactions.purchases.VendorBill;
import com.netsuite.webservices.transactions.purchases.VendorCredit;
import com.netsuite.webservices.transactions.purchases.VendorPayment;
import com.netsuite.webservices.transactions.purchases.VendorReturnAuthorization;
import com.netsuite.webservices.transactions.sales.CashSale;
import com.netsuite.webservices.transactions.sales.Estimate;
import com.netsuite.webservices.transactions.sales.Invoice;
import com.netsuite.webservices.transactions.sales.ItemFulfillment;
import com.netsuite.webservices.transactions.sales.Opportunity;
import com.netsuite.webservices.transactions.sales.SalesOrder;


/**
 * <p>Java class for Record complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Record">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nullFieldList" type="{urn:core_2014_2.platform.webservices.netsuite.com}NullField" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Record", propOrder = {
    "nullFieldList"
})
@XmlSeeAlso({
    ManufacturingCostTemplate.class,
    ManufacturingRouting.class,
    ManufacturingOperationTask.class,
    InterCompanyJournalEntry.class,
    StatisticalJournalEntry.class,
    JournalEntry.class,
    ResourceAllocation.class,
    ProjectTask.class,
    Task.class,
    PhoneCall.class,
    CalendarEvent.class,
    CampaignResponse.class,
    PromotionCode.class,
    CampaignChannel.class,
    CampaignOffer.class,
    CouponCode.class,
    CampaignVertical.class,
    CampaignCategory.class,
    Campaign.class,
    CampaignSearchEngine.class,
    CampaignAudience.class,
    CampaignFamily.class,
    CampaignSubscription.class,
    LandedCost.class,
    InventoryDetail.class,
    Address.class,
    Issue.class,
    SupportCaseIssue.class,
    Topic.class,
    SupportCasePriority.class,
    SupportCaseType.class,
    Solution.class,
    SupportCase.class,
    SupportCaseOrigin.class,
    SupportCaseStatus.class,
    ItemSupplyPlan.class,
    ItemDemandPlan.class,
    PayrollItem.class,
    Employee.class,
    Check.class,
    Deposit.class,
    CustomRecordType.class,
    AppPackage.class,
    CustomRecord.class,
    CustomList.class,
    AppDefinition.class,
    CustomFieldType.class,
    Budget.class,
    SiteCategory.class,
    Folder.class,
    File.class,
    Charge.class,
    CreditMemo.class,
    ReturnAuthorization.class,
    DepositApplication.class,
    CustomerDeposit.class,
    CashRefund.class,
    CustomerPayment.class,
    CustomerRefund.class,
    Estimate.class,
    ItemFulfillment.class,
    SalesOrder.class,
    Opportunity.class,
    CashSale.class,
    Invoice.class,
    Message.class,
    Note.class,
    VendorBill.class,
    VendorCredit.class,
    VendorReturnAuthorization.class,
    ItemReceipt.class,
    VendorPayment.class,
    PurchaseOrder.class,
    TimeEntry.class,
    ExpenseReport.class,
    PaycheckJournal.class,
    TimeBill.class,
    TimeSheet.class,
    NonInventoryPurchaseItem.class,
    SalesRole.class,
    LeadSource.class,
    OtherChargeResaleItem.class,
    ServicePurchaseItem.class,
    Classification.class,
    Currency.class,
    NonInventorySaleItem.class,
    ContactRole.class,
    Nexus.class,
    CostCategory.class,
    SerializedInventoryItem.class,
    LotNumberedAssemblyItem.class,
    AssemblyItem.class,
    Term.class,
    PriceLevel.class,
    SerializedAssemblyItem.class,
    Bin.class,
    CustomerCategory.class,
    BillingSchedule.class,
    ContactCategory.class,
    KitItem.class,
    SalesTaxItem.class,
    BudgetCategory.class,
    RevRecSchedule.class,
    WinLossReason.class,
    ItemRevision.class,
    MarkupItem.class,
    InventoryNumber.class,
    Subsidiary.class,
    AccountingPeriod.class,
    GiftCertificateItem.class,
    PaymentItem.class,
    ServiceResaleItem.class,
    NoteType.class,
    TaxAcct.class,
    PricingGroup.class,
    ItemGroup.class,
    GlobalAccountMapping.class,
    ServiceSaleItem.class,
    PartnerCategory.class,
    CustomerMessage.class,
    DiscountItem.class,
    CurrencyRate.class,
    ItemAccountMapping.class,
    GiftCertificate.class,
    PaymentMethod.class,
    TaxGroup.class,
    LotNumberedInventoryItem.class,
    TaxType.class,
    DownloadItem.class,
    InventoryItem.class,
    RevRecTemplate.class,
    Account.class,
    State.class,
    VendorCategory.class,
    OtherNameCategory.class,
    OtherChargeSaleItem.class,
    Department.class,
    NonInventoryResaleItem.class,
    DescriptionItem.class,
    Location.class,
    UnitsType.class,
    ExpenseCategory.class,
    OtherChargePurchaseItem.class,
    SubtotalItem.class,
    Customer.class,
    Contact.class,
    JobStatus.class,
    EntityGroup.class,
    CustomerStatus.class,
    Job.class,
    JobType.class,
    Vendor.class,
    Partner.class,
    AssemblyUnbuild.class,
    InventoryCostRevaluation.class,
    WorkOrderClose.class,
    WorkOrderCompletion.class,
    BinWorksheet.class,
    WorkOrderIssue.class,
    TransferOrder.class,
    BinTransfer.class,
    InventoryAdjustment.class,
    AssemblyBuild.class,
    InterCompanyTransferOrder.class,
    InventoryTransfer.class,
    WorkOrder.class
})
public abstract class Record {

    protected NullField nullFieldList;

    /**
     * Gets the value of the nullFieldList property.
     * 
     * @return
     *     possible object is
     *     {@link NullField }
     *     
     */
    public NullField getNullFieldList() {
        return nullFieldList;
    }

    /**
     * Sets the value of the nullFieldList property.
     * 
     * @param value
     *     allowed object is
     *     {@link NullField }
     *     
     */
    public void setNullFieldList(NullField value) {
        this.nullFieldList = value;
    }

}
