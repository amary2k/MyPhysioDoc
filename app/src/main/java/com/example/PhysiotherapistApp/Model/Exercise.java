package com.example.PhysiotherapistApp.Model;

import java.io.Serializable;

public class Exercise implements Serializable {

	private long id;

	private String description;

	private byte[] image;
		
	public Exercise() {
		super();
	}
	
	public Exercise( String description, byte[] image) {
		super();
		this.description = description;
		this.image = image;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
}
