package com.example.springbootlibrary.controller;

import com.example.springbootlibrary.entity.Message;
import com.example.springbootlibrary.requestmodels.AdminQuestionRequest;
import com.example.springbootlibrary.service.MessageService;
import com.example.springbootlibrary.utils.ExtractJwt;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/messages")
public class MessagesController {

	private final MessageService messageService;

	public MessagesController(MessageService messageService) {
		this.messageService = messageService;
	}

	@PostMapping("/secure/add/message")
	public void postMessage(@RequestHeader(value = "Authorization") String token, @RequestBody Message messageRequest) {
		String userEmail = ExtractJwt.payloadJwtExtraction(token, Util.SUB);

		messageService.postMessage(messageRequest, userEmail);
	}

	@PutMapping("/secure/admin/message")
	public void putMessage(@RequestHeader(value = "Authorization") String token,
	                       @RequestBody AdminQuestionRequest adminQuestionRequest) throws Exception {
		String userEmail = ExtractJwt.payloadJwtExtraction(token, Util.SUB);
		String admin = ExtractJwt.payloadJwtExtraction(token, "\"userType\"");

		if (admin == null || !admin.equals("admin")) {
			throw new Exception("Administration page only.");
		}

		messageService.putMessage(adminQuestionRequest, userEmail);
	}
}
