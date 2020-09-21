package com.billing.app.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.billing.app.businessrule.BillingViewBusinessRule;
import com.billing.app.constant.ErrorEnum;
import com.billing.app.exception.WebserviceException;
import com.billing.app.model.EcoProfileResponse;
import com.billing.app.model.ViewGetDownloadDocument;
import com.billing.app.model.ViewGetDownloadReceipt;
import com.billing.app.model.ViewGetDownloadSoa;
import com.billing.app.model.ViewGetFacilityReceipt;
import com.billing.app.model.ViewGetOverdueInvoice;
import com.billing.app.model.ViewGetTransaction;
import com.billing.app.thirdparty.EcoV1Connection;
import com.billing.app.util.LogUtility;

import io.swagger.annotations.ApiOperation;

/**
 * @author Hafiz
 */
@CrossOrigin
@RestController
@RequestMapping("/public/v1")
@ApiOperation(value = "Billing View Service")
public class BillingViewController {

	protected final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	EcoV1Connection ecoV1Connection;

	@Autowired
	@Qualifier("BillingViewBusinessRule")
	private BillingViewBusinessRule billingViewBusinessRule;

	/**
	 * API: Get Booking Receipt (PDF URL)
	 * 
	 * @param Unit_id, OR_number, OR_Date
	 * @return PDF_URL
	 * @throws Exception 
	 */
	@RequestMapping(path = "/receipt/booking",
			method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "Get Booking Receipt (PDF URL)")
	public ResponseEntity<?> getDownloadReceipt(HttpServletRequest request, @RequestBody ViewGetDownloadReceipt viewGetDownloadReceipt) {

		logger.info("Accessing request : " + request.getRequestURI());

		EcoProfileResponse accountResp = null;
		Map<String, List<Map<String, String>>> responseBody;

		try {
			accountResp = ecoV1Connection.getProfile(request);
		} catch (Exception ex) {
			logger.error("Unauthorized access!:- "+"Authorization field is "+request.getHeader("Authorization"));
			throw new WebserviceException(ErrorEnum.UNAUTHORIZED_ACCESS, "Authorization field is "+request.getHeader("Authorization"));
		}

		LogUtility.info(request, logger, "User: " + accountResp.firstName);

		logger.info("BillingViewController - getDownloadReceipt request received:- ");
		logger.info("getDownloadReceipt viewGetDownloadReceipt - Unit ID:- "+viewGetDownloadReceipt.getUnitID());
		logger.info("getDownloadReceipt viewGetDownloadReceipt - OR Number:- "+viewGetDownloadReceipt.getOrNumber());
		logger.info("getDownloadReceipt viewGetDownloadReceipt - OR Date:- "+viewGetDownloadReceipt.getOrDate());

		// validate the input
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<ViewGetDownloadReceipt>> violations = validator.validate(viewGetDownloadReceipt);

		logger.info("size of violations : " + violations.size());

		for (ConstraintViolation<ViewGetDownloadReceipt> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");
			responseBody = billingViewBusinessRule.processViewDownloadReceipt(viewGetDownloadReceipt);
		} else {
			logger.error("Validation Failed!");
			throw new WebserviceException(ErrorEnum.VALIDATION_FAILED, ErrorEnum.VALIDATION_FAILED.getDescription());
		}
		logger.info("responseBody:- "+responseBody);
		logger.info("Completed - "+HttpStatus.OK);
		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}

