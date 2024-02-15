package com.example.springbootlibrary.controller;

import com.example.springbootlibrary.entity.Message;
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
}
