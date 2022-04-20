package com.example.babysitter.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.utils.DateUtils;
import com.example.babysitter.activity.MainActivity;
import com.example.babysitter.activity.MyFragmentFactory;
import com.example.babysitter.R;
import com.example.babysitter.databinding.FragmentAvailabilityBinding;
import com.example.babysitter.model.BabySitter;
import com.example.babysitter.model.Booking;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class AvailabilityFragment extends Fragment {

    private BabySitter sitter;

    public AvailabilityFragment() {
        // Required empty public constructor
    }

    public static AvailabilityFragment newInstance(BabySitter sitter) {
        AvailabilityFragment fragment = new AvailabilityFragment();
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
        FragmentAvailabilityBinding binding = FragmentAvailabilityBinding.inflate(inflater, container, false);
        Calendar calendar = Calendar.getInstance();
        LocalDate today = LocalDate.now();
        calendar.set(today.getYear(), today.getMonthValue() - 1, today.getDayOfMonth());
        binding.calendarView.setMinimumDate(calendar);
        binding.calendarView2.setMinimumDate(calendar);
        FirebaseFirestore
                .getInstance()
                .collection("bookings")
                .get()
                .addOnCompleteListener(task -> {
                    List<Booking> allBookings = task.getResult().toObjects(Booking.class);
                    List<Calendar> calendars = new ArrayList<>();
                    LocalDate selectedDate = today;
                    for (Booking booking : allBookings) {
                        if (booking.babysitter.equalsIgnoreCase(sitter.ref)) {
                            LocalDate startDate = Instant.ofEpochMilli(booking.startDate).atZone(ZoneOffset.UTC).toLocalDate();
                            LocalDate endDate = Instant.ofEpochMilli(booking.endDate).atZone(ZoneOffset.UTC).toLocalDate();

                            if ((selectedDate.isAfter(startDate) && selectedDate.isBefore(endDate))
                                    || selectedDate.isEqual(startDate)
                                    || selectedDate.isEqual(endDate)) {
                                if (endDate.plusDays(1).isAfter(selectedDate)) {
                                    selectedDate = endDate.plusDays(1);
                                }
                            }
                            for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
                                calendars.add(
                                        new GregorianCalendar.Builder()
                                                .setDate(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth())
                                                .build()
                                );
                            }
                            calendars.add(
                                    new GregorianCalendar.Builder()
                                            .setDate(endDate.getYear(), endDate.getMonthValue() - 1, endDate.getDayOfMonth())
                                            .build()
                            );
                        }
                    }
                    Calendar selected = Calendar.getInstance();
                    selected.set(selectedDate.getYear(), selectedDate.getMonthValue() - 1, selectedDate.getDayOfMonth());
                    try {
                        binding.calendarView.setDate(selected);
                        binding.calendarView2.setDate(selected);
                    } catch (OutOfDateRangeException e) {
                        e.printStackTrace();
                    }
                    binding.calendarView.setDisabledDays(calendars);
                    binding.calendarView2.setDisabledDays(calendars);
                });

        binding.proceedButton.findViewById(R.id.proceedButton).setOnClickListener(v -> {
            FragmentManager manager = getParentFragmentManager();
            MyFragmentFactory factory = (MyFragmentFactory) manager.getFragmentFactory();
            Calendar startDate = binding.calendarView.getFirstSelectedDate();
            Calendar endDate = binding.calendarView2.getFirstSelectedDate();
            factory.setStartDate(startDate.toInstant().toEpochMilli());
            factory.setEndDate(endDate.toInstant().toEpochMilli());
            manager.beginTransaction()
                    .replace(R.id.fragment_container_view, CheckoutFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null) // name can be null
                    .commit();
        });
        return binding.getRoot();
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