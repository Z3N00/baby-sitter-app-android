package com.example.babysitter.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public abstract class CustomFragmentStateAdapter extends FragmentStateAdapter {

    public CustomFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public abstract String getText(int position);
    public abstract int getDrawable(int position);
}
