package com.example.staffcafeposapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffcafeposapp.Model.MenuItem;
import com.example.staffcafeposapp.Model.Order;
import com.example.staffcafeposapp.Model.OrderItem;
import com.example.staffcafeposapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrdersRecyclerViewAdapter extends RecyclerView.Adapter<OrdersRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Order> orderList;
    private HashMap<String, Double> menuMap;
    private ArrayList<MenuItem> menuItemArrayList;
    private ArrayList<String> menuStringArray;
    private String[] menuArr;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button addItemBtn;
    private Spinner menuSelectionSpinner;
    private EditText itemQuantitySel;

    public OrdersRecyclerViewAdapter(Context context, List<Order> orderList, ArrayList<MenuItem> menuItemArrayList) {
        this.context = context;
        this.orderList = orderList;
        this.menuItemArrayList = menuItemArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.row_orders, parent, false);
        final ViewHolder vHolder = new ViewHolder(view);
        vHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(orderList.get(vHolder.getAdapterPosition()));
            }
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.order_id.setText(String.valueOf(order.getOrder_id()));
        holder.table_no.setText(String.valueOf(order.getTable_no()));
        holder.order_status.setText(order.getOrder_status());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView order_id;
        public TextView table_no;
        public TextView order_status;
        public View view;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            view = itemView;
            order_id = itemView.findViewById(R.id.textview_orders_orderid_data);
            table_no = itemView.findViewById(R.id.textview_orders_tableno_data);
            order_status = itemView.findViewById(R.id.textview_orders_status_data);

        }
    }

    private void showPopup(final Order order) {
        setMenu();
        @SuppressLint("InflateParams") final View popupView = LayoutInflater.from(context).inflate(R.layout.orders_popup_window, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);

        RecyclerView recyclerView = popupView.findViewById(R.id.orders_popup_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
        final PopupOrdersRecyclerViewAdapter adapter = new PopupOrdersRecyclerViewAdapter(context, order);

        final TextView order_total = popupView.findViewById(R.id.orders_total_data);
        order_total.setText(adapter.getOrder_total());

        menuSelectionSpinner = popupView.findViewById(R.id.orders_popup_spinner);
        addItemBtn = popupView.findViewById(R.id.orders_popup_addItembtn);
        itemQuantitySel = popupView.findViewById(R.id.orders_popup_quantitySel);

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selItem = menuSelectionSpinner.getSelectedItem().toString();
                int quantity = 0;
                if(!itemQuantitySel.getText().toString().isEmpty()) {
                    quantity = Integer.parseInt(itemQuantitySel.getText().toString());

                    for(MenuItem menu: menuItemArrayList){
                        if(menu.getItem_name() == selItem){
                            OrderItem orderItem = new OrderItem(menu.getItem_name(), menu.getItem_price(), quantity);
                            order.getOrderItemArrayList().add(orderItem);
                            adapter.notifyDataSetChanged();
                            adapter.updateOrderItem();
                            order_total.setText(adapter.getOrder_total());
                        }
                    }
                }else{
                    Toast.makeText(context, "Please enter quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adapter.getTotalTextview(order_total);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, menuArr);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        menuSelectionSpinner.setAdapter(spinnerAdapter);

        recyclerView.setAdapter(adapter);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);


    }


    private void setMenu(){
        menuMap = new HashMap<>();
        menuStringArray = new ArrayList<>();


        for (MenuItem menu: menuItemArrayList){
            menuMap.put(menu.getItem_name(), menu.getItem_price());
            menuStringArray.add(menu.getItem_name());
        }

        menuArr = new String[menuStringArray.size()];
        menuArr = menuStringArray.toArray(menuArr);
    }

}

