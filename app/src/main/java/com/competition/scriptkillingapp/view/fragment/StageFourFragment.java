package com.competition.scriptkillingapp.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.adapter.ClueCardAdapter;
import com.competition.scriptkillingapp.model.ClueCard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StageFourFragment extends Fragment {

    private static final String TAG = "Stage 4";
    final String URL = "https://scriptkillingapp-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private View view;
    private RecyclerView clueCardRecView;
    private String gameIdx;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stage4, container, false);

        gameIdx = getArguments().getString("gameIdx");

        clueCardRecView = view.findViewById(R.id.clue_card_recview);
        clueCardRecView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        clueCardRecView.setHasFixedSize(true);

        ClueCardAdapter clueCardAdapter = new ClueCardAdapter(getContext());
        ArrayList<ClueCard> searchCards = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance(URL).getReference();
        ref.child("Games/" + gameIdx + "/card_cnt")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        searchCards.clear();

                        // TODO: 第四阶段卡片修改
                        int card_cnt = snapshot.getValue(Integer.class);
                        if (card_cnt == 1) {
                            searchCards.add(new ClueCard("图书馆", "共1条线索", "Scripts/1037公园/线索卡/tsg.png"));
                        } else if (card_cnt == 2) {
                            searchCards.add(new ClueCard("图书馆", "共2条线索", "Scripts/1037公园/线索卡/tsg.png"));
                        } else if (card_cnt == 3) {
                            searchCards.add(new ClueCard("教学楼", "共1条线索", "Scripts/1037公园/线索卡/jsl.png"));
                            searchCards.add(new ClueCard("图书馆", "共2条线索", "Scripts/1037公园/线索卡/tsg.png"));
                        } else {
                            searchCards.add(new ClueCard("教学楼", "共2条线索", "Scripts/1037公园/线索卡/jxl.png"));
                            searchCards.add(new ClueCard("图书馆", "共2条线索", "Scripts/1037公园/线索卡/tsg.png"));
                        }
                        clueCardAdapter.setClueCards(searchCards);
                        clueCardRecView.setAdapter(clueCardAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return view;
    }
}
