package com.competition.scriptkillingapp.view.fragment;

import android.os.Bundle;
import android.util.Log;
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
import com.competition.scriptkillingapp.adapter.ScriptAdapter;
import com.competition.scriptkillingapp.model.ScriptTitle;
import com.competition.scriptkillingapp.util.MyNestedScrollView;

import java.util.ArrayList;

public class ScriptsFragment extends Fragment {
    private static final String TAG = "ScriptsFragment";
    private View view;
    private LinearLayout scriptsHeader;
    private RecyclerView scriptsRecView;
    private MyNestedScrollView scriptsParent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scripts, container, false);

        scriptsParent = view.findViewById(R.id.scripts_parent);
        scriptsHeader = view.findViewById(R.id.scripts_header);
        scriptsRecView = view.findViewById(R.id.scripts_recommendRecView);

        initRecView();
        initListener();

        return view;
    }

    private void initRecView() {
        ArrayList<ScriptTitle> scriptTitles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            scriptTitles.add(new ScriptTitle("测试样例" + i, 2));
        }

        ScriptAdapter adapter = new ScriptAdapter(view.getContext());
        adapter.setScripts(scriptTitles);

        scriptsRecView.setAdapter(adapter);
        scriptsRecView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void initListener() {
        scriptsParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // set the header's height dynamically
                int headerHeight = scriptsHeader.getMeasuredHeight();
                Log.d(TAG, "HeaderHeight --> " + headerHeight);
                scriptsParent.setHeaderHeight(headerHeight);

                // set the RecView's height dynamically
                int measureHeight = scriptsParent.getMeasuredHeight();
                Log.d(TAG, "Fragment MeasureHeight --> " + measureHeight);
                ViewGroup.LayoutParams layoutParams = scriptsRecView.getLayoutParams();
                layoutParams.height = measureHeight;
                scriptsRecView.setLayoutParams(layoutParams);

                if (measureHeight != 0 && scriptsParent.getHeaderHeight() != 0) {
                    scriptsParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }
}
