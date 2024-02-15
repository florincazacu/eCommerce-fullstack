package com.example.springbootlibrary.service;

import com.example.springbootlibrary.dao.MessageRepository;
import com.example.springbootlibrary.entity.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessageService {

	private final MessageRepository messageRepository;

	public MessageService(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	public void postMessage(Message messageRequest, String userEmail) {
		Message message = new Message(messageRequest.getTitle(), messageRequest.getQuestion());
		message.setUserEmail(userEmail);

		messageRepository.save(message);
	}
}
