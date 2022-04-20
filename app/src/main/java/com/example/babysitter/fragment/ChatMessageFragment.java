package com.example.babysitter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.babysitter.adapter.ChatMessageRecyclerViewAdapter;
import com.example.babysitter.databinding.FragmentChatListBinding;
import com.example.babysitter.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageFragment extends Fragment {
    private final List<ChatMessage> messages = new ArrayList<>();

    public ChatMessageFragment() {
    }

    public static ChatMessageFragment newInstance() {
        ChatMessageFragment fragment = new ChatMessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentChatListBinding binding = FragmentChatListBinding.inflate(inflater, container, false);

        messages.add(new ChatMessage("Hey!", "Smith Jones", "avatar.png"));
        messages.add(new ChatMessage("Sounds good, thanks", "Smith Jones", "avatar.png"));
        messages.add(new ChatMessage("Awesome", "Phil Wilson", "review.png"));
        messages.add(new ChatMessage("That's great!", "Phil Jones", "review.png"));
        messages.add(new ChatMessage("Unsure, lets not do that.", "Lucifer", "review.png"));
        Context context = binding.getRoot().getContext();
        binding.list.setLayoutManager(new LinearLayoutManager(context));
        binding.list.setAdapter(new ChatMessageRecyclerViewAdapter(messages));
        return binding.getRoot();
    }
}