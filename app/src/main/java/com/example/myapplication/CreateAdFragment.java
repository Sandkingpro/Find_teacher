package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.models.Advertisement;
import com.example.myapplication.models.User;
import com.example.myapplication.ui.home.HomeFragment1;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.jsoup.select.Evaluator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateAdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateAdFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button create;
    TextView select_subject;
    ChipGroup chipGroup_format;
    ChipGroup type_students;
    ChipGroup additionally;
    EditText price;
    EditText where_educate;
    List<Integer> checked_type_students=new ArrayList<>();
    List<Integer> checked_additionally=new ArrayList<>();
    boolean flag=false;
    private FirebaseAuth mAuth;
    Bundle bundle;
    FirebaseDatabase db;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateAdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateAdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateAdFragment newInstance(String param1, String param2) {
        CreateAdFragment fragment = new CreateAdFragment();
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
        View root=inflater.inflate(R.layout.fragment_create_ad, container, false);
        db = FirebaseDatabase.getInstance("https://find-teacher-a7f26-default-rtdb.firebaseio.com");
        bundle=this.getArguments();
        chipGroup_format=(ChipGroup)root.findViewById(R.id.chipGroup);
        price=(EditText)root.findViewById(R.id.start_price2);
        type_students=(ChipGroup)root.findViewById(R.id.chipGroup2);
        additionally=(ChipGroup)root.findViewById(R.id.chipGroup4);
        mAuth = FirebaseAuth.getInstance();

        create=root.findViewById(R.id.ok_button6);
        if(bundle.getInt("?edit")==1){
            getAd_info();
            create.setText("Изменить");
        }
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checked_additionally=additionally.getCheckedChipIds();
                checked_type_students=type_students.getCheckedChipIds();
                if(bundle.getInt("?edit")==0){
                    if(checkFields()) {
                        create_edit_ad();
                    }
                    else{
                        Toast.makeText(getContext(),"Заполните необходимые поля",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    create_edit_ad();

                }
            }
        });
        select_subject=(TextView) root.findViewById(R.id.select_subject);
        select_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new ListFragment();
                Bundle bundle=new Bundle();
                bundle.putInt("selected",1);
                bundle.putInt("flag_fragment",1);
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().add(R.id.select_subject_frame,fragment).commit();
            }
        });
        return root;
    }
    public void setItem(String subject){
        select_subject.setText(subject);
    }
    private boolean checkFields(){
        return !select_subject.getText().toString().equals("Выберите предмет")
                && !price.getText().toString().equals("")
                && checked_type_students.size() != 0;
    }
    private void create_edit_ad(){
        Chip selected_format=chipGroup_format.findViewById(chipGroup_format.getCheckedChipId());
        List<String> type_students_list=new ArrayList<>();
        for(Integer id:checked_type_students){
            Chip chip=type_students.findViewById(id);
            type_students_list.add(chip.getText().toString());
        }
        List<String> additionally_map=new ArrayList<>();
        for (Integer id:checked_additionally){
            Chip chip=additionally.findViewById(id);
            if(chip.getText().toString().equals("ЕГЭ")){
                additionally_map.add("ЕГЭ");
            }
            if(chip.getText().toString().equals("ОГЭ")){
                additionally_map.add("ОГЭ");
            }
        }
        DatabaseReference ref_ad = db.getReference("advertisements");
        Advertisement advertisement=new Advertisement(mAuth.getCurrentUser().getUid(),select_subject.getText().toString(),Integer.parseInt(price.getText().toString()),selected_format.getText().toString(),additionally_map,type_students_list);
        if(bundle.getInt("?edit")==0){
            ref_ad.child(Objects.requireNonNull(mAuth.getCurrentUser().getUid())).setValue(advertisement);
        }
        else{
            ref_ad.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Advertisement advertisement1=dataSnapshot.getValue(Advertisement.class);
                    if(advertisement1.getPrice()!=advertisement.getPrice()){
                        flag=true;
                    }
                    if(!advertisement1.getSubject().equals(advertisement.getSubject())){
                        flag=true;
                    }
                    if(!advertisement1.getFormat().equals(advertisement.getFormat())){
                        flag=true;
                    }
                    if(advertisement1.getType_students().size()!=advertisement.getType_students().size()){
                        flag=true;
                    }
                    if(advertisement1.getType_students().size()==advertisement.getType_students().size()){
                        for(int i=0;i<advertisement1.getType_students().size();i++){
                            if(!advertisement1.getType_students().get(i).equals(advertisement.getType_students().get(i))){
                                flag=true;
                            }
                        }
                    }
                    if(advertisement1.getAdditionally().size()!=advertisement.getAdditionally().size()){
                        flag=true;
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
                boolean equalMaps(Map<String,Boolean>m1, Map<String,Boolean>m2) {
                    return m1.get("ОГЭ") == m2.get("ОГЭ") && m1.get("ЕГЭ") == m2.get("ЕГЭ");
                }
            });
            ref_ad.child(mAuth.getCurrentUser().getUid()).setValue(advertisement);
        }
        getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim).remove(CreateAdFragment.this).commit();
    }
    private void getAd_info(){
        DatabaseReference ref_ad = db.getReference("advertisements");
        ref_ad.child(Objects.requireNonNull(mAuth.getCurrentUser().getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Advertisement advertisement=dataSnapshot.getValue(Advertisement.class);
                setFormat(Objects.requireNonNull(advertisement));
                setPrice(advertisement);
                setSubject(advertisement);
                setType_student(advertisement);
                setAdditionally(advertisement);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            private void setFormat(Advertisement advertisement){
                Chip chip1=chipGroup_format.findViewById(R.id.chip4);
                Chip chip2=chipGroup_format.findViewById(R.id.chip5);
                Chip chip3=chipGroup_format.findViewById(R.id.chip6);
                if(chip1.getText().toString().equals(advertisement.getFormat())){
                    chip1.setChecked(true);
                }
                if(chip2.getText().toString().equals(advertisement.getFormat())){
                    chip2.setChecked(true);
                }
                if(chip3.getText().toString().equals(advertisement.getFormat())){
                    chip3.setChecked(true);
                }
            }
            private void setPrice(Advertisement advertisement){
                price.setText(String.valueOf(advertisement.getPrice()));
            }
            private void setType_student(Advertisement advertisement){
                Chip chip1=type_students.findViewById(R.id.chip11);
                Chip chip2=type_students.findViewById(R.id.chip10);
                Chip chip3=type_students.findViewById(R.id.chip9);
                Chip chip4=type_students.findViewById(R.id.chip8);
                for(String item:advertisement.getType_students()){
                    if(item.equals(chip1.getText().toString())){
                        chip1.setChecked(true);
                    }
                    if(item.equals(chip2.getText().toString())){
                        chip2.setChecked(true);
                    }
                    if(item.equals(chip3.getText().toString())){
                        chip3.setChecked(true);
                    }
                    if(item.equals(chip4.getText().toString())){
                        chip4.setChecked(true);
                    }
                }
            }
            private void setSubject(Advertisement advertisement){
                select_subject.setText(advertisement.getSubject());
            }
            private void setAdditionally(Advertisement advertisement){
                Chip chip1=additionally.findViewById(R.id.chip17);
                Chip chip2=additionally.findViewById(R.id.chip16);
                for(String item:advertisement.getAdditionally()){
                    if(chip1.getText().toString().equals(item)){
                        chip1.setChecked(true);
                    }
                    if(chip2.getText().toString().equals(item)){
                        chip2.setChecked(true);
                    }
                }
            }
        });
    }
}