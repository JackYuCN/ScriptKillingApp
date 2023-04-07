package com.competition.scriptkillingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.model.Script;

import java.util.ArrayList;

public class ScriptRecViewAdapter extends RecyclerView.Adapter<ScriptRecViewAdapter.ViewHolder> {

    private static final String TAG = "ScriptRecViewAdapter";
    ArrayList<Script> scripts;

    private Context context;

    public ScriptRecViewAdapter(Context context) {
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
                .inflate(R.layout.script_list_item, parent, false);
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
    }

    @Override
    public int getItemCount() {
        return scripts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;

        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.script_list_item_txtScriptName);
            parent = itemView.findViewById(R.id.script_list_item_parent);
        }
    }
}
