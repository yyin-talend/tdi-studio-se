package org.talend.netsuite;

import com.netsuite.webservices.platform.common.TransactionSearchBasic;

public class TalendComponentGenerator { public TalendComponentGenerator() {}

	private static Class<?> findClass(Class<?>[][] classlist, String classname, int key) {
		for (int i = 0; i < classlist.length; i++) {
			if (classlist[i][0].getName().equals(classname)) {
				return classlist[i][key];
			}
		}
		return null;
	}

	public static Class<?> findSearchBasicClass(String classname) {
		for (int i = 0; i < netSuiteClasses.length; i++) {
			if (netSuiteClasses[i][2].getSimpleName().equals(classname)) {
				return netSuiteClasses[i][2];
			}
		}
		return null;
	}

	public static Class<?> getEntityClass(String classname) {
		return findClass(netSuiteClasses, classname, 0);
	}

	public static Class<?> getSearchClass(String classname) {
		return findClass(netSuiteClasses, classname, 1);
	}

	public static Class<?> getSearchBasicClass(String classname) {
		return findClass(netSuiteClasses, classname, 2);
	}

	public static Class<?> getSearchAdvancedClass(String classname) {
		return findClass(netSuiteClasses, classname, 3);
	}

	public static Class<?> getListItemClass(String classname) {
		return findClass(netSuiteListClasses, classname, 0);
	}

	public static Class<?> getListParentClass(String classname) {
		return findClass(netSuiteListClasses, classname, 1);
	}

	public static Class<?> getListClass(String classname) {
		return findClass(netSuiteListClasses, classname, 2);
	}

	public static String toInitialUpper(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}

	public static String toInitialLower(String value) {
		return value.substring(0, 1).toLowerCase() + value.substring(1);
	}

	public static String toNetSuiteType(String value) {
		return "_" + toInitialLower(value);
	}
  
    public static String[] transactionTypeList = { "AssemblyBuild", "AssemblyUnbuild", "CashRefund", "CashSale", "Check", "CreditMemo", "CustomerDeposit", "CustomerPayment", "CustomerRefund", "DepositApplication", "Estimate", "ExpenseReport", "InventoryAdjustment", "Invoice", "ItemFulfillment", "ItemReceipt", "JournalEntry", "Opportunity", "PurchaseOrder", "ReturnAuthorization", "SalesOrder", "TransferOrder", "VendorBill", "VendorPayment" };
    public static String[] itemTypeList = { "Assembly", "Description", "Discount", "DownloadItem", "GiftCertificateItem", "InventoryItem", "ItemGroup", "Kit", "Markup", "NonInventoryItem", "OtherCharge", "Payment", "Service", "Subtotal" };
  
