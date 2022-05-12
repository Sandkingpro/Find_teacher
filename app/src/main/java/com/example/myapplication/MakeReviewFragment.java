package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.models.Review;
import com.example.myapplication.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MakeReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakeReviewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button save_commentary;
    NestedScrollView scrollView;
    EditText comment;
    RatingBar ratingBar;
    FirebaseDatabase db;
    DatabaseReference ref;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MakeReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MakeReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MakeReviewFragment newInstance(String param1, String param2) {
        MakeReviewFragment fragment = new MakeReviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_make_review, container, false);
        if(getView()!=null){
            getView().setEnabled(false);
        }
        db=FirebaseDatabase.getInstance("https://find-teacher-a7f26-default-rtdb.firebaseio.com");
        ratingBar=(RatingBar) root.findViewById(R.id.ratingBar3);
        setRatingBar();
        comment=(EditText)root.findViewById(R.id.commentary);
        save_commentary=(Button) root.findViewById(R.id.save_comment);
        save_commentary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        return root;
    }
    private void save(){
        Bundle bundle=this.getArguments();
        String id=null;
        if (bundle != null) {
            id=bundle.getString("id");
        }
        if(id!=null){
            if(ratingBar.getRating()!=0){
                ref=db.getReference("users").child(id);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user=dataSnapshot.getValue(User.class);
                        List<Review> reviewList=new ArrayList<>();
                        if (user != null) {
                            reviewList=user.getReviews();
                        }
                        TeacherActivity activity= (TeacherActivity) getActivity();
                        if(activity!=null){
                            activity.setReview_toDb(comment.getText().toString(), (int) ratingBar.getRating(),user,reviewList);
                            if(getView()!=null){
                                getView().setEnabled(true);
                            }
                            getParentFragmentManager().beginTransaction().remove(MakeReviewFragment.this).commit();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(),"Не удалось сохранить отзыв",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Toast.makeText(getContext(),"Вы не поставили оценку",Toast.LENGTH_SHORT).show();
            }
        }


    }
    private void setRatingBar(){
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

            }
        });
    }
}