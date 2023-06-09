package com.competition.scriptkillingapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.model.Chat;
import com.competition.scriptkillingapp.model.GlideApp;
import com.competition.scriptkillingapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private static final String URL_STORAGE = "gs://scriptkillingapp.appspot.com";
    private static final String TAG = "ChatAdapter";
    final String URL = "https://scriptkillingapp-default-rtdb.asia-southeast1.firebasedatabase.app/";
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private ArrayList<Chat> chatMessages = new ArrayList<>();
    private String imageURL, myImageURL;
    private Context context;
    private FirebaseUser mCurrentUser;

    public ChatAdapter(Context context, ArrayList<Chat> chatMessages, String imageURL) {
        this.chatMessages = chatMessages;
        this.context = context;
        this.imageURL = imageURL;
        this.mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert this.mCurrentUser != null;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_right, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_left, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {

        Log.d(TAG, "myImageURL " + myImageURL);
        Chat chat = chatMessages.get(position);

        holder.mMessageShown.setText(chat.getMessage());

        if (getItemViewType(position) == MSG_TYPE_LEFT) {
            if (imageURL.equals("default")) {
                holder.mProfileImage.setImageResource(R.drawable.cat);
            } else if (getItemViewType(position) == MSG_TYPE_LEFT) {
                Glide.with(context).load(imageURL).into(holder.mProfileImage);
            }
        } else {
            if (imageURL.equals("default")) {
                holder.mProfileImage.setImageResource(R.mipmap.ic_launcher_round);
            } else {
                DatabaseReference ref = FirebaseDatabase.getInstance(URL)
                        .getReference("Users").child(this.mCurrentUser.getUid());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User my = snapshot.getValue(User.class);
                        Log.d(TAG, "my " + my.toString());
                        myImageURL = my.getImageURL();

                        StorageReference sref = FirebaseStorage.getInstance(URL_STORAGE).getReference();
                        GlideApp.with(context)
                                .load(sref.child(myImageURL))
                                .into(holder.mProfileImage);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mProfileImage;
        private TextView mMessageShown;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mProfileImage = itemView.findViewById(R.id.profile_image);
            mMessageShown = itemView.findViewById(R.id.message_shown);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).getSender().equals(mCurrentUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}