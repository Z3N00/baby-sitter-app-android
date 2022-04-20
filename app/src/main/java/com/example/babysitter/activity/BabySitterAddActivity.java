package com.example.babysitter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babysitter.databinding.ActivityBabySitterAddBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BabySitterAddActivity extends AppCompatActivity {
    String type;
    ActivityBabySitterAddBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBabySitterAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        type = getIntent().getStringExtra("type");
        if (type.equalsIgnoreCase("parent")) {
            binding.descriptionTextField.setVisibility(View.GONE);
            binding.priceTextField.setVisibility(View.GONE);
        }

        binding.signUpButton.setOnClickListener(v -> addUser());
    }

    public void addUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Map<Object, Object> map = new HashMap<>();
        map.put("name", user.getDisplayName());
        map.put("email", user.getEmail());
        map.put("phone", user.getPhoneNumber());
        map.put("address", binding.addressTextField.getEditText().getText().toString());
        int number = (int) (Math.random() * 7);
        map.put("profile", number + ".png");

        if (type.equalsIgnoreCase("babysitter")) {
            String description = binding.descriptionTextField.getEditText().getText().toString();
            double price =  Double.parseDouble(binding.priceTextField.getEditText().getText().toString());
            map.put("description", description);
            map.put("price", price);
            map.put("rating", 0.0f);
            map.put("ratingCount", 0);
        }
        FirebaseFirestore
                .getInstance()
                .collection(type + "s")
                .document(user.getUid())
                .set(map)
                .addOnCompleteListener(task -> startMainActivity());
    }

    public void startMainActivity() {
        Intent intent = new Intent(BabySitterAddActivity.this, MainActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
        finish();
    }
}