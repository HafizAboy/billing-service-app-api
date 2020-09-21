package com.billing.app.noetic.service;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.soap.client.SoapFaultClientException;

import com.billing.app.constant.ErrorEnum;
import com.billing.app.exception.WebserviceException;
import com.billing.app.noetic.config.SOAPConnector;
import com.billing.app.noetic.wsdl.ArrayOfString;
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
import com.billing.app.noetic.wsdl.SendFacilitypayment;
import com.billing.app.noetic.wsdl.SendFacilitypaymentResponse;
import com.billing.app.noetic.wsdl.SendPayment;
import com.billing.app.noetic.wsdl.SendPaymentResponse;
import com.billing.app.service.EmailService;

import java.util.List;

/**
 * @author Hafiz
 */
@Service
public class SOAPClientService {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${noetic.api.url}")
	private String noeticAPIUrl;

	@Autowired
	private SOAPConnector soapConnector;

	@Autowired
	EmailService emailService;

	private int counter = 0;
	String errorResponse;

	@Retryable(value = {SoapFaultClientException.class, WebServiceIOException.class}, maxAttemptsExpression = "#{${noetic.retry.attempt}}", backoff = @Backoff(delayExpression = "#{${noetic.backoff.delay}}"))
	public SendFacilitypaymentResponse sendFacilityPayment(SendFacilitypayment request) {
		logger.info("Ref No. {} :: SOAPClientService - sendFacilityPayment", request.getReferenceNo());
		SendFacilitypaymentResponse response = new SendFacilitypaymentResponse();
		counter++;
		logger.info("Ref No. {} :: Noetic sendFacilityPayment retry counter:- "+counter, request.getReferenceNo());
		response = (SendFacilitypaymentResponse) soapConnector.callWebService(noeticAPIUrl, request);
		if (response == null) {
			logger.info("Ref No. {} :: Noetic sendPayment no response....", request.getReferenceNo());
			if(counter == 1) {
				throw new SoapFaultClientException(null);
			}else if(counter == 2) {
				throw new WebServiceIOException(null);
			}else {
				throw new RuntimeException();
			}
		}else {
			counter = 0;//reset counter
		}
		return response;
	}

	//will recover from any SoapFaultClientException due to SOAP issues
	//will recover from connection to webserver refused
	@Recover
	public SendFacilitypaymentResponse recoverSendFacilitypaymentResponse(Throwable t, SendFacilitypayment request) {
		SendFacilitypaymentResponse response = new SendFacilitypaymentResponse();
		logger.error("Noetic recoverSendFacilitypaymentResponse - recover!");
		logger.error("Noetic recoverSendFacilitypaymentResponse - Error class:- "+t.getClass().getName());
		logger.error("Noetic recoverSendFacilitypaymentResponse - reason for recover:- "+t.getMessage());
		logger.error("Noetic recoverSendFacilitypaymentResponse - request: Unit ID - "+request.getUnitid()+", Reference No - "+request.getReferenceNo());

		counter = 0;//reset counter
		//Sending email notification
        logger.info("Ref No. {} :: Sending email to recipients on Noetic Failure", request.getReferenceNo());
		String content = "Process Booking Payment(Send Facility Payment API) request: Unit ID - "+request.getUnitid()+", Reference No - "+request.getReferenceNo();
		emailService.sendSimpleMessage(content);

		return response;
	}

	private String printArrayOfString(ArrayOfString arrayOfString){
        List<String> listString;
	    if (arrayOfString != null){
	        listString = arrayOfString.getString();
	        if (listString != null) {
                return ArrayUtils.toString(listString.toArray());
            }
        }
	    return "";
    }


