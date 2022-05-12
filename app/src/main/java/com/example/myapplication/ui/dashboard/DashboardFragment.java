package com.example.myapplication.ui.dashboard;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.CreateAdFragment;
import com.example.myapplication.EditProfileFragment;
import com.example.myapplication.R;
import com.example.myapplication.models.Review;
import com.example.myapplication.adapters.ReviewAdapter;
import com.example.myapplication.adapters.SliderAdapter;
import com.example.myapplication.models.SliderItem;
import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.example.myapplication.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private FirebaseAuth mAuth;
    SliderView sliderView;
    private SliderAdapter adapter;
    List<Review> reviews;
    TextView name;
    TextView type_education;
    TextView city;
    RatingBar ratingBar;
    CircleImageView circleImageView;
    TextView email;
    Button make_ad;
    TextView phone;
    RecyclerView rv;
    Button show_full;
    TextView title_review;
    TextView title_slider;
    Button show_full_text;
    TextView about;
    Button edit_profile;
    boolean flag=true;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mAuth=FirebaseAuth.getInstance();
        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        name=root.findViewById(R.id.name_cabinet);
        type_education=root.findViewById(R.id.type_education_cabinet);
        city=root.findViewById(R.id.city_cabinet);
        ratingBar=root.findViewById(R.id.ratingBar2);
        circleImageView=root.findViewById(R.id.photo_cabinet);
        email=root.findViewById(R.id.e_mail);
        make_ad=(Button) root.findViewById(R.id.make_adv);
        phone=(TextView)root.findViewById(R.id.phone_number);
        sliderView=(SliderView) root.findViewById(R.id.imageSlider);
        rv=root.findViewById(R.id.recyclerView);
        show_full=root.findViewById(R.id.hide_show_list);
        title_slider=(TextView)root.findViewById(R.id.textView26);
        title_review=(TextView)root.findViewById(R.id.textView18);
        about=(TextView) root.findViewById(R.id.about_information_cabinet);
        show_full_text=(Button) root.findViewById(R.id.hide_show_text);
        edit_profile=(Button)root.findViewById(R.id.edit_profile);
        getCurrentUser_info();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void renewItems(View view) {
        List<SliderItem> sliderItemList = new ArrayList<>();
        //dummy data
        for (int i = 0; i < 5; i++) {
            SliderItem sliderItem = new SliderItem();
            if (i % 2 == 0) {
                sliderItem.setImageUrl("https://img-fotki.yandex.ru/get/6702/62614826.19/0_16fe2f_458d97d8_orig.jpg");
            } else {
                sliderItem.setImageUrl("https://easyen.ru/_ph/8/42976292.jpg");
            }
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }

    public void removeLastItem(View view) {
        if (adapter.getCount() - 1 >= 0)
            adapter.deleteItem(adapter.getCount() - 1);
    }

    public void addNewItem(View view) {
        SliderItem sliderItem = new SliderItem();
        sliderItem.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        adapter.addItem(sliderItem);
    }
    private List<Review> initializeData(User user){
        return user.getReviews();
    }
    private void getCurrentUser_info(){
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://find-teacher-a7f26-default-rtdb.firebaseio.com");
        DatabaseReference ref = db.getReference("users")
                .child(mAuth.getCurrentUser().getUid());
        SweetAlertDialog pDialog;
        db.getReference("advertisements").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())){
                    make_ad.setText("Изменить объявление");
                }
                else{
                    make_ad.setText("Создать объявление");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        pDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF6C4A"));
        pDialog.setTitleText("Загрузка...");
        pDialog.setCancelable(false);
        pDialog.show();
        // Attach listener
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve latest value
                User user = dataSnapshot.getValue(User.class);
                if(user.getType()==0){
                    title_review.setVisibility(View.GONE);
                    title_slider.setVisibility(View.GONE);
                    make_ad.setVisibility(View.GONE);
                    sliderView.setVisibility(View.GONE);
                    rv.setVisibility(View.GONE);
                    show_full.setVisibility(View.GONE);
                    ratingBar.setVisibility(View.GONE);
                    flag=false;
                }
                updateUI(user,flag);
            }
            private void updateUI(User user,boolean flag){
                name.setText(user.getName());
                phone.setText(user.getPhone());
                email.setText(user.getEmail());
                city.setText(user.getCity());
                if(user.getAbout_me()!=null){
                    about.setText(user.getAbout_me());
                }
                else{
                    about.setText("Нет информации");
                }
                final String[] fullText = {about.getText().toString()};
                System.out.println(fullText[0].length());
                int maxLength=120;
                if(fullText[0].length()>maxLength){
                    show_full_text.setText("Показать полностью...");
                    about.setText(fullText[0].substring(0,maxLength));
                    show_full_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(show_full_text.getText().toString().equals("скрыть")){
                                fullText[0] = fullText[0].substring(0, maxLength);
                                about.setText(fullText[0]);
                                show_full_text.setText("Показать полностью...");
                            }
                            else if(show_full_text.getText().toString().equals("Показать полностью...")){
                                fullText[0] =getResources().getString(R.string.content);
                                about.setText(fullText[0]);
                                show_full_text.setText("скрыть");
                            }

                        }
                    });
                }
                else{
                    about.setText(fullText[0]);
                }
                edit_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditProfileFragment editProfileFragment=new EditProfileFragment();
                        Bundle bundle=new Bundle();
                        bundle.putInt("?type",user.getType());
                        editProfileFragment.setArguments(bundle);
                        getParentFragmentManager().beginTransaction().add(R.id.edit_fragment,editProfileFragment).commit();
                    }
                });
                if(flag){
                    type_education.setText(user.getType_education());
                    ratingBar.setRating((float) user.getRating());
                    adapter = new SliderAdapter(getContext());
                    List<SliderItem> sliderItemList=new ArrayList<>();
                    SliderItem item=new SliderItem();
                    item.setImageUrl("https://img-fotki.yandex.ru/get/6702/62614826.19/0_16fe2f_458d97d8_orig.jpg");
                    SliderItem item2=new SliderItem();
                    item2.setImageUrl("https://easyen.ru/_ph/8/42976292.jpg");
                    sliderItemList.add(item);
                    sliderItemList.add(item2);
                    adapter.renewItems(sliderItemList);
                    sliderView.setSliderAdapter(adapter);
                    sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                    sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    sliderView.setIndicatorSelectedColor(Color.WHITE);
                    sliderView.setIndicatorUnselectedColor(Color.GRAY);
                    sliderView.setScrollTimeInSec(600);
                    reviews=initializeData(user);
                    Collections.sort(reviews, new Comparator<Review>() {
                        @Override
                        public int compare(Review review, Review t1) {
                            return review.getCommentary().compareTo(t1.getCommentary());
                        }
                    });
                    RecyclerView.LayoutManager llm = new LinearLayoutManager(getContext());
                    rv.setLayoutManager(llm);
                    if(reviews.size()>2){
                        show_full.setVisibility(View.VISIBLE);
                        final ReviewAdapter[] reviewAdapter = {new ReviewAdapter(reviews.subList(0, 2), getContext())};
                        rv.setAdapter(reviewAdapter[0]);
                        show_full.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(show_full.getText().toString().equals("показать все")){
                                    reviewAdapter[0] =new ReviewAdapter(reviews,getContext());
                                    rv.setAdapter(reviewAdapter[0]);
                                    show_full.setText("скрыть");
                                }
                                else{
                                    reviewAdapter[0]=new ReviewAdapter(reviews.subList(0,2),getContext());
                                    rv.setAdapter(reviewAdapter[0]);
                                    show_full.setText("показать все");
                                }
                            }
                        });
                    }
                    else{
                        ReviewAdapter reviewAdapter=new ReviewAdapter(reviews,getContext());
                        rv.setAdapter(reviewAdapter);
                        show_full.setVisibility(View.GONE);
                    }

                    make_ad.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CreateAdFragment createAdFragment=new CreateAdFragment();
                            Bundle bundle=new Bundle();
                            if (make_ad.getText().toString().equals("Изменить объявление")){
                                bundle.putInt("?edit",1);
                            }
                            else{
                                bundle.putInt("?edit",0);
                            }
                            createAdFragment.setArguments(bundle);
                            getParentFragmentManager().beginTransaction().add(R.id.create_ad_fragment,createAdFragment).commit();

                        }
                    });
                }

                // Create a reference with an initial file path and name
                if(user.getPhoto()!="none"){
                    if(getActivity()!=null){
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReferenceFromUrl("gs://find-teacher-a7f26.appspot.com/").child("avatars/"+mAuth.getCurrentUser().getUid());
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getContext()).load(uri).
                                        thumbnail( 0.5f )
                                        .override( 200, 200 )
                                        .placeholder(R.drawable.ic_avatar)
                                        .diskCacheStrategy( DiskCacheStrategy.ALL )
                                        .fitCenter()
                                        .priority(Priority.IMMEDIATE)
                                        .into(circleImageView);
                            }
                        });
                    }

                }

                pDialog.dismiss();
                pDialog.dismissWithAnimation();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

}