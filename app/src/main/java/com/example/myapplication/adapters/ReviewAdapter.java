package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.PersonViewHolder>{
    private Context context;
    @NonNull
    @Override
    public ReviewAdapter.PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews, parent, false);
        ReviewAdapter.PersonViewHolder pvh = new ReviewAdapter.PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.PersonViewHolder holder, int position) {

        holder.name.setText(reviews.get(position).getName());
        holder.commentary.setText(reviews.get(position).getCommentary());
        holder.ratingBar.setRating((float) reviews.get(position).getRating());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView commentary;
        RatingBar ratingBar;
        PersonViewHolder(View itemView) {
            super(itemView);
            name=(TextView) itemView.findViewById(R.id.review_name);
            commentary=(TextView) itemView.findViewById(R.id.review_text);
            ratingBar=(RatingBar) itemView.findViewById(R.id.ratingBar);
        }
    }
    List<Review> reviews;
    public ReviewAdapter(List<Review> reviews,Context context){
        this.reviews = reviews;
        this.context=context;
    }
}
