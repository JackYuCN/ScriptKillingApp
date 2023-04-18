package com.competition.scriptkillingapp.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.adapter.GameRoomCharacterAdapter;
import com.competition.scriptkillingapp.model.Script;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class GameStartActivity extends AppCompatActivity {
    final String TAG = "SynchronizeCheck";
    final String URL = "https://scriptkillingapp-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private Button mBtnReady, mBtnSend;
    private DatabaseReference mDatabaseRef;
    private TextView mTxtViewCountDown;
    private int cnt;
    private String gameIdx;
    private CountDownTimer timer;
    private RecyclerView scriptsRecView;
    private TextView tv1, tv2;
    private EditText ed1;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);

        Intent intent = getIntent();
        gameIdx = intent.getStringExtra("gameIdx");
        Log.d(TAG, "gameIdx: " + gameIdx);

        initWindow();
        initWidget();
        initTimer();
        initListener();

//        scriptsRecView = findViewById(R.id.gr_intro_scriptsRecView);
//
//        ArrayList<Script> scripts = new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            scripts.add(new Script("测试样例" + i));
//        }
//
//        GameRoomCharacterAdapter adapter = new GameRoomCharacterAdapter(this);
//        adapter.setScripts(scripts);
//
//        scriptsRecView.setAdapter(adapter);
//        scriptsRecView.setLayoutManager(new LinearLayoutManager(this));
//
//        initView();

    }


    private Button btn;

    private void initView() {
        btn = (Button) findViewById(R.id.belowline_button2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameStartActivity.this, GamePage2Activity.class);
                startActivity(intent);
            }
        });
    }

    private void initWindow() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void initTimer() {
        int time = 1000 * 60;
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTxtViewCountDown.setText(millisUntilFinished / 1000 / 60 + ":" + (millisUntilFinished / 1000) % 60);
            }

            @Override
            public void onFinish() {
                mBtnReady.callOnClick();
            }
        };
        timer.start();
    }

    private void initWidget() {
        mBtnReady = findViewById(R.id.belowline_button2);
        mTxtViewCountDown = findViewById(R.id.game_tv_countDown);
        tv1 = findViewById(R.id.belowline_tv1);
        tv2 = findViewById(R.id.belowline_tv2);
        mBtnSend = findViewById(R.id.belowline_btnSend);
        ed1 = findViewById(R.id.belowline_et);

        mDatabaseRef = FirebaseDatabase.getInstance(URL).getReference("test");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mCurrentUser != null;
    }

    private void initListener() {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cnt = snapshot.child("cnt").getValue(Integer.class);
                Log.d(TAG, "onDataChange: New cnt --> " + cnt);
                if (cnt == 0) {
                    Intent i = new Intent(GameStartActivity.this, GamePage2Activity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        mBtnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cnt -= 1;
                Log.d(TAG, "cnt ===> " + cnt);
                mDatabaseRef.child("cnt").setValue(cnt);
                mBtnReady.setText("已准备");
                mBtnReady.setClickable(false);
                timer.cancel();
            }
        });
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = ed1.getText().toString();
                if (!message.equals("")) {
                    sendMessage(mCurrentUser.getUid(), message);
                }
                ed1.setText("");
            }
        });
        readMessage();
    }

    private void sendMessage(String sender, String message) {
        DatabaseReference ref = FirebaseDatabase.getInstance(URL).getReference("Games/" + gameIdx);
        assert ref != null;

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("message", message);

        ref.child("Chats").push().setValue(hashMap);
    }

    private void readMessage() {
        DatabaseReference mRefChat = FirebaseDatabase.getInstance(URL).getReference("Games/" + gameIdx + "/Chats");
        mRefChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String sender = "", message = "";
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    sender = dataSnapshot.child("sender").getValue(String.class);
                    message = dataSnapshot.child("message").getValue(String.class);
                }
                if (sender != "") {
                    tv1.setText(tv2.getText().toString());
                    tv2.setText(sender + ": " + message);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}