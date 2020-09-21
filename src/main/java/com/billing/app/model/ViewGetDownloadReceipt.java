package com.billing.app.model;

import java.util.Date;

import javax.validation.constraints.Min;

/**
 * @author Hafiz
 */
public class ViewGetDownloadReceipt {

    @Min(value = 1, message = "Unit Id must be greater than 0")
    private int unitID;

    private String orNumber;
    
    private Date orDate;

	public int getUnitID() {
		return unitID;
	}

	public void setUnitID(int unitID) {
		this.unitID = unitID;
	}

	public String getOrNumber() {
		return orNumber;
	}

	public void setOrNumber(String orNumber) {
		this.orNumber = orNumber;
	}

	public Date getOrDate() {
		return orDate;
	}

	public void setOrDate(Date orDate) {
		this.orDate = orDate;
	}

}
