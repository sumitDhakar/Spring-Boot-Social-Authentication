package com.dollop.userauth.security;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.dollop.userauth.exception.AuthenticationFailedException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class InvalidAuthenticationEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
	        AuthenticationException authException) throws IOException, ServletException {
	    
	    Map<String, Object> resp = new HashMap<>();
	    resp.put("status", HttpStatus.UNAUTHORIZED.value());
	    resp.put("error", "Unauthorized");
	    resp.put("message", "Invalid Data Passed Token");
	    resp.put("timestamp", LocalDate.now().toString()); // Optionally, you can include a timestamp
	    
	    ObjectMapper mapper = new ObjectMapper();
	    String jsonResponse = mapper.writeValueAsString(resp);
	    
	    response.setContentType("application/json");
	    response.setStatus(HttpStatus.UNAUTHORIZED.value());
	    response.getWriter().write(jsonResponse);
	}

	
//	@Override
//	public void commence(HttpServletRequest request, HttpServletResponse response,
//			AuthenticationException authException) throws IOException, ServletException {
//		
//		Map<String , String> resp = new HashMap<>();
//		resp.put("status", HttpStatus.UNAUTHORIZED.toString());
//		resp.put("data", "Invalid Data Pass Token");
//		
//		ObjectMapper mapper = new ObjectMapper();
//		String st = mapper.writeValueAsString(resp);
//		
//		response.getWriter().write(st);
////		response.sendError(response.SC_UNAUTHORIZED,"Sorry you are not authorized to access this resource .");
//		response.flushBuffer();
//	
//	}

}

