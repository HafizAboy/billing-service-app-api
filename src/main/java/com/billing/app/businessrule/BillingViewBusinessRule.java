package com.billing.app.businessrule;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.billing.app.constant.ErrorEnum;
import com.billing.app.exception.WebserviceException;
import com.billing.app.model.ViewGetDownloadDocument;
import com.billing.app.model.ViewGetDownloadReceipt;
import com.billing.app.model.ViewGetDownloadSoa;
import com.billing.app.model.ViewGetFacilityReceipt;
import com.billing.app.model.ViewGetOverdueInvoice;
import com.billing.app.model.ViewGetTransaction;
import com.billing.app.noetic.service.SOAPClientService;
import com.billing.app.noetic.wsdl.GetDownloaddocuments;
import com.billing.app.noetic.wsdl.GetDownloaddocumentsResponse;
import com.billing.app.noetic.wsdl.GetDownloadreceipt;
import com.billing.app.noetic.wsdl.GetDownloadreceiptResponse;
import com.billing.app.noetic.wsdl.GetDownloadsoa;
import com.billing.app.noetic.wsdl.GetDownloadsoaResponse;
import com.billing.app.noetic.wsdl.GetFacilityreceipts;
import com.billing.app.noetic.wsdl.GetFacilityreceiptsResponse;
import com.billing.app.noetic.wsdl.GetOutstandinginvoices;
import com.billing.app.noetic.wsdl.GetOutstandinginvoicesResponse;
import com.billing.app.noetic.wsdl.GetOverdueinvoices;
import com.billing.app.noetic.wsdl.GetOverdueinvoicesResponse;
import com.billing.app.noetic.wsdl.GetTransactions;
import com.billing.app.noetic.wsdl.GetTransactionsResponse;
import com.billing.app.noetic.wsdl.SReturnDownloadDocuments;
import com.billing.app.noetic.wsdl.SReturnDownloadReceipt;
import com.billing.app.noetic.wsdl.SReturnDownloadSoa;
import com.billing.app.noetic.wsdl.SReturnFacilityReceipts;
import com.billing.app.noetic.wsdl.SReturnGetOverdueInvoices;
import com.billing.app.noetic.wsdl.SReturnGetTransactions;
import com.billing.app.noetic.wsdl.SReturnOutstandingInvoices;
import com.billing.app.util.RequestIdGenerator;

/**
 * @author Hafiz
 */
@Component("BillingViewBusinessRule")
public class BillingViewBusinessRule {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${noetic.user.id}")
	private String noeticUserId;

	@Value("${noetic.user.password}")
	private String noeticPassword;

	@Autowired
	SOAPClientService soapClientService;

	public HashMap<String, List<Map<String, String>>> processViewDownloadReceipt(ViewGetDownloadReceipt viewGetDownloadReceipt) {
		logger.info("BillingViewBusinessRule - processViewDownloadReceipt");

		//Local declaration
		HashMap<String, List<Map<String, String>>> map = new HashMap<>();
		HashMap<String, String> innerMap; //not initialize because innerMap will be freshen up in each iteration
		GetDownloadreceipt request = new GetDownloadreceipt();
		SReturnDownloadReceipt response = new SReturnDownloadReceipt();

		// set generated requestId
		Long requestId = RequestIdGenerator.uniqueCurrentTimeMS();
		logger.info("requestId generated:- "+requestId);

		//Convert client request - Date to XMLGregorianCalendar - throw exception
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(viewGetDownloadReceipt.getOrDate());
		XMLGregorianCalendar xmlOrDate;
		try {
			xmlOrDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		} catch (DatatypeConfigurationException e) {
			logger.error("Error:- Date conversion failed!");
			throw new WebserviceException(ErrorEnum.DATETIME_PARSING_FAILED, e.getMessage());
		}

		//set data to request object for noetic
		request.setUnitid(viewGetDownloadReceipt.getUnitID());
		request.setOrNumber(viewGetDownloadReceipt.getOrNumber());
		request.setOrDate(xmlOrDate);
		request.setRequestid(requestId);
		request.setUserid(noeticUserId); //change to userID
		request.setPassword(noeticPassword); //change to password

		//logging request
		logger.info("Noetic Get Download Receipt API request");
		logger.info("Noetic Get Download Receipt - Unit ID:- "+ request.getUnitid());
		logger.info("Noetic Get Download Receipt - OR Number:- "+ request.getOrNumber());
		logger.info("Noetic Get Download Receipt - OR Date:- "+ request.getOrDate());
		logger.info("Noetic Get Download Receipt - Request ID:- "+ request.getRequestid());
		logger.info("Noetic Get Download Receipt - User ID:- "+ request.getUserid());

		//send to Noetic and receive response
		response = response(request).getGetDownloadreceiptResult();

		if(response != null) {
			logger.info("Response array from noetic based on return id(response.getProcessStatus().getString().size()) - "+response.getProcessStatus().getString().size());
			//check if array is empty using return id - return id always have value if there is any record in Noetic
			if(response.getProcessStatus().getString().size()==0) {
				logger.error("Error:- getReturnid array is empty");
				throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Noetic " + ErrorEnum.RECORD_NOT_FOUND.getDescription());
			}

			//check if process status received from noetic = Error - noetic still return status 200(ok) with error as response
			if(response.getProcessStatus().getString().get(0).equalsIgnoreCase("Error")) {
				logger.error("Error:- Noetic getProcessStatus = Error");
				logger.info("Neotic Get Download Receipts response - Process Status:- "+ response.getProcessStatus().getString().get(0));
				logger.info("Neotic Get Download Receipts response - Process Error:- "+ response.getProcessError().getString().get(0));
				logger.info("Neotic Get Download Receipts response - Return ID:- "+ response.getReturnid().getLong().get(0));
				throw new WebserviceException(ErrorEnum.RESPONSE_RECEIVED_WITH_ERROR, "Noetic " + ErrorEnum.RESPONSE_RECEIVED_WITH_ERROR.getDescription() + ":- " +response.getProcessError().getString().get(0));
			}

			logger.info("Noetic getProcessStatus = Success");
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
			for(int iterate=0; iterate<response.getProcessStatus().getString().size(); iterate++) {
				if(response.getProcessStatus().getString().get(iterate).equalsIgnoreCase("Successful") || response.getProcessStatus().getString().get(iterate).equalsIgnoreCase("Success")) {
					String orPdfUrl = response.getOrPdfUrl().getString().get(iterate);

					innerMap = new HashMap<>(); // create a new fresh innerMap!
					innerMap.put("orPdfUrl", orPdfUrl);

					logger.info("Noetic Get Download Receipts - line number:- " + iterate + ", record:- " +innerMap);
					
					//convert inner hashmap to string and add to resultList
					resultList.add(innerMap);	
				}
			}//end for loop
			map.put("get_downloadReceiptResult", resultList);
		}
		return map;
	}

