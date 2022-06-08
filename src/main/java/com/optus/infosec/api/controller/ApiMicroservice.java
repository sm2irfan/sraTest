package com.optus.infosec.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiMicroservice {
	
	private static final String SERVICE_URL = "http://INFOSEC-API/infosec-api";
	
	@Autowired
	protected RestTemplate restTemplate;
	
	
	public String getToken() {
		ResponseEntity<String> response = restTemplate.getForEntity(SERVICE_URL+"/live/getToken", String.class);
		return response.getHeaders().getFirst("x-csrf-token");
		
	}

}
