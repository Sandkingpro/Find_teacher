package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.models.Teacher;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Objects;

public class TeacherActivity extends AppCompatActivity {
    TextView name;
    TextView type_education;
    TextView city;
    ImageView photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeacherActivity.super.onBackPressed();
            }
        });
        fab.setColorFilter(Color.argb(255, 255, 255, 255));
        Objects.requireNonNull(getSupportActionBar()).hide();
        Gson gson=new Gson();
        Type type = new TypeToken<Teacher>(){}.getType();
        Teacher teacher=gson.fromJson(getIntent().getStringExtra("teacher"),type);
        final CollapsingToolbarLayout toolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collaps);
        name=(TextView)findViewById(R.id.name_teacher);
        type_education=(TextView)findViewById(R.id.type_education_teacher);
        city=(TextView)findViewById(R.id.city_teacher);
        name.setText(teacher.getName());
        type_education.setText(teacher.getWhere_educate());
        city.setText(teacher.getCity());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        Toolbar mytoolbar=(Toolbar)findViewById(R.id.toolbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    toolbarLayout.setTitle(teacher.getName());
                    toolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
                    isShow = true;
                } else if(isShow) {
                    toolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
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