package com.competition.scriptkillingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.model.GlideApp;
import com.competition.scriptkillingapp.model.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GameRoomProfileAdapter extends RecyclerView.Adapter<GameRoomProfileAdapter.ViewHolder> {

    final String URL = "https://scriptkillingapp-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private static final String URL_STORAGE = "gs://scriptkillingapp.appspot.com";
    private static final String TAG = "ProfileCheck";
    ArrayList<Profile> profiles = new ArrayList<>();

    private Context context;

    public GameRoomProfileAdapter(Context context) {
        this.profiles = new ArrayList<>();
        this.context = context;
    }

    public void setProfilesByUid(ArrayList<String> UidArray) {
        DatabaseReference ref = FirebaseDatabase.getInstance(URL).getReference();
        ref.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profiles.clear();
                boolean flag = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (flag) break;
                    for (String Uid : UidArray) {
                        if (Uid.equals(dataSnapshot.getKey())) {
                            Profile p = dataSnapshot.getValue(Profile.class);
                            profiles.add(p);
                            if (profiles.size() == UidArray.size()) {
                                flag = true;
                                break;
                            }
                        }
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void setProfiles(ArrayList<Profile> profiles) {
        Log.d(TAG, "setProfiles: " + profiles.toString());
        this.profiles = profiles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder ... ");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gameroom_profile, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtName.setText(profiles.get(position).getUsername());
        String imagePath = profiles.get(position).getImageURL();

        Log.d(TAG, "onBindViewHolder --> " + position + " imagePath ---> " + imagePath);
        if (imagePath.equals("default")) {
            holder.ivCharacterImage.setImageResource(R.mipmap.ic_launcher_round);
        } else {
            StorageReference storageReference = FirebaseStorage.getInstance(URL_STORAGE).getReference();
            GlideApp.with(context)
                    .load(storageReference.child(imagePath))
                    .into(holder.ivCharacterImage);
        }
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private ImageView ivCharacterImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.game_room_username);
            ivCharacterImage = itemView.findViewById(R.id.game_room_imageprofile);
        }
    }
}
