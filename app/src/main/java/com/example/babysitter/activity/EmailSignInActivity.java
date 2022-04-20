package com.example.babysitter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babysitter.databinding.ActivityEmailSignInBinding;
import com.example.babysitter.model.BabySitter;
import com.example.babysitter.model.Parent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmailSignInActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ActivityEmailSignInBinding binding;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailSignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();

        type = getIntent().getStringExtra("type");
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
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        checkType(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("SignIn", "signInWithEmail:failure", task.getException());
                        Toast.makeText(EmailSignInActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void checkType(FirebaseUser user) {
        FirebaseFirestore
                .getInstance()
                .collection(type + "s")
                .document(user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot document = task.getResult();
                    Intent intent;
                    if (document.exists()) {
                        intent = new Intent(this, MainActivity.class);
                        intent.putExtra("type", type);
                    } else {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(this,
                                "You can only be babysitter or a parent not both",
                                Toast.LENGTH_SHORT).show();
                        intent = new Intent(this, UserChoiceActivity.class);
                    }
                    startActivity(intent);
                    finish();
                });
    }
}