package com.example.babysitter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.babysitter.databinding.ItemBabySitterBinding;
import com.example.babysitter.placeholder.PlaceholderContent.PlaceholderItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BabySitterRecyclerViewAdapter extends RecyclerView.Adapter<BabySitterRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;
    private FragmentManager manager;

    public BabySitterRecyclerViewAdapter(List<PlaceholderItem> items, FragmentManager manager) {
        mValues = items;
        this.manager = manager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(ItemBabySitterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.findViewById(R.id.viewProfileButton).setOnClickListener(v -> {
                manager.beginTransaction()
                        .replace(R.id.fragment_container_view, BabySitterDetailsFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null) // name can be null
                        .commit();
            });
        }
    }
}