package com.example.babysitter.adapter;

import static com.example.babysitter.Utils.generateImageUrl;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babysitter.databinding.ItemReviewBinding;
import com.example.babysitter.model.Review;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ViewHolder> {

    private final List<Review> mValues;

    public ReviewsRecyclerViewAdapter(List<Review> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemReviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Review review = mValues.get(position);
        holder.binding.content.setText(review.description);
        holder.binding.reviewerName.setText(review.name);
        Picasso.get().setLoggingEnabled(true);
        Picasso
                .get()
                .load(generateImageUrl(review.profile))
                .into(holder.binding.shapeableImageView);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemReviewBinding binding;

        public ViewHolder(ItemReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}