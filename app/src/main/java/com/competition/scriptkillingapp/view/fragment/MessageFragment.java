package com.competition.scriptkillingapp.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.adapter.MessageRecViewAdapter;
import com.competition.scriptkillingapp.model.Message;
import com.competition.scriptkillingapp.util.MyNestedScrollView;

import java.util.ArrayList;

public class MessageFragment extends Fragment {
    private View view;

    private RecyclerView otherMsgRecView;
    private RecyclerView prepareRoomMsgRecView;

    private RelativeLayout messageHeader, messageParent;

    private MyNestedScrollView messageScrollView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);

        messageScrollView = view.findViewById(R.id.message_scrollView);
        messageHeader = view.findViewById(R.id.message_header);
        messageParent = view.findViewById(R.id.message_parent);
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

        initScrollView();

        return view;
    }

    private void initScrollView() {
        // 动态设置RecView的高度
        messageParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int headerHeight = messageHeader.getMeasuredHeight();
                messageScrollView.setHeaderHeight(headerHeight);
                int measureHeight = messageParent.getMeasuredHeight();
                // Log.d(TAG, "onGlobalLayout measureHeight --> " + measureHeight);
                ViewGroup.LayoutParams layoutParams = otherMsgRecView.getLayoutParams();
                layoutParams.height = measureHeight;
                otherMsgRecView.setLayoutParams(layoutParams);
                if (measureHeight != 0) {
                    messageParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }
}
