package com.competition.scriptkillingapp.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.competition.scriptkillingapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StageThreeFragment extends Fragment {

    final String URL = "https://scriptkillingapp-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private static final String TAG = "Stage3Check";
    private View view;
    private ImageView bigView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stage3, container, false);
        Log.d(TAG, "onCreateView: create fragment ===> R.layout.fragment_stage3");

        bigView = view.findViewById(R.id.big_image);
        DatabaseReference ref = FirebaseDatabase.getInstance(URL).getReference();

        String gameIdx = getArguments().getString("gameIdx");
        Log.d(TAG, "gameIdx ---> " + gameIdx);

        ref.child("Games").child(gameIdx).child("search_point").setValue(4);

        bigView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                customDialog();
                return true;
            }
        });


        return view;
    }

    private void customDialog() {
        final Dialog dialog = new Dialog(view.getContext(), R.style.NormalDialogStyle);
        View dialogView = View.inflate(view.getContext(), R.layout.btn_souzhen_confirm, null);
        TextView cancel = (TextView) dialogView.findViewById(R.id.souzhen_cancel);
        TextView confirm = (TextView) dialogView.findViewById(R.id.souzhen_confirm);
        dialog.setContentView(dialogView);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