	/**
	 * API: Get Paid Invoice Receipt (PDF URL)
	 * 
	 * @param Unit_id, Document_No, Document_Date
	 * @return PDF_URL
	 * @throws Exception 
	 */
	@RequestMapping(path = "/receipt/paid-invoice",
			method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "Get Paid Invoice Receipt (PDF URL)")
	public ResponseEntity<?> getDownloadDocument(HttpServletRequest request, @RequestBody ViewGetDownloadDocument viewGetDownloadDocument) {

		logger.info("Accessing request : " + request.getRequestURI());

		EcoProfileResponse accountResp = null;
		Map<String, List<Map<String, String>>> responseBody;

		try {
			accountResp = ecoV1Connection.getProfile(request);
		} catch (Exception ex) {
			logger.error("Unauthorized access!:- "+"Authorization field is "+request.getHeader("Authorization"));
			throw new WebserviceException(ErrorEnum.UNAUTHORIZED_ACCESS, "Authorization field is "+request.getHeader("Authorization"));
		}

		LogUtility.info(request, logger, "User: " + accountResp.firstName);

		logger.info("BillingViewController - getDownloadDocument request received:- ");
		logger.info("getDownloadDocument viewGetDownloadDocument - Unit ID:- "+viewGetDownloadDocument.getUnitID());
		logger.info("getDownloadDocument viewGetDownloadDocument - Document Number:- "+viewGetDownloadDocument.getDocumentNo());
		logger.info("getDownloadDocument viewGetDownloadDocument - Document Date:- "+viewGetDownloadDocument.getDocumentDate());

		// validate the input
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<ViewGetDownloadDocument>> violations = validator.validate(viewGetDownloadDocument);

		logger.info("size of violations : " + violations.size());

		for (ConstraintViolation<ViewGetDownloadDocument> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");
			responseBody = billingViewBusinessRule.processViewDownloadDocument(viewGetDownloadDocument);
		} else {
			logger.error("Validation Failed!");
			throw new WebserviceException(ErrorEnum.VALIDATION_FAILED, ErrorEnum.VALIDATION_FAILED.getDescription());
		}
		logger.info("responseBody:- "+responseBody);
		logger.info("Completed - "+HttpStatus.OK);
		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}

	/**
	 * API: View Paid Invoice
	 * 
	 * @param Unit_id, Transaction_class, Start_date, End_date
	 * @return Transaction_class, ARLedgerItemId, Document_number, Document_date, Due_date, Billing_type, Item_description, Document_amountOutstanding_amount
	 * @throws Exception 
	 */
	@RequestMapping(path = "/view/paid-invoice",
			method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "View Paid Invoice")
	public ResponseEntity<?> getGetTransaction(HttpServletRequest request, @RequestBody ViewGetTransaction viewGetTransaction) {

		logger.info("Accessing request : " + request.getRequestURI());

		EcoProfileResponse accountResp = null;
		Map<String, List<Map<String, String>>> responseBody;

		try {
			accountResp = ecoV1Connection.getProfile(request);
		} catch (Exception ex) {
			logger.error("Unauthorized access!:- "+"Authorization field is "+request.getHeader("Authorization"));
			throw new WebserviceException(ErrorEnum.UNAUTHORIZED_ACCESS, "Authorization field is "+request.getHeader("Authorization"));
		}

		LogUtility.info(request, logger, "User: " + accountResp.firstName);

		logger.info("BillingViewController - getGetTransaction request received:- ");
		logger.info("getGetTransaction viewGetTransaction - Unit ID:- "+viewGetTransaction.getUnitID());
		logger.info("getGetTransaction viewGetTransaction - Start Date:- "+viewGetTransaction.getStartDate());
		logger.info("getGetTransaction viewGetTransaction - End Date:- "+viewGetTransaction.getEndDate());
		for(int iterate=0; iterate<viewGetTransaction.getTransactionClass().length; iterate++) {
			logger.info("getGetTransaction viewGetTransaction - Transaction Class:- "+viewGetTransaction.getTransactionClass()[iterate]);
		}

		// validate the input
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<ViewGetTransaction>> violations = validator.validate(viewGetTransaction);

		logger.info("size of violations : " + violations.size());

		for (ConstraintViolation<ViewGetTransaction> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");
			responseBody = billingViewBusinessRule.processViewTransaction(viewGetTransaction);
		} else {
			logger.error("Validation Failed!");
			throw new WebserviceException(ErrorEnum.VALIDATION_FAILED, ErrorEnum.VALIDATION_FAILED.getDescription());
		}
		logger.info("responseBody:- "+responseBody);
		logger.info("Completed - "+HttpStatus.OK);
		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}

