package com.billing.app.noetic.config;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

/**
 * @author Hafiz
 */
public class SOAPConnector extends WebServiceGatewaySupport {

	private static final Logger logger = LogManager.getLogger(SOAPConnector.class);
	@Autowired
	Jaxb2Marshaller jaxb2Marshaller;
    
	public Object callWebService(String url, Object request) {
    	MessageFactory msgFactory = null;
		try {
			msgFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		} catch (SOAPException e) {
			logger.error("SOAP 1.2 PROTOCAL FAILED");
			logger.error(e.getMessage(), e);
		}
    	WebServiceTemplate webServiceTemplate = new WebServiceTemplate(jaxb2Marshaller);
    	SaajSoapMessageFactory newSoapMessageFactory = new SaajSoapMessageFactory(msgFactory);
    	webServiceTemplate.setMessageFactory(newSoapMessageFactory);
    	
    	return webServiceTemplate.marshalSendAndReceive(url, request);
    }
}
