package com.competition.scriptkillingapp.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.adapter.MessageAdapter;
import com.competition.scriptkillingapp.adapter.UserAdapter;
import com.competition.scriptkillingapp.model.Message;
import com.competition.scriptkillingapp.model.User;
import com.competition.scriptkillingapp.util.MyNestedScrollView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageFragment extends Fragment {
    final String URL = "https://scriptkillingapp-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private static final String TAG = "MessageFragment";
    private View view;
    private RecyclerView otherMsgRecView, prepareRoomMsgRecView;
    private RelativeLayout messageHeader;
    private MyNestedScrollView messageParent;
    private ArrayList<Message> mPrepareRooms;
    private ArrayList<User> mUsers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);

        messageHeader = view.findViewById(R.id.message_header);
        messageParent = view.findViewById(R.id.message_parent);
        prepareRoomMsgRecView = view.findViewById(R.id.message_prepareRoomRecView);
        otherMsgRecView = view.findViewById(R.id.message_otherMsgRecView);

        initRecView();
        initListener();

        return view;
    }

    private void initRecView() {
        mPrepareRooms = new ArrayList<>();
        mUsers = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            mPrepareRooms.add(new Message("等候室测试样例" + i));
        }

        MessageAdapter prepareRoomMsgAdapter = new MessageAdapter(view.getContext());
        prepareRoomMsgAdapter.setMessages(mPrepareRooms);
        prepareRoomMsgRecView.setAdapter(prepareRoomMsgAdapter);
        prepareRoomMsgRecView.setHasFixedSize(true);
        prepareRoomMsgRecView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        otherMsgRecView.setHasFixedSize(true);
        otherMsgRecView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        readUsers();
    }

    private void readUsers() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "current user's id " + firebaseUser.getUid());
        DatabaseReference ref = FirebaseDatabase.getInstance(URL).getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    Log.d(TAG, "add user " + user.toString());

                    if (!user.getId().equals(firebaseUser.getUid())) {
                        mUsers.add(user);
                    }
                }

                UserAdapter otherMsgAdapter = new UserAdapter(view.getContext());
                otherMsgAdapter.setUsers(mUsers);
                otherMsgRecView.setAdapter(otherMsgAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initListener() {
        // 动态设置RecView的高度
        messageParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // set the header's height dynamically
                int headerHeight = messageHeader.getMeasuredHeight();
                Log.d(TAG, "HeaderHeight --> " + headerHeight);
                messageParent.setHeaderHeight(headerHeight);

                // set the otherMsg RecView's height dynamically
                int measureHeight = messageParent.getMeasuredHeight();
                Log.d(TAG, "Fragment MeasureHeight --> " + measureHeight);
                ViewGroup.LayoutParams layoutParams = otherMsgRecView.getLayoutParams();
                layoutParams.height = measureHeight;
                otherMsgRecView.setLayoutParams(layoutParams);

                if (measureHeight != 0 && messageParent.getHeaderHeight() != 0) {
                    messageParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }
}
