package com.example.PhysiotherapistApp.Model;

import java.util.Date;


import com.google.gson.Gson;

public class Message {

	private  long id;
	private String message;
	private Date message_time;
	private String toUser;
	private String fromUser;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	public Date getMessageTime() {
		return message_time;
	}

	public void setMessageTime(Date message_time) {
		this.message_time = message_time;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public Message(String message, Date message_time, String to, String from) {
		this.message = message;
		this.message_time = message_time;
		this.toUser = to;
		this.fromUser = from;
	}

	public Message() {
		super();
	}

	public String getJSON(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}


}
