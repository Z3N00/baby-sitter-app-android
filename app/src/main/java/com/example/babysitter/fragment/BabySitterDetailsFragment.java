package com.example.babysitter.fragment;

import static com.example.babysitter.Utils.generateImageUrl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.babysitter.R;
import com.example.babysitter.activity.MainActivity;
import com.example.babysitter.adapter.ReviewsRecyclerViewAdapter;
import com.example.babysitter.databinding.FragmentBabySitterDetailsBinding;
import com.example.babysitter.model.BabySitter;
import com.example.babysitter.model.Review;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BabySitterDetailsFragment extends Fragment {
    private BabySitter sitter;
    private FragmentBabySitterDetailsBinding binding;
    private ReviewsRecyclerViewAdapter adapter;
    List<Review> reviews = new ArrayList<>();

    public BabySitterDetailsFragment() { }

    public static BabySitterDetailsFragment newInstance(BabySitter sitter) {
        BabySitterDetailsFragment fragment = new BabySitterDetailsFragment();
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
        binding = FragmentBabySitterDetailsBinding.inflate(inflater, container, false);
        binding.babysitterAddress.setText(sitter.address);
        binding.babysitterName.setText(sitter.name);
        binding.babysitterDescription.setText(sitter.description);

        Picasso.get().load(generateImageUrl(sitter.profile)).into(binding.babysitterImage);

        binding.list.setLayoutManager(new LinearLayoutManager(binding.list.getContext()));
        adapter = new ReviewsRecyclerViewAdapter(reviews);
        binding.list.setAdapter(adapter);
        refreshUI();

        binding.checkAvailiability.setOnClickListener(v ->
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view, AvailabilityFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null) // name can be null
                        .commit()
        );
        binding.viewReviews.setOnClickListener(v ->
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view, ReviewsFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null) // name can be null
                        .commit()
        );
        return binding.getRoot();
    }

    private void refreshUI() {
        if (sitter.ratingCount == 0) {
            binding.babysitterRating.setText("No rating yet");
        } else {
            binding.babysitterRating.setText(String.format("%.2f", sitter.rating));
        }
        FirebaseFirestore
                .getInstance()
                .collection("reviews")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reviews.clear();
                        QuerySnapshot snap = task.getResult();
                        for (Review review : snap.toObjects(Review.class)) {
                            if (review.babysitter.equalsIgnoreCase(sitter.ref) && reviews.size() <= 2) {
                                reviews.add(review);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity activity = (MainActivity) getActivity();
        activity.showBackButton(true);
        activity.updateBackButton();
        FirebaseFirestore
                .getInstance()
                .collection("babysitters")
                .document(sitter.ref)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sitter = task.getResult().toObject(BabySitter.class);
                        refreshUI();
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity activity = (MainActivity) getActivity();
        activity.showBackButton(false);
    }

}