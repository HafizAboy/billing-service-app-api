package com.billing.app.service;

import java.util.List;

import com.billing.app.entity.BillingInvoiceEntity;

/**
 * @author Hafiz
 */
public interface BillingInvoiceService {

	List<BillingInvoiceEntity> findInvoiceByTrxnID(String trxnID);
	
	List<BillingInvoiceEntity> saveProcessInvoice(List<BillingInvoiceEntity> billingInvoiceEntity) throws Exception;

	List<BillingInvoiceEntity> updateProcessInvoice(List<BillingInvoiceEntity> billingInvoiceInfo) throws Exception;
	
	String getLastInsertedTransactionID();

	List<BillingInvoiceEntity> findFailedRetryNoeticByReferenceNo(String refNo);
}
