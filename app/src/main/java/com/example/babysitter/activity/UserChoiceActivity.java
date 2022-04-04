package com.example.babysitter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babysitter.R;

public class UserChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_choice);
        ImageView babySitterImage = findViewById(R.id.babysitterImage);
        babySitterImage.setOnClickListener(v -> startSignInManual("babysitter"));
        ImageView parentImage = findViewById(R.id.parentImage);
        parentImage.setOnClickListener(v -> startSignInManual("parent"));
    }

    public void startSignInManual(String type) {
        Intent intent = new Intent(this, SignupOptionsActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

}