	public HashMap<String, List<Map<String, String>>> processViewDownloadDocument(ViewGetDownloadDocument viewGetDownloadDocument) {
		logger.info("BillingViewBusinessRule - processViewDownloadDocument");

		//Local declaration
		HashMap<String, List<Map<String, String>>> map = new HashMap<>();
		HashMap<String, String> innerMap; //not initialize because innerMap will be freshen up in each iteration
		GetDownloaddocuments request = new GetDownloaddocuments();
		SReturnDownloadDocuments response = new SReturnDownloadDocuments();

		// set generated requestId
		Long requestId = RequestIdGenerator.uniqueCurrentTimeMS();
		logger.info("requestId generated:- "+requestId);

		//Convert client request - Date to XMLGregorianCalendar - throw exception
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(viewGetDownloadDocument.getDocumentDate());
		XMLGregorianCalendar xmlDocumentDate;
		try {
			xmlDocumentDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		} catch (DatatypeConfigurationException e) {
			logger.error("Error:- Date conversion failed!");
			throw new WebserviceException(ErrorEnum.DATETIME_PARSING_FAILED, e.getMessage());
		}

		//set data to request object for noetic
		request.setUnitid(viewGetDownloadDocument.getUnitID());
		request.setDocumentNumber(viewGetDownloadDocument.getDocumentNo());
		request.setDocumentDate(xmlDocumentDate);
		request.setRequestid(requestId);
		request.setUserid(noeticUserId); //change to userID
		request.setPassword(noeticPassword); //change to password

		//logging request
		logger.info("Noetic Get Download Document API request");
		logger.info("Noetic Get Download Document - Unit ID:- "+ request.getUnitid());
		logger.info("Noetic Get Download Document - Document Number:- "+ request.getDocumentNumber());
		logger.info("Noetic Get Download Document - Document Date:- "+ request.getDocumentDate());
		logger.info("Noetic Get Download Document - Request ID:- "+ request.getRequestid());
		logger.info("Noetic Get Download Document - User ID:- "+ request.getUserid());

		//send to Noetic and receive response
		response = response(request).getGetDownloaddocumentsResult();

		if(response != null) {
			logger.info("Response array from noetic based on return id(response.getProcessStatus().getString().size()) - "+response.getProcessStatus().getString().size());
			//check if array is empty using return id - return id always have value if there is any record in Noetic
			if(response.getProcessStatus().getString().size()==0) {
				logger.error("Error:- getReturnid array is empty");
				throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Noetic " + ErrorEnum.RECORD_NOT_FOUND.getDescription());
			}

			//check if process status received from noetic = Error - noetic still return status 200(ok) with error as response
			if(response.getProcessStatus().getString().get(0).equalsIgnoreCase("Error")) {
				logger.error("Error:- Noetic getProcessStatus = Error");
				logger.info("Neotic Get Download Document response - Process Status:- "+ response.getProcessStatus().getString().get(0));
				logger.info("Neotic Get Download Document response - Process Error:- "+ response.getProcessError().getString().get(0));
				logger.info("Neotic Get Download Document response - Return ID:- "+ response.getReturnid().getLong().get(0));
				throw new WebserviceException(ErrorEnum.RESPONSE_RECEIVED_WITH_ERROR, "Noetic " + ErrorEnum.RESPONSE_RECEIVED_WITH_ERROR.getDescription() + ":- " +response.getProcessError().getString().get(0));
			}

			logger.info("Noetic getProcessStatus = Success");
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
			for(int iterate=0; iterate<response.getProcessStatus().getString().size(); iterate++) {
				if(response.getProcessStatus().getString().get(iterate).equalsIgnoreCase("Successful") || response.getProcessStatus().getString().get(iterate).equalsIgnoreCase("Success")) {
					String trxnPdfUrl = response.getTransactionPdfUrl().getString().get(iterate);

					innerMap = new HashMap<>(); // create a new fresh innerMap!
					innerMap.put("transactionPdfUrl", trxnPdfUrl);

					logger.info("Noetic Get Download Document - line number:- " + iterate + ", record:- " +innerMap);
					//convert inner hashmap to string and add to resultList
					resultList.add(innerMap);	
				}
			}//end for loop
			map.put("get_downloadDocumentsResult", resultList);
		}
		return map;
	}

