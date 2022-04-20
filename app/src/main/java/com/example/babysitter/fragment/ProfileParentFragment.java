package com.example.babysitter.fragment;

import static com.example.babysitter.Utils.generateImageUrl;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.babysitter.R;
import com.example.babysitter.activity.MainActivity;
import com.example.babysitter.activity.UserChoiceActivity;
import com.example.babysitter.databinding.FragmentProfileBabysitterBinding;
import com.example.babysitter.databinding.FragmentProfileBinding;
import com.example.babysitter.model.Parent;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileParentFragment extends Fragment {
    private Parent parent;
    private FragmentProfileBinding binding;

    public ProfileParentFragment() {
    }

    public static ProfileParentFragment newInstance(Parent parent) {
        ProfileParentFragment fragment = new ProfileParentFragment();
        Bundle args = new Bundle();
        args.putSerializable("parent", parent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parent = (Parent) getArguments().getSerializable("parent");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        binding.nameTextView.setText(parent.name);
        binding.emailTextView.setText(parent.email);
        binding.phoneTextView.setText(parent.phone);
        binding.addressTextView.setText(parent.address);
        if (parent.profile != null) {
            Picasso.get().load(generateImageUrl(parent.profile)).into(binding.imageView);
        } else if (parent.profileUrl != null) {
            Picasso.get().load(parent.profileUrl).into(binding.imageView);
        }
        binding.logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.client_id))
                    .requestEmail()
                    .build();
            GoogleSignIn.getClient(getActivity(), gso).signOut();
            startActivity(new Intent(getActivity(), UserChoiceActivity.class));
            getActivity().finish();
        });
        binding.editButton.setOnClickListener(v -> updateProfile());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity activity = (MainActivity) getActivity();
        activity.binding.title.setNavigationIcon(null);
    }

    public void updateProfile() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", binding.nameTextView.getText().toString());
        map.put("address", binding.addressTextView.getText().toString());
        map.put("phone", binding.phoneTextView.getText().toString());
        FirebaseFirestore
                .getInstance()
                .collection("parents")
                .document(parent.ref)
                .update(map)
                .addOnCompleteListener(task -> {
                    Toast.makeText(getContext(), "Profile updated.", Toast.LENGTH_SHORT).show();
        });
        binding.nameTextView.setEnabled(false);
        binding.addressTextView.setEnabled(false);
        binding.emailTextView.setEnabled(false);
        binding.editButton.setText("Edit");
        binding.editButton.setOnClickListener(v -> enableProfile());
    }

    public void enableProfile() {
        binding.nameTextView.setEnabled(true);
        binding.addressTextView.setEnabled(true);
        binding.phoneTextView.setEnabled(true);
        binding.nameTextView.requestFocus();
        binding.editButton.setOnClickListener(v -> updateProfile());
        binding.editButton.setText("Update");
    }
}
