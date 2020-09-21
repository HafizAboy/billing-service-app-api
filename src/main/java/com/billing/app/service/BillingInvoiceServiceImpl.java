package com.billing.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billing.app.entity.BillingInvoiceEntity;
import com.billing.app.repository.BillingInvoiceRepository;

/**
 * @author Hafiz
 */
@Service
public class BillingInvoiceServiceImpl implements BillingInvoiceService{

	@Autowired
	private BillingInvoiceRepository billingInvoiceRepo;

	@Override
	public List<BillingInvoiceEntity> findInvoiceByTrxnID(String trxnID) {
		return billingInvoiceRepo.findByTransactionID(trxnID);
	}

	@Override
	public List<BillingInvoiceEntity> saveProcessInvoice(List<BillingInvoiceEntity> billingInvoiceEntity)
			throws Exception {
		return billingInvoiceRepo.saveAll(billingInvoiceEntity);
	}

	@Override
	public List<BillingInvoiceEntity> updateProcessInvoice(List<BillingInvoiceEntity> billingInvoiceEntity)
			throws Exception {
		return saveProcessInvoice(billingInvoiceEntity);
	}

	@Override
	public String getLastInsertedTransactionID() {
		// TODO Auto-generated method stub
		return billingInvoiceRepo.TransactionID();
	}

	@Override
	public List<BillingInvoiceEntity> findFailedRetryNoeticByReferenceNo(String refNo) {
		return billingInvoiceRepo.findFailedRetryNoeticByReferenceNo(refNo);
	}

}
