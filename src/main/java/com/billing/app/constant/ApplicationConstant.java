package com.billing.app.constant;

/**
 * @author Hafiz
 */
public class ApplicationConstant {

	public static final String APP = "APP";
	
	public enum ServiceType{
        INSURETECH("Insuretech","01"),
        BILLING("Billing","02"),
        BOOKING("Booking","03");

		private String type;
        private String code;

        ServiceType(String type, String code) {
        	this.type = type;
        	this.code = code;
		}

		public String getCode() {
			return code;
		}

		public String getType() {
			return type;
		}

    }

	public enum PaymentStatus{
        SUCCESS("Success","S"),
        FAILED("Failed","F");

		private String type;
        private String code;

        PaymentStatus(String type, String code) {
        	this.type = type;
        	this.code = code;
		}

		public String getCode() {
			return code;
		}

		public String getType() {
			return type;
		}

    }
}
