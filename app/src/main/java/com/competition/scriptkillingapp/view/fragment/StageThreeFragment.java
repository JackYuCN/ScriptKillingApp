package com.competition.scriptkillingapp.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.competition.scriptkillingapp.R;

public class StageThreeFragment extends Fragment {

    private static final String TAG = "Stage 3";
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stage3, container, false);

        Log.d(TAG, "onCreateView: create fragment ===> R.layout.fragment_stage3");

        return view;
    }

}
