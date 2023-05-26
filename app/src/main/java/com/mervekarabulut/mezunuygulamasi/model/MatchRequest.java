package com.mervekarabulut.mezunuygulamasi.model;

public class MatchRequest {
    public String key; // add this field
    public String senderId;
    public String receiverId;
    public String status;  // "pending", "accepted", "rejected"

    public MatchRequest() {
        // Default constructor required for calls to DataSnapshot.getValue(MatchRequest.class)
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MatchRequest(String key, String senderId, String receiverId, String status) {
        this.key = key;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
    }

    // Getters and setters...
}