	public HashMap<String, List<Map<String, String>>> processViewTransaction(ViewGetTransaction viewGetTransaction) {
		logger.info("BillingViewBusinessRule - processViewTransaction");
		//Local declaration
		HashMap<String, List<Map<String, String>>> map = new HashMap<>();
		HashMap<String, String> innerMap; //not initialize because innerMap will be freshen up in each iteration
		GetTransactions request = new GetTransactions();
		SReturnGetTransactions response = new SReturnGetTransactions();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String getAllTransaction ="A";

		// set generated requestId
		Long requestId = RequestIdGenerator.uniqueCurrentTimeMS();
		logger.info("requestId generated:- "+requestId);
		
		//Convert client request - Date to XMLGregorianCalendar - throw exception
		GregorianCalendar gcStart = new GregorianCalendar();
		GregorianCalendar gcEnd = new GregorianCalendar();
		gcStart.setTime(viewGetTransaction.getStartDate());
		gcEnd.setTime(viewGetTransaction.getEndDate());
		XMLGregorianCalendar xmlStartDate, xmlEndDate;
		try {
			xmlStartDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcStart);
			xmlEndDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcEnd);
		} catch (DatatypeConfigurationException e) {
			logger.error("Error:- Date conversion failed!");
			throw new WebserviceException(ErrorEnum.DATETIME_PARSING_FAILED, e.getMessage());
		}

		//set data to request object for noetic
		request.setUnitid(viewGetTransaction.getUnitID());
		//for getTrxn, since mobile will gives array but noetic only call for single param, we always call [A]ll transaction.
		request.setTransactionClass(getAllTransaction);
		request.setRequestid(requestId);
		request.setStartDate(xmlStartDate);
		request.setEndDate(xmlEndDate);
		request.setUserid(noeticUserId); //change to userID
		request.setPassword(noeticPassword); //change to password

		//logging request
		logger.info("Noetic Get Transaction API request");
		logger.info("Noetic Get Transaction - Unit ID:- "+ request.getUnitid());
		logger.info("Noetic Get Transaction - Transaction Class:- "+ request.getTransactionClass());
		logger.info("Noetic Get Transaction - Request ID:- "+ request.getRequestid());
		logger.info("Noetic Get Transaction - Start Date:- "+ request.getStartDate());
		logger.info("Noetic Get Transaction - End Date:- "+ request.getEndDate());
		logger.info("Noetic Get Transaction - User ID:- "+ request.getUserid());

		//send to Noetic and receive response
		response = response(request).getGetTransactionsResult();

		if(response != null) {
			logger.info("Response array from noetic based on return id(response.getProcessStatus().getString().size()) - "+response.getProcessStatus().getString().size());
			//check if array is empty using return id - return id always have value if there is any record in Noetic
			if(response.getProcessStatus().getString().size()==0) {
				logger.error("Error:- getReturnid array is empty");
				throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Noetic " + ErrorEnum.RECORD_NOT_FOUND.getDescription());
			}

			//check if process status received from noetic = Error - noetic still return status 200(ok) with error as response
			if(response.getProcessStatus().getString().get(0).equalsIgnoreCase("Error")) {
				logger.error("Error:- Noetic getProcessStatus = Error");
				logger.info("Neotic Get Transaction response - Process Status:- "+ response.getProcessStatus().getString().get(0));
				logger.info("Neotic Get Transaction response - Process Error:- "+ response.getProcessError().getString().get(0));
				logger.info("Neotic Get Transaction response - Return ID:- "+ response.getReturnid().getLong().get(0));
				throw new WebserviceException(ErrorEnum.RESPONSE_RECEIVED_WITH_ERROR, "Noetic " + ErrorEnum.RESPONSE_RECEIVED_WITH_ERROR.getDescription() + ":- " +response.getProcessError().getString().get(0));
			}

			logger.info("Noetic getProcessStatus = Success");
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
			for(int iterate=0; iterate<response.getProcessStatus().getString().size(); iterate++) {
				if(response.getProcessStatus().getString().get(iterate).equalsIgnoreCase("Successful") || response.getProcessStatus().getString().get(iterate).equalsIgnoreCase("Success")) {
					for (int i=0; i<viewGetTransaction.getTransactionClass().length; i++) {
						if(response.getTransactionClass().getString().get(iterate).equalsIgnoreCase(viewGetTransaction.getTransactionClass()[i])) {

							int arledgeritemid = response.getArledgeritemid().getInt().get(iterate);
							String trxnClass = response.getTransactionClass().getString().get(iterate);
							String documentNo = response.getDocumentNumber().getString().get(iterate);
							String billingType = response.getBillingType().getString().get(iterate);
							String itemDescription = response.getItemDescription().getString().get(iterate);
							BigDecimal documentAmount = response.getDocumentAmount().getDecimal().get(iterate);
							BigDecimal outstandingAmount = response.getOutstandingAmount().getDecimal().get(iterate);

							//convert XMLGregorianCalendar -> Date -> String
							XMLGregorianCalendar xmlDocDate = response.getDocumentDate().getDateTime().get(iterate);
							Date docDateXML = xmlDocDate.toGregorianCalendar().getTime();
							String documentDate = formatter.format(docDateXML);

							//convert XMLGregorianCalendar -> Date -> String
							XMLGregorianCalendar xmlDueDate = response.getDueDate().getDateTime().get(iterate);
							Date dueDateXML = xmlDueDate.toGregorianCalendar().getTime();
							String dueDate = formatter.format(dueDateXML);

							innerMap = new HashMap<>(); // create a new fresh innerMap!
							innerMap.put("arledgeritemid", String.valueOf(arledgeritemid));
							innerMap.put("transactionClass", trxnClass);
							innerMap.put("documentNo", documentNo);
							innerMap.put("billingType", billingType);
							innerMap.put("itemDescription", itemDescription);
							innerMap.put("documentAmount", documentAmount.toString());
							innerMap.put("outstandingAmount", outstandingAmount.toString());
							innerMap.put("documentDate", documentDate);
							innerMap.put("dueDate", dueDate);

							logger.info("Noetic Get Transaction - line number:- " + iterate + ", record:- " +innerMap);
							//convert inner hashmap to string and add to resultList
							resultList.add(innerMap);
						}
					}//end for loop	
				}
			}//end for loop
			map.put("get_transactionResult", resultList);
		}
		return map;
	}

	public HashMap<String, List<Map<String, String>>> processViewFacilityReceipt(ViewGetFacilityReceipt viewGetFacilityReceipt) {
		logger.info("BillingViewBusinessRule - processViewFacilityReceipt");

		//Local declaration
		HashMap<String, List<Map<String, String>>> map = new HashMap<>();
		HashMap<String, String> innerMap; //not initialize because innerMap will be freshen up in each iteration
		GetFacilityreceipts request = new GetFacilityreceipts();
		SReturnFacilityReceipts response = new SReturnFacilityReceipts();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		// set generated requestId
		Long requestId = RequestIdGenerator.uniqueCurrentTimeMS();
		logger.info("requestId generated:- "+requestId);

		//Convert client request - Date to XMLGregorianCalendar - throw exception
		GregorianCalendar gcStart = new GregorianCalendar();
		GregorianCalendar gcEnd = new GregorianCalendar();
		gcStart.setTime(viewGetFacilityReceipt.getStartReceiptDate());
		gcEnd.setTime(viewGetFacilityReceipt.getEndReceiptDate());
		XMLGregorianCalendar xmlStartDate, xmlEndDate;
		try {
			xmlStartDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcStart);
			xmlEndDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcEnd);
		} catch (DatatypeConfigurationException e) {
			logger.error("Error:- Date conversion failed!");
			throw new WebserviceException(ErrorEnum.DATETIME_PARSING_FAILED, e.getMessage());
		}

		//set data to request object for noetic
		request.setUnitid(viewGetFacilityReceipt.getUnitID());
		request.setRequestid(requestId);
		request.setStartReceiptDate(xmlStartDate);
		request.setEndReceiptDate(xmlEndDate);
		request.setUserid(noeticUserId); //change to userID
		request.setPassword(noeticPassword); //change to password

		//logging request
		logger.info("Noetic Get Facility Receipt API request");
		logger.info("Noetic Get Facility Receipt - Unit ID:- "+ request.getUnitid());
		logger.info("Noetic Get Facility Receipt - Request ID:- "+ request.getRequestid());
		logger.info("Noetic Get Facility Receipt - Start Receipt Date:- "+ request.getStartReceiptDate());
		logger.info("Noetic Get Facility Receipt - End Receipt Date:- "+ request.getEndReceiptDate());
		logger.info("Noetic Get Facility Receipt - User ID:- "+ request.getUserid());

		//send to Noetic and receive response
		response = response(request).getGetFacilityreceiptsResult();

		if(response != null) {
			logger.info("Response array from noetic based on return id(response.getProcessStatus().getString().size()) - "+response.getProcessStatus().getString().size());
			//check if array is empty using return id - return id always have value if there is any record in Noetic
			if(response.getProcessStatus().getString().size()==0) {
				logger.error("Error:- getReturnid array is empty");
				throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Noetic " + ErrorEnum.RECORD_NOT_FOUND.getDescription());
			}

			//check if process status received from noetic = Error - noetic still return status 200(ok) with error as response
			if(response.getProcessStatus().getString().get(0).equalsIgnoreCase("Error")) {
				logger.error("Error:- Noetic getProcessStatus = Error");
				logger.info("Neotic Get Facility Receipt response - Process Status:- "+ response.getProcessStatus().getString().get(0));
				logger.info("Neotic Get Facility Receipt response - Process Error:- "+ response.getProcessError().getString().get(0));
				logger.info("Neotic Get Facility Receipt response - Return ID:- "+ response.getReturnid().getLong().get(0));
				throw new WebserviceException(ErrorEnum.RESPONSE_RECEIVED_WITH_ERROR, "Noetic " + ErrorEnum.RESPONSE_RECEIVED_WITH_ERROR.getDescription() + ":- " +response.getProcessError().getString().get(0));
			}

			logger.info("Noetic getProcessStatus = Success");
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
			for(int iterate=0; iterate<response.getProcessStatus().getString().size(); iterate++) {
				if(response.getProcessStatus().getString().get(iterate).equalsIgnoreCase("Successful") || response.getProcessStatus().getString().get(iterate).equalsIgnoreCase("Success")) {
					String orNumber = response.getOrNumber().getString().get(iterate);
					String referenceNo = response.getReferenceNo().getString().get(iterate);
					BigDecimal orAmount = response.getOrAmount().getDecimal().get(iterate);

					//convert XMLGregorianCalendar -> Date -> String
					XMLGregorianCalendar xmlOrDate = response.getOrDate().getDateTime().get(iterate);
					Date orDateXML = xmlOrDate.toGregorianCalendar().getTime();
					String orDate = formatter.format(orDateXML);

					innerMap = new HashMap<>(); // create a new fresh innerMap!
					innerMap.put("referenceNo", referenceNo);
					innerMap.put("orNumber", orNumber);
					innerMap.put("orDate", orDate);
					innerMap.put("orAmount", orAmount.toString());

					logger.info("Noetic Get Faciliy Receipt - line number:- " + iterate + ", record:- " +innerMap);
					//convert inner hashmap to string and add to resultList
					resultList.add(innerMap);	
				}
			}//end for loop
			map.put("get_facilityReceiptsResult", resultList);
		}
		return map;
	}

	public HashMap<String, List<Map<String, String>>> processViewOutstandingInvoice(int unitId) {
		logger.info("BillingViewBusinessRule - processViewOutstandingInvoice");

		//Local declaration
		HashMap<String, List<Map<String, String>>> map = new HashMap<>();
		HashMap<String, String> innerMap; //not initialize because innerMap will be freshen up in each iteration
		GetOutstandinginvoices request = new GetOutstandinginvoices();
		SReturnOutstandingInvoices response = new SReturnOutstandingInvoices();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		// set generated requestId
		Long requestId = RequestIdGenerator.uniqueCurrentTimeMS();
		logger.info("requestId generated:- "+requestId);

		//set data to request object for noetic
		request.setUnitid(unitId);
		request.setRequestid(requestId);
		request.setUserid(noeticUserId); //change to userID
		request.setPassword(noeticPassword); //change to password

		//logging request
		logger.info("Noetic Get Outstanding Invoice API request");
		logger.info("Noetic Get Outstanding Invoice - Unit ID:- "+ request.getUnitid());
		logger.info("Noetic Get Outstanding Invoice - Request ID:- "+ request.getRequestid());
		logger.info("Noetic Get Outstanding Invoice - User ID:- "+ request.getUserid());

		//send to Noetic and receive response
		response = response(request).getGetOutstandinginvoicesResult();

		if(response != null) {
			logger.info("Response array from noetic based on return id(response.getProcessStatus().getString().size()) - "+response.getProcessStatus().getString().size());
			//check if array is empty using return id - return id always have value if there is any record in Noetic
			if(response.getProcessStatus().getString().size()==0) {
				logger.error("Error:- Noetic response array empty");
				throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Noetic " + ErrorEnum.RECORD_NOT_FOUND.getDescription());
			}

			//check if process status received from noetic = Error - noetic still return status 200(ok) with error as response
			if(response.getProcessStatus().getString().get(0).equalsIgnoreCase("Error")) {
				logger.error("Error:- Noetic getProcessStatus = Error");
				logger.info("Neotic Get Outstanding Invoice response - Process Status:- "+ response.getProcessStatus().getString().get(0));
				logger.info("Neotic Get Outstanding Invoice response - Process Error:- "+ response.getProcessError().getString().get(0));
				logger.info("Neotic Get Outstanding Invoice response - Return ID:- "+ response.getReturnid().getLong().get(0));
				throw new WebserviceException(ErrorEnum.RESPONSE_RECEIVED_WITH_ERROR, "Noetic " + ErrorEnum.RESPONSE_RECEIVED_WITH_ERROR.getDescription() + ":- " +response.getProcessError().getString().get(0));
			}

			logger.info("Noetic getProcessStatus = Success");
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
			for(int iterate=0; iterate<response.getProcessStatus().getString().size(); iterate++) {
				if(response.getProcessStatus().getString().get(iterate).equalsIgnoreCase("Successful") || response.getProcessStatus().getString().get(iterate).equalsIgnoreCase("Success")) {
					int arledgeritemid = response.getArledgeritemid().getInt().get(iterate);
					String trxnClass = response.getTransactionClass().getString().get(iterate);
					String documentNo = response.getDocumentNumber().getString().get(iterate);
					String trxnMode = response.getTransactionMode().getString().get(iterate);
					String billingType = response.getBillingType().getString().get(iterate);
					String itemDescription = response.getItemDescription().getString().get(iterate);
					BigDecimal documentAmount = response.getDocumentAmount().getDecimal().get(iterate);
					BigDecimal outstandingAmount = response.getOutstandingAmount().getDecimal().get(iterate);

					//convert XMLGregorianCalendar -> Date -> String
					XMLGregorianCalendar xmlDocDate = response.getDocumentDate().getDateTime().get(iterate);
					Date docDateXML = xmlDocDate.toGregorianCalendar().getTime();
					String documentDate = formatter.format(docDateXML);

					//convert XMLGregorianCalendar -> Date -> String
					XMLGregorianCalendar xmlDueDate = response.getDueDate().getDateTime().get(iterate);
					Date dueDateXML = xmlDueDate.toGregorianCalendar().getTime();
					String dueDate = formatter.format(dueDateXML);

					innerMap = new HashMap<>(); // create a new fresh innerMap!
					innerMap.put("arledgeritemid", String.valueOf(arledgeritemid));
					innerMap.put("transactionClass", trxnClass);
					innerMap.put("documentNo", documentNo);
					innerMap.put("documentDate", documentDate);
					innerMap.put("transactionMode", trxnMode);
					innerMap.put("dueDate", dueDate);
					innerMap.put("billingType", billingType);
					innerMap.put("itemDescription", itemDescription);
					innerMap.put("documentAmount", documentAmount.toString());
					innerMap.put("outstandingAmount", outstandingAmount.toString());

					logger.info("Noetic Get Outstanding Invoice - line number:- " + iterate + ", record:- " +innerMap);
					//convert inner hashmap to string and add to resultList
					resultList.add(innerMap);
				}
				map.put("get_outstandingInvoicesResult", resultList);
			}
		}
		return map;
	}

	public HashMap<String, List<Map<String, String>>> processViewOverdueInvoice(ViewGetOverdueInvoice viewGetOverdueInvoice) {
		logger.info("BillingViewBusinessRule - processViewOverdueInvoice");

		//Local declaration
		HashMap<String, List<Map<String, String>>> map = new HashMap<>();
		HashMap<String, String> innerMap; //not initialize because innerMap will be freshen up in each iteration
		GetOverdueinvoices request = new GetOverdueinvoices();
		SReturnGetOverdueInvoices response = new SReturnGetOverdueInvoices();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		// set generated requestId
		Long requestId = RequestIdGenerator.uniqueCurrentTimeMS();
		logger.info("requestId generated:- "+requestId);

		//Convert client request - Date to XMLGregorianCalendar - throw exception
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(viewGetOverdueInvoice.getAsAtDate());
		XMLGregorianCalendar xmlAsAtDate;
		try {
			xmlAsAtDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		} catch (DatatypeConfigurationException e) {
			logger.error("Error:- Date conversion failed!");
			throw new WebserviceException(ErrorEnum.DATETIME_PARSING_FAILED, e.getMessage());
		}

		//set data to request object for noetic
		request.setUnitid(viewGetOverdueInvoice.getUnitID());
		request.setRequestid(requestId);
		request.setAsAtDate(xmlAsAtDate);
		request.setUserid(noeticUserId); //change to userID
		request.setPassword(noeticPassword); //change to password

		//logging request
		logger.info("Noetic Get Overdue Invoice API request");
		logger.info("Noetic Get Overdue Invoice - Unit ID:- "+ request.getUnitid());
		logger.info("Noetic Get Overdue Invoice - Request ID:- "+ request.getRequestid());
		logger.info("Noetic Get Overdue Invoice - As At Date:- "+ request.getAsAtDate());
		logger.info("Noetic Get Overdue Invoice - User ID:- "+ request.getUserid());

		//send to Noetic and receive response
		response = response(request).getGetOverdueinvoicesResult();

		if(response != null) {
			logger.info("Response array from noetic based on return id(response.getProcessStatus().getString().size()) - "+response.getProcessStatus().getString().size());
			//check if array is empty using return id - return id always have value if there is any record in Noetic
			if(response.getProcessStatus().getString().size()==0) {
				logger.error("Error:- getReturnid array is empty");
				throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Noetic " + ErrorEnum.RECORD_NOT_FOUND.getDescription());
			}

			//check if process status received from noetic = Error - noetic still return status 200(ok) with error as response
			if(response.getProcessStatus().getString().get(0).equalsIgnoreCase("Error")) {
				logger.error("Error:- Noetic getProcessStatus = Error");
				logger.info("Neotic Get Overdue Invoice response - Process Status:- "+ response.getProcessStatus().getString().get(0));
				logger.info("Neotic Get Overdue Invoice response - Process Error:- "+ response.getProcessError().getString().get(0));
				logger.info("Neotic Get Overdue Invoice response - Return ID:- "+ response.getReturnid().getLong().get(0));
				throw new WebserviceException(ErrorEnum.RESPONSE_RECEIVED_WITH_ERROR, "Noetic " + ErrorEnum.RESPONSE_RECEIVED_WITH_ERROR.getDescription() + ":- " +response.getProcessError().getString().get(0));
			}

			logger.info("Noetic getProcessStatus = Success");
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
			for(int iterate=0; iterate<response.getProcessStatus().getString().size(); iterate++) {
				if(response.getProcessStatus().getString().get(iterate).equalsIgnoreCase("Successful") || response.getProcessStatus().getString().get(iterate).equalsIgnoreCase("Success")) {
					int arledgeritemid = response.getArledgeritemid().getInt().get(iterate);
					String trxnClass = response.getTransactionClass().getString().get(iterate);
					String documentNo = response.getDocumentNumber().getString().get(iterate);
					String billingType = response.getBillingType().getString().get(iterate);
					String itemDescription = response.getItemDescription().getString().get(iterate);
					BigDecimal documentAmount = response.getDocumentAmount().getDecimal().get(iterate);
					BigDecimal outstandingAmount = response.getOutstandingAmount().getDecimal().get(iterate);

					//convert XMLGregorianCalendar -> Date -> String
					XMLGregorianCalendar xmlDocDate = response.getDocumentDate().getDateTime().get(iterate);
					Date docDateXML = xmlDocDate.toGregorianCalendar().getTime();
					String documentDate = formatter.format(docDateXML);

					//convert XMLGregorianCalendar -> Date -> String
					XMLGregorianCalendar xmlDueDate = response.getDueDate().getDateTime().get(iterate);
					Date dueDateXML = xmlDueDate.toGregorianCalendar().getTime();
					String dueDate = formatter.format(dueDateXML);

					innerMap = new HashMap<>(); // create a new fresh innerMap!
					innerMap.put("transactionClass", trxnClass);
					innerMap.put("arledgeritemid", String.valueOf(arledgeritemid));
					innerMap.put("documentNo", documentNo);
					innerMap.put("documentDate", documentDate);
					innerMap.put("dueDate", dueDate);
					innerMap.put("billingType", billingType);
					innerMap.put("itemDescription", itemDescription);
					innerMap.put("documentAmount", documentAmount.toString());
					innerMap.put("outstandingAmount", outstandingAmount.toString());

					logger.info("Noetic Get Overdue Invoice - line number:- " + iterate + ", record:- " +innerMap);
					//convert inner hashmap to string and add to resultList
					resultList.add(innerMap);	
				}
			}//end for loop
			map.put("get_overdueInvoicesResult", resultList);
		}
		return map;
	}

	public HashMap<String, List<Map<String, String>>> processViewDownloadSoa(ViewGetDownloadSoa viewGetDownloadSoa) {
		logger.info("BillingViewBusinessRule - processViewDownloadSoa");

		//Local declaration
		HashMap<String, List<Map<String, String>>> map = new HashMap<>();
		HashMap<String, String> innerMap; //not initialize because innerMap will be freshen up in each iteration
		GetDownloadsoa request = new GetDownloadsoa();
		SReturnDownloadSoa response = new SReturnDownloadSoa();

		// set generated requestId
		Long requestId = RequestIdGenerator.uniqueCurrentTimeMS();
		logger.info("requestId generated:- "+requestId);

		//Convert client request - Date to XMLGregorianCalendar - throw exception
		GregorianCalendar gcStart = new GregorianCalendar();
		GregorianCalendar gcEnd = new GregorianCalendar();
		gcStart.setTime(viewGetDownloadSoa.getStartDate());
		gcEnd.setTime(viewGetDownloadSoa.getEndDate());
		XMLGregorianCalendar xmlStartDate, xmlEndDate;
		try {
			xmlStartDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcStart);
			xmlEndDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcEnd);
		} catch (DatatypeConfigurationException e) {
			logger.error("Error:- Date conversion failed!");
			throw new WebserviceException(ErrorEnum.DATETIME_PARSING_FAILED, e.getMessage());
		}

		//set data to request object for noetic
		request.setUnitid(viewGetDownloadSoa.getUnitID());
		request.setRequestid(requestId);
		request.setStartDate(xmlStartDate);
		request.setEndDate(xmlEndDate);
		request.setUserid(noeticUserId); //change to userID
		request.setPassword(noeticPassword); //change to password

		//logging request
		logger.info("Noetic Get Download SOA API request");
		logger.info("Noetic Get Download SOA - Unit ID:- "+ request.getUnitid());
		logger.info("Noetic Get Download SOA - Request ID:- "+ request.getRequestid());
		logger.info("Noetic Get Download SOA - Start Date:- "+ request.getStartDate());
		logger.info("Noetic Get Download SOA - End Date:- "+ request.getEndDate());
		logger.info("Noetic Get Download SOA - User ID:- "+ request.getUserid());

		//send to Noetic and receive response
		response = response(request).getGetDownloadsoaResult();

		if(response != null) {
			logger.info("Response array from noetic based on return id(response.getProcessStatus().getString().size()) - "+response.getProcessStatus().getString().size());
			//check if array is empty using return id - return id always have value if there is any record in Noetic
			if(response.getProcessStatus().getString().size()==0) {
				logger.error("Error:- getReturnid array is empty");
				throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Noetic " + ErrorEnum.RECORD_NOT_FOUND.getDescription());
			}

			//check if process status received from noetic = Error - noetic still return status 200(ok) with error as response
			if(response.getProcessStatus().getString().get(0).equalsIgnoreCase("Error")) {
				logger.error("Error:- Noetic getProcessStatus = Error");
				logger.info("Neotic Get Download SOA response - Process Status:- "+ response.getProcessStatus().getString().get(0));
				logger.info("Neotic Get Download SOA response - Process Error:- "+ response.getProcessError().getString().get(0));
				logger.info("Neotic Get Download SOA response - Return ID:- "+ response.getReturnid().getLong().get(0));
				throw new WebserviceException(ErrorEnum.RESPONSE_RECEIVED_WITH_ERROR, "Noetic " + ErrorEnum.RESPONSE_RECEIVED_WITH_ERROR.getDescription() + ":- " +response.getProcessError().getString().get(0));
			}

			logger.info("Noetic getProcessStatus = Success");
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
			for(int iterate=0; iterate<response.getProcessStatus().getString().size(); iterate++) {
				if(response.getProcessStatus().getString().get(iterate).equalsIgnoreCase("Successful") || response.getProcessStatus().getString().get(iterate).equalsIgnoreCase("Success")) {
					String soaPdfUrl = response.getSoaPdfUrl().getString().get(iterate);

					innerMap = new HashMap<>(); // create a new fresh innerMap!
					innerMap.put("soaPdfUrl", soaPdfUrl);

					logger.info("Noetic Get Download Receipts - line number:- " + iterate + ", record:- " +innerMap);
					//convert inner hashmap to string and add to resultList
					resultList.add(innerMap);	
				}
			}
			map.put("get_downloadSoaResult", resultList);
		}
		return map;
	}

	/* Sending request to NOETIC */
	private GetDownloadreceiptResponse response(GetDownloadreceipt request) {
		logger.info("BillingViewBusinessRule - GetDownloadreceiptResponse");
		return soapClientService.getDownloadReceipt(request);
	}

	/* Sending request to NOETIC */
	private GetDownloaddocumentsResponse response(GetDownloaddocuments request) {
		logger.info("BillingViewBusinessRule - GetDownloaddocumentsResponse");
		return soapClientService.getDownloadDocument(request);
	}

	/* Sending request to NOETIC */
	private GetTransactionsResponse response(GetTransactions request) {
		logger.info("BillingViewBusinessRule - GetTransactionsResponse");
		return soapClientService.getTransaction(request);
	}

	/* Sending request to NOETIC */
	private GetFacilityreceiptsResponse response(GetFacilityreceipts request) {
		logger.info("BillingViewBusinessRule - GetFacilityreceiptsResponse");
		return soapClientService.getFacilityReceipt(request);
	}

	/* Sending request to NOETIC */
	private GetOutstandinginvoicesResponse response(GetOutstandinginvoices request) {
		logger.info("BillingViewBusinessRule - GetOutstandinginvoicesResponse");
		return soapClientService.getOutstandingInvoices(request);
	}

	/* Sending request to NOETIC */
	private GetOverdueinvoicesResponse response(GetOverdueinvoices request) {
		logger.info("BillingViewBusinessRule - GetOverdueinvoicesResponse");
		return soapClientService.getOverdueInvoices(request);
	}

	/* Sending request to NOETIC */
	private GetDownloadsoaResponse response(GetDownloadsoa request) {
		logger.info("BillingViewBusinessRule - GetDownloadsoaResponse");
		return soapClientService.getDownloadSoa(request);
	}

}
