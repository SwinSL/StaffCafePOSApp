package com.example.staffcafeposapp.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffcafeposapp.Adapter.Beverages_Adapter;
import com.example.staffcafeposapp.Adapter.MenuAdapter;
import com.example.staffcafeposapp.Adapter.OrderSummaryAdapter;
import com.example.staffcafeposapp.Model.MenuItem;
import com.example.staffcafeposapp.Model.Order;
import com.example.staffcafeposapp.Model.OrderItem;
import com.example.staffcafeposapp.Model.Tables;
import com.example.staffcafeposapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

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
    private TextView totalPrice_textView;
    private Spinner tableNo_spinner;
    private Button submitButton;
    private double total;
    private String todayString;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupTableSpinner();

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
                setOrderTotal();
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
                setOrderTotal();
            }
        });
        beverages_recyclerView.setAdapter(beverages_Adapter);

        orderSummary_recyclerView = view.findViewById(R.id.recyclerView_orderSummary);
        orderSummary_recyclerView.setHasFixedSize(true);
        orderSummary_recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        orderSummary_adapter = new OrderSummaryAdapter(this.getContext(), selectedMenuItemList);
        orderSummary_recyclerView.setAdapter(orderSummary_adapter);

        tableNo_spinner = view.findViewById(R.id.spinner_tableNo);
        totalPrice_textView = view.findViewById(R.id.textView_totalPrice);
        submitButton = view.findViewById(R.id.button_orderSubmit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedMenuItemList.size() > 0 && !tableNo_spinner.getSelectedItem().toString().isEmpty()) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Submit Order")
                            .setMessage("Are you sure you want to submit this order?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Date todayDate = Calendar.getInstance().getTime();
                                    SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
                                    todayString = formatter.format(todayDate);

                                    final int[] dailyOrderCounter = {0};
                                    CollectionReference collectionReference = db.collection("Orders");
                                    collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (DocumentSnapshot document : task.getResult()) {
                                                    Order order = document.toObject(Order.class);

                                                    assert order != null;
                                                    if (order.getOrder_date().equals(todayString)) {
                                                        dailyOrderCounter[0] = Integer.parseInt(order.getOrder_id().substring(9));
                                                    }
                                                }

                                                submitOrder(dailyOrderCounter[0]);
                                            }
                                        }
                                    });
                                }

                            })
                            .setNegativeButton("No", null)
                            .show();
                }else{
                    Toast.makeText(getContext(), "Select at least 1 item and enter table no.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getMenu()
    {
        foodCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult())){
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
                    for(QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult())){
                        MenuItem beverages = document.toObject(MenuItem.class);
                        beveragesArrayList.add(beverages);
                        beverages_Adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void setupTableSpinner(){
        final ArrayList<String> tableNoArrayList = new ArrayList<>();
        CollectionReference tableCollectionReference = db.collection("Tables");
        tableCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult()))
                    {
                        Tables tables = document.toObject(Tables.class);
                        if(tables.getTableStatus().equals("Available")){
                            tableNoArrayList.add(tables.getTableNo());
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, tableNoArrayList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    tableNo_spinner.setAdapter(adapter);
                }
            }
        });


    }


    private void setOrderTotal(){
        total = 0;
        for(OrderItem item: selectedMenuItemList){
            total += (item.getItem_quantity()*item.getItem_price());
        }

        totalPrice_textView.setText(String.format("RM%s", total));
    }

    private void submitOrder(int noOfDailyOrder){
        String formatted_noOfDailyOrder = String.format("%02d", noOfDailyOrder+1);
        Order order = new Order(todayString + "-" + formatted_noOfDailyOrder, tableNo_spinner.getSelectedItem().toString(), total, selectedMenuItemList, todayString);
        DocumentReference documentReference = db.collection("Orders").document(order.getOrder_id());
        documentReference.set(order);
        updateTableStatus();
        Toast.makeText(getContext(), "Order Successful!", Toast.LENGTH_SHORT).show();
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new OrdersFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void updateTableStatus(){
        final CollectionReference tableCollectionReference = db.collection("Tables");
        tableCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult()))
                    {
                        Tables table = document.toObject(Tables.class);
                        if(table.getTableNo().equals(tableNo_spinner.getSelectedItem().toString())){
                            table.setTableStatus("Not Available");
                            DocumentReference documentReference = tableCollectionReference.document(table.getTableNo());
                            documentReference.set(table);
                        }
                    }
                }
            }
        });
    }
}