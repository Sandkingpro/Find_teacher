package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public View view;

    public FilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterFragment newInstance(String param1, String param2) {
        FilterFragment fragment = new FilterFragment();
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
        view=inflater.inflate(R.layout.fragment_filter,container,false);
        Button button =view.findViewById(R.id.ok_button);
        EditText start_price = (EditText) view.findViewById(R.id.start_price);
        EditText end_price = (EditText) view.findViewById(R.id.end_price);
        CrystalRangeSeekbar seekBar = (CrystalRangeSeekbar) view.findViewById(R.id.rangeSeekbar5);
        seekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                start_price.setText(String.valueOf(minValue));
                end_price.setText(String.valueOf(maxValue));
            }
        });
        TextView subject=view.findViewById(R.id.subject_filter);
        subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new ListFragment();
                Bundle bundle=new Bundle();
                bundle.putInt("selected",1);
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().add(R.id.select_item_frame,fragment).commit();
                button.setVisibility(View.GONE);

            }
        });
        TextView city=(TextView) view.findViewById(R.id.location_filter);
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new ListFragment();
                Bundle bundle=new Bundle();
                bundle.putInt("selected",2);
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().add(R.id.select_item_frame,fragment).commit();
                button.setVisibility(View.GONE);
            }
        });
        return view;
    }
}