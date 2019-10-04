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

import com.example.staffcafeposapp.Adapter.OrdersRecyclerViewAdapter;
import com.example.staffcafeposapp.Model.MenuItem;
import com.example.staffcafeposapp.Model.Order;
import com.example.staffcafeposapp.R;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {
    private TextView textView;
    private Button button;
    private RecyclerView recyclerView;
    private OrdersRecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Order> orderArrayList;
    private ArrayList<MenuItem> menuItemsArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.orders_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        orderArrayList = new ArrayList<>();

        orderArrayList.add(new Order(1, 1, 10, menuItemsArrayList));
        orderArrayList.add(new Order(2, 2, 20, menuItemsArrayList));
        orderArrayList.add(new Order(3, 3, 30, menuItemsArrayList));
        orderArrayList.add(new Order(4, 4, 40, menuItemsArrayList));

        recyclerViewAdapter = new OrdersRecyclerViewAdapter(this.getContext(), orderArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);

    }
}
