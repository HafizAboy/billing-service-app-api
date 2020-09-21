package com.billing.app.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.billing.app.entity.BillingInvoiceEntity;

/**
 * @author Hafiz
 */
@Repository
public interface BillingInvoiceRepository extends JpaRepository<BillingInvoiceEntity, String> {

	List<BillingInvoiceEntity> findByTransactionID(String trxnID);

	@Transactional
	@Query(value="SELECT MAX(SUBSTR(`TRANSACTION_ID`,9)) FROM `T_BILLING_PROC_INVOICE_TXN`", nativeQuery=true)
	String TransactionID();

	//findByTrxnId where process_status "Failed" and process_error "Retry to Noetic failed"
	@Transactional
	@Query(value="SELECT * FROM `T_BILLING_PROC_INVOICE_TXN` WHERE PROCESS_STATUS = 'Error' and PROCESS_ERROR = 'Retry to Noetic failed' and REFERENCE_NO=?", nativeQuery=true)
	List<BillingInvoiceEntity> findFailedRetryNoeticByReferenceNo(String refNo);

}
