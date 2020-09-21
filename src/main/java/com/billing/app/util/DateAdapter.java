package com.billing.app.util;

import java.text.SimpleDateFormat;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class DateAdapter extends XmlAdapter<String, XMLGregorianCalendar> {
	
	private static final String CUSTOM_FORMAT_STRING = "yyyy-MM-dd";

	@Override
	public XMLGregorianCalendar unmarshal(String v) throws Exception {
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(v);
	}

	@Override
	public String marshal(XMLGregorianCalendar v) throws Exception {
        return new SimpleDateFormat(CUSTOM_FORMAT_STRING).format(v);
	}
}