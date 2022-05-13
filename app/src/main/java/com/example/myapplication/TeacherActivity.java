package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.adapters.ReviewAdapter;
import com.example.myapplication.adapters.SliderAdapter;
import com.example.myapplication.adapters.TypeStudentAdapter;
import com.example.myapplication.models.Advertisement;
import com.example.myapplication.models.Review;
import com.example.myapplication.models.SliderItem;
import com.example.myapplication.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherActivity extends AppCompatActivity {
    final FragmentManager fm = getSupportFragmentManager();
    MakeReviewFragment fragment=new MakeReviewFragment();
    RecyclerView additionally_rv;
    TextView name;
    TextView type_education;
    TextView city;
    ImageView photo;
    RatingBar ratingBar;
    TextView phone;
    TextView email;
    TextView subject;
    TextView price;
    TextView format_ad;
    TextView title_review;
    TextView about;
    Button make_rev;
    Button hide_show_rev;
    ImageButton call_button;
    RecyclerView rv_type_st;
    RecyclerView rv;
    SliderView sliderView;
    String Uid;
    FirebaseDatabase db;
    DatabaseReference ref;
    DatabaseReference ref_user;
    DatabaseReference ref_current_user;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout toolbarLayout;
    private FirebaseAuth mAuth;
    List<Review> reviewList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        mAuth=FirebaseAuth.getInstance();
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyBack();
            }
        });
        fab.setColorFilter(Color.argb(255, 255, 255, 255));
        Objects.requireNonNull(getSupportActionBar()).hide();
        Uid=getIntent().getStringExtra("id");
        db = FirebaseDatabase.getInstance("https://find-teacher-a7f26-default-rtdb.firebaseio.com");
        ref = db.getReference("advertisements").child(Uid);
        ref_user=db.getReference("users").child(Uid);
        ref_current_user=db.getReference("users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        checkTypeAccount();
        toolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collaps);
        sliderView=(SliderView)findViewById(R.id.imageSlider);
        name=(TextView)findViewById(R.id.name_teacher);
        type_education=(TextView)findViewById(R.id.type_education_teacher);
        city=(TextView)findViewById(R.id.city_teacher);
        photo=(CircleImageView)findViewById(R.id.photo_ad);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar2);
        email=(TextView) findViewById(R.id.e_mail) ;
        phone=(TextView)findViewById(R.id.phone_number);
        title_review=(TextView)findViewById(R.id.textView18);
        subject=(TextView)findViewById(R.id.subject_current_ad);
        price=(TextView)findViewById(R.id.price_ad);
        make_rev=(Button)findViewById(R.id.make_review);
        hide_show_rev=(Button) findViewById(R.id.hide_show_list);
        call_button=(ImageButton)findViewById(R.id.call_button);
        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                make_call();
            }
        });
        format_ad=(TextView) findViewById(R.id.format_ad);
        about=(TextView)findViewById(R.id.about_information_cabinet);
        rv=(RecyclerView)findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getBaseContext());
        rv.setLayoutManager(llm);
        RecyclerView.LayoutManager llm2 = new GridLayoutManager(getBaseContext(),2,GridLayoutManager.HORIZONTAL,false);
        rv_type_st=(RecyclerView)findViewById(R.id.recyclerView3);
        rv_type_st.setLayoutManager(llm2);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        additionally_rv=(RecyclerView) findViewById(R.id.additionally_rv);
        RecyclerView.LayoutManager grd=new GridLayoutManager(getBaseContext(),1,GridLayoutManager.HORIZONTAL,false);
        additionally_rv.setLayoutManager(grd);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        SweetAlertDialog pDialog;
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF6C4A"));
        pDialog.setTitleText("Загрузка...");
        pDialog.setCancelable(false);
        pDialog.show();
        getTeacherAccount();
        getAdvertisement_info();
        pDialog.dismiss();
        pDialog.dismissWithAnimation();



    }
    @Override
    public void onBackPressed(){
        MyBack();
    }
    private void MyBack(){
        if(fm.findFragmentById(R.id.make_rev_fragment)!=null){
            fm.beginTransaction().remove(fragment).commit();
        }
        else{
            super.onBackPressed();
        }
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
    private double updateRating(List<Review> reviews){
        double numerator=0;
        for(Review review:reviews){
            numerator+=review.getRating();
        }
        return numerator/reviews.size();

    }
    private void checkTypeAccount(){
        ref_current_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User current_user=dataSnapshot.getValue(User.class);
                if(Objects.requireNonNull(current_user).getType()==1){
                    make_rev.setVisibility(View.GONE);
                    call_button.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getTeacherAccount(){
        ref_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                UpdateUI(Objects.requireNonNull(user));
            }
            private void UpdateUI(User user){
                name.setText(user.getName());
                type_education.setText(user.getType_education());
                city.setText(user.getCity());
                ratingBar.setRating((float) user.getRating());
                email.setText(user.getEmail());
                phone.setText(user.getPhone());
                if(user.getAbout_me()!=null){
                    about.setText(user.getAbout_me());
                }
                else{
                    about.setText("Нет информации");
                }
                if(!user.getPhoto().equals("none")){
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReferenceFromUrl(user.getPhoto());
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(getBaseContext()).load(uri).
                                    thumbnail( 0.5f )
                                    .override( 200, 200 )
                                    .placeholder(R.drawable.ic_avatar)
                                    .diskCacheStrategy( DiskCacheStrategy.ALL )
                                    .fitCenter()
                                    .priority(Priority.IMMEDIATE)
                                    .into(photo);
                        }
                    });
                }
                if(user.getDocuments()!=null){
                    List<String> documents=user.getDocuments();
                    SliderAdapter adapter = new SliderAdapter(getBaseContext());
                    List<SliderItem> sliderItemList=new ArrayList<>();
                    for(int i=0;i<documents.size();i++){
                        SliderItem item=new SliderItem();
                        item.setImageUrl(documents.get(i));
                        sliderItemList.add(item);
                        adapter.renewItems(sliderItemList);
                        sliderView.setSliderAdapter(adapter);
                        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                        sliderView.setIndicatorSelectedColor(Color.WHITE);
                        sliderView.setIndicatorUnselectedColor(Color.GRAY);
                        sliderView.setScrollTimeInSec(600);
                    }
                }
                reviewList=user.getReviews();
                if(reviewList!=null){
                    String text="Отзывы "+"("+String.valueOf(reviewList.size())+")";
                    title_review.setText(text);
                }
                else{
                    String text="Отзывы (0)";
                    title_review.setText(text);
                }
                Collections.sort(reviewList, new Comparator<Review>() {
                    @Override
                    public int compare(Review review, Review t1) {
                        return review.getCommentary().compareTo(t1.getCommentary());
                    }
                });
                if(reviewList.size()>2){
                    hide_show_rev.setVisibility(View.VISIBLE);
                    final ReviewAdapter[] reviewAdapter = {new ReviewAdapter(reviewList.subList(0, 2), getBaseContext())};
                    rv.setAdapter(reviewAdapter[0]);
                    hide_show_rev.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(hide_show_rev.getText().toString().equals("показать все")){
                                reviewAdapter[0] =new ReviewAdapter(reviewList,getBaseContext());
                                rv.setAdapter(reviewAdapter[0]);
                                hide_show_rev.setText("скрыть");
                            }
                            else{
                                reviewAdapter[0]=new ReviewAdapter(reviewList.subList(0,2),getBaseContext());
                                rv.setAdapter(reviewAdapter[0]);
                                hide_show_rev.setText("показать все");
                            }
                        }
                    });
                }
                else{
                    ReviewAdapter adapter=new ReviewAdapter(reviewList,getBaseContext());
                    rv.setAdapter(adapter);
                    hide_show_rev.setVisibility(View.GONE);
                }
                appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    boolean isShow = true;
                    int scrollRange = -1;

                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        if (scrollRange == -1) {
                            scrollRange = appBarLayout.getTotalScrollRange();
                        }
                        if (scrollRange + verticalOffset == 0) {
                            toolbarLayout.setTitle(user.getName());
                            toolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
                            isShow = true;
                        } else if(isShow) {
                            toolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                            isShow = false;
                        }
                    }
                });
                make_Review(user);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getAdvertisement_info(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Advertisement advertisement=dataSnapshot.getValue(Advertisement.class);
                UpdateUi(Objects.requireNonNull(advertisement));

            }
            private void UpdateUi(Advertisement advertisement){
                subject.setText(advertisement.getSubject());
                price.setText(String.valueOf(advertisement.getPrice()));
                format_ad.setText(advertisement.getFormat());
                TypeStudentAdapter adapter=new TypeStudentAdapter(advertisement.getType_students(),getBaseContext());
                rv_type_st.setAdapter(adapter);
                List<String> additionally_list=advertisement.getAdditionally();
                TypeStudentAdapter adapter1;
                if(additionally_list.size()!=0){
                    adapter1=new TypeStudentAdapter(additionally_list,getBaseContext());
                }
                else{
                    additionally_list=new ArrayList<>();
                    additionally_list.add("Нет подготовки к экзаменам");
                    adapter1=new TypeStudentAdapter(additionally_list,getBaseContext());
                }


                additionally_rv.setAdapter(adapter1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
    private void make_Review(User user){
        make_rev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("id",user.getUid());
                fragment.setArguments(bundle);
                fm.beginTransaction().add(R.id.make_rev_fragment,fragment).commit();

            }
        });
    }
    private void make_call(){
        String phoneNo = phone.getText().toString();
        if(!TextUtils.isEmpty(phoneNo)) {
            String dial = "tel:" + phoneNo;
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
        }else {
            Toast.makeText(getBaseContext(), "Enter a phone number", Toast.LENGTH_SHORT).show();
        }
    }
    public void setReview_toDb(String comment,int rating,User user,List<Review> reviewList){
        ref_current_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User me=dataSnapshot.getValue(User.class);
                if(me!=null){
                    reviewList.add(new Review(me.getName(),comment,rating));
                    user.setReviews(reviewList);
                    user.setRating(updateRating(reviewList));
                    ref_user.setValue(user);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}