package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorJoiner;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.models.Person;
import com.example.myapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    private String image_uri;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath=null;
    TextView choose_img;
    Button register;
    EditText login;
    EditText password;
    CheckBox checkBox;
    EditText phone;
    EditText name_surname;
    ChipGroup chipGroup;
    ChipGroup chipGroup_gender;
    SharedPreferences sharedPreferences;
    ArrayList<Person> list=new ArrayList<>();
    Gson gson=new Gson();
    boolean flag=false;
    private static final String SHARED_PREF_NAME ="my_shared_preff";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://find-teacher-a7f26.appspot.com/");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                } else {
                    // User is signed out

                }

            }
        };
        setContentView(R.layout.activity_register);
        checkBox=(CheckBox)findViewById(R.id.check_type);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        login=(EditText) findViewById(R.id.login2);
        password=(EditText) findViewById(R.id.password2);
        register=(Button) findViewById(R.id.button3);
        choose_img=(TextView) findViewById(R.id.chooseimg);
        name_surname=(EditText) findViewById(R.id.name_surname);
        phone=(EditText)findViewById(R.id.editTextPhone);
        chipGroup=(ChipGroup) findViewById(R.id.chipGroup3);
        chipGroup_gender=(ChipGroup)findViewById(R.id.chipGroup5);
        choose_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView=(TextView)findViewById(R.id.textView28);
                if(checkBox.isChecked()){
                    chipGroup.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
                else{
                    chipGroup.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFields()){
                    registration(login.getText().toString(),password.getText().toString());
                    login.setText("");
                    password.setText("");
                }
                else{
                    Toast.makeText(RegisterActivity.this,"Вы не заполнили необходимые поля",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void registration (String email , String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(RegisterActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                    uploadImage();
                    signin(email,password);
                    FirebaseAuth.getInstance().signOut();
                }
                else
                    Toast.makeText(RegisterActivity.this, "Регистрация провалена", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void signin(String email , String password)
    {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    if(filePath!=null){
                        image_uri="gs://find-teacher-a7f26.appspot.com/avatars/"+mAuth.getCurrentUser().getUid();
                    }
                    else{
                        image_uri="none";
                    }
                    List<String> documents=new ArrayList<>();
                    FirebaseDatabase db = FirebaseDatabase.getInstance("https://find-teacher-a7f26-default-rtdb.firebaseio.com");
                    Chip selected_gender =(Chip)findViewById(chipGroup_gender.getCheckedChipId());
                    User user;
                    if(checkBox.isChecked()){
                        Chip selected_status_tutor=(Chip)findViewById(chipGroup.getCheckedChipId());
                        user=new User(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail(),name_surname.getText().toString()
                                ,selected_gender.getText().toString(),image_uri,0,"Челябинск",1,documents,selected_status_tutor.getText().toString(),phone.getText().toString());
                    }
                    else{
                        user=new User(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail(),name_surname.getText().toString()
                                ,selected_gender.getText().toString(),image_uri,0,"Челябинск",0,null,null,phone.getText().toString());

                    }
                    DatabaseReference myRef = db.getReference("users");
                    myRef.child(Objects.requireNonNull(mAuth.getCurrentUser().getUid())).setValue(user);





                }else
                    Toast.makeText(RegisterActivity.this, "Aвторизация провалена", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    private boolean checkFields(){
        return !login.getText().toString().equals("")
                && !password.getText().toString().equals("")
                && !name_surname.getText().toString().equals("")
                && !phone.getText().toString().equals("");

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            choose_img.setText(filePath.toString());
        }
    }
    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child("avatars/"+ Objects.requireNonNull(mAuth.getCurrentUser()).getUid().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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