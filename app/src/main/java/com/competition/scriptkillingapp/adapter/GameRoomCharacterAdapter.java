package com.competition.scriptkillingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.model.GlideApp;
import com.competition.scriptkillingapp.model.MyAppGlideModule;
import com.competition.scriptkillingapp.model.Script;
import com.competition.scriptkillingapp.model.ScriptCharacter;
import com.competition.scriptkillingapp.view.activity.GameStartActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GameRoomCharacterAdapter extends RecyclerView.Adapter<GameRoomCharacterAdapter.ViewHolder> {

    private static final String URL_STORAGE = "gs://scriptkillingapp.appspot.com";
    private static final String TAG = "GameRoomCharacterAdapter";
    ArrayList<ScriptCharacter> scripts;

    private Context context;

    public GameRoomCharacterAdapter(Context context) {
        this.scripts = new ArrayList<>();
        this.context = context;
    }

    public void setScripts(ArrayList<ScriptCharacter> scripts) {
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

        StorageReference storageReference = FirebaseStorage.getInstance(URL_STORAGE).getReference();
        GlideApp.with(context)
                .load(storageReference.child("Scripts/1037公园/人物头像").child(scripts.get(position).getName() + ".png"))
                .into(holder.ivCharacterImage);
        holder.txtName.setText(scripts.get(position).getName());
        holder.txtIntro.setText(scripts.get(position).getIntro());
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
        private TextView txtName, txtIntro;
        private Button btnStart;
        private ImageView ivCharacterImage;
        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.game_room1_item_txtScriptName);
            txtIntro = itemView.findViewById(R.id.game_room1_item_txtScriptType);
            ivCharacterImage = itemView.findViewById(R.id.game_room1_item_image);

            parent = itemView.findViewById(R.id.gameroom_character_intro_parent);
            btnStart = itemView.findViewById(R.id.game_room1_item_button);
        }
    }
}
