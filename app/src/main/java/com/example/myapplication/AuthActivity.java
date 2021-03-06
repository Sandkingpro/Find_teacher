package com.example.myapplication;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.models.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Objects;

public class AuthActivity extends AppCompatActivity {
    TextView register;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Button recover_password;
    Button auth;
    EditText login;
    EditText password;
    SharedPreferences sharedPreferences;
    ArrayList<Person> list=new ArrayList<>();
    Gson gson=new Gson();
    boolean flag=false;
    private static final String SHARED_PREF_NAME ="my_shared_preff";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        sharedPreferences=getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        setListeners();



    }
    public void signin(String email , String password)
    {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(AuthActivity.this, "A???????????????????? ??????????????", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("login",email);
                    editor.putString("password",password);
                    editor.apply();
                    Intent intent=new Intent(AuthActivity.this,MainActivity.class);
                    startActivity(intent);
                }else
                    if(!isNetworkConnected()){
                        Toast.makeText(AuthActivity.this, "?????? ?????????????????????? ?? ??????????????????", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(AuthActivity.this, "???????????????? ?????????? ?????? ????????????", Toast.LENGTH_SHORT).show();
                    }


            }
        });
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().findFragmentById(R.id.recover_password_container)!=null){
            getSupportFragmentManager().beginTransaction().remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.recover_password_container))).commit();
            auth.setVisibility(View.VISIBLE);
            recover_password.setVisibility(View.VISIBLE);
        }
        else{
            finishAffinity();
        }

    }
    public void setListeners() {
        login=(EditText) findViewById(R.id.login1);
        password=(EditText)findViewById(R.id.password1);
        register=(TextView) findViewById(R.id.textView2);
        recover_password=(Button)findViewById(R.id.recover_password_button);
        auth=(Button) findViewById(R.id.button);
        ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                    }
                });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartForResult.launch(new Intent(AuthActivity.this,RegisterActivity.class));
            }
        });
        recover_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().add(R.id.recover_password_container,new RecoverPasswordFragment()).commit();
                auth.setVisibility(View.GONE);
                recover_password.setVisibility(View.GONE);
            }
        });
        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!login.getText().toString().equals("") && !password.getText().toString().equals("")){
                    signin(login.getText().toString(),password.getText().toString());
                    login.setText("");
                    password.setText("");
                }
                else{
                    Toast.makeText(AuthActivity.this,"?????????????? ?????????? ?? ???????????? ?????? ??????????",Toast.LENGTH_SHORT).show();
                }



            }
        });
    }
    private boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}