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
import com.competition.scriptkillingapp.model.Chat;
import com.competition.scriptkillingapp.model.ScriptTitle;
import com.competition.scriptkillingapp.view.activity.ChatActivity;
import com.competition.scriptkillingapp.view.activity.GameStartActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ScriptAdapter extends RecyclerView.Adapter<ScriptAdapter.ViewHolder> {
    final String URL = "https://scriptkillingapp-default-rtdb.asia-southeast1.firebasedatabase.app/";

    private static final String TAG = "ScriptRecViewAdapter";
    ArrayList<ScriptTitle> scriptTitles;

    private Context context;

    public ScriptAdapter(Context context) {
        this.scriptTitles = new ArrayList<>();
        this.context = context;
    }

    public void setScripts(ArrayList<ScriptTitle> scriptTitles) {
        this.scriptTitles = scriptTitles;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder ... ");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.script_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.d(TAG, "onBindViewHolder --> " + position);
        holder.txtName.setText(scriptTitles.get(position).getName());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, scriptTitles.get(position).getName() + " clicked", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance(URL).getReference("Games").push();
                Intent intent = new Intent(context, GameStartActivity.class);
                intent.putExtra("gameIdx", ref.getKey());
                intent.putExtra("scriptName", "1037公园");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scriptTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private Button btnStart;

        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.script_list_item_txtScriptName);
            parent = itemView.findViewById(R.id.script_list_item_parent);
            btnStart = itemView.findViewById(R.id.script_list_item_open_btn);
        }
    }
}
