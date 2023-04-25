package com.competition.scriptkillingapp.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.model.GlideApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StageThreeFragment extends Fragment {
    private static final String URL_STORAGE = "gs://scriptkillingapp.appspot.com";
    final String URL = "https://scriptkillingapp-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private static final String TAG = "Stage3Check";
    private View view;
    private ImageView bigView;
    private Button btnShowCardCnt;
    private String gameIdx;
    private int card_cnt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stage3, container, false);

        card_cnt = 2;
        gameIdx = getArguments().getString("gameIdx");
        Log.d(TAG, "gameIdx ---> " + gameIdx);

        DatabaseReference ref = FirebaseDatabase.getInstance(URL).getReference();
        ref.child("Games").child(gameIdx).child("search_point").setValue(4);

        bigView = view.findViewById(R.id.big_image);
        btnShowCardCnt = view.findViewById(R.id.show_card_left);

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
        final Dialog reconfirm_dialog = new Dialog(view.getContext(), R.style.NormalDialogStyle);
        View dialogView = View.inflate(view.getContext(), R.layout.btn_souzhen_confirm, null);
        TextView cancel = (TextView) dialogView.findViewById(R.id.souzhen_cancel);
        TextView confirm = (TextView) dialogView.findViewById(R.id.souzhen_confirm);
        reconfirm_dialog.setContentView(dialogView);
        //使得点击对话框外部不消失对话框
        reconfirm_dialog.setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reconfirm_dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCard();
                reconfirm_dialog.dismiss();
            }
        });
        reconfirm_dialog.show();
    }

    private void showCard() {
        update_btn();

        final Dialog card_dialog = new Dialog(view.getContext(), R.style.NormalDialogStyle);
        View dialogView = View.inflate(view.getContext(), R.layout.search_card_layout, null);
        LinearLayout card = dialogView.findViewById(R.id.search_card_body);
        ImageView image = dialogView.findViewById(R.id.search_card_image);
        TextView result_title = dialogView.findViewById(R.id.search_card_searchtitle);
        TextView image_title = dialogView.findViewById(R.id.search_card_imagetitle);
        TextView intro = dialogView.findViewById(R.id.search_card_intro);

        DatabaseReference ref = FirebaseDatabase.getInstance(URL).getReference();
        ref.child("Games").child(gameIdx).child("card_cnt")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            String imageUrl, res, img, itr;
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int cnt = snapshot.getValue(Integer.class);
                                switch (cnt % 4) {
                                    case 0:
                                        imageUrl = "Scripts/1037公园/线索卡/clock.png";
                                        res = "图书馆 搜证结果";
                                        img = "古董钟";
                                        itr = "图书馆地下室内发现，有密码锁。钟表时间与穆镶死亡的时间一致。";
                                        break;
                                    case 1:
                                        imageUrl = "Scripts/1037公园/线索卡/map.png";
                                        res = "图书馆 搜证结果";
                                        img = "一张地图";
                                        itr = "图书馆地下室内发现，不知是谁遗落的。";
                                        break;
                                    case 2:
                                        imageUrl = "Scripts/1037公园/线索卡/post.png";
                                        res = "教学楼 搜证结果";
                                        img = "一摞邮票";
                                        itr = "邮票从集邮册中散落，堆在一起。";
                                        break;
                                    case 3:
                                        imageUrl = "Scripts/1037公园/线索卡/mini_clock.png";
                                        res = "教学楼 搜证结果";
                                        img = "迷你古董钟";
                                        itr = "迷你古董钟上没有密码锁，样式和大古董钟十分相像。";
                                        break;
                                }
                                StorageReference sref = FirebaseStorage.getInstance(URL_STORAGE).getReference();
                                GlideApp.with(getContext())
                                        .load(sref.child(imageUrl))
                                        .into(image);
                                result_title.setText(res);
                                image_title.setText(img);
                                intro.setText(itr);

                                card_dialog.setContentView(dialogView);
                                card_dialog.show();

                                ref.child("Games").child(gameIdx).child("card_cnt").setValue(cnt + 1);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_dialog.dismiss();
            }
        });
    }

    private void update_btn() {
        card_cnt -= 1;
        btnShowCardCnt.setText("行动点"+card_cnt+"/2");
        if (card_cnt == 0) {
            bigView.setOnLongClickListener(null);
        }
    }

}
