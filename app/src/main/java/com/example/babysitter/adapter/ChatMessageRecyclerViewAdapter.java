package com.example.babysitter.adapter;

import static com.example.babysitter.Utils.generateImageUrl;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babysitter.databinding.ItemMessageBinding;
import com.example.babysitter.model.ChatMessage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatMessageRecyclerViewAdapter extends RecyclerView.Adapter<ChatMessageRecyclerViewAdapter.ViewHolder> {

    private final List<ChatMessage> mValues;

    public ChatMessageRecyclerViewAdapter(List<ChatMessage> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ChatMessage message = mValues.get(position);
        holder.binding.content.setText(message.message);
        holder.binding.reviewerName.setText(message.sender);
        Picasso.get().load(generateImageUrl(message.profile)).into(holder.binding.shapeableImageView);
        holder.binding.delete.setOnClickListener(view -> {
            mValues.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemMessageBinding binding;

        public ViewHolder(ItemMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}