package com.example.babysitter.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.babysitter.R;
import com.example.babysitter.activity.MainActivity;
import com.example.babysitter.databinding.FragmentAddReviewBinding;
import com.example.babysitter.model.BabySitter;
import com.example.babysitter.model.Booking;
import com.example.babysitter.model.Parent;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddReviewFragment extends Fragment {
    private Booking booking;
    private Parent parent;

    private FragmentAddReviewBinding binding;

    public AddReviewFragment() { }

    public static AddReviewFragment newInstance(Booking booking, Parent parent) {
        AddReviewFragment fragment = new AddReviewFragment();
        Bundle args = new Bundle();
        args.putSerializable("booking", booking);
        args.putSerializable("parent", parent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            booking = (Booking) getArguments().getSerializable("booking");
            parent = (Parent) getArguments().getSerializable("parent");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddReviewBinding.inflate(inflater, container, false);
        binding.textView3.setText("Write a review " + booking.name);
        binding.addReviewButton.setOnClickListener(v -> addReview());
        return binding.getRoot();
    }

    private void addReview() {
        String text = binding.descriptionTextField.getEditText().getText().toString();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.descriptionTextField.getWindowToken(), 0);
        Map<String, Object> map = new HashMap<>();
        map.put("babysitter", booking.babysitter);
        map.put("description", text);
        map.put("name", parent.name);
        map.put("profile", parent.profile);
        FirebaseFirestore
                .getInstance()
                .collection("reviews")
                .add(map)
                .addOnCompleteListener(task -> addRating());
    }

    private void addRating() {
        FirebaseFirestore
                .getInstance()
                .collection("babysitters")
                .document(booking.babysitter)
                .get()
                .addOnCompleteListener(task -> {
                    BabySitter sitter = (BabySitter) task.getResult().toObject(BabySitter.class);
                    float rating = binding.ratingBar.getRating();
                    float newRating = (rating + sitter.rating * sitter.ratingCount) / (sitter.ratingCount + 1);
                    Map<String, Object> map = new HashMap<>();
                    map.put("rating", newRating);
                    map.put("ratingCount", sitter.ratingCount + 1);
                    FirebaseFirestore
                            .getInstance()
                            .collection("babysitters")
                            .document(booking.babysitter)
                            .update(map)
                            .addOnCompleteListener(task2 -> updateBooking());
                });
    }

    private void updateBooking() {
        Map<String, Object> map = new HashMap<>();
        map.put("review", true);
        FirebaseFirestore
                .getInstance()
                .collection("bookings")
                .document(booking.ref)
                .update(map)
                .addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        Toast.makeText(getContext(), "Review published.", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    } else {
                        Toast.makeText(getContext(), "Unable to publish review. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity activity = (MainActivity) getActivity();
        activity.showBackButton(true);
        activity.updateBackButton();
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity activity = (MainActivity) getActivity();
        activity.showBackButton(false);
    }
}