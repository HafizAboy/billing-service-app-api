//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.09.21 at 04:59:57 PM SGT 
//


package com.billing.app.noetic.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for s_return_outstanding_invoices complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="s_return_outstanding_invoices"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="process_status" type="{http://tempurl.org}ArrayOfString" minOccurs="0"/&gt;
 *         &lt;element name="process_error" type="{http://tempurl.org}ArrayOfString" minOccurs="0"/&gt;
 *         &lt;element name="transaction_class" type="{http://tempurl.org}ArrayOfString" minOccurs="0"/&gt;
 *         &lt;element name="arledgeritemid" type="{http://tempurl.org}ArrayOfInt" minOccurs="0"/&gt;
 *         &lt;element name="document_number" type="{http://tempurl.org}ArrayOfString" minOccurs="0"/&gt;
 *         &lt;element name="document_date" type="{http://tempurl.org}ArrayOfDateTime" minOccurs="0"/&gt;
 *         &lt;element name="transaction_mode" type="{http://tempurl.org}ArrayOfString" minOccurs="0"/&gt;
 *         &lt;element name="due_date" type="{http://tempurl.org}ArrayOfDateTime" minOccurs="0"/&gt;
 *         &lt;element name="billing_type" type="{http://tempurl.org}ArrayOfString" minOccurs="0"/&gt;
 *         &lt;element name="item_description" type="{http://tempurl.org}ArrayOfString" minOccurs="0"/&gt;
 *         &lt;element name="document_amount" type="{http://tempurl.org}ArrayOfDecimal" minOccurs="0"/&gt;
 *         &lt;element name="outstanding_amount" type="{http://tempurl.org}ArrayOfDecimal" minOccurs="0"/&gt;
 *         &lt;element name="returnid" type="{http://tempurl.org}ArrayOfLong" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "s_return_outstanding_invoices", propOrder = {
    "processStatus",
    "processError",
    "transactionClass",
    "arledgeritemid",
    "documentNumber",
    "documentDate",
    "transactionMode",
    "dueDate",
    "billingType",
    "itemDescription",
    "documentAmount",
    "outstandingAmount",
    "returnid"
})
public class SReturnOutstandingInvoices {

    @XmlElement(name = "process_status")
    protected ArrayOfString processStatus;
    @XmlElement(name = "process_error")
    protected ArrayOfString processError;
    @XmlElement(name = "transaction_class")
    protected ArrayOfString transactionClass;
    protected ArrayOfInt arledgeritemid;
    @XmlElement(name = "document_number")
    protected ArrayOfString documentNumber;
    @XmlElement(name = "document_date")
    protected ArrayOfDateTime documentDate;
    @XmlElement(name = "transaction_mode")
    protected ArrayOfString transactionMode;
    @XmlElement(name = "due_date")
    protected ArrayOfDateTime dueDate;
    @XmlElement(name = "billing_type")
    protected ArrayOfString billingType;
    @XmlElement(name = "item_description")
    protected ArrayOfString itemDescription;
    @XmlElement(name = "document_amount")
    protected ArrayOfDecimal documentAmount;
    @XmlElement(name = "outstanding_amount")
    protected ArrayOfDecimal outstandingAmount;
    protected ArrayOfLong returnid;

    /**
     * Gets the value of the processStatus property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getProcessStatus() {
        return processStatus;
    }

    /**
     * Sets the value of the processStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setProcessStatus(ArrayOfString value) {
        this.processStatus = value;
    }

    /**
     * Gets the value of the processError property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getProcessError() {
        return processError;
    }

    /**
     * Sets the value of the processError property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setProcessError(ArrayOfString value) {
        this.processError = value;
    }

    /**
     * Gets the value of the transactionClass property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getTransactionClass() {
        return transactionClass;
    }

    /**
     * Sets the value of the transactionClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setTransactionClass(ArrayOfString value) {
        this.transactionClass = value;
    }

    /**
     * Gets the value of the arledgeritemid property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfInt }
     *     
     */
    public ArrayOfInt getArledgeritemid() {
        return arledgeritemid;
    }

    /**
     * Sets the value of the arledgeritemid property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfInt }
     *     
     */
    public void setArledgeritemid(ArrayOfInt value) {
        this.arledgeritemid = value;
    }

    /**
     * Gets the value of the documentNumber property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getDocumentNumber() {
        return documentNumber;
    }

    /**
     * Sets the value of the documentNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setDocumentNumber(ArrayOfString value) {
        this.documentNumber = value;
    }

    /**
     * Gets the value of the documentDate property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfDateTime }
     *     
     */
    public ArrayOfDateTime getDocumentDate() {
        return documentDate;
    }

    /**
     * Sets the value of the documentDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfDateTime }
     *     
     */
    public void setDocumentDate(ArrayOfDateTime value) {
        this.documentDate = value;
    }

    /**
     * Gets the value of the transactionMode property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getTransactionMode() {
        return transactionMode;
    }

    /**
     * Sets the value of the transactionMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setTransactionMode(ArrayOfString value) {
        this.transactionMode = value;
    }

    /**
     * Gets the value of the dueDate property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfDateTime }
     *     
     */
    public ArrayOfDateTime getDueDate() {
        return dueDate;
    }

    /**
     * Sets the value of the dueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfDateTime }
     *     
     */
    public void setDueDate(ArrayOfDateTime value) {
        this.dueDate = value;
    }

    /**
     * Gets the value of the billingType property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getBillingType() {
        return billingType;
    }

    /**
     * Sets the value of the billingType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setBillingType(ArrayOfString value) {
        this.billingType = value;
    }

    /**
     * Gets the value of the itemDescription property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getItemDescription() {
        return itemDescription;
    }

    /**
     * Sets the value of the itemDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setItemDescription(ArrayOfString value) {
        this.itemDescription = value;
    }

    /**
     * Gets the value of the documentAmount property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfDecimal }
     *     
     */
    public ArrayOfDecimal getDocumentAmount() {
        return documentAmount;
    }

    /**
     * Sets the value of the documentAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfDecimal }
     *     
     */
    public void setDocumentAmount(ArrayOfDecimal value) {
        this.documentAmount = value;
    }

    /**
     * Gets the value of the outstandingAmount property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfDecimal }
     *     
     */
    public ArrayOfDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    /**
     * Sets the value of the outstandingAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfDecimal }
     *     
     */
    public void setOutstandingAmount(ArrayOfDecimal value) {
        this.outstandingAmount = value;
    }

    /**
     * Gets the value of the returnid property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfLong }
     *     
     */
    public ArrayOfLong getReturnid() {
        return returnid;
    }

    /**
     * Sets the value of the returnid property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfLong }
     *     
     */
    public void setReturnid(ArrayOfLong value) {
        this.returnid = value;
    }

}
