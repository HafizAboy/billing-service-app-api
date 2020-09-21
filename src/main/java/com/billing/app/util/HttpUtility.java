package com.billing.app.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import javax.activation.UnsupportedDataTypeException;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.billing.app.model.KeyValuePair;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpUtility {
	private static final Logger LOG = LoggerFactory.getLogger(HttpUtility.class);
	
	public static HashMap<String, String> postAndGetWwwUrlEncode(String urlString, String accessToken, List<KeyValuePair> params) throws IllegalAccessException, InstantiationException, IOException {
		HashMap<String, String> values = new HashMap<>();
		String urlParameters = "";

		for (KeyValuePair param : params) {
			if (!urlParameters.equals("")) {
				urlParameters += "&";
			}

			urlParameters += URLEncoder.encode(param.key, "UTF-8") + "=" + URLEncoder.encode(param.value, "UTF-8");
		}
		
		// TODO Remove this
		System.out.println("urlParameters " + urlParameters);

		byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
		int postDataLength = postData.length;

		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setDoOutput(true);
		conn.setInstanceFollowRedirects(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
		
		if (accessToken != null && !accessToken.trim().isEmpty()) {
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
		}
		
		conn.setUseCaches(false);

		try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
			wr.write(postData);
		}

		String urlEncodeResponse = "";

		try (InputStreamReader in = new InputStreamReader(conn.getInputStream())) {
			try (BufferedReader br = new BufferedReader(in)) {
				String text = "";
				while ((text = br.readLine()) != null) {
					urlEncodeResponse += text;
				}
			}
		}

		try {
			// TODO: Remove this
			System.out.println("urlEncodeResponse " + urlEncodeResponse);
			
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			for (String pair: urlEncodeResponse.split("&")) {
				String[] keyValue = pair.split("=");
				
				if (keyValue.length == 1) {
					values.put(keyValue[0], "");
				} else if (keyValue.length == 2) {
					values.put(keyValue[0], URLDecoder.decode(keyValue[1], "UTF-8"));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new UnsupportedDataTypeException(ex.getMessage());
		}

		return values;
	}

	public static <T> T postWithWwwUrlEncode(Class<T> cl, String urlString, String accessToken, List<KeyValuePair> params) throws IllegalAccessException, InstantiationException, IOException {
		T value = null;

		String urlParameters = "";

		for (KeyValuePair param : params) {
			if (!urlParameters.equals("")) {
				urlParameters += "&";
			}

			urlParameters += URLEncoder.encode(param.key, "UTF-8") + "=" + URLEncoder.encode(param.value, "UTF-8");
		}
		
		// TODO Remove this
		System.out.println("urlParameters " + urlParameters);

		byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
		int postDataLength = postData.length;

		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setDoOutput(true);
		conn.setInstanceFollowRedirects(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
		
		if (accessToken != null && !accessToken.trim().isEmpty()) {
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
		}
		
		conn.setUseCaches(false);

		try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
			wr.write(postData);
		}

		String jsonResponse = "";

		try (InputStreamReader in = new InputStreamReader(conn.getInputStream())) {
			try (BufferedReader br = new BufferedReader(in)) {
				String text = "";
				while ((text = br.readLine()) != null) {
					jsonResponse += text;
				}
			}
		}

		try {
			// TODO: Remove this
			System.out.println("jsonResponse " + jsonResponse);
			
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			value = objectMapper.readValue(jsonResponse, cl);
		} catch (Exception ex) {
			throw new UnsupportedDataTypeException(ex.getMessage());
		}

		return value;
	}

	public static <T> T postWithJson(Class<T> cl, String urlString, String accessToken, Object postItem, boolean parseOutput) throws IllegalAccessException, InstantiationException, IOException {
		T value = null;

		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		byte[] postData = objectMapper.writeValueAsBytes(postItem);
		int postDataLength = postData.length;

		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setDoOutput(true);
		conn.setInstanceFollowRedirects(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		conn.setRequestProperty("accept", "application/json");
		conn.setRequestProperty("Accept-Charset", "UTF-8");
		conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
		
		if (accessToken != null && !accessToken.trim().isEmpty()) {
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
		}
		
		conn.setUseCaches(false);

		try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
			wr.write(postData);
		}

		String jsonResponse = "";

		try (InputStreamReader in = new InputStreamReader(conn.getInputStream())) {
			try (BufferedReader br = new BufferedReader(in)) {
				String text = "";
				while ((text = br.readLine()) != null) {
					jsonResponse += text;
				}
			}
		}

		if (parseOutput) {
			try {
				value = objectMapper.readValue(jsonResponse, cl);
			} catch (Exception ex) {
				throw new UnsupportedDataTypeException(ex.getMessage());
			}
		}

		return value;
	}
	
	public static String postToNoeticSoap(String urlString, String companyCode, String supplierNo, String paymentMethod, String referenceNo, 
			String receivedFrom, String receivedDate, BigDecimal amount, BigDecimal promo,
			String userId, String password) throws IllegalAccessException, InstantiationException, IOException {
		String response = "";

		DecimalFormat df = new DecimalFormat("#,##0.00");
		
		String requestXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n"
			+ "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\r\n"
			+ "  <soap12:Body>\r\n"
			+ "    <of_process_insuretech xmlns=\"http://tempurl.org\">\r\n"
			+ "      <fs_company_code>" + StringEscapeUtils.escapeXml11(companyCode) + "</fs_company_code>\r\n"
			+ "      <fs_supplier_no>" + StringEscapeUtils.escapeXml11(supplierNo) + "</fs_supplier_no>\r\n"
			+ "      <fs_payment_method>" + StringEscapeUtils.escapeXml11(paymentMethod) + "</fs_payment_method>\r\n"
			+ "      <fs_reference_no>" + StringEscapeUtils.escapeXml11(referenceNo) + "</fs_reference_no>\r\n"
			+ "      <fs_received_from>" + StringEscapeUtils.escapeXml11(receivedFrom) + "</fs_received_from>\r\n"
			+ "      <fs_received_date>" + StringEscapeUtils.escapeXml11(receivedDate) + "</fs_received_date>\r\n"
			+ "      <fd_received_amount>" + StringEscapeUtils.escapeXml11(df.format(amount)) + "</fd_received_amount>\r\n"
			+ "      <fd_absorption_amount>" + StringEscapeUtils.escapeXml11(df.format(promo)) + "</fd_absorption_amount>\r\n"
			+ "      <fs_userid>" + StringEscapeUtils.escapeXml11(userId) + "</fs_userid>\r\n"
			+ "      <fs_password>" + StringEscapeUtils.escapeXml11(password) + "</fs_password>\r\n"
			+ "    </of_process_insuretech>\r\n"
			+ "  </soap12:Body>\r\n"
			+ "</soap12:Envelope>";
		
		LOG.info("Sending to Noetic: " + requestXml);

		byte[] postData = requestXml.getBytes(StandardCharsets.UTF_8);
		int postDataLength = postData.length;

		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setDoOutput(true);
		conn.setInstanceFollowRedirects(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
		conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
		
		conn.setUseCaches(false);

		try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
			wr.write(postData);
		}

		try (InputStreamReader in = new InputStreamReader(conn.getInputStream())) {
			try (BufferedReader br = new BufferedReader(in)) {
				String text = "";
				while ((text = br.readLine()) != null) {
					response += text;
				}
			}
		}
		
		LOG.info("Noetic response: " + response);

		return response;
	}

	public static <T> T get(Class<T> cl, String urlString, String accessToken) throws IllegalAccessException, InstantiationException, IOException {
		T value = null;

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		URL obj = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		conn.setRequestMethod("GET");
		
		if (accessToken != null && !accessToken.trim().isEmpty()) {
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
		}
		

		String jsonResponse = "";

		try (InputStreamReader in = new InputStreamReader(conn.getInputStream())) {
			try (BufferedReader br = new BufferedReader(in)) {
				String text = "";
				while ((text = br.readLine()) != null) {
					jsonResponse += text;
				}
			}
		}

		try {
			value = objectMapper.readValue(jsonResponse, cl);
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
			throw new UnsupportedDataTypeException(ex.getMessage());
		}

		return value;
	}



	public static <T> T getWithBasicAuth(Class<T> cl, String urlString, String auth) throws IllegalAccessException, InstantiationException, IOException {
		T value = null;

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		URL obj = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		conn.setRequestMethod("GET");
		
		if (auth != null && !auth.trim().isEmpty()) {
			conn.setRequestProperty("Authorization", "Basic " + auth);
		}
		

		String jsonResponse = "";

		try (InputStreamReader in = new InputStreamReader(conn.getInputStream())) {
			try (BufferedReader br = new BufferedReader(in)) {
				String text = "";
				while ((text = br.readLine()) != null) {
					jsonResponse += text;
				}
			}
		}

		try {
			value = objectMapper.readValue(jsonResponse, cl);
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
			throw new UnsupportedDataTypeException(ex.getMessage());
		}

		return value;
	}
}
