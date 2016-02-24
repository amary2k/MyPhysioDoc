package com.example.PhysiotherapistApp.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class Schedule {

	private long id;

	private Collection<Exercise> exercises = new ArrayList<Exercise>();

	private Date exercise_date;

	public Collection<Exercise> getPatients() {
		return exercises;
	}

	public void setPatients(Collection<Exercise> patients) {
		this.exercises = patients;
	}

	public Date getExercise_date() {
		return exercise_date;
	}

	public void setExercise_date(Date exercise_date) {
		this.exercise_date = exercise_date;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
}
