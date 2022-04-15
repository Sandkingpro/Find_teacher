package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.models.Person;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AuthActivity extends AppCompatActivity {
    TextView register;
    Button auth;
    EditText login;
    EditText password;
    SharedPreferences sharedPreferences;
    ArrayList<Person> list=new ArrayList<>();
    Gson gson=new Gson();
    boolean flag;
    private static final String SHARED_PREF_NAME ="my_shared_preff";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        if(sharedPreferences.getBoolean("isLogin", false)){
            setContentView(R.layout.progress_bar_layout);
            startActivity(new Intent(AuthActivity.this,MainActivity.class));
            return;
        }
        else{
            setContentView(R.layout.activity_auth);
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
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Type type = new TypeToken<ArrayList<Person>>(){}.getType();
                list=gson.fromJson(sharedPreferences.getString("list",""), type);
                Person person=new Person(login.getText().toString(),password.getText().toString());
                if(!list.isEmpty()){
                    for (Person p:list){
                        if(p.getLogin().equals(person.getLogin()) && p.getPassword().equals(person.getPassword())){
                            Toast.makeText(AuthActivity.this, "Успешно", Toast.LENGTH_SHORT).show();
                            editor.putBoolean("isLogin",true);
                            editor.apply();
                            startActivity(new Intent(getBaseContext(), MainActivity.class));
                            flag=false;
                            break;
                        }
                        else{
                            flag=true;
                        }
                    }
                }

                if (flag){
                    Toast.makeText(AuthActivity.this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
                }
                login.setText("");
                password.setText("");


            }
        });

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}