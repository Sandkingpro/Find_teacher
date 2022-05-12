package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseDatabase db;
    FirebaseAuth mAuth;
    boolean flag=true;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Bundle bundle;
    ChipGroup status_teacher;
    TextView title_status;
    CircleImageView avatar;
    EditText about_me;
    TextView city;
    User new_user=new User();
    Button edit_avatar;
    Button edit_profile;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath=null;
    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
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
        mAuth= FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://find-teacher-a7f26-default-rtdb.firebaseio.com");
        View root=inflater.inflate(R.layout.fragment_edit_profile, container, false);
        status_teacher=(ChipGroup) root.findViewById(R.id.chipGroup3);
        title_status=(TextView)root.findViewById(R.id.textView29);
        avatar=root.findViewById(R.id.imageView10);
        edit_avatar=(Button)root.findViewById(R.id.edit_avatar);
        city=(TextView)root.findViewById(R.id.edit_city);
        about_me=(EditText)root.findViewById(R.id.edit_about_me);
        edit_profile=(Button)root.findViewById(R.id.edit_profile_button);
        bundle=this.getArguments();
        checkType();
        getSettings();
        edit_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new ListFragment();
                Bundle bundle=new Bundle();
                bundle.putInt("selected",2);
                bundle.putInt("flag_fragment",3);
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().add(R.id.select_edit_city_frame,fragment).commit();
                edit_profile.setVisibility(View.GONE);

            }
        });
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSettings();
            }
        });
        return root;
    }
    private void checkType(){
        if(bundle.getInt("?type")==0){
            status_teacher.setVisibility(View.GONE);
            title_status.setVisibility(View.GONE);
        }
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            if(filePath!=null){
                avatar.setImageURI(filePath);
            }

        }
    }
    private void getSettings(){
        DatabaseReference ref = db.getReference("users")
                .child(mAuth.getCurrentUser().getUid());
        SweetAlertDialog pDialog;
        pDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF6C4A"));
        pDialog.setTitleText("Загрузка...");
        pDialog.setCancelable(false);
        pDialog.show();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                if(dataSnapshot.hasChild("about_me")){
                    about_me.setText(Objects.requireNonNull(user).getAbout_me());
                }
                else{
                    about_me.setText("Нет информации");
                }
                city.setText(Objects.requireNonNull(user).getCity());
                if(!user.getPhoto().equals("none")){
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://find-teacher-a7f26.appspot.com/").child("avatars/"+mAuth.getCurrentUser().getUid());
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(getActivity().getBaseContext()).load(uri).
                                    thumbnail( 0.5f )
                                    .override( 200, 200 )
                                    .placeholder(R.drawable.ic_avatar)
                                    .diskCacheStrategy( DiskCacheStrategy.ALL )
                                    .fitCenter()
                                    .priority(Priority.IMMEDIATE)
                                    .into(avatar);
                        }
                    });
                }
                if(user.getType()==1){
                    Chip chip1=status_teacher.findViewById(R.id.chip15);
                    Chip chip2=status_teacher.findViewById(R.id.chip14);
                    Chip chip3=status_teacher.findViewById(R.id.chip13);
                    Chip chip4=status_teacher.findViewById(R.id.chip12);
                    if(user.getType_education().equals(chip1.getText().toString())){
                        chip1.setChecked(true);
                    }
                    if(user.getType_education().equals(chip2.getText().toString())){
                        chip2.setChecked(true);
                    }
                    if(user.getType_education().equals(chip3.getText().toString())){
                        chip3.setChecked(true);
                    }
                    if(user.getType_education().equals(chip4.getText().toString())){
                        chip4.setChecked(true);
                    }
                }
                pDialog.dismiss();
                pDialog.dismissWithAnimation();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setSettings(){
        DatabaseReference ref = db.getReference("users")
                .child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                if(!user.getCity().equals(city.getText().toString())){
                    user.setCity(city.getText().toString());
                }
                if(dataSnapshot.hasChild("about_me")){
                    if(!user.getAbout_me().equals(about_me.getText().toString())){
                        user.setAbout_me(about_me.getText().toString());
                    }
                }
                if(!dataSnapshot.hasChild("about_me")){
                    if(!about_me.getText().toString().equals("Нет информации")){
                        user.setAbout_me(about_me.getText().toString());
                    }
                }
                uploadImage();
                if(filePath!=null){
                    user.setPhoto("gs://find-teacher-a7f26.appspot.com/avatars/"+mAuth.getCurrentUser().getUid());
                }
                if(user.getType()==1){
                    Chip chip=status_teacher.findViewById(status_teacher.getCheckedChipId());
                    if(!chip.getText().toString().equals(user.getType_education())){
                        user.setType_education(chip.getText().toString());
                    }
                }
                ref.setValue(user);
                if(filePath==null){
                    getParentFragmentManager().beginTransaction().remove(EditProfileFragment.this).commit();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void setItem(String _item){
        city.setText(_item);

    }
    public User newUser(User user){
        return user;
    }
    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            FirebaseStorage storage;
            StorageReference storageReference;
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReferenceFromUrl("gs://find-teacher-a7f26.appspot.com/");
            StorageReference ref = storageReference.child("avatars/"+ Objects.requireNonNull(mAuth.getCurrentUser()).getUid().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            getParentFragmentManager().beginTransaction().remove(EditProfileFragment.this).commit();
                            MainActivity activity= (MainActivity) getActivity();
                            Objects.requireNonNull(activity).re_initialized();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });



        }

    }
}