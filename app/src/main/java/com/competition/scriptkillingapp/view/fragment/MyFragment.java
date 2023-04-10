package com.competition.scriptkillingapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.view.activity.LogInActivity;
import com.competition.scriptkillingapp.view.activity.MainActivity;

public class MyFragment extends Fragment {
    private View view;

    private TextView mtxtViewSignOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, container, false);

        mtxtViewSignOut = view.findViewById(R.id.my_tvSignOut);

        initListener();

        return view;
    }

    private void initListener() {
        mtxtViewSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LogInActivity.class);
                startActivity(intent);
            }
        });
    }
}