	/**
	 * API: View Paid Booking
	 * 
	 * @param Unit_id, Start_receipt_date, End_receipt_date
	 * @return OR_number, OR_Date, Reference_no, OR_amount
	 * @throws Exception 
	 */
	@RequestMapping(path = "/view/paid-booking",
			method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "View Paid Booking")
	public ResponseEntity<?> getFacilityReceipt(HttpServletRequest request, @RequestBody ViewGetFacilityReceipt viewGetFacilityReceipt) {

		logger.info("Accessing request : " + request.getRequestURI());

		EcoProfileResponse accountResp = null;
		Map<String, List<Map<String, String>>> responseBody;

		try {
			accountResp = ecoV1Connection.getProfile(request);
		} catch (Exception ex) {
			logger.error("Unauthorized access!:- "+"Authorization field is "+request.getHeader("Authorization"));
			throw new WebserviceException(ErrorEnum.UNAUTHORIZED_ACCESS, "Authorization field is "+request.getHeader("Authorization"));
		}

		LogUtility.info(request, logger, "User: " + accountResp.firstName);

		logger.info("BillingViewController - getFacilityReceipt request received:- ");
		logger.info("getFacilityReceipt viewGetFacilityReceipt - Unit ID:- "+viewGetFacilityReceipt.getUnitID());
		logger.info("getFacilityReceipt viewGetFacilityReceipt - Start Receipt Date:- "+viewGetFacilityReceipt.getStartReceiptDate());
		logger.info("getFacilityReceipt viewGetFacilityReceipt - End Receipt Date:- "+viewGetFacilityReceipt.getEndReceiptDate());
		
		// validate the input
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<ViewGetFacilityReceipt>> violations = validator.validate(viewGetFacilityReceipt);

		logger.info("size of violations : " + violations.size());

		for (ConstraintViolation<ViewGetFacilityReceipt> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");
			responseBody = billingViewBusinessRule.processViewFacilityReceipt(viewGetFacilityReceipt);
		} else {
			logger.error("Validation Failed!");
			throw new WebserviceException(ErrorEnum.VALIDATION_FAILED, ErrorEnum.VALIDATION_FAILED.getDescription());
		}
		logger.info("responseBody:- "+responseBody);
		logger.info("Completed - "+HttpStatus.OK);
		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}

	/**
	 * API: Get Outstanding Invoice
	 * 
	 * @param Unit_id
	 * @return Transaction_class, ARLEdgerItemId, Document_number, Document_date, Transaction_mode, Due_date, Billing_type, Item_description, Document_amount, Outstanding_amount
	 * @throws Exception 
	 */
	@GetMapping(path = "/invoice/get-outstanding")
	@ResponseBody
	@ApiOperation(value = "Get Outstanding Invoice")
	public ResponseEntity<?> getOutstandingInvoice(HttpServletRequest request, @RequestParam("unitId") int unitId) {

		logger.info("Accessing request : " + request.getRequestURI());

		EcoProfileResponse accountResp = null;
		Map<String, List<Map<String, String>>> responseBody;

		try {
			accountResp = ecoV1Connection.getProfile(request);
		} catch (Exception ex) {
			logger.error("Unauthorized access!:- "+"Authorization field is "+request.getHeader("Authorization"));
			throw new WebserviceException(ErrorEnum.UNAUTHORIZED_ACCESS, "Authorization field is "+request.getHeader("Authorization"));
		}

		LogUtility.info(request, logger, "User: " + accountResp.firstName);

		logger.info("BillingViewController - getOutstandingInvoice request received:- ");
		logger.info("getOutstandingInvoice request param - Unit ID:- "+unitId);
		
		if(unitId == 0) {
			logger.error("Validation Failed!");
			throw new WebserviceException(ErrorEnum.VALIDATION_FAILED, "Unit Id = " + unitId);
		}

		responseBody = billingViewBusinessRule.processViewOutstandingInvoice(unitId);

		logger.info("responseBody:- "+responseBody);
		logger.info("Completed - "+HttpStatus.OK);
		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}