	@Retryable(value = {SoapFaultClientException.class, WebServiceIOException.class}, maxAttemptsExpression = "#{${noetic.retry.attempt}}", backoff = @Backoff(delayExpression = "#{${noetic.backoff.delay}}"))
	public SendPaymentResponse sendPayment(SendPayment request) {
		logger.info("Ref No. {} :: SOAPClientService - sendPayment", printArrayOfString(request.getReferenceNo()));
		SendPaymentResponse response = new SendPaymentResponse();
		counter++;
		logger.info("Ref No. {} :: Noetic sendPayment retry counter:- "+counter, printArrayOfString(request.getReferenceNo()));
		response = (SendPaymentResponse) soapConnector.callWebService(noeticAPIUrl, request);
		if (response == null) {
			logger.info("Ref No. {} :: Noetic sendPayment no response....", printArrayOfString(request.getReferenceNo()));
			if(counter == 1) {
				throw new SoapFaultClientException(null);
			}else if(counter == 2) {
				throw new WebServiceIOException(null);
			}else {
				throw new RuntimeException();
			}
		}else {
			counter = 0;//reset counter
		}
		return response;
	}

	//will recover from any SoapFaultClientException due to SOAP issues
	//will recover from connection to webserver refused
	@Recover
	public SendPaymentResponse recoverSendPaymentResponse(Throwable t, SendPayment request) {
		SendPaymentResponse response = new SendPaymentResponse();
		logger.error("Noetic recoverSendPaymentResponse - recover!");
		logger.error("Noetic recoverSendPaymentResponse - Error class:- "+t.getClass().getName());
		logger.error("Noetic recoverSendPaymentResponse -  reason for recover:- "+t.getMessage());

		counter = 0;//reset counter
		String content = "Process Invoice Payment(Send Payment API)";
		for(int iterate=0; iterate<request.getUnitid().getInt().size(); iterate++) {

			logger.error("Noetic recoverSendPaymentResponse - request: Unit ID - "+request.getUnitid().getInt().get(iterate)+", Reference No - "+request.getReferenceNo().getString().get(iterate));

			content = content+" request: Unit ID - "+request.getUnitid().getInt().get(iterate)+", Reference No - "+request.getReferenceNo().getString().get(iterate);
		}
		//Sending email notification
        logger.info("Ref No. {} :: Sending email to recipients on Noetic Failure", printArrayOfString(request.getReferenceNo()));
		emailService.sendSimpleMessage(content);

		return response;
	}

	public GetDownloadreceiptResponse getDownloadReceipt(GetDownloadreceipt request) {
		logger.info("SOAPClientService - getDownloadReceipt");
		GetDownloadreceiptResponse response = new GetDownloadreceiptResponse();
		try {
			response = (GetDownloadreceiptResponse) soapConnector.callWebService(noeticAPIUrl, request);
		} catch (Exception e) {
			logger.error("Noetic access request failed!");
			e.printStackTrace();
			if(e.getCause()==null) {
				errorResponse = e.getLocalizedMessage();
			}else {
				errorResponse = e.getCause().getMessage();
			}
			throw new WebserviceException(ErrorEnum.ACCESS_DENIED_INVALID_ENDPOINT, "Noetic error response - " + errorResponse);
		}
		return response;
	}

	public GetDownloaddocumentsResponse getDownloadDocument(GetDownloaddocuments request) {
		logger.info("SOAPClientService - getDownloadDocument");
		GetDownloaddocumentsResponse response = new GetDownloaddocumentsResponse();
		try {
			response = (GetDownloaddocumentsResponse) soapConnector.callWebService(noeticAPIUrl, request);
		} catch (Exception e) {
			logger.error("Noetic access request failed!");
			e.printStackTrace();
			if(e.getCause()==null) {
				errorResponse = e.getLocalizedMessage();
			}else {
				errorResponse = e.getCause().getMessage();
			}
			throw new WebserviceException(ErrorEnum.ACCESS_DENIED_INVALID_ENDPOINT, "Noetic error response - " + errorResponse);
		}
		return response;
	}

