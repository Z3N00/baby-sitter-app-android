package com.example.babysitter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babysitter.databinding.ActivityEmailSignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EmailSignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ActivityEmailSignUpBinding binding;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        type = getIntent().getStringExtra("type");
        if (type.equalsIgnoreCase("parent")) {
            binding.descriptionTextField.setVisibility(View.GONE);
            binding.priceTextField.setVisibility(View.GONE);
        }

        binding.signUpButton.setOnClickListener(v -> {
            String email = binding.emailTextField.getEditText().getText().toString();
            String password = binding.passwordTextField.getEditText().getText().toString();

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Log.d("EmailSignUp", "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w("EmailSignUp", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(EmailSignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    public void updateUI(FirebaseUser user) {
        String address = binding.addressTextField.getEditText().getText().toString();
        String name = binding.nameTextField.getEditText().getText().toString();
        String email = binding.emailTextField.getEditText().getText().toString();
        String phone = binding.phoneTextField.getEditText().getText().toString();

        Map<Object, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("email", email);
        map.put("phone", phone);
        map.put("address", address);
        int number = (int) (Math.random() * 7);
        map.put("profile", number + ".png");

        if (type.equalsIgnoreCase("babysitter")) {
            String description = binding.descriptionTextField.getEditText().getText().toString();
            double price =  Double.parseDouble(binding.priceTextField.getEditText().getText().toString());
            map.put("description", description);
            map.put("price", price);
        }
        FirebaseFirestore
                .getInstance()
                .collection(type + "s")
                .document(user.getUid())
                .set(map)
                .addOnCompleteListener(task -> {
                    Intent intent = new Intent(EmailSignUpActivity.this, MainActivity.class);
                    intent.putExtra("type", type);
                    startActivity(intent);
                    finish();
                });
    }
}