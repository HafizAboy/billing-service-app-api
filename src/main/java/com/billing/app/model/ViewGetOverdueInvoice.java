package com.billing.app.model;

import java.util.Date;

import javax.validation.constraints.Min;

/**
 * @author Hafiz
 */
public class ViewGetOverdueInvoice {

    @Min(value = 1, message = "Unit Id must be greater than 0")
    private int unitID;

    private Date asAtDate;

	public int getUnitID() {
		return unitID;
	}

	public void setUnitID(int unitID) {
		this.unitID = unitID;
	}

	public Date getAsAtDate() {
		return asAtDate;
	}

	public void setAsAtDate(Date asAtDate) {
		this.asAtDate = asAtDate;
	}

}
