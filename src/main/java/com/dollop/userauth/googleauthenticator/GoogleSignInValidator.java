package com.dollop.userauth.googleauthenticator;

//******************************** GOOGLE SIGNIN VALIDATOR ***************************************

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import com.dollop.userauth.constants.AppConstants;
import com.dollop.userauth.entity.payload.SocialLoginRequest;
import com.dollop.userauth.exception.AuthenticationFailedException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

public class GoogleSignInValidator {

//	//@Value("${CLIENT.ID}")
//    private static String CLIENT_ID = ""; // Replace with your Google API Client ID

	public static GoogleIdToken.Payload validateIdToken(String idTokenString, String clientId) {

		try {
			HttpTransport transport = new NetHttpTransport();
			JsonFactory jsonFactory = GsonFactory.getDefaultInstance(); // Use GsonFactory

			// Set up the Google ID token verifier
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
					.setAudience(Collections.singletonList(clientId)).build();
               System.err.println("verifier"+ verifier);
			// Verify the ID token
			GoogleIdToken idToken = null;
			try {
				System.err.println(verifier.getIssuers());
				idToken = verifier.verify(idTokenString);
				System.out.println(idToken);
			} catch (IllegalArgumentException ex) {
				System.out.println("IllegalArgumentException: " + ex.getMessage());
			}
			if (idToken != null) {
				GoogleIdToken.Payload payload = idToken.getPayload();
				// You can access user information from the payload
				return payload;
			} else {
				// Invalid token
				throw new AuthenticationFailedException(AppConstants.INVALID_TOKEN);
			}
		} catch (GeneralSecurityException | IOException e) {
			// Handle exceptions
			throw new AuthenticationFailedException(AppConstants.INVALID_TOKEN);
		}
//		return null;
	}
	
	
	
	
	


	
}
