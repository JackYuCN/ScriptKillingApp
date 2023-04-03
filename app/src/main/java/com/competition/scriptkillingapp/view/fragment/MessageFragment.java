package com.competition.scriptkillingapp.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.adapter.MessageRecViewAdapter;
import com.competition.scriptkillingapp.model.Message;

import java.util.ArrayList;

public class MessageFragment extends Fragment {
    private View view;

    private RecyclerView otherMsgRecView;
    private RecyclerView prepareRoomMsgRecView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);

        prepareRoomMsgRecView = view.findViewById(R.id.message_prepareRoomRecView);
        otherMsgRecView = view.findViewById(R.id.message_otherMsgRecView);

        ArrayList<Message> prepareRoomMsg = new ArrayList<>();
        ArrayList<Message> otherMsg = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            prepareRoomMsg.add(new Message("等候室测试样例" + i));
        }

        for (int i = 0; i < 10; i++) {
            otherMsg.add(new Message("其他消息测试样例" + i));
        }

        MessageRecViewAdapter prepareRoomMsgAdapter = new MessageRecViewAdapter(view.getContext());
        prepareRoomMsgAdapter.setMessages(prepareRoomMsg);
        prepareRoomMsgRecView.setAdapter(prepareRoomMsgAdapter);
        prepareRoomMsgRecView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        MessageRecViewAdapter otherMsgAdapter = new MessageRecViewAdapter(view.getContext());
        otherMsgAdapter.setMessages(otherMsg);
        otherMsgRecView.setAdapter(otherMsgAdapter);
        otherMsgRecView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }
}
