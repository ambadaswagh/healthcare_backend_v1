package com.healthcare.chat.model;

import lombok.Data;

import java.util.List;

@Data
public class GroupChatRequest {
	private Long groupId;
	private String name;
	private List<Long> selectedIds;
}