    public static final Class<?>[][] netSuiteClasses = {
	    { com.netsuite.webservices.lists.accounting.AccountingPeriod.class, com.netsuite.webservices.lists.accounting.AccountingPeriodSearch.class, com.netsuite.webservices.platform.common.AccountingPeriodSearchBasic.class, com.netsuite.webservices.lists.accounting.AccountingPeriodSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.Subsidiary.class, com.netsuite.webservices.lists.accounting.SubsidiarySearch.class, com.netsuite.webservices.platform.common.SubsidiarySearchBasic.class, com.netsuite.webservices.lists.accounting.SubsidiarySearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.SalesRole.class, com.netsuite.webservices.lists.accounting.SalesRoleSearch.class, com.netsuite.webservices.platform.common.SalesRoleSearchBasic.class, com.netsuite.webservices.lists.accounting.SalesRoleSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.GiftCertificateItem.class, com.netsuite.webservices.lists.accounting.ItemSearch.class, com.netsuite.webservices.platform.common.ItemSearchBasic.class, com.netsuite.webservices.lists.accounting.ItemSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.purchases.VendorPayment.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.PaymentItem.class, com.netsuite.webservices.lists.accounting.ItemSearch.class, com.netsuite.webservices.platform.common.ItemSearchBasic.class, com.netsuite.webservices.lists.accounting.ItemSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.NoteType.class, com.netsuite.webservices.lists.accounting.NoteTypeSearch.class, com.netsuite.webservices.platform.common.NoteTypeSearchBasic.class, com.netsuite.webservices.lists.accounting.NoteTypeSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.support.Issue.class, com.netsuite.webservices.lists.support.IssueSearch.class, com.netsuite.webservices.platform.common.IssueSearchBasic.class, com.netsuite.webservices.lists.support.IssueSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.sales.ItemFulfillment.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.inventory.AssemblyUnbuild.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.Classification.class, com.netsuite.webservices.lists.accounting.ClassificationSearch.class, com.netsuite.webservices.platform.common.ClassificationSearchBasic.class, com.netsuite.webservices.lists.accounting.ClassificationSearchAdvanced.class }, 
	    { com.netsuite.webservices.activities.scheduling.ProjectTask.class, com.netsuite.webservices.activities.scheduling.ProjectTaskSearch.class, com.netsuite.webservices.platform.common.ProjectTaskSearchBasic.class, com.netsuite.webservices.activities.scheduling.ProjectTaskSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.website.SiteCategory.class, com.netsuite.webservices.lists.website.SiteCategorySearch.class, com.netsuite.webservices.platform.common.SiteCategorySearchBasic.class, com.netsuite.webservices.lists.website.SiteCategorySearchAdvanced.class }, 
	    { com.netsuite.webservices.activities.scheduling.CalendarEvent.class, com.netsuite.webservices.activities.scheduling.CalendarEventSearch.class, com.netsuite.webservices.platform.common.CalendarEventSearchBasic.class, com.netsuite.webservices.activities.scheduling.CalendarEventSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.customers.CashRefund.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.PartnerCategory.class, com.netsuite.webservices.lists.accounting.PartnerCategorySearch.class, com.netsuite.webservices.platform.common.PartnerCategorySearchBasic.class, com.netsuite.webservices.lists.accounting.PartnerCategorySearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.ContactRole.class, com.netsuite.webservices.lists.accounting.ContactRoleSearch.class, com.netsuite.webservices.platform.common.ContactRoleSearchBasic.class, com.netsuite.webservices.lists.accounting.ContactRoleSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.KitItem.class, com.netsuite.webservices.lists.accounting.ItemSearch.class, com.netsuite.webservices.platform.common.ItemSearchBasic.class, com.netsuite.webservices.lists.accounting.ItemSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.customers.CreditMemo.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.DiscountItem.class, com.netsuite.webservices.lists.accounting.ItemSearch.class, com.netsuite.webservices.platform.common.ItemSearchBasic.class, com.netsuite.webservices.lists.accounting.ItemSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.customers.ReturnAuthorization.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.sales.Invoice.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.GiftCertificate.class, com.netsuite.webservices.lists.accounting.GiftCertificateSearch.class, com.netsuite.webservices.platform.common.GiftCertificateSearchBasic.class, com.netsuite.webservices.lists.accounting.GiftCertificateSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.PaymentMethod.class, com.netsuite.webservices.lists.accounting.PaymentMethodSearch.class, com.netsuite.webservices.platform.common.PaymentMethodSearchBasic.class, com.netsuite.webservices.lists.accounting.PaymentMethodSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.customers.CustomerDeposit.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.support.SupportCase.class, com.netsuite.webservices.lists.support.SupportCaseSearch.class, com.netsuite.webservices.platform.common.SupportCaseSearchBasic.class, com.netsuite.webservices.lists.support.SupportCaseSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.purchases.PurchaseOrder.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.Term.class, com.netsuite.webservices.lists.accounting.TermSearch.class, com.netsuite.webservices.platform.common.TermSearchBasic.class, com.netsuite.webservices.lists.accounting.TermSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.PriceLevel.class, com.netsuite.webservices.lists.accounting.PriceLevelSearch.class, com.netsuite.webservices.platform.common.PriceLevelSearchBasic.class, com.netsuite.webservices.lists.accounting.PriceLevelSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.marketing.Campaign.class, com.netsuite.webservices.lists.marketing.CampaignSearch.class, com.netsuite.webservices.platform.common.CampaignSearchBasic.class, com.netsuite.webservices.lists.marketing.CampaignSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.PricingGroup.class, com.netsuite.webservices.lists.accounting.PricingGroupSearch.class, com.netsuite.webservices.platform.common.PricingGroupSearchBasic.class, com.netsuite.webservices.lists.accounting.PricingGroupSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.Bin.class, com.netsuite.webservices.lists.accounting.BinSearch.class, com.netsuite.webservices.platform.common.BinSearchBasic.class, com.netsuite.webservices.lists.accounting.BinSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.sales.SalesOrder.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.activities.scheduling.Task.class, com.netsuite.webservices.activities.scheduling.TaskSearch.class, com.netsuite.webservices.platform.common.TaskSearchBasic.class, com.netsuite.webservices.activities.scheduling.TaskSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.purchases.ItemReceipt.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.sales.CashSale.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.marketing.PromotionCode.class, com.netsuite.webservices.lists.marketing.PromotionCodeSearch.class, com.netsuite.webservices.platform.common.PromotionCodeSearchBasic.class, com.netsuite.webservices.lists.marketing.PromotionCodeSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.inventory.TransferOrder.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.CustomerCategory.class, com.netsuite.webservices.lists.accounting.CustomerCategorySearch.class, com.netsuite.webservices.platform.common.CustomerCategorySearchBasic.class, com.netsuite.webservices.lists.accounting.CustomerCategorySearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.InventoryItem.class, com.netsuite.webservices.lists.accounting.ItemSearch.class, com.netsuite.webservices.platform.common.ItemSearchBasic.class, com.netsuite.webservices.lists.accounting.ItemSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.employees.ExpenseReport.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.ContactCategory.class, com.netsuite.webservices.lists.accounting.ContactCategorySearch.class, com.netsuite.webservices.platform.common.ContactCategorySearchBasic.class, com.netsuite.webservices.lists.accounting.ContactCategorySearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.employees.TimeBill.class, com.netsuite.webservices.transactions.employees.TimeBillSearch.class, com.netsuite.webservices.platform.common.TimeBillSearchBasic.class, com.netsuite.webservices.transactions.employees.TimeBillSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.Account.class, com.netsuite.webservices.lists.accounting.AccountSearch.class, com.netsuite.webservices.platform.common.AccountSearchBasic.class, com.netsuite.webservices.lists.accounting.AccountSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.relationships.JobStatus.class, com.netsuite.webservices.lists.relationships.JobStatusSearch.class, com.netsuite.webservices.platform.common.JobStatusSearchBasic.class, com.netsuite.webservices.lists.relationships.JobStatusSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.VendorCategory.class, com.netsuite.webservices.lists.accounting.VendorCategorySearch.class, com.netsuite.webservices.platform.common.VendorCategorySearchBasic.class, com.netsuite.webservices.lists.accounting.VendorCategorySearchAdvanced.class }, 
	    { com.netsuite.webservices.activities.scheduling.PhoneCall.class, com.netsuite.webservices.activities.scheduling.PhoneCallSearch.class, com.netsuite.webservices.platform.common.PhoneCallSearchBasic.class, com.netsuite.webservices.activities.scheduling.PhoneCallSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.relationships.CustomerStatus.class, com.netsuite.webservices.lists.relationships.CustomerStatusSearch.class, com.netsuite.webservices.platform.common.CustomerStatusSearchBasic.class, com.netsuite.webservices.lists.relationships.CustomerStatusSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.financial.Budget.class, com.netsuite.webservices.transactions.financial.BudgetSearch.class, com.netsuite.webservices.platform.common.BudgetSearchBasic.class, com.netsuite.webservices.transactions.financial.BudgetSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.customers.CustomerPayment.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.DownloadItem.class, com.netsuite.webservices.lists.accounting.ItemSearch.class, com.netsuite.webservices.platform.common.ItemSearchBasic.class, com.netsuite.webservices.lists.accounting.ItemSearchAdvanced.class }, 
	    { com.netsuite.webservices.setup.customization.CustomList.class, com.netsuite.webservices.setup.customization.CustomListSearch.class, com.netsuite.webservices.platform.common.CustomListSearchBasic.class, com.netsuite.webservices.setup.customization.CustomListSearch.class }, 
	    { com.netsuite.webservices.transactions.sales.Opportunity.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.inventory.InventoryAdjustment.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.support.Topic.class, com.netsuite.webservices.lists.support.TopicSearch.class, com.netsuite.webservices.platform.common.TopicSearchBasic.class, com.netsuite.webservices.lists.support.TopicSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.support.Solution.class, com.netsuite.webservices.lists.support.SolutionSearch.class, com.netsuite.webservices.platform.common.SolutionSearchBasic.class, com.netsuite.webservices.lists.support.SolutionSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.Department.class, com.netsuite.webservices.lists.accounting.DepartmentSearch.class, com.netsuite.webservices.platform.common.DepartmentSearchBasic.class, com.netsuite.webservices.lists.accounting.DepartmentSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.relationships.Partner.class, com.netsuite.webservices.lists.relationships.PartnerSearch.class, com.netsuite.webservices.platform.common.PartnerSearchBasic.class, com.netsuite.webservices.lists.relationships.PartnerSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.WinLossReason.class, com.netsuite.webservices.lists.accounting.WinLossReasonSearch.class, com.netsuite.webservices.platform.common.WinLossReasonSearchBasic.class, com.netsuite.webservices.lists.accounting.WinLossReasonSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.relationships.Customer.class, com.netsuite.webservices.lists.relationships.CustomerSearch.class, com.netsuite.webservices.platform.common.CustomerSearchBasic.class, com.netsuite.webservices.lists.relationships.CustomerSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.purchases.VendorBill.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.bank.Check.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.sales.Estimate.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.general.JournalEntry.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.documents.filecabinet.Folder.class, com.netsuite.webservices.documents.filecabinet.FolderSearch.class, com.netsuite.webservices.platform.common.FolderSearchBasic.class, com.netsuite.webservices.documents.filecabinet.FolderSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.inventory.AssemblyBuild.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.general.communication.Message.class, com.netsuite.webservices.general.communication.MessageSearch.class, com.netsuite.webservices.platform.common.MessageSearchBasic.class, com.netsuite.webservices.general.communication.MessageSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.DescriptionItem.class, com.netsuite.webservices.lists.accounting.ItemSearch.class, com.netsuite.webservices.platform.common.ItemSearchBasic.class, com.netsuite.webservices.lists.accounting.ItemSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.relationships.Contact.class, com.netsuite.webservices.lists.relationships.ContactSearch.class, com.netsuite.webservices.platform.common.ContactSearchBasic.class, com.netsuite.webservices.lists.relationships.ContactSearchAdvanced.class }, 
	    { com.netsuite.webservices.documents.filecabinet.File.class, com.netsuite.webservices.documents.filecabinet.FileSearch.class, com.netsuite.webservices.platform.common.FileSearchBasic.class, com.netsuite.webservices.documents.filecabinet.FileSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.Location.class, com.netsuite.webservices.lists.accounting.LocationSearch.class, com.netsuite.webservices.platform.common.LocationSearchBasic.class, com.netsuite.webservices.lists.accounting.LocationSearchAdvanced.class }, 
	    { com.netsuite.webservices.general.communication.Note.class, com.netsuite.webservices.general.communication.NoteSearch.class, com.netsuite.webservices.platform.common.NoteSearchBasic.class, com.netsuite.webservices.general.communication.NoteSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.customers.DepositApplication.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.MarkupItem.class, com.netsuite.webservices.lists.accounting.ItemSearch.class, com.netsuite.webservices.platform.common.ItemSearchBasic.class, com.netsuite.webservices.lists.accounting.ItemSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.relationships.EntityGroup.class, com.netsuite.webservices.lists.relationships.EntityGroupSearch.class, com.netsuite.webservices.platform.common.EntityGroupSearchBasic.class, com.netsuite.webservices.lists.relationships.EntityGroupSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.employees.Employee.class, com.netsuite.webservices.lists.employees.EmployeeSearch.class, com.netsuite.webservices.platform.common.EmployeeSearchBasic.class, com.netsuite.webservices.lists.employees.EmployeeSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.AssemblyItem.class, com.netsuite.webservices.lists.accounting.ItemSearch.class, com.netsuite.webservices.platform.common.ItemSearchBasic.class, com.netsuite.webservices.lists.accounting.ItemSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.relationships.Job.class, com.netsuite.webservices.lists.relationships.JobSearch.class, com.netsuite.webservices.platform.common.JobSearchBasic.class, com.netsuite.webservices.lists.relationships.JobSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.ExpenseCategory.class, com.netsuite.webservices.lists.accounting.ExpenseCategorySearch.class, com.netsuite.webservices.platform.common.ExpenseCategorySearchBasic.class, com.netsuite.webservices.lists.accounting.ExpenseCategorySearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.UnitsType.class, com.netsuite.webservices.lists.accounting.UnitsTypeSearch.class, com.netsuite.webservices.platform.common.UnitsTypeSearchBasic.class, com.netsuite.webservices.lists.accounting.UnitsTypeSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.relationships.JobType.class, com.netsuite.webservices.lists.relationships.JobTypeSearch.class, com.netsuite.webservices.platform.common.JobTypeSearchBasic.class, com.netsuite.webservices.lists.relationships.JobTypeSearchAdvanced.class }, 
	    { com.netsuite.webservices.setup.customization.CustomRecord.class, com.netsuite.webservices.setup.customization.CustomRecordSearch.class, com.netsuite.webservices.platform.common.CustomRecordSearchBasic.class, com.netsuite.webservices.setup.customization.CustomRecordSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.relationships.Vendor.class, com.netsuite.webservices.lists.relationships.VendorSearch.class, com.netsuite.webservices.platform.common.VendorSearchBasic.class, com.netsuite.webservices.lists.relationships.VendorSearchAdvanced.class }, 
	    { com.netsuite.webservices.lists.accounting.SubtotalItem.class, com.netsuite.webservices.lists.accounting.ItemSearch.class, com.netsuite.webservices.platform.common.ItemSearchBasic.class, com.netsuite.webservices.lists.accounting.ItemSearchAdvanced.class }, 
	    { com.netsuite.webservices.transactions.customers.CustomerRefund.class, com.netsuite.webservices.transactions.sales.TransactionSearch.class, TransactionSearchBasic.class, com.netsuite.webservices.transactions.sales.TransactionSearchAdvanced.class } };
  

