package com.softwaregiants.careertinder.models;

public class ChatModel {
	private String sender;
	private String message;
	private String id;

	public ChatModel() {
	}

	public ChatModel(String sender, String message) {
		this.sender = sender;
		this.message = message;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
