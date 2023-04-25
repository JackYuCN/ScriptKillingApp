package com.competition.scriptkillingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.model.ClueCard;
import com.competition.scriptkillingapp.model.GlideApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ClueCardAdapter extends RecyclerView.Adapter<ClueCardAdapter.ViewHolder> {
    private static final String URL_STORAGE = "gs://scriptkillingapp.appspot.com";
    private Context context;
    private ArrayList<ClueCard> clueCards;
    private StorageReference storageReference = FirebaseStorage.getInstance(URL_STORAGE).getReference();

    public ClueCardAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.clue_card, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title_up.setText(clueCards.get(position).getTitle1());
        holder.title_down.setText(clueCards.get(position).getTitle2());
        GlideApp.with(context)
                .load(storageReference.child(clueCards.get(position).getImageUrl()))
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return clueCards.size();
    }

    public void setClueCards(ArrayList<ClueCard> clueCards) {
        this.clueCards = clueCards;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title_up, title_down;
        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title_up = itemView.findViewById(R.id.clue_card_title1);
            title_down = itemView.findViewById(R.id.clue_card_title2);
            image = itemView.findViewById(R.id.clue_card_image);
        }
    }
}
