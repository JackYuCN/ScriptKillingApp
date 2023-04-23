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
import com.competition.scriptkillingapp.model.RoomSetting;
import com.competition.scriptkillingapp.model.Script;
import com.competition.scriptkillingapp.view.activity.GameStartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ScriptAdapter extends RecyclerView.Adapter<ScriptAdapter.ViewHolder> {
    final String URL = "https://scriptkillingapp-default-rtdb.asia-southeast1.firebasedatabase.app/";

    private static final String TAG = "ScriptRecViewAdapter";
    ArrayList<Script> script_info;
    ArrayList<RoomSetting> room_settings;

    private Context context;

    public ScriptAdapter(Context context) {
        this.script_info = new ArrayList<>();
        this.context = context;
    }

    public void setScriptInfo(ArrayList<Script> scriptTitles) {
        this.script_info = scriptTitles;
        notifyDataSetChanged();
    }

    public void setRoomSettings(ArrayList<RoomSetting> room_settings) {
        this.room_settings = room_settings;
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
        // TODO: 在这里修改首页选择小卡片
        Log.d(TAG, "onBindViewHolder --> " + position);
        holder.txtName.setText(script_info.get(position).getName());
        holder.txtType.setText("类型：" + script_info.get(position).getType());
        holder.txtReview.setText("JackYu：后面反转真的很牛！前期破冰也很自然，强推！");
        holder.txtRequire.setText("人数：等" + script_info.get(position).getPeople() + "人");
        holder.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gameRef = room_settings.get(position).getGameRef();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference tmp = FirebaseDatabase.getInstance(URL).getReference();
                tmp.child("Users").child(user.getUid()).child("playing")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean playing = snapshot.getValue(Boolean.class);
                                if (playing == true) {
                                    Toast.makeText(context, "请先完成正在进行的剧本哦", Toast.LENGTH_SHORT).show();
                                } else if (room_settings.get(position).getPlayers().size() >= script_info.get(position).getPeople()) {
                                    Toast.makeText(context, "房间已满！", Toast.LENGTH_SHORT).show();
                                } else {
                                    DatabaseReference tmp_ref = FirebaseDatabase.getInstance(URL).getReference();
                                    if (gameRef.equals("-1")) {
                                        Log.d(TAG, "GameRef is null");
                                        tmp_ref = tmp_ref.child("Games").push();
                                    } else {
                                        tmp_ref = tmp_ref.child("Games").child(gameRef);
                                    }
                                    tmp_ref.child("act_as").child(user.getUid()).setValue("null");
                                    tmp_ref.child("max_cnt").setValue(script_info.get(position).getPeople());
                                    tmp_ref.child("cnt").setValue(0);
                                    tmp_ref.child("stages").setValue(0);
                                    Intent intent = new Intent(context, GameStartActivity.class);
                                    intent.putExtra("gameIdx", tmp_ref.getKey());
                                    intent.putExtra("scriptName", script_info.get(position).getName());
                                    intent.putExtra("roomIdx", room_settings.get(position).getRoomRef());
                                    context.startActivity(intent);
                                }
                                tmp.removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return script_info.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtType, txtRequire, txtReview;
        private Button btnStart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.script_list_item_txtScriptName);
            txtType = itemView.findViewById(R.id.script_list_item_txtScriptType);
            txtRequire = itemView.findViewById(R.id.script_list_item_txtScriptPeople);
            txtReview = itemView.findViewById(R.id.script_list_item_txtHotReviews);
            btnStart = itemView.findViewById(R.id.script_list_item_open_btn);
        }
    }
}
