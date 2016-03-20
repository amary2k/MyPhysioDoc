package com.example.PhysiotherapistApp.Model;


import java.io.Serializable;

public class ExercisePK implements Serializable{
	private String title;
	private boolean isComplete;

	private int painLevel;
	
	public ExercisePK() {}
	
	public ExercisePK(String title, boolean isComplete) {
		super();
		this.title = title;
		this.isComplete = isComplete;
	}
	public ExercisePK(String title, boolean isComplete, int painLevel) {
		super();
		this.title = title;
		this.isComplete = isComplete;
		this.painLevel = painLevel;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isComplete() {
		return isComplete;
	}
	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public int getPainLevel() {
		return painLevel;
	}

	public void setPainLevel(int painLevel) {
		this.painLevel = painLevel;
	}
}
