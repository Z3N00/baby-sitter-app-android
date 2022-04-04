package com.example.babysitter.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babysitter.databinding.ActivityMainParentsBinding;
import com.example.babysitter.model.BabySitter;
import com.example.babysitter.model.Parent;
import com.example.babysitter.viewpager.BabySitterPagerAdapter;
import com.example.babysitter.viewpager.CustomFragmentStateAdapter;
import com.example.babysitter.viewpager.ParentPagerAdapter;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private ActivityMainParentsBinding binding;
    private MyFragmentFactory factory = new MyFragmentFactory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportFragmentManager().setFragmentFactory(factory);
        super.onCreate(savedInstanceState);
        binding = ActivityMainParentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.title.setNavigationOnClickListener(v -> this.onBackPressed());

        String type = getIntent().getStringExtra("type");
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore
                .getInstance()
                .collection(type + "s")
                .document(uid)
                .get()
                .addOnCompleteListener(task -> setupPager(type, task.getResult()));
    }

    public void setupPager(String type, DocumentSnapshot result) {
        CustomFragmentStateAdapter adapter;
        if (type.equalsIgnoreCase("parent")) {
            Parent parent = result.toObject(Parent.class);
            factory.setParent(parent);
            adapter = new ParentPagerAdapter(this, parent);
        } else {
            BabySitter sitter = result.toObject(BabySitter.class);
            adapter = new BabySitterPagerAdapter(this, sitter);
        }
        binding.viewPager.setAdapter(adapter);
        new TabLayoutMediator(binding.tabs, binding.viewPager, (tab, position) -> {
            tab.setText(adapter.getText(position));
            tab.setIcon(adapter.getDrawable(position));
        }).attach();
    }

}
