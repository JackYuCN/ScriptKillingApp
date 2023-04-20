package com.competition.scriptkillingapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
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
import com.competition.scriptkillingapp.model.ScriptTitle;
import com.competition.scriptkillingapp.model.User;
import com.competition.scriptkillingapp.util.MyNestedScrollView;
import com.competition.scriptkillingapp.view.activity.AddScriptActivity;
import com.competition.scriptkillingapp.view.activity.GamePage2Activity;
import com.competition.scriptkillingapp.view.activity.GamePage3Activity;
import com.competition.scriptkillingapp.view.activity.GamePage4Activity;
import com.competition.scriptkillingapp.view.activity.GamePage5Activity;
import com.competition.scriptkillingapp.view.activity.GamePage6Activity;
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
    final String URL = "https://scriptkillingapp-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private static final String TAG = "HomeFragment";
    private View view;
    private TextView txtReadyRoom, txtBookRoom;
    private Spinner spinner1, spinner2, spinner3;
    private ArrayAdapter<String> spinnerAdapterTime, spinnerAdapterCnt, spinnerAdapterType;
    private RecyclerView scriptsRecView;
    private ArrayList<ScriptTitle> scriptsListReady = new ArrayList<>(), scriptsListBook = new ArrayList<>();
    private ScriptAdapter adapterReady, adapterBook;
    private RelativeLayout homeHeader;
    private MyNestedScrollView homeParent;
    private FloatingActionButton fabAddScript;
    private SearchView searchEdtText;
    private FirebaseUser mCurrentUser;
    private ImageView IsPlaying;
    private int stage;
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
                scriptsListBook.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ScriptTitle scriptTitle = dataSnapshot.child("script").getValue(ScriptTitle.class);
                    scriptsListBook.add(scriptTitle);
                }
                adapterBook.setScripts(scriptsListBook);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mRef.child("rooms").child("right_now")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scriptsListReady.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ScriptTitle scriptTitle = dataSnapshot.child("script").getValue(ScriptTitle.class);
                    scriptsListReady.add(scriptTitle);
                }
                adapterReady.setScripts(scriptsListReady);
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
                                    switch (stage) {
                                        case 1:
                                            intent = new Intent(getContext(), GameStartActivity.class);
                                            break;
                                        case 2:
                                            intent = new Intent(getContext(), GamePage2Activity.class);
                                            break;
                                        case 3:
                                            intent = new Intent(getContext(), GamePage3Activity.class);
                                            break;
                                        case 4:
                                            intent = new Intent(getContext(), GamePage4Activity.class);
                                            break;
                                        case 5:
                                            intent = new Intent(getContext(), GamePage5Activity.class);
                                            break;
                                        case 6:
                                            intent = new Intent(getContext(), GamePage6Activity.class);
                                            break;
                                        default:
                                            assert false;
                                    }
                                    // 获取stage以后关闭listener
                                    mRef.child("Games").child(gameIdx)
                                            .child("stages").removeEventListener(this);
                                    intent.putExtra("gameIdx", gameIdx);
                                    startActivity(intent);
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
