package com.example.babysitter.model;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;
import java.util.List;

public class Booking implements Serializable {
    @DocumentId
    public String ref;
    public String name;
    public long startDate;
    public long endDate;
    public List<String> services;
    public double total;
    public String babysitter;
    public String parent;
    public String profile;
    public Boolean accepted;
    public boolean review;

    Booking() {
    }

}
