package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
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

public class AuthActivity extends AppCompatActivity {
    TextView register;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
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
        isInternetAvailable();



    }
    public void signin(String email , String password)
    {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(AuthActivity.this, "Aвторизация успешна", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("login",email);
                    editor.putString("password",password);
                    editor.apply();
                    Intent intent=new Intent(AuthActivity.this,MainActivity.class);
                    startActivity(intent);
                }else
                    Toast.makeText(AuthActivity.this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
    public void isInternetAvailable() {
        if(isNetworkConnected()){
            if(sharedPreferences.getString("login",null)!=null
                    && sharedPreferences.getString("password",null)!=null){
                String saved_email=sharedPreferences.getString("login",null);
                String saved_password=sharedPreferences.getString("password",null);
                signin(saved_email,saved_password);
            }
            login=(EditText) findViewById(R.id.login1);
            password=(EditText)findViewById(R.id.password1);
            register=(TextView) findViewById(R.id.textView2);
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
            auth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!login.getText().toString().equals("") && !password.getText().toString().equals("")){
                        signin(login.getText().toString(),password.getText().toString());
                        login.setText("");
                        password.setText("");
                    }
                    else{
                        Toast.makeText(AuthActivity.this,"Введите логин и пароль для входа",Toast.LENGTH_SHORT).show();
                    }



                }
            });
        }
        else{
            MyDialogFragment dialogFragment=new MyDialogFragment();
            dialogFragment.show(getSupportFragmentManager(),"noInternet");
        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}