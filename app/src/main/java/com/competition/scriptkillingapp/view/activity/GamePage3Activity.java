package com.competition.scriptkillingapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.competition.scriptkillingapp.R;

public class GamePage3Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamepage3);

        initView();
    }

    private Button btn;

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

