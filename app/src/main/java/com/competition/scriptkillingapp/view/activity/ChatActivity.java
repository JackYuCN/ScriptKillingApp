package com.competition.scriptkillingapp.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.adapter.ChatAdapter;
import com.competition.scriptkillingapp.model.Chat;
import com.competition.scriptkillingapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    final String URL = "https://scriptkillingapp-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private static final String TAG = "ChatActivity";
    private String mChatterId;
    private Intent intent;
    private ImageView mBackImage;
    private TextView mChatterName;
    private DatabaseReference mRef;
    private FirebaseUser mCurrentUser;
    private Button mBtnSend;
    private EditText mEdtTextMsg;
    private ChatAdapter mChatAdapter;
    private ArrayList<Chat> chatMessages = new ArrayList<>();
    private RecyclerView mRecViewChatBody;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        intent = getIntent();
        mChatterId = intent.getStringExtra("userId");

        initWindow();
        initWidget();
        initListener();
    }

    /**
     * 初始化各控件的监听器
     */
    private void initListener() {
        mBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mEdtTextMsg.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(mCurrentUser.getUid(), mChatterId, msg);
                } else {
                    Toast.makeText(ChatActivity.this, "发送不能为空", Toast.LENGTH_SHORT).show();
                }
                mEdtTextMsg.setText("");
            }
        });

        mRef.child("Users").child(mChatterId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User chatter = snapshot.getValue(User.class);
                mChatterName.setText(chatter.getUsername());
                readMessage(mCurrentUser.getUid(), mChatterId, chatter.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * 初始化控件
     */
    private void initWidget() {
        mChatterName = findViewById(R.id.chatter_name);
        mBackImage = findViewById(R.id.arrow_back);
        mBtnSend = findViewById(R.id.btn_send);
        mEdtTextMsg = findViewById(R.id.edtText_msg);

        mRecViewChatBody = findViewById(R.id.chat_body);
        mRecViewChatBody.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        mRecViewChatBody.setLayoutManager(linearLayoutManager);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mCurrentUser != null;

        mRef = FirebaseDatabase.getInstance(URL).getReference();
    }

    /**
     * 设置顶部框透明
     */
    private void initWindow() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    /**
     * 将用户发送的信息按照<发送者, 接收者, 信息内容>的格式上传到 FirebaseDatabase 的"Chats"一栏中
     *
     * @param sender   -->  发送者Uid
     * @param receiver -->  接收者Uid
     * @param message  -->  发送信息
     */
    private void sendMessage(String sender, String receiver, String message) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        mRef.child("Chats").push().setValue(hashMap);
    }

    /**
     * 实时读取用户所发送和接收的信息，并将其存入chatMessages中，后者用于RecView的输出
     *
     * @param my       --> 用户Uid
     * @param sender   --> 接收者Uid
     * @param imageUrl --> 图片网址（头像）
     */
    private void readMessage(String my, String sender, String imageUrl) {
        mRef.child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 遍历Chats，将所有与用户相关的信息存储到chatMessages数组中
                chatMessages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(my) && chat.getSender().equals(sender) ||
                            chat.getReceiver().equals(sender) && chat.getSender().equals(my)) {
                        chatMessages.add(chat);
                    }

                    mChatAdapter = new ChatAdapter(ChatActivity.this, chatMessages, imageUrl);
                    mRecViewChatBody.setAdapter(mChatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
