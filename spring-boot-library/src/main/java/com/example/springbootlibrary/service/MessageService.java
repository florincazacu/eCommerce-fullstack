package com.example.springbootlibrary.service;

import com.example.springbootlibrary.dao.MessageRepository;
import com.example.springbootlibrary.entity.Message;
import com.example.springbootlibrary.requestmodels.AdminQuestionRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

	public void putMessage(AdminQuestionRequest adminQuestionRequest, String userEmail) throws Exception {
		Optional<Message> messageOptional = messageRepository.findById(adminQuestionRequest.getId());

		if (messageOptional.isEmpty()) {
			throw new Exception("Message not found");
		}
		Message message = messageOptional.get();

		message.setAdminEmail(userEmail);
		message.setResponse(adminQuestionRequest.getResponse());
		message.setClosed(true);
		messageRepository.save(message);
	}
}
