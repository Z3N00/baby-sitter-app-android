package com.example.babysitter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.babysitter.R;
import com.example.babysitter.activity.MainActivity;
import com.example.babysitter.adapter.BookingRecyclerViewAdapter;
import com.example.babysitter.databinding.FragmentBookingListBinding;
import com.example.babysitter.model.Booking;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment {
    private final List<Booking> bookings = new ArrayList<>();
    private String type;
    private String ref;
    private BookingRecyclerViewAdapter adapter;

    public BookingFragment() {
    }

    public static BookingFragment newInstance(String type, String ref) {
        BookingFragment fragment = new BookingFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putSerializable("ref", ref);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString("type");
            ref = getArguments().getString("ref");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentBookingListBinding binding = FragmentBookingListBinding.inflate(inflater, container, false);
        Context context = binding.getRoot().getContext();
        binding.list.setLayoutManager(new LinearLayoutManager(context));
        adapter = new BookingRecyclerViewAdapter(bookings, type, getParentFragmentManager());
        binding.list.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity activity = (MainActivity) getActivity();
        activity.binding.title.setNavigationIcon(null);

        FirebaseFirestore
                .getInstance()
                .collection("bookings")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        refreshUI(task.getResult().toObjects(Booking.class));
                    }
                });
    }

    public void refreshUI(List<Booking> allBookings) {
        bookings.clear();
        for (Booking booking : allBookings) {
            if ((type.equalsIgnoreCase("babysitter") && booking.babysitter.equalsIgnoreCase(ref))
                    || (type.equalsIgnoreCase("parent") && booking.parent.equalsIgnoreCase(ref))) {
                bookings.add(booking);
            }
        }
        adapter.notifyDataSetChanged();
    }

}
