package com.competition.scriptkillingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.model.User;
import com.competition.scriptkillingapp.view.activity.ChatActivity;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private static final String TAG = "UserAdapter";
    ArrayList<User> mUsers;

    private Context mContext;

    public UserAdapter(Context context) {
        this.mUsers = new ArrayList<>();
        this.mContext = context;
    }

    public void setUsers(ArrayList<User> users) {
        this.mUsers = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_list_item, parent, false);
        UserAdapter.ViewHolder holder = new UserAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        User currentUser = mUsers.get(position);
        holder.txtName.setText(currentUser.getUsername());
        if (currentUser.getImageURL().equals("default")) {
            holder.profileImage.setImageResource(R.mipmap.ic_launcher_round);
        } else {
            Glide.with(mContext).load(currentUser.getImageURL()).into(holder.profileImage);
        }
        holder.msgCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("userId", currentUser.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private ImageView profileImage;
        private CardView msgCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.message_list_item_title);
            profileImage = itemView.findViewById(R.id.message_list_item_image);
            msgCard = itemView.findViewById(R.id.message_list_item_parent);
        }
    }
}


