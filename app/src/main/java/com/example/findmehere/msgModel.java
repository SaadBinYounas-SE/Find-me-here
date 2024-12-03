package com.example.findmehere;


public class msgModel {
    String messege;
    String senderId;
    String profile;
    long timestamp;
    String recieverId;

    public msgModel() {
    }
    public msgModel(String messege, String senderId,String profile,String recieverId, long timestamp) {
        this.messege = messege;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.profile=profile;
        this.recieverId=recieverId;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getMessege() {
        return messege;
    }

    public void setMessege(String messege) {
        this.messege = messege;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
