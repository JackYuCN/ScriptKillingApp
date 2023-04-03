package com.competition.scriptkillingapp.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.adapter.ScriptRecViewAdapter;
import com.competition.scriptkillingapp.model.Script;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private View view;

    private RecyclerView scriptsRecView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        scriptsRecView = view.findViewById(R.id.home_scriptsRecView);

        ArrayList<Script> scripts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            scripts.add(new Script("测试样例" + i));
        }

        ScriptRecViewAdapter adapter = new ScriptRecViewAdapter(view.getContext());
        adapter.setScripts(scripts);

        scriptsRecView.setAdapter(adapter);
        scriptsRecView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }
}
