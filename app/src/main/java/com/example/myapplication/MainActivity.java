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
import androidx.recyclerview.widget.RecyclerView;

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
    Fragment fragment2=new DashboardFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active;
    BottomNavigationView navView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavHostFragment navHostFragment = (NavHostFragment) fm.findFragmentById(R.id.nav_host_fragment_activity_main);
        fragment1=navHostFragment.getChildFragmentManager().getFragments().get(0);
        fm.beginTransaction().add(R.id.nav_host_fragment_activity_main,fragment1, "1").commit();
        fm.beginTransaction().add(R.id.nav_host_fragment_activity_main, fragment2, "2").hide(fragment2).commit();
        initializedFragments(navView);


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
            if(fragment2.getParentFragmentManager().findFragmentById(R.id.create_ad_fragment)==null &&
            fragment2.getParentFragmentManager().findFragmentById(R.id.edit_fragment)==null){
                finishAffinity();
            }
            if(fragment2.getParentFragmentManager().findFragmentById(R.id.create_ad_fragment)!=null){
                if(Objects.requireNonNull(fragment2.getParentFragmentManager().findFragmentById(R.id.create_ad_fragment))
                        .getParentFragmentManager().findFragmentById(R.id.select_subject_frame)!=null){
                    Objects.requireNonNull(fragment2.getParentFragmentManager()
                            .findFragmentById(R.id.create_ad_fragment)).getParentFragmentManager().beginTransaction().remove(
                            Objects.requireNonNull(Objects.requireNonNull(
                                    fragment2.getParentFragmentManager().findFragmentById(R.id.create_ad_fragment)).
                                    getParentFragmentManager().findFragmentById(R.id.select_subject_frame))
                    ).commit();
                    return;
                }
                fragment2.getParentFragmentManager().beginTransaction().remove(Objects.requireNonNull(fragment2.getParentFragmentManager()
                        .findFragmentById(R.id.create_ad_fragment))).commit();
                fragment2.getView().findViewById(R.id.make_adv).setEnabled(true);
                return;

            }

            if(fragment2.getParentFragmentManager().findFragmentById(R.id.edit_fragment)!=null){
                if(Objects.requireNonNull(fragment2.getParentFragmentManager().findFragmentById(R.id.edit_fragment))
                        .getParentFragmentManager().findFragmentById(R.id.select_edit_city_frame)!=null){
                    Objects.requireNonNull(fragment2.getParentFragmentManager()
                            .findFragmentById(R.id.edit_fragment)).getParentFragmentManager().beginTransaction().remove(
                            Objects.requireNonNull(Objects.requireNonNull(
                                    fragment2.getParentFragmentManager().findFragmentById(R.id.edit_fragment)).
                                    getParentFragmentManager().findFragmentById(R.id.select_edit_city_frame))
                    ).commit();
                    fragment2.getParentFragmentManager().findFragmentById(R.id.edit_fragment).getView().findViewById(R.id.edit_profile_button).setVisibility(View.VISIBLE);
                    return;
                }
                fragment2.getParentFragmentManager().beginTransaction().remove(Objects.requireNonNull(fragment2.getParentFragmentManager()
                        .findFragmentById(R.id.edit_fragment))).commit();

            }
        }



    }
    public void initializedFragments(BottomNavigationView navView){
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
                sharedPreferences=getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.remove("login");
                editor.remove("password");
                editor.apply();
                startActivity(new Intent(MainActivity.this,AuthActivity.class));
            }
        });
    }
    public void re_initialized(){
        fm.beginTransaction().remove(fragment1).commit();
        fm.beginTransaction().remove(fragment2).commit();
        fragment1=new HomeFragment1();
        fragment2=new DashboardFragment();
        fm.beginTransaction().add(R.id.nav_host_fragment_activity_main,fragment1,"1").commit();
        fm.beginTransaction().add(R.id.nav_host_fragment_activity_main,fragment2,"2").commit();
        active=fragment2;
        initializedFragments(navView);
        fm.beginTransaction().show(fragment2).commit();
        fm.beginTransaction().hide(fragment1).commit();

    }




}