package com.healthcare.chat.model;

import lombok.Data;

@Data
public class ChatMessageModel {
	private String message;
	private Long groupId;
	private Long toAdminId;
}
