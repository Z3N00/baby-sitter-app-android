package com.example.babysitter.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.babysitter.fragment.BabySitterListFragment;
import com.example.babysitter.fragment.BookingFragment;
import com.example.babysitter.fragment.ProfileParentFragment;
import com.example.babysitter.R;
import com.example.babysitter.model.Parent;

public class ParentPagerAdapter extends CustomFragmentStateAdapter {
    private final Parent parent;

    public ParentPagerAdapter(FragmentActivity fa, Parent parent) {
        super(fa);
        this.parent = parent;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1: return BookingFragment.newInstance("parent", parent.ref);
            case 2: return ProfileParentFragment.newInstance(parent);
            default: return new BabySitterListFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public String getText(int position) {
        switch (position) {
            case 1: return "Booking";
            case 2: return "Profile";
            default: return "Home";
        }
    }

    public int getDrawable(int position) {
        switch (position) {
            case 1: return R.drawable.ic_booking;
            case 2: return R.drawable.ic_profile;
            default: return R.drawable.ic_home;
        }
    }
}
