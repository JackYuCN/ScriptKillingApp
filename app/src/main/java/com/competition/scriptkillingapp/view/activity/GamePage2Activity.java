package com.competition.scriptkillingapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.adapter.GamePage2Adapter;
import com.competition.scriptkillingapp.adapter.GameRoomCharacterAdapter;
import com.competition.scriptkillingapp.model.Script;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.annotations.concurrent.Background;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


// An highlighted block
public class GamePage2Activity extends AppCompatActivity {

    final String URL = "https://scriptkillingapp-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private String gameIdx;
    private Button btnConfrim;
    //声明数组
    private String[] episodeArray = {"第一篇-序章", "第二篇-回忆1", "第一轮搜证", "第三篇-回忆2", "第二轮搜证", "第四篇-现实", "第一轮技能卡", "第五篇-结尾"};
    private DatabaseReference mRef;
    private ImageView ivScript;
    private boolean isScriptSelectable = false;
    private TextView tvScript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamepage2);

        btnConfrim = findViewById(R.id.gameStartActivity_btnConfirm);
        ivScript = findViewById(R.id.gameStartActivity_bottom_ivScript);
        mRef = FirebaseDatabase.getInstance(URL).getReference();

        Intent intent = getIntent();
        gameIdx = intent.getStringExtra("gameIdx");

        updateGameState();

        //数组适配器
        ArrayAdapter<String> germsAdapt = new ArrayAdapter<String>(this,
                R.layout.item_select, episodeArray);
        germsAdapt.setDropDownViewResource(R.layout.item_dropdown);

        Spinner sp = findViewById(R.id.gamepage2_dropdown);
        sp.setPrompt("请选择篇章");
        sp.setAdapter(germsAdapt);
        sp.setSelection(0);

        initListener();
    }

    private void updateGameState() {
        mRef.child("Games").child(gameIdx).child("stages").setValue(2);
    }

    private void initListener() {
        btnConfrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GamePage2Activity.this, GamePage3Activity.class);
                intent.putExtra("gameIdx", gameIdx);
                startActivity(intent);
            }
        });
        ivScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView markup;
                TextView tvScript;


                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        GamePage2Activity.this, R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.layout_bottom_script,
                                findViewById(R.id.bottom_script_container)
                        );
                bottomSheetDialog.setContentView(bottomSheetView);

                markup = bottomSheetDialog.findViewById(R.id.bottom_script_ivMarkup);
                tvScript = bottomSheetDialog.findViewById(R.id.bottom_tv_script);
                markup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isScriptSelectable = !isScriptSelectable;
                        tvScript.setTextIsSelectable(isScriptSelectable);
                        if (isScriptSelectable == true) {
                            markup.setImageResource(R.drawable.markup_pressed);
                            tvScript.setCustomSelectionActionModeCallback(new ActionMode.Callback2() {
                                private Menu mMenu;
                                @Override
                                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                                    menu.clear();
                                    MenuInflater menuInflater = mode.getMenuInflater();
                                    menuInflater.inflate(R.menu.menu_highlight, menu);
                                    return true;
                                }

                                @Override
                                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                                    this.mMenu = menu;
                                    return false;
                                }

                                @Override
                                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.it_highlight:
                                            int start = tvScript.getSelectionStart();
                                            int end = tvScript.getSelectionEnd();
                                            SpannableStringBuilder style = new SpannableStringBuilder(getString(R.string.script_episode1));
                                            style.setSpan(new BackgroundColorSpan(Color.GREEN), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            tvScript.setText(style);
                                    }
                                    return false;
                                }

                                @Override
                                public void onDestroyActionMode(ActionMode mode) {

                                }
                            });
                        } else {
                            markup.setImageResource(R.drawable.markup);
                        }
                    }
                });

                bottomSheetDialog.show();
            }
        });
    }

}
