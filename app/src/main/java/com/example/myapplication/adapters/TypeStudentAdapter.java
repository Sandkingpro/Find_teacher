package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ListFragment;
import com.example.myapplication.R;

import java.util.List;

public class TypeStudentAdapter extends RecyclerView.Adapter<TypeStudentAdapter.PersonViewHolder>{
    private Context context;
    @NonNull
    @Override
    public TypeStudentAdapter.PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_students_layout, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        holder.element.setText(elements.get(position));
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView element;
        PersonViewHolder(View itemView) {
            super(itemView);
            element=(TextView) itemView.findViewById(R.id.type_student_textview);
        }
    }
    List<String> elements;
    public TypeStudentAdapter(List<String> elements, Context context){
        this.elements= elements;
        this.context=context;
    }
}
