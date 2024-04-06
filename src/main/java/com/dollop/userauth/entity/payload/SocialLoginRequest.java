package com.dollop.userauth.entity.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SocialLoginRequest {

	private String clientId;
	private String userToken;
	private String type;
}
