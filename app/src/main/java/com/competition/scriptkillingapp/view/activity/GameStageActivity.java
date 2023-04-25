package com.competition.scriptkillingapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.adapter.GameRoomProfileAdapter;
import com.competition.scriptkillingapp.adapter.SearchCardAdapter;
import com.competition.scriptkillingapp.model.Profile;
import com.competition.scriptkillingapp.model.SearchCard;
import com.competition.scriptkillingapp.view.fragment.StageFiveFragment;
import com.competition.scriptkillingapp.view.fragment.StageFourFragment;
import com.competition.scriptkillingapp.view.fragment.StageOneFragment;
import com.competition.scriptkillingapp.view.fragment.StageSixFragment;
import com.competition.scriptkillingapp.view.fragment.StageThreeFragment;
import com.competition.scriptkillingapp.view.fragment.StageTwoFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


// An highlighted block
public class GameStageActivity extends AppCompatActivity {
    private String TAG = "MyCheck";
    final String URL = "https://scriptkillingapp-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private String gameIdx;
    private Button mBtnReady;
    //声明数组
    private String[] episodeArray = {"第一篇-序章", "第二篇-回忆1", "第一轮搜证", "第三篇-回忆2", "第二轮搜证", "第四篇-现实", "第一轮技能卡", "第五篇-结尾"};
    private DatabaseReference mRef;
    private ImageView ivScript, ivClue;
    private CountDownTimer timer;
    private TextView mTxtViewCountDown;
    private boolean isScriptSelectable = false;
    private Spinner sp, sp_bottom;
    private int stage = 0;          // 表示当前所处stage
    private int frag_stage = 0;     // 表示当前所浏览的stage
    private int intent_stage = 0;
    private FragmentManager fragmentManager;
    private RecyclerView profileRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_stage);

        fragmentManager = getSupportFragmentManager();

        sp = findViewById(R.id.gamepage2_dropdown);
        mBtnReady = findViewById(R.id.gameStartActivity_btnConfirm);
        mTxtViewCountDown = findViewById(R.id.game_stage_countdown);
        ivScript = findViewById(R.id.gameStartActivity_bottom_ivScript);
        ivClue = findViewById(R.id.gameStartActivity_bottom_ivClue);
        mRef = FirebaseDatabase.getInstance(URL).getReference();

        Intent intent = getIntent();
        gameIdx = intent.getStringExtra("gameIdx");
        intent_stage = intent.getIntExtra("stage", 1);

        initFragment();
        initListener();
        initTimer();

        profileRecView = findViewById(R.id.game_room_image_recview);
        profileRecView.setLayoutManager(new LinearLayoutManager(this));
        profileRecView.setHasFixedSize(true);

        //数组适配器
        ArrayAdapter<String> germsAdapt = new ArrayAdapter<String>(this,
                R.layout.item_select, episodeArray);
        germsAdapt.setDropDownViewResource(R.layout.item_dropdown);
        sp.setPrompt("请选择篇章");
        sp.setAdapter(germsAdapt);
        sp.setSelection(intent_stage - 1);

        GameRoomProfileAdapter profilesAdapter = new GameRoomProfileAdapter(this);
        ArrayList<Profile> profiles = new ArrayList<>();
        mRef.child("Games/" + gameIdx + "/act_as")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        profiles.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String characterName = dataSnapshot.getValue(String.class);
                            profiles.add(new Profile(characterName, "Scripts/1037公园/人物头像/" + characterName + ".png"));
                        }
                        profilesAdapter.setProfiles(profiles);
                        profileRecView.setAdapter(profilesAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void initTimer() {
        int time = 1000 * 60 * 3;  // 3 minute count down
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String emptyAdd;
                if ((millisUntilFinished / 1000) % 60 < 10) {
                    emptyAdd = "0";
                } else {
                    emptyAdd = "";
                }
                mTxtViewCountDown.setText("0" + millisUntilFinished / 1000 / 60 + ":" + emptyAdd + (millisUntilFinished / 1000) % 60);
            }

            @Override
            public void onFinish() {
                mBtnReady.callOnClick();
            }
        };
        timer.start();
    }

    private void initFragment() {
        Log.d(TAG, "initFragment: intent_stage ---> " + intent_stage);
        if (intent_stage == 1) {
            fragmentManager.beginTransaction()
                    .replace(R.id.gamepage_fragment, new StageOneFragment(), "stage1")
                    .commit();
            stage = frag_stage = 1;
        } else if (intent_stage == 2) {
            fragmentManager.beginTransaction()
                    .replace(R.id.gamepage_fragment, new StageTwoFragment(), "stage2")
                    .commit();
            stage = frag_stage = 2;
        } else if (intent_stage == 3) {
            Log.d(TAG, "onFinish: transfer gameidx to fragment 3");
            Bundle bundle = new Bundle();
            bundle.putString("gameIdx", gameIdx);
            Fragment fragment = new StageThreeFragment();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.gamepage_fragment, fragment, "stage3")
                    .commit();
            stage = frag_stage = 3;
        } else if (intent_stage == 4) {
            fragmentManager.beginTransaction()
                    .replace(R.id.gamepage_fragment, new StageFourFragment(), "stage4")
                    .commit();
            stage = frag_stage = 4;
        }
    }

    private void initListener() {
        mBtnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                if (stage == frag_stage) {
                    add_fragment();
                } else if (stage > frag_stage) {
                    fragmentManager.beginTransaction()
                            .hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("stage" + frag_stage)))
                            .show(Objects.requireNonNull(fragmentManager.findFragmentByTag("stage" + (frag_stage + 1))))
                            .commit();
                    frag_stage += 1;
                    sp.setSelection(frag_stage - 1);
                } else {
                    Log.d(TAG, "STAGE FAULT: stage --> " + stage + " fragment_stage --> " + frag_stage);
                    assert false;
                }
            }
        });
        // “剧本”界面
        ivScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView markup;
                TextView tvScript;

                String[] episodeBottomArray = {"第一篇-序章", "第二篇-回忆1", "第三篇-回忆2", "第四篇-现实", "第五篇-结尾"};

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        GameStageActivity.this, R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.layout_bottom_script,
                                findViewById(R.id.bottom_script_container)
                        );
                bottomSheetDialog.setContentView(bottomSheetView);

                markup = bottomSheetDialog.findViewById(R.id.bottom_script_ivMarkup);
                tvScript = bottomSheetDialog.findViewById(R.id.bottom_tv_script);
                sp_bottom = bottomSheetDialog.findViewById(R.id.bottom_script_sp);

                ArrayAdapter<String> germsAdapt = new ArrayAdapter<String>(bottomSheetDialog.getContext(),
                        R.layout.item_select, episodeBottomArray);
                germsAdapt.setDropDownViewResource(R.layout.item_dropdown);
                sp_bottom.setPrompt("请选择篇章");
                sp_bottom.setAdapter(germsAdapt);
                sp_bottom.setSelection(0);

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
                                            SpannableStringBuilder style = new SpannableStringBuilder(tvScript.getText().toString());
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
                            markup.setImageResource(R.drawable.bottom_sheet_markup);
                        }
                    }
                });

                sp_bottom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int stage_of_position = position + 1; // TODO: 求出 position 对应游戏中的 stage
                        if (stage_of_position > stage) {
                            Toast.makeText(GameStageActivity.this, "还不能浏览后面阶段的内容哦", Toast.LENGTH_SHORT).show();
                        } else if (stage_of_position == 1) {
                            tvScript.setText(R.string.script_episode1);
                        } else if (stage_of_position == 2) {
                            tvScript.setText(R.string.script_episode2);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                bottomSheetDialog.show();
            }
        });
        // “线索”界面
        ivClue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        GameStageActivity.this, R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.layout_bottom_clue,
                                findViewById(R.id.bottom_clue_container)
                        );
                bottomSheetDialog.setContentView(bottomSheetView);

                RecyclerView searchCardsRecView = bottomSheetDialog.findViewById(R.id.bottom_clue_recview);
                searchCardsRecView.setLayoutManager(new GridLayoutManager(bottomSheetDialog.getContext(), 2));
                searchCardsRecView.setHasFixedSize(true);
                SearchCardAdapter searchCardAdapter = new SearchCardAdapter(bottomSheetDialog.getContext());
                ArrayList<SearchCard> searchCards = new ArrayList<>();
                mRef.child("Games/" + gameIdx + "/card_cnt")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                searchCards.clear();

                                int card_cnt = snapshot.getValue(Integer.class);
                                if (card_cnt >= 1)
                                    searchCards.add(new SearchCard("图书馆 搜证结果", "古董钟", "图书馆地下室内发现，有密码锁。钟表时间与穆镶死亡的时间一致。", "Scripts/1037公园/线索卡/clock.png"));
                                if (card_cnt >= 2)
                                    searchCards.add(new SearchCard("图书馆 搜证结果", "一张地图", "图书馆地下室内发现，不知是谁遗落的。", "Scripts/1037公园/线索卡/map.png"));
                                if (card_cnt >= 3)
                                    searchCards.add(new SearchCard("教学楼 搜证结果", "一摞邮票", "邮票从集邮册中散落，堆在一起。", "Scripts/1037公园/线索卡/post.png"));
                                if (card_cnt >= 4)
                                    searchCards.add(new SearchCard("教学楼 搜证结果", "迷你古董钟", "迷你古董钟上没有密码锁，样式和大古董钟十分相像。", "Scripts/1037公园/线索卡/mini_clock.png"));
                                if (card_cnt >= 4)
                                    searchCards.add(new SearchCard("教学楼 搜证结果", "迷你古董钟", "迷你古董钟上没有密码锁，样式和大古董钟十分相像。", "Scripts/1037公园/线索卡/mini_clock.png"));

                                searchCardAdapter.setSearchCards(searchCards);
                                searchCardsRecView.setAdapter(searchCardAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                bottomSheetDialog.show();
            }
        });
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int stage_of_position = position + 1; // TODO: 求出 position 对应游戏中的 stage
                Log.d(TAG, "onItemSelected: choose position ---> " + stage_of_position);
                if (stage_of_position > stage) {
                    Toast.makeText(GameStageActivity.this, "还不能浏览后面阶段的内容哦", Toast.LENGTH_SHORT).show();
                    sp.setSelection(frag_stage - 1);
                } else if (stage_of_position == 1) {
                    if (fragmentManager.findFragmentByTag("stage1") != null) {
                        fragmentManager.beginTransaction()
                                .hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("stage" + frag_stage)))
                                .show(Objects.requireNonNull(fragmentManager.findFragmentByTag("stage1")))
                                .commit();
                    } else {
                        fragmentManager.beginTransaction()
                                .hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("stage" + frag_stage)))
                                .add(R.id.gamepage_fragment, new StageOneFragment(), "stage1")
                                .commit();
                    }
                    frag_stage = 1;
                } else if (stage_of_position == 2) {
                    if (fragmentManager.findFragmentByTag("stage2") != null) {
                        fragmentManager.beginTransaction()
                                .hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("stage" + frag_stage)))
                                .show(Objects.requireNonNull(fragmentManager.findFragmentByTag("stage2")))
                                .commit();
                    } else {
                        fragmentManager.beginTransaction()
                                .hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("stage" + frag_stage)))
                                .add(R.id.gamepage_fragment, new StageTwoFragment(), "stage2")
                                .commit();
                    }
                    frag_stage = 2;
                } else if (stage_of_position == 3) {
                    if (fragmentManager.findFragmentByTag("stage3") != null) {
                        fragmentManager.beginTransaction()
                                .hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("stage" + frag_stage)))
                                .show(Objects.requireNonNull(fragmentManager.findFragmentByTag("stage3")))
                                .commit();
                    } else {
                        fragmentManager.beginTransaction()
                                .hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("stage" + frag_stage)))
                                .add(R.id.gamepage_fragment, new StageThreeFragment(), "stage3")
                                .commit();
                    }
                    frag_stage = 3;
                } else if (stage_of_position == 4) {
                    if (fragmentManager.findFragmentByTag("stage4") != null) {
                        fragmentManager.beginTransaction()
                                .hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("stage" + frag_stage)))
                                .show(Objects.requireNonNull(fragmentManager.findFragmentByTag("stage4")))
                                .commit();
                    } else {
                        fragmentManager.beginTransaction()
                                .hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("stage" + frag_stage)))
                                .add(R.id.gamepage_fragment, new StageThreeFragment(), "stage4")
                                .commit();
                    }
                    frag_stage = 4;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void add_fragment() {
        assert stage == frag_stage;
        timer.cancel();
        int time = 1000 * 5;  // 5 seconds count down
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTxtViewCountDown.setText("00:0" + (millisUntilFinished / 1000) % 60);
            }

            @Override
            public void onFinish() {
                Fragment fragment = null;
                Bundle bundle = new Bundle();
                // TODO: fix the assert fault here
                switch (stage) {
                    case 1:
                        fragment = new StageTwoFragment();
                        break;
                    case 2:
                        Log.d(TAG, "onFinish: transfer gameidx to fragment 3");
                        bundle.putString("gameIdx", gameIdx);
                        fragment = new StageThreeFragment();
                        fragment.setArguments(bundle);
                        break;
                    case 3:
                        bundle.putString("gameIdx", gameIdx);
                        fragment = new StageFourFragment();
                        fragment.setArguments(bundle);
                        break;
                    case 4:
                        fragment = new StageFiveFragment();
                        break;
                    case 5:
                        fragment = new StageSixFragment();
                        break;
                    default:
                        assert false;
                        break;
                }
                fragmentManager.beginTransaction()
                        .hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("stage" + stage)))
                        .add(R.id.gamepage_fragment, fragment, "stage" + (stage + 1))
                        .commit();
                stage += 1;
                frag_stage += 1;
                sp.setSelection(frag_stage - 1);
                initTimer();
            }
        };
        timer.start();
    }

}
