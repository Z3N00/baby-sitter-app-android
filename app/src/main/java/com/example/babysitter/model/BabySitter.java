package com.example.babysitter.model;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class BabySitter implements Serializable {
    @DocumentId
    public String ref;
    public String name;
    public String profile;
    public String description;
    public float rating;
    public int ratingCount;
    public String address;
    public double price;

    public BabySitter() {
    }
}
