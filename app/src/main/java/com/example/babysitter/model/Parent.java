package com.example.babysitter.model;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class Parent implements Serializable {
    @DocumentId
    public String ref;
    public String name;
    public String profile;
    public String profileUrl;
    public String email;
    public String phone;
    public String address;

    public Parent() {
    }
}
