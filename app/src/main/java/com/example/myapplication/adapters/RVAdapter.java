package com.example.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;
import com.example.myapplication.models.Advertisement;
import com.example.myapplication.TeacherActivity;
import com.example.myapplication.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{
    private Context context;
    private FirebaseAuth mAuth;
    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_layout, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int i) {
        mAuth=FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://find-teacher-a7f26-default-rtdb.firebaseio.com");
        DatabaseReference ref = db.getReference("users")
                .child(advertisements.get(i).getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                holder.name.setText(Objects.requireNonNull(user).getName());
                holder.city.setText(user.getCity());
                if(!user.getPhoto().equals("none")){
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReferenceFromUrl(user.getPhoto());
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(holder.itemView).load(uri).
                                    thumbnail( 0.5f )
                                    .override( 200, 200 )
                                    .placeholder(R.drawable.ic_avatar)
                                    .diskCacheStrategy( DiskCacheStrategy.ALL )
                                    .fitCenter()
                                    .priority(Priority.IMMEDIATE)
                                    .into(holder.photo);
                        }
                    });
                }

                holder.ratingBar.setRating((float) user.getRating());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.format.setText(advertisements.get(i).getFormat());
        holder.price.setText(String.valueOf(advertisements.get(i).getPrice()));
        holder.subject.setText(advertisements.get(i).getSubject());
        Advertisement advertisement =new Advertisement(advertisements.get(i).getUid(),
                advertisements.get(i).getSubject(),advertisements.get(i).getPrice(),advertisements.get(i).getFormat(),advertisements.get(i).getAdditionally(),advertisements.get(i).getType_students());
        Gson gson=new Gson();
        String json=gson.toJson(advertisement);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, TeacherActivity.class);
                intent.putExtra("advertisement",json);
                intent.putExtra("id",advertisement.getUid());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return advertisements.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView city;
        TextView format;
        TextView price;
        TextView subject;
        CircleImageView photo;
        RatingBar ratingBar;
        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            name = (TextView)itemView.findViewById(R.id.name);
            price = (TextView)itemView.findViewById(R.id.price);
            photo = (CircleImageView)itemView.findViewById(R.id.photo);
            city=(TextView)itemView.findViewById(R.id.city);
            format=(TextView)itemView.findViewById(R.id.format);
            subject=(TextView) itemView.findViewById(R.id.subject);
            ratingBar=(RatingBar) itemView.findViewById(R.id.ratingBar_adv);
        }
    }

    List<Advertisement> advertisements;
    public RVAdapter(List<Advertisement> advertisements, Context context){
        this.advertisements = advertisements;
        this.context=context;
    }
    public void clear() {
        int size = advertisements.size();
        advertisements.clear();
        notifyItemRangeRemoved(0, size);
    }
}
