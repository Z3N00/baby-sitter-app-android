package com.example.babysitter.model;

import com.google.firebase.firestore.DocumentId;

public class Service {
    @DocumentId
    public String ref;
    public String name;
    public double price;
    public boolean selected = false;

    public Service() {
    }
}
