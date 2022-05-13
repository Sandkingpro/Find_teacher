package com.example.myapplication.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;
import com.example.myapplication.models.Review;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class CertificateAdapter extends RecyclerView.Adapter<CertificateAdapter.PersonViewHolder>{
    private Context context;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    @NonNull
    @Override
    public CertificateAdapter.PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.certificate_layout, parent, false);
        CertificateAdapter.PersonViewHolder pvh = new CertificateAdapter.PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CertificateAdapter.PersonViewHolder holder, int position) {

        if(documents.get(position).startsWith("gs")){
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl(documents.get(position));
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(holder.itemView).load(uri).
                            thumbnail( 0.5f )
                            .diskCacheStrategy( DiskCacheStrategy.ALL )
                            .priority(Priority.IMMEDIATE)
                            .into(holder.certificate);
                }
            });
        }
        else {
            Glide.with(holder.itemView).load(Uri.parse(documents.get(position))).
                    thumbnail( 0.5f )
                    .override(200,200)
                    .diskCacheStrategy( DiskCacheStrategy.ALL )
                    .priority(Priority.IMMEDIATE)
                    .into(holder.certificate);
        }

    }

    @Override
    public int getItemCount() {
        return documents.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        ImageView certificate;
        PersonViewHolder(View itemView) {
            super(itemView);
            certificate=(ImageView) itemView.findViewById(R.id.certificate_image);
        }
    }
    List<String> documents;
    public CertificateAdapter(List<String> documents,Context context){
        this.documents = documents;
        this.context=context;
    }
}
