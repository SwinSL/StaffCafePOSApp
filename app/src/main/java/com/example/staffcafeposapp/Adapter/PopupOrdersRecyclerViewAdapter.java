package com.example.staffcafeposapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffcafeposapp.Model.MenuItem;
import com.example.staffcafeposapp.Model.Order;
import com.example.staffcafeposapp.Model.OrderItem;
import com.example.staffcafeposapp.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PopupOrdersRecyclerViewAdapter extends RecyclerView.Adapter<PopupOrdersRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private double order_total;
    private Order order;

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

        String price = String.format("%.2f", orderItem.getItem_price());

        holder.item_name.setText(orderItem.getItem_name());
        holder.item_quantity.setText(String.valueOf(orderItem.getItem_quantity()));
        holder.item_price.setText(price);

        holder.removebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order.getOrderItemArrayList().remove(holder.getAdapterPosition());
                updateOrderItem(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
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

        for(MenuItem item: order.getOrderItemArrayList()){
            order_total+=item.getItem_price();
        }

        String total = String.format("%.2f", order_total);
        total = "RM" + total;

        return total;
    }

    public void updateOrderItem(int adapterPosition){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Orders").document("Order02");

        documentReference.set(order);
    }
}
