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
    private String mChatId;
    private Intent intent;
    private ImageView mBackImage;
    private TextView mChatName;
    private DatabaseReference mRef;
    private FirebaseUser mUser;
    private Button mBtnSend;
    private EditText mEdtTextMsg;
    private ChatAdapter mChatAdapter;
    private ArrayList<Chat> chatMessages;
    private RecyclerView mRecViewChatBody;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatName = findViewById(R.id.chatter_name);
        mBackImage = findViewById(R.id.arrow_back);
        mBtnSend = findViewById(R.id.btn_send);
        mEdtTextMsg = findViewById(R.id.edtText_msg);
        mRecViewChatBody = findViewById(R.id.chat_body);

        mRecViewChatBody.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        mRecViewChatBody.setLayoutManager(linearLayoutManager);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mUser != null;

        intent = getIntent();
        mChatId = intent.getStringExtra("userId");

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
                    sendMessage(mUser.getUid(), mChatId, msg);
                } else {
                    Toast.makeText(ChatActivity.this, "发送不为空", Toast.LENGTH_SHORT).show();
                }
                mEdtTextMsg.setText("");
            }
        });

        mRef = FirebaseDatabase.getInstance(URL).getReference("Users").child(mChatId);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                mChatName.setText(user.getUsername());

                readMessage(mUser.getUid(), mChatId, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        initWindow();
    }

    private void initWindow() {
        //设置顶部框透明
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference ref = FirebaseDatabase.getInstance(URL).getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        ref.child("Chats").push().setValue(hashMap);
    }

    private void readMessage(String my, String sender, String imageUrl) {
        chatMessages = new ArrayList<>();

        mRef = FirebaseDatabase.getInstance(URL).getReference("Chats");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