	public GetTransactionsResponse getTransaction(GetTransactions request) {
		logger.info("SOAPClientService - getTransaction");
		GetTransactionsResponse response = new GetTransactionsResponse();
		try {
			response = (GetTransactionsResponse) soapConnector.callWebService(noeticAPIUrl, request);
		} catch (Exception e) {
			logger.error("Noetic access request failed!");
			e.printStackTrace();
			if(e.getCause()==null) {
				errorResponse = e.getLocalizedMessage();
			}else {
				errorResponse = e.getCause().getMessage();
			}
			throw new WebserviceException(ErrorEnum.ACCESS_DENIED_INVALID_ENDPOINT, "Noetic error response - " + errorResponse);
		}
		return response;
	}

	public GetFacilityreceiptsResponse getFacilityReceipt(GetFacilityreceipts request) {
		logger.info("SOAPClientService - getFacilityReceipt");
		GetFacilityreceiptsResponse response = new GetFacilityreceiptsResponse();
		try {
			response = (GetFacilityreceiptsResponse) soapConnector.callWebService(noeticAPIUrl, request);
		} catch (Exception e) {
			logger.error("Noetic access request failed!");
			e.printStackTrace();
			if(e.getCause()==null) {
				errorResponse = e.getLocalizedMessage();
			}else {
				errorResponse = e.getCause().getMessage();
			}
			throw new WebserviceException(ErrorEnum.ACCESS_DENIED_INVALID_ENDPOINT, "Noetic error response - " + errorResponse);
		}
		return response;
	}

	public GetOutstandinginvoicesResponse getOutstandingInvoices(GetOutstandinginvoices request) {
		logger.info("SOAPClientService - getOutstandingInvoices");
		GetOutstandinginvoicesResponse response = new GetOutstandinginvoicesResponse();
		try {
			response = (GetOutstandinginvoicesResponse) soapConnector.callWebService(noeticAPIUrl, request);
		} catch (Exception e) {
			logger.error("Noetic access request failed!");
			e.printStackTrace();
			if(e.getCause()==null) {
				errorResponse = e.getLocalizedMessage();
			}else {
				errorResponse = e.getCause().getMessage();
			}
			throw new WebserviceException(ErrorEnum.ACCESS_DENIED_INVALID_ENDPOINT, "Noetic error response - " + errorResponse);
		}
		return response;
	}

	public GetOverdueinvoicesResponse getOverdueInvoices(GetOverdueinvoices request) {
		logger.info("SOAPClientService - getOverdueInvoices");
		GetOverdueinvoicesResponse response = new GetOverdueinvoicesResponse();
		try {
			response = (GetOverdueinvoicesResponse) soapConnector.callWebService(noeticAPIUrl, request);
		} catch (Exception e) {
			logger.error("Noetic access request failed!");
			e.printStackTrace();
			if(e.getCause()==null) {
				errorResponse = e.getLocalizedMessage();
			}else {
				errorResponse = e.getCause().getMessage();
			}
			throw new WebserviceException(ErrorEnum.ACCESS_DENIED_INVALID_ENDPOINT, "Noetic error response - " + errorResponse);
		}
		return response;
	}

	public GetDownloadsoaResponse getDownloadSoa(GetDownloadsoa request) {
		logger.info("SOAPClientService - getDownloadSoa");
		GetDownloadsoaResponse response = new GetDownloadsoaResponse();
		try {
			response = (GetDownloadsoaResponse) soapConnector.callWebService(noeticAPIUrl, request);
		} catch (Exception e) {
			logger.error("Noetic access request failed!");
			e.printStackTrace();
			if(e.getCause()==null) {
				errorResponse = e.getLocalizedMessage();
			}else {
				errorResponse = e.getCause().getMessage();
			}
			throw new WebserviceException(ErrorEnum.ACCESS_DENIED_INVALID_ENDPOINT, "Noetic error response - " + errorResponse);
		}
		return response;
	}
}
