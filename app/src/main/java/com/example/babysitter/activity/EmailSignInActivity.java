package com.example.babysitter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babysitter.databinding.ActivityEmailSignInBinding;
import com.google.firebase.auth.FirebaseAuth;

public class EmailSignInActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ActivityEmailSignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailSignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        binding.loginButton.setOnClickListener(v -> signIn());
        binding.forgotPassword.setOnClickListener(v -> forgotPassword());
    }

    public void forgotPassword() {
        String email = binding.emailTextFieldLogin.getEditText().getText().toString();
        if (email.trim().isEmpty()) {
            Toast.makeText(this, "Please fill the email above and try again!", Toast.LENGTH_SHORT).show();
        } else {
            auth
                    .sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(EmailSignInActivity.this, "Password reset email sent successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EmailSignInActivity.this, "Could not send email, please try again later!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void signIn() {
        String email = binding.emailTextFieldLogin.getEditText().getText().toString();
        String password = binding.passwordTextFieldLogin.getEditText().getText().toString();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("SignIn", "signInWithEmail:success");
                        Intent intent = new Intent(EmailSignInActivity.this, MainActivity.class);
                        intent.putExtra("type", getIntent().getStringExtra("type"));
                        startActivity(intent);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("SignIn", "signInWithEmail:failure", task.getException());
                        Toast.makeText(EmailSignInActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}