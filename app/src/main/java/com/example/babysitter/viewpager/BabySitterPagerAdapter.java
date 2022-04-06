package com.example.babysitter.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.babysitter.fragment.BookingFragment;
import com.example.babysitter.fragment.ProfileBabysitterFragment;
import com.example.babysitter.R;
import com.example.babysitter.model.BabySitter;

public class BabySitterPagerAdapter extends CustomFragmentStateAdapter {
    private final BabySitter sitter;

    public BabySitterPagerAdapter(FragmentActivity fa, BabySitter sitter) {
        super(fa);
        this.sitter = sitter;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1: return ProfileBabysitterFragment.newInstance(sitter);
            default: return BookingFragment.newInstance("babysitter", sitter.ref);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public String getText(int position) {
        switch (position) {
            case 1: return "Profile";
            default: return "Booking";
        }
    }

    public int getDrawable(int position) {
        switch (position) {
            case 1: return R.drawable.ic_profile;
            default: return R.drawable.ic_booking;
        }
    }

}