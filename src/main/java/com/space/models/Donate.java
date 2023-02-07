package com.space.models;

public class Donate {
    private String doId;
    private String userId;
    private double doAmount;
    private String doData;
    private String doState;
    public Donate() {
    }
    public Donate(String doId, String userId, double doAmount, String doData, String doState) {
        this.doId = doId;
        this.userId = userId;
        this.doAmount = doAmount;
        this.doData = doData;
        this.doState = doState;
    }
    public String getDoId() {
        return doId;
    }
    public void setDoId(String doId) {
        this.doId = doId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public double getDoAmount() {
        return doAmount;
    }
    public void setDoAmount(double doAmount) {
        this.doAmount = doAmount;
    }
    public String getDoData() {
        return doData;
    }
    public void setDoData(String doData) {
        this.doData = doData;
    }
    public String getDoState() {
        return doState;
    }
    public void setDoState(String doState) {
        this.doState = doState;
    }
}
