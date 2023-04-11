package com.competition.scriptkillingapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.adapter.ScriptRecViewAdapter;
import com.competition.scriptkillingapp.model.Script;
import com.competition.scriptkillingapp.util.MyNestedScrollView;
import com.competition.scriptkillingapp.view.activity.AddScriptActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";
    private View view;
    private TextView txtReadyRoom, txtBookRoom;
    private Spinner spinner1, spinner2, spinner3;
    private ArrayAdapter<String> spinnerAdapterTime, spinnerAdapterCnt, spinnerAdapterType;
    private RecyclerView scriptsRecView;
    private ArrayList<Script> scriptsListReady, scriptsListBook;
    private ScriptRecViewAdapter adapterReady, adapterBook;
    private RelativeLayout homeHeader;
    private MyNestedScrollView homeParent;
    private FloatingActionButton fabAddScript;

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(), "clicked", Toast.LENGTH_SHORT).show();
        switch (v.getId()) {
            case R.id.home_txtBookingRoom:
                changeState(false);
                break;
            case R.id.home_txtReadyToOpenRoom:
                changeState(true);
                break;
            case R.id.home_fabAddScript:
                Intent intent = new Intent(v.getContext(), AddScriptActivity.class);
                startActivity(intent);
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
        scriptsRecView = view.findViewById(R.id.home_scriptsRecView);
        homeParent = view.findViewById(R.id.home_parent);
        homeHeader = view.findViewById(R.id.home_header);

        txtReadyRoom = view.findViewById(R.id.home_txtReadyToOpenRoom);
        txtBookRoom = view.findViewById(R.id.home_txtBookingRoom);
        spinner1 = view.findViewById(R.id.home_spinner1);
        spinner2 = view.findViewById(R.id.home_spinner2);
        spinner3 = view.findViewById(R.id.home_spinner3);
        fabAddScript = view.findViewById(R.id.home_fabAddScript);
    }
    private void initRecView() {
        scriptsListReady = new ArrayList<>();
        scriptsListBook = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            scriptsListReady.add(new Script("即开房测试样例" + i));
            scriptsListBook.add(new Script("预约房测试样例" + i));
        }

        adapterReady = new ScriptRecViewAdapter(view.getContext());
        adapterBook = new ScriptRecViewAdapter(view.getContext());
        adapterReady.setScripts(scriptsListReady);
        adapterBook.setScripts(scriptsListBook);

        scriptsRecView.setLayoutManager(new LinearLayoutManager(view.getContext()));
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

        txtReadyRoom.setOnClickListener(this);
        txtBookRoom.setOnClickListener(this);
        fabAddScript.setOnClickListener(this);

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
