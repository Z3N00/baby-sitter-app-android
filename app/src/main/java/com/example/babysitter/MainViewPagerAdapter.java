package com.example.babysitter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainViewPagerAdapter extends FragmentStateAdapter {

    public MainViewPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1: return new BookingFragment();
            case 2: return new ChatMessageFragment();
            case 3: return new ProfileFragment();
            default: return new BabySitterListFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public String getText(int position) {
        switch (position) {
            case 1: return "Booking";
            case 2: return "Chat";
            case 3: return "Profile";
            default: return "Home";
        }
    }

    public int getDrawable(int position) {
        switch (position) {
            case 1: return R.drawable.ic_booking;
            case 2: return R.drawable.ic_chat;
            case 3: return R.drawable.ic_profile;
            default: return R.drawable.ic_home;
        }
    }

}