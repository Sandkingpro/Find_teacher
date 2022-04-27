package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapplication.ui.dashboard.DashboardFragment;
import com.example.myapplication.ui.home.HomeFragment1;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME ="my_shared_preff";
    Fragment fragment1=new HomeFragment1();
    final Fragment fragment2=new DashboardFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavHostFragment navHostFragment = (NavHostFragment) fm.findFragmentById(R.id.nav_host_fragment_activity_main);
        fragment1=navHostFragment.getChildFragmentManager().getFragments().get(0);
        fm.beginTransaction().add(R.id.nav_host_fragment_activity_main,fragment1, "1").commit();
        fm.beginTransaction().add(R.id.nav_host_fragment_activity_main, fragment2, "2").hide(fragment2).commit();
        navView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fm.beginTransaction().hide(fragment2).show(fragment1).commit();
                    active=fragment1;
                    return true;

                case R.id.navigation_dashboard:
                    fm.beginTransaction().hide(fragment1).show(fragment2).commit();
                    active=fragment2;
                    return true;
            }
            return false;
        });
        ImageButton logout=(ImageButton)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,AuthActivity.class));
            }
        });



    }
    @Override
    public void onBackPressed() {
        if(active==fragment1 || active==null){
            if(fragment1.getParentFragmentManager().findFragmentById(R.id.filter_frame)==null){
                finishAffinity();
            }
            if(Objects.requireNonNull(fragment1.getParentFragmentManager().findFragmentById(R.id.filter_frame))
                    .getParentFragmentManager().findFragmentById(R.id.select_item_frame)!=null){
                Objects.requireNonNull(fragment1.getParentFragmentManager()
                        .findFragmentById(R.id.filter_frame)).getParentFragmentManager().beginTransaction().remove(
                        Objects.requireNonNull(Objects.requireNonNull(
                                fragment1.getParentFragmentManager().findFragmentById(R.id.filter_frame)).
                                getParentFragmentManager().findFragmentById(R.id.select_item_frame))
                ).commit();
                fragment1.getParentFragmentManager().findFragmentById(R.id.filter_frame).getView()
                        .findViewById(R.id.ok_button).setVisibility(View.VISIBLE);
                return;
            }
            if(fragment1.getParentFragmentManager().findFragmentById(R.id.filter_frame)!=null){
                fragment1.getParentFragmentManager().beginTransaction().remove(
                        Objects.requireNonNull(fragment1.getParentFragmentManager().findFragmentById(R.id.filter_frame)))
                        .commit();
                fragment1.getView().findViewById(R.id.filter_button).setVisibility(View.VISIBLE);

            }
        }
        if(active==fragment2){
            if(fragment2.getParentFragmentManager().findFragmentById(R.id.create_ad_fragment)==null){
                finishAffinity();
            }
            if(fragment2.getParentFragmentManager().findFragmentById(R.id.create_ad_fragment)!=null){
                fragment2.getParentFragmentManager().beginTransaction().remove(Objects.requireNonNull(fragment2.getParentFragmentManager()
                        .findFragmentById(R.id.create_ad_fragment))).commit();
                fragment2.getView().findViewById(R.id.make_adv).setEnabled(true);
            }
        }



    }





}