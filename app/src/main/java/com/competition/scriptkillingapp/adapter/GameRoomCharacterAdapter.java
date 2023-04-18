package com.competition.scriptkillingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.model.Script;
import com.competition.scriptkillingapp.view.activity.GameStartActivity;

import java.util.ArrayList;

public class GameRoomCharacterAdapter extends RecyclerView.Adapter<GameRoomCharacterAdapter.ViewHolder> {

    private static final String TAG = "GameRoomCharacterAdapter";
    ArrayList<Script> scripts;

    private Context context;

    public GameRoomCharacterAdapter(Context context) {
        this.scripts = new ArrayList<>();
        this.context = context;
    }

    public void setScripts(ArrayList<Script> scripts) {
        this.scripts = scripts;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder ... ");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gameroom_character_intro1, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.d(TAG, "onBindViewHolder --> " + position);
        holder.txtName.setText(scripts.get(position).getName());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, scripts.get(position).getName() + " clicked", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GameStartActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scripts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private Button btnStart;

        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.game_room1_item_txtScriptName);
            parent = itemView.findViewById(R.id.gameroom_character_intro_parent);
            btnStart = itemView.findViewById(R.id.game_room1_item_button);
        }
    }
}
