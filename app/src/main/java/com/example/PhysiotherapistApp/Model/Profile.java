package com.example.PhysiotherapistApp.Model;

public class Profile {
    private String name;
    private String address;
    private int phoneNo;
    private String branch;
    private int sIN;
    private String history;
    private String currentDiag;
    private String exercisePeriod;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public int getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(int phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getsIN() {
        return sIN;
    }

    public void setsIN(int sIN) {
        this.sIN = sIN;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getCurrentDiag() {
        return currentDiag;
    }

    public void setCurrentDiag(String currentDiag) {
        this.currentDiag = currentDiag;
    }

    public String getExercisePeriod() {
        return exercisePeriod;
    }

    public void setExercisePeriod(String exercisePeriod) {
        this.exercisePeriod = exercisePeriod;
    }
}
