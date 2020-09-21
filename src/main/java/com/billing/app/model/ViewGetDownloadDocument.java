package com.billing.app.model;

import java.util.Date;

import javax.validation.constraints.Min;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Hafiz
 */
public class ViewGetDownloadDocument {

    @Min(value = 1, message = "Unit Id must be greater than 0")
    private int unitID;

    private String documentNo;
    
    @ApiModelProperty(required = true,example = "2018-03-20T00:00:00.000+08:00")
    private Date documentDate;

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

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}
}
