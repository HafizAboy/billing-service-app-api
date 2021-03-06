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
 * <p>Java class for s_return_download_receipt complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="s_return_download_receipt"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="process_status" type="{http://tempurl.org}ArrayOfString" minOccurs="0"/&gt;
 *         &lt;element name="process_error" type="{http://tempurl.org}ArrayOfString" minOccurs="0"/&gt;
 *         &lt;element name="or_pdf_url" type="{http://tempurl.org}ArrayOfString" minOccurs="0"/&gt;
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
@XmlType(name = "s_return_download_receipt", propOrder = {
    "processStatus",
    "processError",
    "orPdfUrl",
    "returnid"
})
public class SReturnDownloadReceipt {

    @XmlElement(name = "process_status")
    protected ArrayOfString processStatus;
    @XmlElement(name = "process_error")
    protected ArrayOfString processError;
    @XmlElement(name = "or_pdf_url")
    protected ArrayOfString orPdfUrl;
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
     * Gets the value of the orPdfUrl property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getOrPdfUrl() {
        return orPdfUrl;
    }

    /**
     * Sets the value of the orPdfUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setOrPdfUrl(ArrayOfString value) {
        this.orPdfUrl = value;
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
