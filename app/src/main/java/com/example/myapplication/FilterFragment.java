package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.myapplication.models.Advertisement;
import com.example.myapplication.models.User;
import com.example.myapplication.ui.home.HomeFragment1;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    HomeFragment1 homeFragment1=new HomeFragment1();
    public View view;
    TextView subject;
    TextView city;
    boolean isSubject;
    Button button;
    EditText end_price;
    EditText start_price;
    ChipGroup chipGroup;
    ChipGroup additionally;
    ChipGroup format;
    ChipGroup gender;
    ChipGroup type_teacher;
    public FilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterFragment newInstance(String param1, String param2) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_filter,container,false);
        homeFragment1= (HomeFragment1) getParentFragmentManager().findFragmentByTag("1");
        button =view.findViewById(R.id.ok_button);
        start_price = (EditText) view.findViewById(R.id.start_price);
        end_price = (EditText) view.findViewById(R.id.end_price);
        CrystalRangeSeekbar seekBar = (CrystalRangeSeekbar) view.findViewById(R.id.rangeSeekbar5);
        seekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                start_price.setText(String.valueOf(minValue));
                end_price.setText(String.valueOf(maxValue));
            }
        });
        subject=view.findViewById(R.id.subject_filter);
        subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new ListFragment();
                Bundle bundle=new Bundle();
                bundle.putInt("selected",1);
                bundle.putInt("flag_fragment",2);
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().add(R.id.select_item_frame,fragment).commit();
                button.setVisibility(View.GONE);
                isSubject=true;

            }
        });
        city=(TextView) view.findViewById(R.id.location_filter);
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new ListFragment();
                Bundle bundle=new Bundle();
                bundle.putInt("selected",2);
                bundle.putInt("flag_fragment",2);
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().add(R.id.select_item_frame,fragment).commit();
                button.setVisibility(View.GONE);
                isSubject=false;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase db = FirebaseDatabase.getInstance("https://find-teacher-a7f26-default-rtdb.firebaseio.com");
                DatabaseReference ref = db.getReference("advertisements");
                setFilters(ref);
            }
        });
        chipGroup=(ChipGroup) view.findViewById(R.id.chipGroup2);
        additionally=(ChipGroup) view.findViewById(R.id.chipGroup4);
        format=(ChipGroup) view.findViewById(R.id.chipGroup);
        gender=(ChipGroup) view.findViewById(R.id.gender_chipg);
        type_teacher=(ChipGroup) view.findViewById(R.id.chipGroup3);
        return view;
    }
    public void setItem(String _item){
        if(isSubject){
            subject.setText(_item);
            button.setVisibility(View.VISIBLE);
        }
        else{
            button.setVisibility(View.VISIBLE);
            city.setText(_item);
        }

    }
    private void setFilters(DatabaseReference ref){
        List<Advertisement> advertisements=new ArrayList<>();
        List<User> users=new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setQuery_to_DB(dataSnapshot,advertisements,users);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setQuery_to_DB(DataSnapshot dataSnapshot,List<Advertisement> advertisements,List<User> users){
        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
            Advertisement advertisement=new Advertisement();
            advertisement=childDataSnapshot.getValue(Advertisement.class);
            advertisement=OrderBySubject(advertisement,subject.getText().toString());
            if(advertisement!=null){
                advertisements.add(advertisement);
            }
        }
        OrderByAdvFilters(advertisements);
        readData(new MyCallback() {
            @Override
            public void onCallback(DataSnapshot value) {
                for(DataSnapshot child: value.getChildren()){
                    Chip chip=gender.findViewById(gender.getCheckedChipId());
                    List<Integer> ids=type_teacher.getCheckedChipIds();
                    List<String> type_teacher_list=new ArrayList<>();
                    for(int id:ids){
                        Chip chip1=type_teacher.findViewById(id);
                        type_teacher_list.add(chip1.getText().toString());
                    }
                    User user=child.getValue(User.class);
                    for(int i=0;i<advertisements.size();i++){
                        Advertisement advertisement=advertisements.get(i);
                        if(user.getUid().equals(advertisement.getUid()) && !user.getCity().equals(city.getText().toString())
                        && !city.getText().toString().equals("Выберите город")){
                            advertisements.remove(advertisement);
                            continue;
                        }
                        if(user.getUid().equals(advertisement.getUid()) && !user.getGender().equals(chip.getText().toString())
                                && !chip.getText().toString().equals("Любой")){
                            advertisements.remove(advertisement);
                            continue;
                        }
                        if(type_teacher_list.size()!=0){
                            if(user.getUid().equals(advertisement.getUid()) && !type_teacher_list.contains(user.getType_education())){
                                advertisements.remove(advertisement);
                            }
                        }


                    }
                }
                Objects.requireNonNull(homeFragment1).setFilter(advertisements);
                getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim).remove(FilterFragment.this).commit();
            }


        });
    }
    private Advertisement OrderBySubject(Advertisement advertisement,String subject){
        if(subject.equals("Выберите предмет") || advertisement.getSubject().equals(subject)){
            return advertisement;
        }
        else{
            return null;
        }
    }
    private List<Advertisement> OrderByAdvFilters(List<Advertisement> advertisements){
        List<Integer> list_ids=chipGroup.getCheckedChipIds();
        List<String> type_students=new ArrayList<>();
        for(int id:list_ids){
            Chip chip=chipGroup.findViewById(id);
            type_students.add(chip.getText().toString());

        }
        List<Integer> ids=additionally.getCheckedChipIds();
        List<String> additionally_list=new ArrayList<>();
        for(Integer id:ids){
            Chip chip=additionally.findViewById(id);
            additionally_list.add(chip.getText().toString());
        }
        Chip chip=format.findViewById(format.getCheckedChipId());
        for(int i=0;i<advertisements.size();i++){
            Advertisement advertisement=advertisements.get(i);
            List<String>  user_type_students=advertisement.getType_students();
            boolean flag=true;
            if(advertisement.getPrice()<Integer.parseInt(start_price.getText().toString()) || advertisement.getPrice()>Integer.parseInt(end_price.getText().toString())){
                advertisements.remove(advertisement);
                i--;
                continue;
            }
            if(type_students.size()!=0) {
                for (int j = 0; j < user_type_students.size(); j++) {
                    if (type_students.contains(user_type_students.get(j))) {
                        flag = false;
                    }
                }
                if (flag) {
                    advertisements.remove(advertisement);
                    i--;
                    continue;
                }
            }
            flag=true;
            if(additionally_list.size()!=0){
                List<String> additionally_user=advertisement.getAdditionally();
                for(int j=0;j<additionally_user.size();j++){
                    if(additionally_list.contains(additionally_user.get(j))){
                        flag=false;
                    }
                }
                if(flag){
                    advertisements.remove(advertisement);
                    i--;
                    continue;
                }
            }
            if(!advertisement.getFormat().equals(chip.getText().toString()) && !chip.getText().toString().equals("Любой")){
                advertisements.remove(advertisement);
                i--;
            }
        }
        return advertisements;
    }
    public void readData(MyCallback myCallback) {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://find-teacher-a7f26-default-rtdb.firebaseio.com");

        db.getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myCallback.onCallback(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}

interface MyCallback {
    void onCallback(DataSnapshot value);
}