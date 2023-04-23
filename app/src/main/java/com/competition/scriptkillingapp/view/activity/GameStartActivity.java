package com.competition.scriptkillingapp.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.adapter.GameRoomCharacterAdapter;
import com.competition.scriptkillingapp.adapter.GameRoomProfileAdapter;
import com.competition.scriptkillingapp.model.Profile;
import com.competition.scriptkillingapp.model.RoomSetting;
import com.competition.scriptkillingapp.model.Script;
import com.competition.scriptkillingapp.model.ScriptCharacter;
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
    final String TAG = "GameStartCheck";
    final String URL = "https://scriptkillingapp-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private Button mBtnReady;
    private DatabaseReference mRef;
    private TextView mTxtViewCountDown;
    private String gameIdx, roomIdx;
    private String scriptName;
    private CountDownTimer timer;
    private ImageView mBtnSend;
    private RecyclerView scriptsRecView, profileRecView;
    private TextView tv1, tv2, scriptScore, scriptTitle, scriptType, scriptRequire;
    private EditText ed1;
    private FirebaseUser mCurrentUser;
    private String act_as;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);

        Intent intent = getIntent();
        gameIdx = intent.getStringExtra("gameIdx");
        roomIdx = intent.getStringExtra("roomIdx");
        scriptName = intent.getStringExtra("scriptName");
        Log.d(TAG, "gameIdx: " + gameIdx + "\nscriptName: " + scriptName);

        updateRoomState();
        initWindow();
        initWidget();
        setUserState();
        initTimer();
        initListener();

        GameRoomCharacterAdapter adapter = new GameRoomCharacterAdapter(this);
        ArrayList<ScriptCharacter> scripts = new ArrayList<>();
        mRef.child("ScriptsLib").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scripts.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Script s = dataSnapshot.child("ScriptInfo").getValue(Script.class);
                    if (s.getName().equals(scriptName)) {
                        scriptTitle.setText(s.getName());
                        scriptScore.setText(s.getScore());
                        scriptType.setText(s.getType());
                        scriptRequire.setText(s.getPeople() + "人 | " + s.getHour() + "小时");
                        for (DataSnapshot ds : dataSnapshot.child("Characters").getChildren()) {
                            scripts.add(ds.getValue(ScriptCharacter.class));
                        }
                        break;
                    }
                }
                adapter.setScripts(scripts);
                scriptsRecView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        GameRoomProfileAdapter profilesAdapter = new GameRoomProfileAdapter(this);
        mRef.child("rooms/right_now/" + roomIdx + "/settings")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RoomSetting rs = snapshot.getValue(RoomSetting.class);
                Log.d(TAG, "rs ---> " + rs.toString());
                profilesAdapter.setProfiles(rs.getPlayers());
                profileRecView.setAdapter(profilesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateRoomState() {
        DatabaseReference tmp_ref = FirebaseDatabase.getInstance(URL).getReference("rooms/right_now/" + roomIdx + "/settings");
        tmp_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean flag = false;
                RoomSetting roomSetting = snapshot.getValue(RoomSetting.class);
                for (String userId : roomSetting.getPlayers()) {
                    if (mCurrentUser.getUid().equals(userId)) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    roomSetting.getPlayers().add(mCurrentUser.getUid());
                    roomSetting.setGameRef(gameIdx);
                    tmp_ref.setValue(roomSetting);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUserState() {
        mRef.child("Users").child(mCurrentUser.getUid()).child("playing").setValue(true);
        mRef.child("Users").child(mCurrentUser.getUid()).child("gameIdx").setValue(gameIdx);
        mRef.child("Users").child(mCurrentUser.getUid()).child("act_as").setValue("null");
    }

    private void updateGameState() {
        mRef.child("Games").child(gameIdx).child("stages").setValue(1);
        Intent intent = new Intent(GameStartActivity.this, GameStageActivity.class);
        intent.putExtra("gameIdx", gameIdx);
        startActivity(intent);
    }

    private void initWindow() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.BLACK);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void initTimer() {
        int time = 1000 * 60 * 3;  // 3 minute count down
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String emptyFill;
                if ((millisUntilFinished / 1000) % 60 < 10) {
                    emptyFill = "0";
                } else {
                    emptyFill = "";
                }
                mTxtViewCountDown.setText("0" + millisUntilFinished / 1000 / 60 + ":" + emptyFill + (millisUntilFinished / 1000) % 60);
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

        scriptsRecView = findViewById(R.id.gr_intro_scriptsRecView);
        scriptsRecView.setHasFixedSize(true);
        scriptsRecView.setLayoutManager(new LinearLayoutManager(this));

        profileRecView = findViewById(R.id.game_room_image_recview);
        profileRecView.setLayoutManager(new LinearLayoutManager(this));

        scriptScore = findViewById(R.id.gr_intro_item_txtScriptScore);
        scriptTitle = findViewById(R.id.gr_intro_item_txtScriptName);
        scriptType = findViewById(R.id.gr_intro_item_txtScriptType);
        scriptRequire = findViewById(R.id.gr_intro_item_txtScriptPeople);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mCurrentUser != null;

        mRef = FirebaseDatabase.getInstance(URL).getReference();
    }

    private void initListener() {
        mBtnReady.setOnClickListener(new View.OnClickListener() {
            boolean arrive = false; // 是否是第一次触发listener ———— false表示第一次
            @Override
            public void onClick(View v) {
                timer.cancel();
                mBtnReady.setText("已准备");
                mRef.child("Games").child(gameIdx).addValueEventListener(new ValueEventListener() {
                    //TODO: 统计点击确定的人数
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean jmpFlag = false;
                        int current_cnt = snapshot.child("cnt").getValue(Integer.class);
                        int total_cnt = snapshot.child("max_cnt").getValue(Integer.class);
                        assert current_cnt < total_cnt;

                        mRef.child("Users").child(mCurrentUser.getUid()).child("act_as")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                        if (arrive == false) {
                            current_cnt = (current_cnt + 1) % total_cnt;
                            if (current_cnt == 0) {
                                jmpFlag = true;
                            }
                            mRef.child("Games").child(gameIdx).child("cnt").setValue(current_cnt);
                        } else {
                            Log.d(TAG, "not first enter : check value");
                            if (current_cnt == 0) {
                                jmpFlag = true;
                            }
                        }
                        if (jmpFlag) {
                            int time = 1000 * 5;
                            timer = new CountDownTimer(time, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    mTxtViewCountDown.setText("00:0" + (millisUntilFinished / 1000) % 60);
                                }

                                @Override
                                public void onFinish() {
                                    updateGameState();
                                }
                            };
                            timer.start();
                            mRef.child("Games").child(gameIdx).removeEventListener(this);
                        }
                        arrive = true;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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

    private void sendMessage(String senderUid, String message) {
        HashMap<String, Object> hashMap = new HashMap<>();
        mRef.child("Users").child(senderUid).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String sender = snapshot.getValue(String.class);
                hashMap.put("sender", sender);
                hashMap.put("message", message);
                mRef.child("rooms").child("right_now").child(roomIdx).child("Chats")
                        .push().setValue(hashMap);
                mRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessage() {
        mRef.child("rooms").child("right_now").child(roomIdx).child("Chats")
                .addValueEventListener(new ValueEventListener() {
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