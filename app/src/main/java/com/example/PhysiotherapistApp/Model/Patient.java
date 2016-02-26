package com.example.PhysiotherapistApp.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.google.gson.Gson;



public class Patient {


	private long id;
	private String email;
	String name;
	String branch;
	String hospital_name;

	private Date admission_date;

	private Collection<Schedule> schedule = new ArrayList<Schedule>();

	private Address homeAddress;

	private Address officeAddress;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getHospital_name() {
		return hospital_name;
	}

	public void setHospital_name(String hospital_name) {
		this.hospital_name = hospital_name;
	}

	public Date getJoining_date() {
		return admission_date;
	}

	public void setJoining_date(Date joining_date) {
		this.admission_date = joining_date;
	}

	public Address getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(Address homeAddress) {
		this.homeAddress = homeAddress;
	}

	public Address getOfficeAddress() {
		return officeAddress;
	}

	public void setOfficeAddress(Address officeAddress) {
		this.officeAddress = officeAddress;
	}

	public Collection<Schedule> getSchedule() {
		return schedule;
	}

	public void setSchedule(Collection<Schedule> schedule) {
		this.schedule = schedule;
	}

	public Patient() {
		super();
	}

	public Patient(String name, String branch, String hospital_name,String email) {
		super();
		this.name = name;
		this.branch = branch;
		this.hospital_name = hospital_name;
		this.email = email;
	}

	public Patient(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getJSON(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}


}
