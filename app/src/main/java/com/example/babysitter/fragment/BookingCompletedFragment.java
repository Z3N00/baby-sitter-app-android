package com.example.babysitter.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.babysitter.R;
import com.example.babysitter.activity.MainActivity;

public class BookingCompletedFragment extends Fragment {

    public BookingCompletedFragment() {
    }

    public static BookingCompletedFragment newInstance() {
        return new BookingCompletedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_completed, container, false);
        view.setOnClickListener(v -> getParentFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE));
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getParentFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return view;
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
