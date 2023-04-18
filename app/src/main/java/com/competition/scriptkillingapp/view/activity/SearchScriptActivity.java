package com.competition.scriptkillingapp.view.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SearchScriptActivity extends AppCompatActivity {
    private String searchText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        searchText = intent.getStringExtra("searchText");
    }
}
