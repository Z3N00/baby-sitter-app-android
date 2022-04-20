package com.example.babysitter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.babysitter.R;
import com.example.babysitter.activity.MainActivity;
import com.example.babysitter.adapter.CheckoutRecyclerViewAdapter;
import com.example.babysitter.databinding.FragmentCheckoutBinding;
import com.example.babysitter.model.BabySitter;
import com.example.babysitter.model.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutFragment extends Fragment {

    private BabySitter sitter;
    private long startDate, endDate, days;
    private final List<Service> allServices = new ArrayList<>();
    private final List<Service> selectedServices = new ArrayList<>();
    private double subtotal, total, tax, basePrice;
    private FragmentCheckoutBinding binding;

    public CheckoutFragment() {
        // Required empty public constructor
    }

    public static CheckoutFragment newInstance(BabySitter sitter, long startDate, long endDate) {
        CheckoutFragment fragment = new CheckoutFragment();
        Bundle args = new Bundle();
        args.putSerializable("sitter", sitter);
        args.putLong("startDate", startDate);
        args.putLong("endDate", endDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sitter = (BabySitter) getArguments().getSerializable("sitter");
            startDate = getArguments().getLong("startDate");
            endDate = getArguments().getLong("endDate");
            days = Period.between(
                    Instant.ofEpochMilli(startDate).atZone(ZoneOffset.UTC).toLocalDate(),
                    Instant.ofEpochMilli(endDate).atZone(ZoneOffset.UTC).toLocalDate()
            ).getDays();
            basePrice = sitter.price * days;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.binding = FragmentCheckoutBinding.inflate(inflater, container, false);

        Context context = binding.getRoot().getContext();
        binding.list.setLayoutManager(new LinearLayoutManager(context));
        CheckoutRecyclerViewAdapter.UpdateTotal updateTotal = service -> {
            if (service.selected) {
                selectedServices.add(service);
            } else {
                selectedServices.remove(service);
            }
            calculateTotal();
        };
        CheckoutRecyclerViewAdapter adapter = new CheckoutRecyclerViewAdapter(allServices, updateTotal);
        binding.list.setAdapter(adapter);
        FirebaseFirestore
                .getInstance()
                .collection("services")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        allServices.clear();
                        selectedServices.clear();
                        allServices.addAll(task.getResult().toObjects(Service.class));
                        adapter.notifyDataSetChanged();
                    }
                });

        binding.checkoutButton.findViewById(R.id.checkoutButton).setOnClickListener(v -> {
            Map<String, Object> object = new HashMap<>();
            object.put("total", total);
            object.put("name", sitter.name);
            object.put("profile", sitter.profile);
            object.put("babysitter", sitter.ref);
            object.put("parent", FirebaseAuth.getInstance().getCurrentUser().getUid());
            object.put("endDate", endDate);
            object.put("startDate", startDate);

            List<String> items = new ArrayList<>();
            for (Service service : selectedServices) {
                items.add(service.name);
            }
            object.put("services", items);

            FirebaseFirestore
                    .getInstance()
                    .collection("bookings")
                    .add(object)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            getParentFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container_view, BookingCompletedFragment.class, null)
                                    .setReorderingAllowed(true)
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            Toast.makeText(context, "Unable to complete booking currently. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        calculateTotal();
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

    private void calculateTotal() {
        total = 0.0;
        subtotal = basePrice;
        tax = 0.0;
        for (Service service : selectedServices) {
            subtotal += service.price;
        }
        tax = subtotal * 18.0 / 100.0;
        total = subtotal + tax;
        binding.subtotal.setText(String.format("$ %04.2f", subtotal));
        binding.tax.setText(String.format("$ %04.2f", tax));
        binding.total.setText(String.format("$ %04.2f", total));
    }
}