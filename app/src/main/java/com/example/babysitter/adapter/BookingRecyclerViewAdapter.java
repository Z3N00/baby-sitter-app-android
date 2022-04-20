package com.example.babysitter.adapter;

import static com.example.babysitter.Utils.generateImageUrl;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babysitter.R;
import com.example.babysitter.activity.MyFragmentFactory;
import com.example.babysitter.databinding.ItemBookingBinding;
import com.example.babysitter.fragment.AddReviewFragment;
import com.example.babysitter.model.Booking;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingRecyclerViewAdapter extends RecyclerView.Adapter<BookingRecyclerViewAdapter.ViewHolder> {

    private final List<Booking> mValues;
    private final String type;
    private final FragmentManager manager;

    public BookingRecyclerViewAdapter(List<Booking> items, String type, FragmentManager manager) {
        mValues = items;
        this.type = type;
        this.manager = manager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ItemBookingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Booking booking = mValues.get(position);
        holder.binding.description.setText(
                Instant.ofEpochMilli(booking.startDate).atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
                        + " - "
                        + Instant.ofEpochMilli(booking.endDate).atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
                        + "\r\n"
                        + "Charges: "
                        + String.format("$ %04.2f", booking.total)
        );
        holder.binding.services.removeAllViews();
        for (String service : booking.services) {
            Chip chip = new Chip(holder.binding.services.getContext());
            chip.setText(service);
            holder.binding.services.addView(chip);
        }

        holder.binding.babysitterName.setText(booking.name);
        Picasso.get().load(generateImageUrl(booking.profile)).into(holder.binding.babysitterImage);

        if (type.equalsIgnoreCase("parent")) {
            forParent(holder, booking);
        } else {
            forBabysitter(holder, booking, position);
        }
    }

    public void forBabysitter(ViewHolder holder, Booking booking, int position) {
        holder.binding.addReview.setVisibility(View.GONE);
        if (booking.accepted == null) {
            holder.binding.acceptButton.setEnabled(true);
            holder.binding.rejectButton.setEnabled(true);
            holder.binding.acceptButton.setVisibility(View.VISIBLE);
            holder.binding.rejectButton.setVisibility(View.VISIBLE);
            holder.binding.acceptButton.setOnClickListener(v -> updateBookingStatus(position, booking, true));
            holder.binding.rejectButton.setOnClickListener(v -> updateBookingStatus(position, booking, false));
        } else if (booking.accepted) {
            holder.binding.acceptButton.setEnabled(false);
            holder.binding.rejectButton.setVisibility(View.GONE);
            holder.binding.acceptButton.setOnClickListener(null);
            holder.binding.rejectButton.setOnClickListener(null);
        } else {
            holder.binding.rejectButton.setEnabled(false);
            holder.binding.acceptButton.setVisibility(View.GONE);
            holder.binding.rejectButton.setOnClickListener(null);
            holder.binding.acceptButton.setOnClickListener(null);
        }
    }

    public void updateBookingStatus(int position, Booking booking, boolean action) {
        Map<String, Object> map = new HashMap<>();
        map.put("accepted", action);
        FirebaseFirestore
                .getInstance()
                .collection("bookings")
                .document(booking.ref)
                .update(map);
        booking.accepted = action;
        notifyItemChanged(position);
    }

    public void forParent(ViewHolder holder, Booking booking) {
        if (booking.accepted == null) {
            holder.binding.acceptButton.setVisibility(View.GONE);
            holder.binding.rejectButton.setVisibility(View.GONE);
        } else if (booking.accepted) {
            holder.binding.rejectButton.setVisibility(View.GONE);
            holder.binding.acceptButton.setEnabled(false);
        } else {
            holder.binding.acceptButton.setVisibility(View.GONE);
            holder.binding.rejectButton.setEnabled(false);
        }

        long currentTimestamp = Instant.now().toEpochMilli();
        if (currentTimestamp > booking.endDate) {
            if (booking.review) {
                holder.binding.addReview.setVisibility(View.GONE);
            } else {
                holder.binding.addReview.setVisibility(View.VISIBLE);
                holder.binding.addReview.setOnClickListener(v -> {
                            MyFragmentFactory factory = (MyFragmentFactory) manager.getFragmentFactory();
                            factory.setBooking(booking);
                            manager.beginTransaction()
                                    .replace(R.id.fragment_container_view2, AddReviewFragment.class, null)
                                    .setReorderingAllowed(true)
                                    .addToBackStack(null) // name can be null
                                    .commit();
                        }
                );
            }
        } else {
            holder.binding.addReview.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemBookingBinding binding;

        public ViewHolder(ItemBookingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
