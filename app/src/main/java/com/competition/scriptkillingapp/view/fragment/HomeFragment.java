package com.competition.scriptkillingapp.view.fragment;

import android.annotation.SuppressLint;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.adapter.ScriptRecViewAdapter;
import com.competition.scriptkillingapp.model.Script;
import com.competition.scriptkillingapp.util.MyNestedScrollView;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";
    private View view;

    private TextView txtReadyRoom, txtBookRoom;

    private Spinner spinner1, spinner2, spinner3;

    private ArrayAdapter<String> spinnerAdapterTime, spinnerAdapterCnt, spinnerAdapterType;

    private RecyclerView scriptsRecView;

    private ArrayList<Script> scriptsListReady, scriptsListBook;

    private ScriptRecViewAdapter adapterReady, adapterBook;

    private RelativeLayout homePageParent, homePageHeader;

    private MyNestedScrollView homeScrollView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        scriptsRecView = view.findViewById(R.id.home_scriptsRecView);

        homePageParent = view.findViewById(R.id.home_parent);
        homePageHeader = view.findViewById(R.id.home_header);
        homeScrollView = view.findViewById(R.id.home_scrollView);
        txtReadyRoom = view.findViewById(R.id.home_txtReadyToOpenRoom);
        txtBookRoom = view.findViewById(R.id.home_txtBookingRoom);
        spinner1 = view.findViewById(R.id.home_spinner1);
        spinner2 = view.findViewById(R.id.home_spinner2);
        spinner3 = view.findViewById(R.id.home_spinner3);

        txtReadyRoom.setOnClickListener(this);
        txtBookRoom.setOnClickListener(this);

        initRecView();
        initSpinnerAdapter();

        changeState(true);

        initScrollView();

        return view;
    }

    private void initScrollView() {
        // 动态设置RecView的高度
        homePageParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int headerHeight = homePageHeader.getMeasuredHeight();
                homeScrollView.setHeaderHeight(headerHeight);
                int measureHeight = homePageParent.getMeasuredHeight();
                // Log.d(TAG, "onGlobalLayout measureHeight --> " + measureHeight);
                ViewGroup.LayoutParams layoutParams = scriptsRecView.getLayoutParams();
                layoutParams.height = measureHeight;
                scriptsRecView.setLayoutParams(layoutParams);

                // TODO: 注意，这里如果关闭监听器会出现白边，暂时不知道如何解决
                // if (measureHeight != 0) {
                //     homePageParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // }
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
    }

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
            default:
                break;
        }
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
