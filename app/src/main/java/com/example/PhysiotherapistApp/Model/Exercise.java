package com.example.PhysiotherapistApp.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;


public class Exercise implements Serializable{


	private ExercisePK exercisePK;


	//private String title;

	private String description;

	private String imageName;

	private String videoLink;


	//private boolean isComplete;

	private transient Collection<Schedule> schedule = new ArrayList<Schedule>();

	public Exercise() {
		super();
	}

	public Exercise( String description, String imageName, ExercisePK exercisePK) {
		super();
		this.exercisePK = exercisePK;
		this.description = description;
		this.imageName = imageName;
	}

	public Exercise( String description, String imageName, String videoLink, ExercisePK exercisePK) {
		super();
		this.exercisePK = exercisePK;
		this.description = description;
		this.imageName = imageName;
		this.videoLink = videoLink;
	}
	/*public Exercise(String title, String description, String imageName, boolean isComplete) {
		super();
		this.title = title;
		this.description = description;
		this.imageName = imageName;
		this.isComplete = isComplete;
	}

	public Exercise(String title, String description, String imageName, String videoLink, boolean isComplete) {
		super();
		this.title = title;
		this.description = description;
		this.imageName = imageName;
		this.videoLink = videoLink;
		this.isComplete = isComplete;
	}

	public boolean isComplete() {
		return isComplete;
	}


	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}*/

	/*public boolean isComplete() {
		return isComplete;
	}


	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}*/


	public Collection<Schedule> getSchedule() {
		return schedule;
	}

	public void setSchedule(Collection<Schedule> schedule) {
		this.schedule = schedule;
	}

	/*public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}*/
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}


	public String getVideoLink() {
		return videoLink;
	}


	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}


	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}


	/*public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}*/

	public ExercisePK getExercisePK() {
		return exercisePK;
	}

	public void setExercisePK(ExercisePK exercisePK) {
		this.exercisePK = exercisePK;
	}
}
