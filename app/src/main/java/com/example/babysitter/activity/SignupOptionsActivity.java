package com.example.babysitter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babysitter.R;
import com.example.babysitter.databinding.ActivitySignupOptionsBinding;
import com.example.babysitter.model.BabySitter;
import com.example.babysitter.model.Parent;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupOptionsActivity extends AppCompatActivity {
    private GoogleSignInClient client;
    private FirebaseAuth auth;
    private CallbackManager manager;
    private static final int RC_SIGN_IN = 1000;
    private ActivitySignupOptionsBinding binding;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySignupOptionsBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        type = getIntent().getStringExtra("type");
        auth = FirebaseAuth.getInstance();

        manager = CallbackManager.Factory.create();
        binding.facebookSignIn.setReadPermissions("email", "public_profile");
        binding.facebookSignIn.registerCallback(manager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("UserDetails", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("UserDetails", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("UserDetails", "facebook:onError", error);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this, gso);
        binding.googleSignIn.setOnClickListener(v -> startActivityForResult(client.getSignInIntent(), RC_SIGN_IN));

        binding.emailSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(SignupOptionsActivity.this, EmailSignUpActivity.class);
            intent.putExtra("type", getIntent().getStringExtra("type"));
            startActivity(intent);
            finish();
        });

        binding.signInText.setOnClickListener(v -> {
            Intent intent = new Intent(SignupOptionsActivity.this, EmailSignInActivity.class);
            intent.putExtra("type", getIntent().getStringExtra("type"));
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("UserDetails", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("UserDetails", "Google sign in failed", e);
            }
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("UserDetails", "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("UserDetails", "signInWithCredential:success");
                        FirebaseUser user = auth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Log.w("UserDetails", "signInWithCredential:failure", task.getException());
                        Toast.makeText(SignupOptionsActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("UserDetails", "signInWithCredential:success");
                        FirebaseUser user = auth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Log.w("UserDetails", "signInWithCredential:failure", task.getException());
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        boolean isNewUser = Math.abs(
                user.getMetadata().getLastSignInTimestamp() - user.getMetadata().getCreationTimestamp()
        ) < 10000;
        if (isNewUser) {
            Intent intent = new Intent(SignupOptionsActivity.this, BabySitterAddActivity.class);
            intent.putExtra("type", type);
            startActivity(intent);
            finish();
        } else {
            FirebaseFirestore
                    .getInstance()
                    .collection(type + "s")
                    .document(user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        DocumentSnapshot document = task.getResult();
                        Intent intent;
                        if (document.exists()) {
                            intent = new Intent(SignupOptionsActivity.this, MainActivity.class);
                            intent.putExtra("type", type);
                            if (type.equalsIgnoreCase("parent")) {
                                intent.putExtra("parent", document.toObject(Parent.class));
                            } else {
                                intent.putExtra("sitter", document.toObject(BabySitter.class));
                            }
                        } else {
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(SignupOptionsActivity.this,
                                    "You can only be babysitter or a parent not both",
                                    Toast.LENGTH_SHORT).show();
                            intent = new Intent(SignupOptionsActivity.this, UserChoiceActivity.class);
                        }
                        startActivity(intent);
                        finish();
                    });
        }
    }

}
