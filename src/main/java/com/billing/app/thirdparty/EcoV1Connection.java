package com.billing.app.thirdparty;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.billing.app.model.EcoProfileResponse;
import com.billing.app.stereotype.ThirdParty;
import com.billing.app.util.HttpUtility;
import com.billing.app.util.LogUtility;

@ThirdParty
public class EcoV1Connection {
	private static final Logger logger = LogManager.getLogger(EcoV1Connection.class);
	
	@Value("${acc.v1.api.account.url}")
	private String apiUrl;

	public EcoProfileResponse getProfile(HttpServletRequest request) throws IllegalAccessException, InstantiationException, IOException {
		String bearerToken = request.getHeader("Authorization");
		
		LogUtility.info(request, logger, "Authorization field is " + bearerToken);
		
		if (bearerToken.toLowerCase().startsWith("bearer ")) {
			bearerToken = bearerToken.substring("bearer ".length());
		}
		
		EcoProfileResponse response = HttpUtility.get(EcoProfileResponse.class, apiUrl, bearerToken);
		
		// TODO: Remove this (or not)
		String respJson = new ObjectMapper().writeValueAsString(response);
		LogUtility.info(request, logger, "Response from profile: " + respJson);
		
		return response;
	}

	public EcoProfileResponse getProfile(String bearer) throws IllegalAccessException, InstantiationException, IOException {	
		return HttpUtility.get(EcoProfileResponse.class, apiUrl, bearer);
	}
}
