package com.example.babysitter;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.babysitter.databinding.ActivityMainParentsBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainParentsActivity extends AppCompatActivity {

    private ActivityMainParentsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainParentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewPager2 viewPager = binding.viewPager;
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        TabLayout tabs = binding.tabs;
        new TabLayoutMediator(tabs, viewPager, (tab, position) -> {
            tab.setText(adapter.getText(position));
            tab.setIcon(adapter.getDrawable(position));
        }).attach();
    }
}