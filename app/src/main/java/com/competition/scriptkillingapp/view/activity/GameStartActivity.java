package com.competition.scriptkillingapp.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.competition.scriptkillingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameStartActivity extends AppCompatActivity {

    final String TAG = "SynchronizeCheck";
    final String URL = "https://scriptkillingapp-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private Button mBtnReady;
    private DatabaseReference mDatabaseRef;
    private int cnt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);

        //设置顶部框透明
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mBtnReady = findViewById(R.id.game_btn_start);

        mDatabaseRef = FirebaseDatabase.getInstance(URL).getReference("test");

        initListener();
    }

    private void initListener() {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cnt = snapshot.child("cnt").getValue(Integer.class);
                Log.d(TAG, "onDataChange: New cnt --> " + cnt);
                if (cnt == 0) {
                    Intent i = new Intent(GameStartActivity.this, GameOverActivity.class);
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
                mBtnReady.setClickable(false);
            }
        });
    }
}