package com.example.babysitter.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

import com.example.babysitter.fragment.AddReviewFragment;
import com.example.babysitter.fragment.AvailabilityFragment;
import com.example.babysitter.fragment.BabySitterDetailsFragment;
import com.example.babysitter.fragment.CheckoutFragment;
import com.example.babysitter.fragment.ReviewsFragment;
import com.example.babysitter.model.BabySitter;
import com.example.babysitter.model.Booking;
import com.example.babysitter.model.Parent;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentFactory extends FragmentFactory {
    private BabySitter sitter;
    private Booking booking;
    private Parent parent;
    private long startDate, endDate;

    public MyFragmentFactory() {
        super();
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public void setSitter(BabySitter sitter) {
        this.sitter = sitter;
    }

    public void setParent(Parent parent) { this.parent = parent; }

    public void setBooking(Booking booking) { this.booking = booking; }

    @NonNull
    @Override
    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
        Class<? extends Fragment> fragmentClass = loadFragmentClass(classLoader, className);
        if (fragmentClass == BabySitterDetailsFragment.class) {
            return BabySitterDetailsFragment.newInstance(sitter);
        } else if (fragmentClass == ReviewsFragment.class) {
            return ReviewsFragment.newInstance(sitter);
        } else if (fragmentClass == AvailabilityFragment.class) {
            return AvailabilityFragment.newInstance(sitter);
        } else if (fragmentClass == AddReviewFragment.class) {
            return AddReviewFragment.newInstance(booking, parent);
        } else if (fragmentClass == CheckoutFragment.class) {
            return CheckoutFragment.newInstance(sitter, startDate, endDate);
        } else {
            return super.instantiate(classLoader, className);
        }
    }
}
