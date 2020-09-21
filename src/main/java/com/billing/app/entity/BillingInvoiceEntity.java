package com.billing.app.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author Hafiz
 */
@Entity
@Table(name = "T_BILLING_PROC_INVOICE_TXN")
public class BillingInvoiceEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name="ID", nullable=false)
    private String id;

    @Column(name="CREATED_DT", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable=false)
    private Date createdDate;

    @Column(name="UPDATED_DT", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date updatedDate;

    @Column(name="TOTAL_AMOUNT_FPX", precision=15, scale=2)
    private BigDecimal totalAmountFpx;

    @Column(name="ITEM_AMOUNT", precision=15, scale=2)
    private BigDecimal itemAmount;

    @Column(name="TRANSACTION_ID", columnDefinition="char(19)")
    private String transactionID;

    @Column(name="SERVICE_TYPE", columnDefinition="int(3)")
    private int serviceType;

    @Column(name="AR_LEDGER_ITEM_ID", columnDefinition="int(12)")
    private int arLedgerItemID;

    @Column(name="PAYMENT_PROFILE_ID", length=30)
    private String paymentProfileID;

    @Column(name="TRANSACTION_CLASS", columnDefinition="char(1)")
    private String transactionClass;

    @Column(name="PROCESS_ERROR", length=300)
    private String processError;

    @Column(name="PROCESS_STATUS", length=10)
    private String processStatus;

    @Column(name="OR_NUMBER", length=20)
    private String orNumber;

    @Column(name="REQUEST_ID", columnDefinition="bigint(12)")
    private Long requestID;

    @Column(name="RETURN_ID", columnDefinition="bigint(12)")
    private Long returnID;

    @Column(name="REFERENCE_NO", length=50)
    private String referenceNo;

    @Column(name="PAYMENT_METHOD", columnDefinition="char(1)")
    private String paymentMethod;

    @Column(name="UNIT_ID", columnDefinition="int(12)")
    private int unitID;

    @Column(name="DOCUMENT_NO", length=20)
    private String documentNo;

    @Column(name="SELLER_ID", length=30)
    private String sellerID;

    @Column(name="DP_USER_ID", length=50)
    private String dpUserID;

	@Column(name="HOUSE_UNIT", length=20)
	private String houseUnit;

	@Column(name="FIRST_NAME", length=50)
	private String firstName;

    @PrePersist
    protected void onCreate() {
    	Date currentDate = new Date();
    	createdDate = currentDate;
    }
    
    @PreUpdate
    protected void onUpdate() {
    	Date currentDate = new Date();
    	updatedDate = currentDate;
    }

	public String getHouseUnit() {
		return houseUnit;
	}

	public void setHouseUnit(String houseUnit) {
		this.houseUnit = houseUnit;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public BigDecimal getTotalAmountFpx() {
		return totalAmountFpx;
	}

	public void setTotalAmountFpx(BigDecimal totalAmountFpx) {
		this.totalAmountFpx = totalAmountFpx;
	}

	public BigDecimal getItemAmount() {
		return itemAmount;
	}

	public void setItemAmount(BigDecimal itemAmount) {
		this.itemAmount = itemAmount;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public int getServiceType() {
		return serviceType;
	}

	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}

	public int getArLedgerItemID() {
		return arLedgerItemID;
	}

	public void setArLedgerItemID(int arLedgerItemID) {
		this.arLedgerItemID = arLedgerItemID;
	}

	public String getPaymentProfileID() {
		return paymentProfileID;
	}

	public void setPaymentProfileID(String paymentProfileID) {
		this.paymentProfileID = paymentProfileID;
	}

	public String getTransactionClass() {
		return transactionClass;
	}

	public void setTransactionClass(String transactionClass) {
		this.transactionClass = transactionClass;
	}

	public String getProcessError() {
		return processError;
	}

	public void setProcessError(String processError) {
		this.processError = processError;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public String getOrNumber() {
		return orNumber;
	}

	public void setOrNumber(String orNumber) {
		this.orNumber = orNumber;
	}

	public Long getRequestID() {
		return requestID;
	}

	public void setRequestID(Long requestID) {
		this.requestID = requestID;
	}

	public Long getReturnID() {
		return returnID;
	}

	public void setReturnID(Long returnID) {
		this.returnID = returnID;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public int getUnitID() {
		return unitID;
	}

	public void setUnitID(int unitID) {
		this.unitID = unitID;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public String getSellerID() {
		return sellerID;
	}

	public void setSellerID(String sellerID) {
		this.sellerID = sellerID;
	}

	public String getDpUserID() {
		return dpUserID;
	}

	public void setDpUserID(String dpUserID) {
		this.dpUserID = dpUserID;
	}

}
