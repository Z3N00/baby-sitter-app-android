package com.example.babysitter.adapter;

import static com.example.babysitter.Utils.generateImageUrl;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babysitter.databinding.ItemBabySitterBinding;
import com.example.babysitter.fragment.BabySitterDetailsFragment;
import com.example.babysitter.activity.MyFragmentFactory;
import com.example.babysitter.R;
import com.example.babysitter.model.BabySitter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BabySitterRecyclerViewAdapter extends RecyclerView.Adapter<BabySitterRecyclerViewAdapter.ViewHolder> {

    private final List<BabySitter> mValues;
    private final FragmentManager manager;

    public BabySitterRecyclerViewAdapter(List<BabySitter> items, FragmentManager manager) {
        mValues = items;
        this.manager = manager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ItemBabySitterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        BabySitter sitter = mValues.get(position);
        holder.name.setText(sitter.name);
        if (sitter.ratingCount == 0) {
            holder.rating.setText("No rating yet");
        } else {
            holder.rating.setText(String.format("%.2f", sitter.rating));
        }
        Picasso.get().setIndicatorsEnabled(true);
        Picasso.get().load(generateImageUrl(sitter.profile)).into(holder.profile);
        holder.itemView.findViewById(R.id.viewProfileButton).setOnClickListener(v -> {
            MyFragmentFactory factory = (MyFragmentFactory) manager.getFragmentFactory();
            factory.setSitter(sitter);
            manager
                    .beginTransaction()
                    .replace(R.id.fragment_container_view, BabySitterDetailsFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null) // name can be null
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, rating;
        ImageView profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.babysitterName);
            rating = itemView.findViewById(R.id.babysitterRating);
            profile = itemView.findViewById(R.id.babysitterImage);
        }
    }
}