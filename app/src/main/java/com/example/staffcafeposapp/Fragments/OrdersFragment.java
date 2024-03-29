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

import com.example.staffcafeposapp.Adapter.OrdersRecyclerViewAdapter;
import com.example.staffcafeposapp.Model.MenuItem;
import com.example.staffcafeposapp.Model.Order;
import com.example.staffcafeposapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {
    private RecyclerView recyclerView;
    private OrdersRecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Order> orderArrayList;
    private ArrayList<MenuItem> menuArrayList;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Orders");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderArrayList = new ArrayList<>();
        menuArrayList = new ArrayList<>();
        getOrders();
        fetchMenu();

        recyclerView = view.findViewById(R.id.orders_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerViewAdapter = new OrdersRecyclerViewAdapter(this.getContext(), orderArrayList, menuArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void getOrders(){
        collectionReference.orderBy("order_id");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document: task.getResult()){
                        Order order = document.toObject(Order.class);
                        for(Order orderItem: orderArrayList){
                            if(order.getOrder_id().equals(orderItem.getOrder_id())){
                                orderItem.setOrderItemArrayList(order.getOrderItemArrayList());
                            }
                        }
                        orderArrayList.add(order);
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void fetchMenu(){
        menuArrayList = new ArrayList<>();

        CollectionReference foodCollectionReference = db.collection("Food");
        CollectionReference drinkCollectionReference = db.collection("Drink");

        foodCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document: task.getResult()){
                        MenuItem food = document.toObject(MenuItem.class);
                        menuArrayList.add(food);
                    }
                }
            }
        });

        drinkCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document: task.getResult()){
                        MenuItem drink = document.toObject(MenuItem.class);
                        menuArrayList.add(drink);
                    }
                }
            }
        });
    }
}
