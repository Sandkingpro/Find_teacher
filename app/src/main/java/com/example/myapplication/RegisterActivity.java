package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.CursorJoiner;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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
    final FragmentManager fm = getSupportFragmentManager();
    ListFragment fragment=new ListFragment();
    private FirebaseAuth mAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    private String image_uri;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath=null;
    Button register;
    EditText login;
    EditText password;
    EditText password2;
    CheckBox checkBox;
    EditText phone;
    EditText name_surname;
    ChipGroup chipGroup;
    ChipGroup chipGroup_gender;
    TextView city;
    SharedPreferences sharedPreferences;
    ArrayList<Person> list=new ArrayList<>();
    Gson gson=new Gson();
    boolean flag_passwords=false;
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
        password2=(EditText) findViewById(R.id.password);
        password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    password2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    password.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(password2.getText().toString().equals(password.getText().toString())){
                    password2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    password.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    flag_passwords=true;
                }

            }
        });
        city=(TextView)findViewById(R.id.location_filter2);
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("selected",2);
                bundle.putInt("flag_fragment",4);
                fragment.setArguments(bundle);
                register.setVisibility(View.GONE);
                fm.beginTransaction().add(R.id.city_register_fragment,fragment).commit();
            }
        });
        register=(Button) findViewById(R.id.button3);
        name_surname=(EditText) findViewById(R.id.name_surname);
        phone=(EditText)findViewById(R.id.editTextPhone);
        chipGroup=(ChipGroup) findViewById(R.id.chipGroup3);
        chipGroup_gender=(ChipGroup)findViewById(R.id.chipGroup5);
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
                    if(Patterns.EMAIL_ADDRESS.matcher(login.getText().toString()).matches()){
                        registration(login.getText().toString(),password.getText().toString());
                        login.setText("");
                        password.setText("");
                    }
                    else{
                        login.setError("Введите корректную почту");
                    }

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
                    signin(email,password);
                    FirebaseAuth.getInstance().signOut();
                    finish();
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
                    image_uri="none";
                    List<String> documents=new ArrayList<>();
                    FirebaseDatabase db = FirebaseDatabase.getInstance("https://find-teacher-a7f26-default-rtdb.firebaseio.com");
                    Chip selected_gender =(Chip)findViewById(chipGroup_gender.getCheckedChipId());
                    User user;
                    if(checkBox.isChecked()){
                        Chip selected_status_tutor=(Chip)findViewById(chipGroup.getCheckedChipId());
                        user=new User(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail(),name_surname.getText().toString()
                                ,selected_gender.getText().toString(),image_uri,0,city.getText().toString(),1,documents,selected_status_tutor.getText().toString(),phone.getText().toString(),mAuth.getCurrentUser().getUid());
                    }
                    else{
                        user=new User(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail(),name_surname.getText().toString()
                                ,selected_gender.getText().toString(),image_uri,0,city.getText().toString(),0,null,null,phone.getText().toString(),mAuth.getCurrentUser().getUid());
                    }
                    DatabaseReference myRef = db.getReference("users");
                    myRef.child(Objects.requireNonNull(mAuth.getCurrentUser().getUid())).setValue(user);

                }else
                    Toast.makeText(RegisterActivity.this, "Aвторизация провалена", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private boolean checkFields(){
        return !login.getText().toString().equals("")
                && !password.getText().toString().equals("")
                && !name_surname.getText().toString().equals("")
                && !phone.getText().toString().equals("") && flag_passwords && !city.getText().toString().equals("Выберите город");

    }
    public void setCity(String item){
        register.setVisibility(View.VISIBLE);
        city.setText(item);
    }
}