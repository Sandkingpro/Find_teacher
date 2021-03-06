package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.FilterFragment;
import com.example.myapplication.ListFragment;
import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity;

import java.util.List;
import java.util.Objects;

public class FilterListAdapter extends RecyclerView.Adapter<FilterListAdapter.PersonViewHolder>{
    private Context context;
    private ListFragment fragment=new ListFragment();
    private RegisterActivity activity;
    @NonNull
    @Override
    public FilterListAdapter.PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_list, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        holder.element.setText(elements.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.getItem(holder.element.getText().toString());
                fragment.getParentFragmentManager().beginTransaction().remove(fragment).commit();
            }
        });
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
            element=(TextView) itemView.findViewById(R.id.element);
        }
    }
    List<String> elements;
    public FilterListAdapter(List<String> elements, Context context,ListFragment fragment){
        this.elements= elements;
        this.context=context;
        this.fragment=fragment;
    }
}
