package com.competition.scriptkillingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.model.GlideApp;
import com.competition.scriptkillingapp.model.SearchCard;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SearchCardAdapter extends RecyclerView.Adapter<SearchCardAdapter.ViewHolder> {

    private static final String URL_STORAGE = "gs://scriptkillingapp.appspot.com";
    private Context context;
    private ArrayList<SearchCard> searchCards;
    private StorageReference storageReference = FirebaseStorage.getInstance(URL_STORAGE).getReference();

    public SearchCardAdapter(Context context) {
        this.context = context;
    }

    public void setSearchCards(ArrayList<SearchCard> searchCards) {
        this.searchCards = searchCards;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_card_clue, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title_down.setText(searchCards.get(position).getTitle_down());
        holder.title_up.setText(searchCards.get(position).getTitle_up());
        holder.intro.setText(searchCards.get(position).getInfo());
        GlideApp.with(context)
                .load(storageReference.child(searchCards.get(position).getImageUrl()))
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return searchCards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title_up, title_down, intro;
        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title_up = itemView.findViewById(R.id.search_clue_searchtitle);
            title_down = itemView.findViewById(R.id.search_clue_imagetitle);
            intro = itemView.findViewById(R.id.search_clue_intro);
            image = itemView.findViewById(R.id.search_clue_image);
        }
    }
}
