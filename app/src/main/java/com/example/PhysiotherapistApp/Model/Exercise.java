package com.example.PhysiotherapistApp.Model;

import com.example.PhysiotherapistApp.Model.Schedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;




public class Exercise implements Serializable {

	private long id;

	private String title;

	private String description;
	/*@Lob
	@Column(length=100000)
	private byte[] image;*/
	private String imageName;


	private String videoLink;

	private transient Collection<Schedule> schedule = new ArrayList<Schedule>();

	public Exercise() {
		super();
	}


	public Exercise(String title, String description, String imageName) {
		super();
		this.title = title;
		this.description = description;
		this.imageName = imageName;
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

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public String getVideoLink() {
		return videoLink;
	}

	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}
}
