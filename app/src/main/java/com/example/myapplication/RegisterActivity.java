package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.models.Person;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    Button register;
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
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        login=(EditText) findViewById(R.id.login2);
        password=(EditText) findViewById(R.id.password2);
        register=(Button) findViewById(R.id.button3);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!login.getText().toString().equals("") && !password.getText().toString().equals("")){
                    sharedPreferences=getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if(!sharedPreferences.getString("list","").equals("")){
                        Type type = new TypeToken<ArrayList<Person>>(){}.getType();
                        list=gson.fromJson(sharedPreferences.getString("list",""), type);
                    }
                    if(list.size()!=0){
                        for(Person p:list){
                            if(p.getLogin().equals(login.getText().toString())){
                                Toast.makeText(RegisterActivity.this, "Пользователь с таким логином уже есть", Toast.LENGTH_SHORT).show();
                                flag=true;
                                login.setText("");
                                password.setText("");
                                break;
                            }
                        }
                    }
                    if(!flag){
                        list.add(new Person(login.getText().toString(),password.getText().toString()));
                        String json=gson.toJson(list);
                        editor.putString("list", json);
                        editor.apply();
                        Intent intent=new Intent();
                        setResult(RESULT_OK,intent);
                        finish();
                    }


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
}