package com.competition.scriptkillingapp.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.adapter.ScriptRecViewAdapter;
import com.competition.scriptkillingapp.model.Script;
import com.competition.scriptkillingapp.util.MyNestedScrollView;

import java.util.ArrayList;

public class ScriptsFragment extends Fragment {

    private View view;

    private LinearLayout scriptsHeader, scriptsParent;
    private RecyclerView scriptsRecView;

    private MyNestedScrollView scriptsScrollView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scripts, container, false);

        scriptsParent = view.findViewById(R.id.scripts_parent);
        scriptsScrollView = view.findViewById(R.id.scripts_scrollView);
        scriptsHeader = view.findViewById(R.id.scripts_header);
        scriptsRecView = view.findViewById(R.id.hot_scripts_recommend_recview);

        ArrayList<Script> scripts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            scripts.add(new Script("测试样例" + i));
        }

        ScriptRecViewAdapter adapter = new ScriptRecViewAdapter(view.getContext());
        adapter.setScripts(scripts);

        scriptsRecView.setAdapter(adapter);
        scriptsRecView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        initScrollView();

        return view;
    }

    private void initScrollView() {
        // 动态设置RecView的高度
        scriptsParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int headerHeight = scriptsHeader.getMeasuredHeight();
                scriptsScrollView.setHeaderHeight(headerHeight);
                int measureHeight = scriptsParent.getMeasuredHeight();
                // Log.d(TAG, "onGlobalLayout measureHeight --> " + measureHeight);
                ViewGroup.LayoutParams layoutParams = scriptsRecView.getLayoutParams();
                layoutParams.height = measureHeight;
                scriptsRecView.setLayoutParams(layoutParams);
                if (measureHeight != 0) {
                    scriptsParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }
}
