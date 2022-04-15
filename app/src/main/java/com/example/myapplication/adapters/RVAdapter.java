package com.example.myapplication.adapters;

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

import com.example.myapplication.R;
import com.example.myapplication.models.Teacher;
import com.example.myapplication.TeacherActivity;
import com.google.gson.Gson;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{
    private Context context;
    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_layout, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int i) {
        holder.name.setText(teachers.get(i).getName());
        holder.where_educate.setText(teachers.get(i).getWhere_educate());
        holder.photo.setImageResource(teachers.get(i).getPhoto());
        holder.type_education.setText(teachers.get(i).getType_education());
        holder.price.setText(String.valueOf(teachers.get(i).getPrice()));
        holder.subject.setText(teachers.get(i).getSubject());
        Teacher teacher=new Teacher(teachers.get(i).getName(),teachers.get(i).getType_education(),
                teachers.get(i).getWhere_educate(),teachers.get(i).getSubject(),teachers.get(i).getPrice(),teachers.get(i).getPhoto());
        Gson gson=new Gson();
        String json=gson.toJson(teacher);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, TeacherActivity.class);
                intent.putExtra("teacher",json);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView where_educate;
        TextView type_education;
        TextView price;
        TextView subject;
        ImageView photo;
        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            name = (TextView)itemView.findViewById(R.id.name);
            price = (TextView)itemView.findViewById(R.id.price);
            photo = (ImageView)itemView.findViewById(R.id.photo);
            where_educate=(TextView)itemView.findViewById(R.id.where_educate);
            type_education=(TextView)itemView.findViewById(R.id.type_education);
            subject=(TextView) itemView.findViewById(R.id.subject);
        }
    }

    List<Teacher> teachers;
    public RVAdapter(List<Teacher> teachers,Context context){
        this.teachers = teachers;
        this.context=context;
    }
}
