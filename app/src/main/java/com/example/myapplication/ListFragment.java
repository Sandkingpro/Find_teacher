package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.adapters.FilterListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FilterFragment filterFragment;
    CreateAdFragment createAdFragment;
    EditProfileFragment editProfileFragment;
    int flag_fragment;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
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
        List<String> elements=new ArrayList<>();
        Bundle bundle=this.getArguments();
        if(Objects.requireNonNull(bundle).getInt("selected")==1){
            elements= Arrays.asList(getResources().getStringArray(R.array.subjects));
        }
        else{
            elements=Arrays.asList(getResources().getStringArray(R.array.cities));

        }
        flag_fragment=bundle.getInt("flag_fragment");
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView rv=(RecyclerView) view.findViewById(R.id.list);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        FilterListAdapter adapter=new FilterListAdapter(elements,getContext(), this);
        rv.setAdapter(adapter);
        return view;
    }
    public void getItem(String _item){
        filterFragment= (FilterFragment) this.getParentFragmentManager().findFragmentById(R.id.filter_frame);
        createAdFragment= (CreateAdFragment) this.getParentFragmentManager().findFragmentById(R.id.create_ad_fragment);
        editProfileFragment=(EditProfileFragment) this.getParentFragmentManager().findFragmentById(R.id.edit_fragment);
        if(filterFragment!=null && flag_fragment==2){
            Objects.requireNonNull(filterFragment).setItem(_item);
        }
        if(createAdFragment!=null && flag_fragment==1){
            Objects.requireNonNull(createAdFragment).setItem(_item);
        }
        if(editProfileFragment!=null && flag_fragment==3){
            editProfileFragment.setItem(_item);
            editProfileFragment.getView().findViewById(R.id.edit_profile_button).setVisibility(View.VISIBLE);
        }
        if(flag_fragment==4){
            RegisterActivity activity= (RegisterActivity) getActivity();
            if(activity!=null){
                activity.setCity(_item);
            }
        }

    }
}