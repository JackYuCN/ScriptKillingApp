package com.competition.scriptkillingapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.adapter.ScriptAdapter;
import com.competition.scriptkillingapp.model.RoomSetting;
import com.competition.scriptkillingapp.model.Script;
import com.competition.scriptkillingapp.model.ScriptCharacter;
import com.competition.scriptkillingapp.model.User;
import com.competition.scriptkillingapp.util.MyNestedScrollView;
import com.competition.scriptkillingapp.view.activity.AddScriptActivity;
import com.competition.scriptkillingapp.view.activity.GameStageActivity;
import com.competition.scriptkillingapp.view.activity.GameStartActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {
    final String URL_db = "gs://scriptkillingapp.appspot.com";
    final String URL = "https://scriptkillingapp-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private static final String TAG = "HomeFragment";
    private View view;
    private TextView txtReadyRoom, txtBookRoom;
    private Spinner spinner1, spinner2, spinner3;
    private ArrayAdapter<String> spinnerAdapterTime, spinnerAdapterCnt, spinnerAdapterType;
    private RecyclerView scriptsRecView;
    private ArrayList<Script> scriptsInfoListReady = new ArrayList<>(), scriptsInfoListBook = new ArrayList<>();
    private ArrayList<RoomSetting> roomSettingListReady = new ArrayList<>(), roomSettingListBook = new ArrayList<>();
    private ScriptAdapter adapterReady, adapterBook;
    private RelativeLayout homeHeader;
    private MyNestedScrollView homeParent;
    private FloatingActionButton fabAddScript;
    private SearchView searchEdtText;
    private FirebaseUser mCurrentUser;
    private ImageView IsPlaying;
    private int stage;
    private Button btnAddData;
    DatabaseReference mRef;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_txtBookingRoom:
                changeState(false);
                break;
            case R.id.home_txtReadyToOpenRoom:
                changeState(true);
                break;
            case R.id.home_addScript:
                mRef = FirebaseDatabase.getInstance(URL).getReference().child("ScriptsLib").push();
                mRef.child("Characters").push()
                        .setValue(new ScriptCharacter("穆镶", "探险队队长，校园内知名的学霸，性格独立，有点神秘。", "female"));
                mRef.child("Characters").push()
                        .setValue(new ScriptCharacter("穆一", "穆镶的亲弟弟，与穆镶关系紧密，对古董宝藏有所了解。", "male"));
                mRef.child("Characters").push()
                        .setValue(new ScriptCharacter("方茴", "清远高中的数学老师，探险队管理员，聪明机智。", "female"));
                mRef.child("Characters").push()
                        .setValue(new ScriptCharacter("方北辰", "方茴的堂哥，清远高中的英语老师，对古董宝藏有浓厚兴趣。", "male"));
                mRef.child("Characters").push()
                        .setValue(new ScriptCharacter("宋婷", "探险队成员，温文尔雅，学霸，善良贴心，对古董宝藏有一定了解", "female"));
                mRef.child("Characters").push()
                        .setValue(new ScriptCharacter("华峰", "探险队成员，运动员，热血阳光，与穆镶关系友好，对古董宝藏有好奇心", "male"));
                mRef.child("ScriptInfo").setValue(new Script("《1037公园》", "情感,入门", "4.8", 6, 5));
                break;
            default:
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        initWidget();
        initRecView();
        initSpinnerAdapter();
        initListener();

        return view;
    }

    private void initWidget() {
        homeParent = view.findViewById(R.id.home_parent);
        homeHeader = view.findViewById(R.id.home_header);

        txtReadyRoom = view.findViewById(R.id.home_txtReadyToOpenRoom);
        txtBookRoom = view.findViewById(R.id.home_txtBookingRoom);
        spinner1 = view.findViewById(R.id.home_spinner1);
        spinner2 = view.findViewById(R.id.home_spinner2);
        spinner3 = view.findViewById(R.id.home_spinner3);
        fabAddScript = view.findViewById(R.id.home_fabAddScript);
        searchEdtText = view.findViewById(R.id.home_searchScripts);
        scriptsRecView = view.findViewById(R.id.home_scriptsRecView);
        IsPlaying = view.findViewById(R.id.home_imagePlaying);

        btnAddData = view.findViewById(R.id.home_addScript);
        btnAddData.setOnClickListener(this);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mCurrentUser != null;

        mRef = FirebaseDatabase.getInstance(URL).getReference();
    }
    private void initRecView() {
        scriptsRecView.setHasFixedSize(true);
        scriptsRecView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        adapterReady = new ScriptAdapter(view.getContext());
        adapterBook = new ScriptAdapter(view.getContext());
        mRef.child("rooms").child("booking")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scriptsInfoListBook.clear();
                roomSettingListBook.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Script scriptInfo = dataSnapshot.child("script_info").getValue(Script.class);
                    scriptsInfoListBook.add(scriptInfo);
                    RoomSetting roomSetting = dataSnapshot.child("settings").getValue(RoomSetting.class);
                    roomSettingListBook.add(roomSetting);
                }
                adapterBook.setScriptInfo(scriptsInfoListBook);
                adapterBook.setRoomSettings(roomSettingListBook);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mRef.child("rooms").child("right_now")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scriptsInfoListReady.clear();
                roomSettingListReady.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Script scriptInfo = dataSnapshot.child("script_info").getValue(Script.class);
                    scriptsInfoListReady.add(scriptInfo);
                    RoomSetting roomSetting = dataSnapshot.child("settings").getValue(RoomSetting.class);
                    roomSettingListReady.add(roomSetting);
                }
                adapterReady.setScriptInfo(scriptsInfoListReady);
                adapterReady.setRoomSettings(roomSettingListReady);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initSpinnerAdapter() {
        spinnerAdapterTime = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); // "Hint to be displayed"
                } else {
                    Log.d(TAG, "getView position " + position);
                    ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                    layoutParams.height = -2; // wrap_content
                    v.setLayoutParams(layoutParams);
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you don't display last item. It is used as hint.
            }
        };
        spinnerAdapterCnt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); // "Hint to be displayed"
                } else {
                    Log.d(TAG, "getView position " + position);
                    ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                    layoutParams.height = -2; // wrap_content
                    v.setLayoutParams(layoutParams);
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you don't display last item. It is used as hint.
            }
        };
        spinnerAdapterType = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); // "Hint to be displayed"
                } else {
                    Log.d(TAG, "getView position " + position);
                    ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                    layoutParams.height = -2; // wrap_content
                    v.setLayoutParams(layoutParams);
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you don't display last item. It is used as hint.
            }
        };

        spinnerAdapterTime.add("上午场");
        spinnerAdapterTime.add("下午场");
        spinnerAdapterTime.add("晚场");
        spinnerAdapterTime.add("修仙场");
        spinnerAdapterTime.add("时间");

        spinnerAdapterCnt.add("1~2");
        spinnerAdapterCnt.add("3~5");
        spinnerAdapterCnt.add("5+");
        spinnerAdapterCnt.add("缺位");

        spinnerAdapterType.add("情感");
        spinnerAdapterType.add("惊悚");
        spinnerAdapterType.add("玄幻");
        spinnerAdapterType.add("类型");

        changeState(true);
    }
    private void initListener() {

        mRef.child("Users").child(mCurrentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.isPlaying()) {
                    fabAddScript.setVisibility(View.GONE);
                    fabAddScript.setOnClickListener(null);
                    IsPlaying.setVisibility(View.VISIBLE);
                    //设置监听器
                    IsPlaying.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String gameIdx = user.getGameIdx();
                            Log.d(TAG, "onClick: " + gameIdx);
                            // 获取当前stages值
                            mRef.child("Games").child(gameIdx).child("stages")
                                    .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    stage = snapshot.getValue(Integer.class);
                                    Intent intent = null;
                                    if (stage == 0) {
                                        intent = new Intent(getContext(), GameStartActivity.class);
                                    } else {
                                        intent = new Intent(getContext(), GameStageActivity.class);
                                        intent.putExtra("stage", stage);
                                    }
                                    intent.putExtra("gameIdx", gameIdx);
                                    startActivity(intent);
                                    // 获取stage以后关闭listener
                                    mRef.child("Games").child(gameIdx)
                                            .child("stages").removeEventListener(this);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                } else {
                    IsPlaying.setVisibility(View.GONE);
                    IsPlaying.setOnClickListener(null);
                    fabAddScript.setVisibility(View.VISIBLE);
                    fabAddScript.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), AddScriptActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        txtReadyRoom.setOnClickListener(this);
        txtBookRoom.setOnClickListener(this);
        searchEdtText.setOnClickListener(this);

        homeParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            int cnt = 0;

            @Override
            public void onGlobalLayout() {
                // set the header's height dynamically
                int headerHeight = homeHeader.getMeasuredHeight();
                Log.d(TAG, "HeaderHeight --> " + headerHeight);
                homeParent.setHeaderHeight(headerHeight);

                // set the RecView's height dynamically
                int measureHeight = homeParent.getMeasuredHeight();
                Log.d(TAG, "Fragment MeasureHeight --> " + measureHeight);
                ViewGroup.LayoutParams layoutParams = scriptsRecView.getLayoutParams();
                layoutParams.height = measureHeight;
                scriptsRecView.setLayoutParams(layoutParams);

                // NOTICE:
                // 这里不知道为什么homeParent的MeasureHeight会改变多次，所以不能在发生任意改变后就关闭监听
                // 打Log可知MeasureHeight改变10次以后会变成最后的值，充分起见，设置阈值为20
                if (measureHeight != 0 && cnt > 20 && homeParent.getHeaderHeight() != 0) {
                    homeParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                cnt++;
            }
        });
    }

    private void changeState(boolean isReady) {
        if (isReady) {
            // user click on ReadyRoom button
            scriptsRecView.setAdapter(adapterReady);

            txtBookRoom.setBackground(null);
            txtReadyRoom.setBackgroundResource(R.drawable.text_underline);
        } else {
            // user click on BookingRoom button
            scriptsRecView.setAdapter(adapterBook);

            txtReadyRoom.setBackground(null);
            txtBookRoom.setBackgroundResource(R.drawable.text_underline);
        }
        setSpinner(isReady);
    }

    private void setSpinner(boolean isReady) {
        if (isReady) {
            spinner1.setAdapter(spinnerAdapterCnt);
            spinner1.setSelection(spinnerAdapterCnt.getCount());
            spinner2.setAdapter(spinnerAdapterType);
            spinner2.setSelection(spinnerAdapterType.getCount());
            spinner3.setVisibility(View.GONE);
        } else {
            spinner1.setAdapter(spinnerAdapterTime);
            spinner1.setSelection(spinnerAdapterTime.getCount());
            spinner2.setAdapter(spinnerAdapterCnt);
            spinner2.setSelection(spinnerAdapterCnt.getCount());
            spinner3.setVisibility(View.VISIBLE);
            spinner3.setAdapter(spinnerAdapterType);
            spinner3.setSelection(spinnerAdapterType.getCount());
        }
    }
}
