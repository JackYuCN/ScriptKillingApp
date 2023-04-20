package com.competition.scriptkillingapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.competition.scriptkillingapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GamePage3Activity extends AppCompatActivity {

    final String URL = "https://scriptkillingapp-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private DatabaseReference mRef;
    private String gameIdx;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamepage3);

        mRef = FirebaseDatabase.getInstance(URL).getReference();

        Intent intent = getIntent();
        gameIdx = intent.getStringExtra("gameIdx");

        updateGameState();

        initView();
    }

    private Button btn;

    private void updateGameState() {
        mRef.child("Games").child(gameIdx).child("stages").setValue(2);
    }

    private void initView() {
        btn = (Button) findViewById(R.id.belowline2_button6);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GamePage3Activity.this,GamePage4Activity.class);
                startActivity(intent);
            }
        });
    }
}