	/**
	 * API: Get Overdue Invoice
	 * 
	 * @param Unit_id, As_at_date
	 * @return Transaction_class, ARLEdgerItemId, Document_number, Document_date, Due_date, Billing_type, Item_description, Document_amount, Outstanding_amount
	 * @throws Exception 
	 */
	@RequestMapping(path = "/invoice/get-overdue",
			method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "Get Overdue Invoice")
	public ResponseEntity<?> getOverdueInvoice(HttpServletRequest request, @RequestBody ViewGetOverdueInvoice viewGetOverdueInvoice) {

		logger.info("Accessing request : " + request.getRequestURI());

		EcoProfileResponse accountResp = null;
		Map<String, List<Map<String, String>>> responseBody;

		try {
			accountResp = ecoV1Connection.getProfile(request);
		} catch (Exception ex) {
			logger.error("Unauthorized access!:- "+"Authorization field is "+request.getHeader("Authorization"));
			throw new WebserviceException(ErrorEnum.UNAUTHORIZED_ACCESS, "Authorization field is "+request.getHeader("Authorization"));
		}

		LogUtility.info(request, logger, "User: " + accountResp.firstName);

		logger.info("BillingViewController - getOverdueInvoice request received:- ");
		logger.info("getOverdueInvoice viewGetOverdueInvoice - Unit ID:- "+viewGetOverdueInvoice.getUnitID());
		logger.info("getOverdueInvoice viewGetOverdueInvoice - As At Date:- "+viewGetOverdueInvoice.getAsAtDate());
		
		// validate the input
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<ViewGetOverdueInvoice>> violations = validator.validate(viewGetOverdueInvoice);

		logger.info("size of violations : " + violations.size());

		for (ConstraintViolation<ViewGetOverdueInvoice> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");
			responseBody = billingViewBusinessRule.processViewOverdueInvoice(viewGetOverdueInvoice);
		} else {
			logger.error("Validation Failed!");
			throw new WebserviceException(ErrorEnum.VALIDATION_FAILED, ErrorEnum.VALIDATION_FAILED.getDescription());
		}
		logger.info("responseBody:- "+responseBody);
		logger.info("Completed - "+HttpStatus.OK);
		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}

	/**
	 * API: Get Statement of Accounts (PDF URL)
	 * 
	 * @param Unit_id, Start_date, End_date
	 * @return SOA_PDF_URL
	 * @throws Exception 
	 */
	@RequestMapping(path = "/account/get-statement",
			method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "Get Statement of Accounts (PDF URL)")
	public ResponseEntity<?> getDownloadSoa(HttpServletRequest request, @RequestBody ViewGetDownloadSoa viewGetDownloadSoa) {

		logger.info("Accessing request : " + request.getRequestURI());

		EcoProfileResponse accountResp = null;
		Map<String, List<Map<String, String>>> responseBody;

		try {
			accountResp = ecoV1Connection.getProfile(request);
		} catch (Exception ex) {
			logger.error("Unauthorized access!:- "+"Authorization field is "+request.getHeader("Authorization"));
			throw new WebserviceException(ErrorEnum.UNAUTHORIZED_ACCESS, "Authorization field is "+request.getHeader("Authorization"));
		}

		LogUtility.info(request, logger, "User: " + accountResp.firstName);

		logger.info("BillingViewController - getDownloadSoa request received:- ");
		logger.info("getDownloadSoa viewGetDownloadSoa - Unit ID:- "+viewGetDownloadSoa.getUnitID());
		logger.info("getDownloadSoa viewGetDownloadSoa - Start Date:- "+viewGetDownloadSoa.getStartDate());
		logger.info("getDownloadSoa viewGetDownloadSoa - End Date:- "+viewGetDownloadSoa.getEndDate());
		
		// validate the input
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<ViewGetDownloadSoa>> violations = validator.validate(viewGetDownloadSoa);

		logger.info("size of violations : " + violations.size());

		for (ConstraintViolation<ViewGetDownloadSoa> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");
			responseBody = billingViewBusinessRule.processViewDownloadSoa(viewGetDownloadSoa);
		} else {
			logger.error("Validation Failed!");
			throw new WebserviceException(ErrorEnum.VALIDATION_FAILED, ErrorEnum.VALIDATION_FAILED.getDescription());
		}
		logger.info("responseBody:- "+responseBody);
		logger.info("Completed - "+HttpStatus.OK);
		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}

}
