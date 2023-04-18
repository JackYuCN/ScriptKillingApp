package com.competition.scriptkillingapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.competition.scriptkillingapp.R;


// An highlighted block
public class GamePage6Activity extends AppCompatActivity {

    //声明数组
    private String[] episodeArray = {"第一篇-序章", "第二篇-回忆1", "投票阶段", "第三篇-回忆2", "第二轮搜证", "第四篇-现实","第一轮技能卡","第五篇-结尾"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamepage6);

        //数组适配器
        ArrayAdapter<String> germsAdapt = new ArrayAdapter<String>(this,
                R.layout.item_select, episodeArray);
        germsAdapt.setDropDownViewResource(R.layout.item_dropdown);

        Spinner sp = findViewById(R.id.gamepage6_dropdown);
        sp.setPrompt("请选择篇章");
        sp.setAdapter(germsAdapt);
        sp.setSelection(2);
        sp.setOnItemSelectedListener(new MySelectedListener());


        initView();
    }

    private Button btn;

    private void initView() {
        btn = (Button) findViewById(R.id.belowline5_button6);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GamePage6Activity.this,GameOverActivity.class);
                startActivity(intent);
            }
        });
    }

    public class MySelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(GamePage6Activity.this,"您选择的是"+episodeArray[position],Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
}
