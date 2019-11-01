package com.example.staffcafeposapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.staffcafeposapp.Adapter.Beverages_Adapter;
import com.example.staffcafeposapp.Adapter.MenuAdapter;
import com.example.staffcafeposapp.Adapter.OrderSummaryAdapter;
import com.example.staffcafeposapp.Model.MenuItem;
import com.example.staffcafeposapp.Model.OrderItem;
import com.example.staffcafeposapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MenuFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference foodCollectionReference = db.collection("Food");
    private CollectionReference beveragesCollectionReference = db.collection("Drink");


    private RecyclerView food_recyclerView;
    private MenuAdapter food_Adapter;
    private ArrayList<MenuItem> foodArrayList;

    private RecyclerView beverages_recyclerView;
    private Beverages_Adapter beverages_Adapter;
    private ArrayList<MenuItem> beveragesArrayList;

    private RecyclerView orderSummary_recyclerView;
    private ArrayList<OrderItem> selectedMenuItemList;
    private OrderSummaryAdapter orderSummary_adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        foodArrayList = new ArrayList<>();
        beveragesArrayList = new ArrayList<>();
        selectedMenuItemList = new ArrayList<>();
        getMenu();

        //Food Recycler View
        food_recyclerView = view.findViewById(R.id.recyclerView_Food);
        food_recyclerView.setHasFixedSize(true);
        food_recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        food_Adapter = new MenuAdapter(this.getContext(), foodArrayList, new MenuAdapter.FoodListener() {
            @Override
            public void onDataSent(OrderItem selectedFood) {
                if(selectedMenuItemList.contains(selectedFood)){
                    for(OrderItem item: selectedMenuItemList){
                        if(item.getItem_name().equals(selectedFood.getItem_name())){
                            if(item.getItem_quantity() == 0){
                                selectedMenuItemList.remove(item);
                                break;
                            }else {
                                item.setItem_quantity(selectedFood.getItem_quantity());
                            }
                        }
                    }
                }else{
                    selectedMenuItemList.add(selectedFood);
                }
                orderSummary_adapter.notifyDataSetChanged();
            }
        });
        food_recyclerView.setAdapter(food_Adapter);

        beverages_recyclerView = view.findViewById(R.id.recyclerView_Beverages);
        beverages_recyclerView.setHasFixedSize(true);
        beverages_recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        beverages_Adapter = new Beverages_Adapter(this.getContext(), beveragesArrayList, new Beverages_Adapter.BeverageListener() {
            @Override
            public void onDataSent(OrderItem selectedBeverage) {
                if(selectedMenuItemList.contains(selectedBeverage)){
                    for(OrderItem item: selectedMenuItemList){
                        if(item.getItem_name().equals(selectedBeverage.getItem_name())){
                            if(item.getItem_quantity() == 0){
                                selectedMenuItemList.remove(item);
                                break;
                            }else {
                                item.setItem_quantity(selectedBeverage.getItem_quantity());
                            }
                        }
                    }
                }else{
                    selectedMenuItemList.add(selectedBeverage);
                }
                orderSummary_adapter.notifyDataSetChanged();
            }
        });
        beverages_recyclerView.setAdapter(beverages_Adapter);

        orderSummary_recyclerView = view.findViewById(R.id.recyclerView_orderSummary);
        orderSummary_recyclerView.setHasFixedSize(true);
        orderSummary_recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        orderSummary_adapter = new OrderSummaryAdapter(this.getContext(), selectedMenuItemList);
        orderSummary_recyclerView.setAdapter(orderSummary_adapter);

    }

    private void getMenu()
    {
        foodCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        MenuItem food = document.toObject(MenuItem.class);
                        foodArrayList.add(food);
                        food_Adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        beveragesCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        MenuItem beverages = document.toObject(MenuItem.class);
                        beveragesArrayList.add(beverages);
                        beverages_Adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

}