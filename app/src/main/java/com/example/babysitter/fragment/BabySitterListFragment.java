package com.example.babysitter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babysitter.R;
import com.example.babysitter.activity.MainActivity;
import com.example.babysitter.adapter.BabySitterRecyclerViewAdapter;
import com.example.babysitter.model.BabySitter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BabySitterListFragment extends Fragment {

    private final List<BabySitter> babySitterList = new ArrayList<>();
    private BabySitterRecyclerViewAdapter adapter;

    public BabySitterListFragment() {
    }

    public static BabySitterListFragment newInstance() {
        return new BabySitterListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baby_sitter_list, container, false);
        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.list_baby_sitter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new BabySitterRecyclerViewAdapter(babySitterList, getFragmentManager());
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void refreshUI() {
        FirebaseFirestore
                .getInstance()
                .collection("babysitters")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        babySitterList.clear();
                        babySitterList.addAll(task.getResult().toObjects(BabySitter.class));
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity activity = (MainActivity) getActivity();
        activity.updateBackButton();
        refreshUI();
    }

}