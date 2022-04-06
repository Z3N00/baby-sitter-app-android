package com.example.babysitter.model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;

public class Review implements Serializable {
    @DocumentId
    public String ref;
    public String name;
    public String profile;
    public String description;
    public String babysitter;

    public Review() {
    }
}
