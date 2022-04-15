package com.example.myapplication.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.FilterFragment;
import com.example.myapplication.R;
import com.example.myapplication.adapters.RVAdapter;
import com.example.myapplication.models.Teacher;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment1 extends Fragment {
    private List<Teacher> teachers;
    private HomeViewModel homeViewModel;
    public View root;
    int currentVisiblePosition = 0;
    private RecyclerView rv;
    private FloatingActionButton floatingActionButton;
    private FragmentHomeBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        rv = (RecyclerView)root.findViewById(R.id.recycleview);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        teachers=initializeData();
        RVAdapter adapter = new RVAdapter(teachers,getContext());
        rv.setAdapter(adapter);
        floatingActionButton=(FloatingActionButton)root.findViewById(R.id.filter_button);
        floatingActionButton.setColorFilter(Color.argb(255, 255, 255, 255));
        floatingActionButton.setOnClickListener(filterClickListener);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private List<Teacher> initializeData(){
        teachers=new ArrayList<>();
        teachers.add(new Teacher("Владислав Сальников","Формат занятий: очно","Частный преподаватель","Математика",700,R.drawable.vlad_teacher));
        teachers.add(new Teacher("Владислав Сальников","Формат занятий: очно","Частный преподаватель","Математика",700,R.drawable.vlad_teacher));
        teachers.add(new Teacher("Владислав Сальников","Формат занятий: очно","Частный преподаватель","Математика",700,R.drawable.vlad_teacher));
        teachers.add(new Teacher("Владислав Сальников","Формат занятий: очно","Частный преподаватель","Математика",700,R.drawable.vlad_teacher));
        teachers.add(new Teacher("Владислав Сальников","Формат занятий: очно","Частный преподаватель","Математика",700,R.drawable.vlad_teacher));
        return teachers;
    }
    View.OnClickListener filterClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            floatingActionButton.setVisibility(View.GONE);
            getParentFragmentManager().beginTransaction().add(R.id.filter_frame,new FilterFragment()).commit();
        }
    };

}