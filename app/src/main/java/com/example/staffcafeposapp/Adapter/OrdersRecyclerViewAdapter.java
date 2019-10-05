package com.example.staffcafeposapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

public class OrdersRecyclerViewAdapter extends RecyclerView.Adapter<OrdersRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrdersRecyclerViewAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.row_orders, parent, false);
        final ViewHolder vHolder = new ViewHolder(view);

        vHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(orderList.get(vHolder.getAdapterPosition()).getOrderItemArrayList());
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

    private void showPopup(ArrayList<MenuItem> orderItemArrayList) {
        @SuppressLint("InflateParams") final View popupView = LayoutInflater.from(context).inflate(R.layout.orders_popup_window, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);

        RecyclerView recyclerView = popupView.findViewById(R.id.orders_popup_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
        PopupOrdersRecyclerViewAdapter adapter = new PopupOrdersRecyclerViewAdapter(context, orderItemArrayList);

        TextView order_total = popupView.findViewById(R.id.orders_total_data);
        order_total.setText(adapter.getOrder_total());

        recyclerView.setAdapter(adapter);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

    }
}
