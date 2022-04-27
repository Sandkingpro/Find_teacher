package com.example.myapplication.ui.home;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.example.myapplication.models.Advertisement;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeFragment1 extends Fragment {
    private List<Advertisement> advertisements;
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
        initializeData();
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


    private void initializeData(){
        SweetAlertDialog pDialog;

        pDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF6C4A"));
        pDialog.setTitleText("Загрузка...");
        pDialog.setCancelable(false);
        pDialog.show();
        advertisements =new ArrayList<>();
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://find-teacher-a7f26-default-rtdb.firebaseio.com");
        DatabaseReference ref = db.getReference("advertisements");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                advertisements.clear();
                Advertisement advertisement=new Advertisement();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    advertisement=childDataSnapshot.getValue(Advertisement.class);
                    advertisements.add(advertisement);
                }
                pDialog.dismiss();
                pDialog.dismissWithAnimation();
                RVAdapter adapter = new RVAdapter(advertisements,getContext());
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
    View.OnClickListener filterClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            floatingActionButton.setVisibility(View.GONE);
            getParentFragmentManager().beginTransaction().add(R.id.filter_frame,new FilterFragment()).commit();
        }
    };

}