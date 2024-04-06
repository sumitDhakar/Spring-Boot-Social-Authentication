package com.dollop.userauth.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailUtils {

	@Autowired
	private JavaMailSender emailSender;

	public String sendEmail(String to,String name,String otp) throws MessagingException, IOException {

		String subject="Email Verification";
		
		// Load the HTML template
        ClassPathResource htmlResource = new ClassPathResource("templates/VerificationEmail.html");
        InputStream inputStream = htmlResource.getInputStream();
        Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
        String htmlContent = scanner.hasNext() ? scanner.next() : "";
		
        htmlContent = htmlContent.replace("[UserName]", name);
        htmlContent = htmlContent.replace("[OTP]", otp);
        htmlContent = htmlContent.replace("[verification_link]", "http://localhost:4200/login?email="+to+"&&value="+otp);
		
        
        
		MimeMessage message = emailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom("javatesting406@gmail.com");
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(htmlContent,true);

//		FileSystemResource file = new FileSystemResource(new File("C:/Users/Hp/Downloads/New-file.gif"));
//		helper.addAttachment("Invoice", file);

		emailSender.send(message);
		
		return "Email Sent Successfully to "+to;
	}


}
