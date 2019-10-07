package com.example.staffcafeposapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffcafeposapp.Model.MenuItem;
import com.example.staffcafeposapp.Model.Order;
import com.example.staffcafeposapp.Model.OrderItem;
import com.example.staffcafeposapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PopupOrdersRecyclerViewAdapter extends RecyclerView.Adapter<PopupOrdersRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private double order_total;
    private Order order;
    private TextView total_textview;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public PopupOrdersRecyclerViewAdapter(Context context, Order order) {
        this.context = context;
        this.order = order;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.orders_popup_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        OrderItem orderItem = order.getOrderItemArrayList().get(position);

        String price = String.format("%.2f", (orderItem.getItem_price()*orderItem.getItem_quantity()));

        holder.item_name.setText(orderItem.getItem_name());
        holder.item_quantity.setText(String.valueOf(orderItem.getItem_quantity()));
        holder.item_price.setText(price);

        holder.removebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order.getOrderItemArrayList().remove(holder.getAdapterPosition());
                updateOrderItem();
                notifyItemRemoved(holder.getAdapterPosition());
                total_textview.setText(getOrder_total());
            }
        });
    }

    @Override
    public int getItemCount() {
        return order.getOrderItemArrayList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView item_name;
        private TextView item_price;
        private TextView item_quantity;
        private Button removebtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.orders_item_name);
            item_price = itemView.findViewById(R.id.orders_item_price);
            item_quantity = itemView.findViewById(R.id.orders_item_quantity);
            removebtn = itemView.findViewById(R.id.orders_popup_removebtn);
        }
    }

    public String getOrder_total() {
        order_total = 0;

        for(OrderItem item: order.getOrderItemArrayList()){
            order_total+=(item.getItem_price()*item.getItem_quantity());
        }

        String total = String.format("%.2f", order_total);
        total = "RM" + total;

        return total;
    }

    public void updateOrderItem(){
        DocumentReference documentReference = db.collection("Orders").document(order.getOrder_id());

        documentReference.set(order);
    }

    public void getTotalTextview(TextView total_textview){
        this.total_textview = total_textview;
    }

}
