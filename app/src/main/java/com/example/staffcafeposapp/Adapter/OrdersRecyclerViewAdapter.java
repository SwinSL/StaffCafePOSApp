package com.example.staffcafeposapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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
    private ArrayList<MenuItem> menuItemArrayList;
    private ArrayList<String> menuStringArray;
    private String[] menuArr;
    private Button addItemBtn, paymentbtn;
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
        paymentbtn = popupView.findViewById(R.id.payment_button);


        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!order.getOrder_status().equals("Paid")) {
                    String selItem = menuSelectionSpinner.getSelectedItem().toString();
                    int quantity;
                    if (!itemQuantitySel.getText().toString().isEmpty()) {
                        quantity = Integer.parseInt(itemQuantitySel.getText().toString());

                        for (MenuItem menu : menuItemArrayList) {
                            if (menu.getItem_name().equals(selItem)) {
                                OrderItem orderItem = new OrderItem(menu.getItem_name(), menu.getItem_price(), quantity);
                                order.getOrderItemArrayList().add(orderItem);
                                order_total.setText(adapter.getOrder_total());
                                adapter.updateOrderItem();
                            }
                        }
                    } else {
                        Toast.makeText(context, "Please enter quantity", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "Order has been paid.", Toast.LENGTH_SHORT).show();
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

        paymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!order.getOrder_status().equals("Paid")) {
                    popupWindow.dismiss();
                    @SuppressLint("InflateParams") final View paymentView = LayoutInflater.from(context).inflate(R.layout.payment_window, null);
                    final PopupWindow paymentWindow = new PopupWindow(paymentView, 400, WindowManager.LayoutParams.WRAP_CONTENT);

                    TextView total = paymentView.findViewById(R.id.payment_total_data);
                    total.setText(adapter.getOrder_total());

                    final TextView change_text = paymentView.findViewById(R.id.calculate_change_data);

                    final EditText amountPaid = paymentView.findViewById(R.id.amount_paid_input);
                    amountPaid.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if (!amountPaid.getText().toString().isEmpty()) {
                                double change = Double.parseDouble(amountPaid.getText().toString()) - order.getOrder_total();
                                change_text.setText(String.valueOf(change));
                            }
                        }
                    });

                    Button confirmBtn = paymentView.findViewById(R.id.confirmPaymentbtn);
                    confirmBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Double.parseDouble(change_text.getText().toString()) >= 0) {
                                order.setOrder_status("Paid");
                                adapter.updateOrderItem();
                                notifyDataSetChanged();
                                paymentWindow.dismiss();
                            } else {
                                Toast.makeText(context, "Invalid Amount", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    paymentWindow.setOutsideTouchable(true);
                    paymentWindow.setFocusable(true);
                    paymentWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                }else{
                    Toast.makeText(context, "Order has been paid.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void setMenu(){
        menuStringArray = new ArrayList<>();

        for (MenuItem menu: menuItemArrayList){
            menuStringArray.add(menu.getItem_name());
        }

        menuArr = new String[menuStringArray.size()];
        menuArr = menuStringArray.toArray(menuArr);
    }

}

