package com.billing.app.model;

import java.util.Date;

import javax.validation.constraints.Min;

/**
 * @author Hafiz
 */
public class ViewGetTransaction {

    @Min(value = 1, message = "Unit Id must be greater than 0")
    private int unitID;

    private String[] transactionClass;

    private Date startDate;
    
    private Date endDate;

	public int getUnitID() {
		return unitID;
	}

	public void setUnitID(int unitID) {
		this.unitID = unitID;
	}

	public String[] getTransactionClass() {
		return transactionClass;
	}

	public void setTransactionClass(String[] transactionClass) {
		this.transactionClass = transactionClass;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
