package com.dollop.userauth.exception;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiErrorResponse {

	private String message;
	private boolean success;
	private String status;
	private LocalDate date = LocalDate.now();
}
