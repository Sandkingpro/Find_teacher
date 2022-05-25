package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME ="my_shared_preff";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        sharedPreferences=getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        mAuth=FirebaseAuth.getInstance();
        isInternetAvailable();

    }
    private void isInternetAvailable(){
        if(isNetworkConnected()) {
            if (sharedPreferences.getString("login", null) != null
                    && sharedPreferences.getString("password", null) != null) {
                String saved_email = sharedPreferences.getString("login", null);
                String saved_password = sharedPreferences.getString("password", null);
                signin(saved_email, saved_password);
            }
            else{
                Intent intent=new Intent(SplashScreenActivity.this,AuthActivity.class);
                startActivity(intent);
                finish();
            }
        }
        else{
            MyDialogFragment dialogFragment=new MyDialogFragment();
            dialogFragment.show(getSupportFragmentManager(),"noInternet");
        }
    }
    private boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public void signin(String email , String password)
    {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(SplashScreenActivity.this, "Aвторизация успешна", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("login",email);
                    editor.putString("password",password);
                    editor.apply();
                    Intent intent=new Intent(SplashScreenActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }
}