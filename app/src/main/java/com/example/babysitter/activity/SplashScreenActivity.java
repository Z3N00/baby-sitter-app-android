package com.example.babysitter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babysitter.R;
import com.example.babysitter.model.BabySitter;
import com.example.babysitter.model.Parent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SplashScreenActivity.this, UserChoiceActivity.class));
                finish();
            }, 3000);
        } else {
            FirebaseFirestore
                    .getInstance()
                    .collection("parents")
                    .document(user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.getResult().exists()) {
                            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            intent.putExtra("type", "parent");
                            intent.putExtra("parent", task.getResult().toObject(Parent.class));
                            startActivity(intent);
                        }
                    });
            FirebaseFirestore
                    .getInstance()
                    .collection("babysitters")
                    .document(user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.getResult().exists()) {
                            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            intent.putExtra("type", "babysitter");
                            intent.putExtra("sitter", task.getResult().toObject(BabySitter.class));
                            startActivity(intent);
                        }
                    });
        }
    }
}