package com.example.myapplication.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Review;
import com.example.myapplication.adapters.ReviewAdapter;
import com.example.myapplication.adapters.SliderAdapter;
import com.example.myapplication.models.SliderItem;
import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    SliderView sliderView;
    private SliderAdapter adapter;
    List<Review> reviews;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        TextView about=(TextView) root.findViewById(R.id.about_information_cabinet);
        final String[] fullText = {getResources().getString(R.string.content)};
        System.out.println(fullText[0].length());
        int maxLength=120;
        Button show_full_text=(Button) root.findViewById(R.id.hide_show_text);
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
                        fullText[0]=getResources().getString(R.string.content);
                        about.setText(fullText[0]);
                        show_full_text.setText("скрыть");
                    }

                }
            });
        }
        else{
            about.setText(fullText[0]);
        }

        sliderView=(SliderView) root.findViewById(R.id.imageSlider);
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
        reviews=initializeData();
        RecyclerView rv=root.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        final ReviewAdapter[] reviewAdapter = {new ReviewAdapter(reviews.subList(0, 2), getContext())};
        rv.setAdapter(reviewAdapter[0]);
        Button show_full=root.findViewById(R.id.hide_show_list);
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
    private List<Review> initializeData(){
        reviews=new ArrayList<>();
        reviews.add(new Review("Таисия Кузнецова","Хороший учитель, понятно и внятно объясняет материал, за месяц занятий наверстал все,то что не понимал в школе"
        ,5));
        reviews.add(new Review("Таисия Кузнецова","Хороший учитель, понятно и внятно объясняет материал, за месяц занятий наверстал все,то что не понимал в школе"
                ,5));
        reviews.add(new Review("Таисия Кузнецова","Хороший учитель, понятно и внятно объясняет материал, за месяц занятий наверстал все,то что не понимал в школе"
                ,5));
        reviews.add(new Review("Таисия Кузнецова","Хороший учитель, понятно и внятно объясняет материал, за месяц занятий наверстал все,то что не понимал в школе"
                ,5));
        reviews.add(new Review("Таисия Кузнецова","Хороший учитель, понятно и внятно объясняет материал, за месяц занятий наверстал все,то что не понимал в школе"
                ,5));
        return reviews;
    }
}