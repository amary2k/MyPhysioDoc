package com.example.PhysiotherapistApp.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;

public class Physiotherapist {

	private long id;


	private String phoneRegisterationId;

	private String name;

	private String email;

	private String branch;

	private String hospital_name;

	private Date joining_date;

	private Address address;


	private Set<Message> messages = new HashSet<Message>();

	private Collection<Patient> patients = new ArrayList<Patient>();
	
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
		return joining_date;
	}
	public void setJoining_date(Date joining_date) {
		this.joining_date = joining_date;
	}

	public String getPhoneRegisterationId() {
		return phoneRegisterationId;
	}

	public void setPhoneRegisterationId(String phoneRegisterationId) {
		this.phoneRegisterationId = phoneRegisterationId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public Collection<Patient> getPatients() {
		return patients;
	}
	
	public void setPatients(Set<Patient> patients) {
		this.patients = patients;
	}
	
	public void addPatients(Patient patient){
		this.patients.add(patient);
	}

	public Set<Message> getMessages() {
		return messages;
	}

	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}

	public String getJSON(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
}
