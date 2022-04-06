package com.example.babysitter.model;

public class ChatMessage {
    public String message;
    public String sender;
    public String profile;

    public ChatMessage(String message, String sender, String profile) {
        this.message = message;
        this.sender = sender;
        this.profile = profile;
    }
}
