package com.example.babysitter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babysitter.R;
import com.example.babysitter.activity.MainActivity;
import com.example.babysitter.adapter.ReviewsRecyclerViewAdapter;
import com.example.babysitter.databinding.FragmentReviewsListBinding;
import com.example.babysitter.model.BabySitter;
import com.example.babysitter.model.Review;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ReviewsFragment extends Fragment {
    private BabySitter sitter;
    private final List<Review> reviews = new ArrayList<>();

    public ReviewsFragment() {
    }

    public static ReviewsFragment newInstance(BabySitter sitter) {
        ReviewsFragment fragment = new ReviewsFragment();
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
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentReviewsListBinding binding = FragmentReviewsListBinding.inflate(inflater, container, false);
        binding.list.setLayoutManager(new LinearLayoutManager(binding.list.getContext()));
        ReviewsRecyclerViewAdapter adapter = new ReviewsRecyclerViewAdapter(reviews);
        binding.list.setAdapter(adapter);
        FirebaseFirestore
                .getInstance()
                .collection("reviews")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reviews.clear();
                        QuerySnapshot snap = task.getResult();
                        for (Review review : snap.toObjects(Review.class)) {
                            if (review.babysitter.equalsIgnoreCase(sitter.ref)) {
                                reviews.add(review);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
        return binding.getRoot();
    }
}