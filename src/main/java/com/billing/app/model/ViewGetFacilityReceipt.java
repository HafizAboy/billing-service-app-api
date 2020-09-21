package com.billing.app.model;

import java.util.Date;

import javax.validation.constraints.Min;

/**
 * @author Hafiz
 */
public class ViewGetFacilityReceipt {

    @Min(value = 1, message = "Unit Id must be greater than 0")
    private int unitID;

    private Date startReceiptDate;
    
    private Date endReceiptDate;

	public int getUnitID() {
		return unitID;
	}

	public void setUnitID(int unitID) {
		this.unitID = unitID;
	}

	public Date getStartReceiptDate() {
		return startReceiptDate;
	}

	public void setStartReceiptDate(Date startReceiptDate) {
		this.startReceiptDate = startReceiptDate;
	}

	public Date getEndReceiptDate() {
		return endReceiptDate;
	}

	public void setEndReceiptDate(Date endReceiptDate) {
		this.endReceiptDate = endReceiptDate;
	}

}
