package com.example.staffcafeposapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.staffcafeposapp.Adapter.Beverages_Adapter;
import com.example.staffcafeposapp.Adapter.MenuAdapter;
import com.example.staffcafeposapp.Model.Beverages;
import com.example.staffcafeposapp.Model.Food;
import com.example.staffcafeposapp.R;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    private RecyclerView food_recyclerView;
    private MenuAdapter food_Adapter;
    private ArrayList<Food> foodList;

    private RecyclerView beverages_recyclerView;
    private Beverages_Adapter beverages_Adapter;
    private ArrayList<Beverages> beveragesArrayList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Food Recycler View
        food_recyclerView = view.findViewById(R.id.recyclerView_Food);
        food_recyclerView.setHasFixedSize(true);
        food_recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        foodList = new ArrayList<>();

        foodList.add(new Food("Chicken", 20));
        foodList.add(new Food("Fish", 21));
        foodList.add(new Food("Fried Rice", 15));
        foodList.add(new Food("Noddles", 10));

        food_Adapter = new MenuAdapter(this.getContext(), foodList);
        food_recyclerView.setAdapter(food_Adapter);

        //Beverages RecyclerView
        beverages_recyclerView = view.findViewById(R.id.recyclerView_Beverages);
        beverages_recyclerView.setHasFixedSize(true);
        beverages_recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        beveragesArrayList = new ArrayList<>();
        beveragesArrayList.add(new Beverages("Coffee", 6));
        beveragesArrayList.add(new Beverages("Lemonade", 5));
        beveragesArrayList.add(new Beverages("Hot chocolate", 7));
        beveragesArrayList.add(new Beverages("Milkshake", 10));
        beveragesArrayList.add(new Beverages("Water", 1));

        beverages_Adapter = new Beverages_Adapter(this.getContext(), beveragesArrayList);
        beverages_recyclerView.setAdapter(beverages_Adapter);
    }
}