    private static final Class<?>[][] netSuiteListClasses = {
	    { com.netsuite.webservices.transactions.sales.InvoiceItem.class, com.netsuite.webservices.transactions.sales.Invoice.class, com.netsuite.webservices.transactions.sales.InvoiceItemList.class }, 
	    { com.netsuite.webservices.lists.relationships.ContactAddressbook.class, com.netsuite.webservices.lists.relationships.Contact.class, com.netsuite.webservices.lists.relationships.ContactAddressbookList.class }, 
	    { com.netsuite.webservices.lists.relationships.CustomerAddressbook.class, com.netsuite.webservices.lists.relationships.Customer.class, com.netsuite.webservices.lists.relationships.CustomerAddressbookList.class }, 
	    { com.netsuite.webservices.documents.filecabinet.File.class, com.netsuite.webservices.general.communication.Message.class, com.netsuite.webservices.general.communication.MessageMediaItemList.class }, 
	    { com.netsuite.webservices.transactions.sales.ItemFulfillmentItem.class, com.netsuite.webservices.transactions.sales.ItemFulfillment.class, com.netsuite.webservices.transactions.sales.ItemFulfillmentItemList.class }, 
	    { com.netsuite.webservices.transactions.customers.ReturnAuthorizationItem.class, com.netsuite.webservices.transactions.customers.ReturnAuthorization.class, com.netsuite.webservices.transactions.customers.ReturnAuthorizationItemList.class }, 
	    { com.netsuite.webservices.transactions.sales.OpportunityItem.class, com.netsuite.webservices.transactions.sales.Opportunity.class, com.netsuite.webservices.transactions.sales.OpportunityItemList.class }, 
	    { com.netsuite.webservices.transactions.purchases.ItemReceiptItem.class, com.netsuite.webservices.transactions.purchases.ItemReceipt.class, com.netsuite.webservices.transactions.purchases.ItemReceiptItemList.class }, 
	    { com.netsuite.webservices.transactions.sales.SalesOrderItem.class, com.netsuite.webservices.transactions.sales.SalesOrder.class, com.netsuite.webservices.transactions.sales.SalesOrderItemList.class }, 
	    { com.netsuite.webservices.lists.support.SolutionTopics.class, com.netsuite.webservices.lists.support.Solution.class, com.netsuite.webservices.lists.support.SolutionTopicsList.class } };
}