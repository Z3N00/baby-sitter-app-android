package com.example.babysitter.fragment;

import static com.example.babysitter.Utils.generateImageUrl;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.babysitter.R;
import com.example.babysitter.activity.MainActivity;
import com.example.babysitter.activity.UserChoiceActivity;
import com.example.babysitter.databinding.FragmentProfileBabysitterBinding;
import com.example.babysitter.model.BabySitter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileBabysitterFragment extends Fragment {
    private FragmentProfileBabysitterBinding binding;
    private BabySitter sitter;

    public ProfileBabysitterFragment() {
    }

    public static ProfileBabysitterFragment newInstance(BabySitter sitter) {
        ProfileBabysitterFragment fragment = new ProfileBabysitterFragment();
        Bundle args = new Bundle();
        args.putSerializable("sitter", sitter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sitter = (BabySitter) getArguments().getSerializable("sitter");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBabysitterBinding.inflate(inflater, container, false);
        updateUI();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity activity = (MainActivity) getActivity();
        activity.binding.title.setNavigationIcon(null);
    }

    public void updateUI() {
        binding.babysitterName.setText(sitter.name);
        binding.babysitterDescription.setText(sitter.description);
        binding.babysitterDescription.setEnabled(false);
        if (sitter.ratingCount == 0) {
            binding.babysitterRating.setText("No rating yet");
        } else {
            binding.babysitterRating.setText(String.format("%.2f", sitter.rating));
        }
        binding.priceUpdateTextField.getEditText().setText(String.valueOf(sitter.price));
        binding.priceUpdateTextField.getEditText().setEnabled(false);

        binding.updatePrice.setOnClickListener(v -> enablePriceEdit());
        binding.updateDescription.setOnClickListener(v -> enableDescriptionEdit());
        binding.logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), UserChoiceActivity.class));
            getActivity().finish();
        });

        Picasso.get().load(generateImageUrl(sitter.profile)).into(binding.babysitterImage);
    }

    public void enableDescriptionEdit() {
        binding.babysitterDescription.setEnabled(true);
        binding.babysitterDescription.requestFocus();
        binding.updateDescription.setOnClickListener(v -> updateDescription());
        binding.updateDescription.setText("Update");
    }

    public void enablePriceEdit() {
        binding.priceUpdateTextField.setEnabled(true);
        binding.priceUpdateTextField.getEditText().requestFocus();
        binding.updatePrice.setOnClickListener(v -> updatePrice());
        binding.updatePrice.setText("Update");
    }

    public void updateDescription() {
        Map<String, Object> data = new HashMap<>();
        data.put("description", binding.babysitterDescription.getText().toString());
        FirebaseFirestore
                .getInstance()
                .collection("babysitters")
                .document(sitter.ref)
                .update(data);
        binding.babysitterDescription.setEnabled(false);
        binding.updateDescription.setOnClickListener(v -> enableDescriptionEdit());
        binding.updateDescription.setText("Edit");
    }

    public void updatePrice() {
        Map<String, Object> data = new HashMap<>();
        double price = Double.parseDouble(binding.priceUpdateTextField.getEditText().getText().toString());
        data.put("price", price);
        FirebaseFirestore
                .getInstance()
                .collection("babysitters")
                .document(sitter.ref)
                .update(data);
        binding.priceUpdateTextField.setEnabled(false);
        binding.updatePrice.setOnClickListener(v -> enablePriceEdit());
        binding.updatePrice.setText("Edit");
    }
}