package com.example.staffcafeposapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffcafeposapp.Model.Order;
import com.example.staffcafeposapp.Model.OrderItem;
import com.example.staffcafeposapp.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PopupOrdersRecyclerViewAdapter extends RecyclerView.Adapter<PopupOrdersRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private double order_total;
    private Order order;
    private TextView total_textview;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    PopupOrdersRecyclerViewAdapter(Context context, Order order) {
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
                if(order.getOrder_status().equals("Not Paid")) {
                    if(order.getOrderItemArrayList().size() != 1){
                        order.getOrderItemArrayList().remove(holder.getAdapterPosition());
                        total_textview.setText(getOrder_total());
                        updateOrderItem();
                        notifyItemRemoved(holder.getAdapterPosition());
                    }else{
                        Toast.makeText(context, "There must be at least 1 order item in an order.", Toast.LENGTH_LONG).show();
                    }
                }else if(order.getOrder_status().equals("Paid")){
                    Toast.makeText(context, "Order has been paid.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Order has been cancelled.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return order.getOrderItemArrayList().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView item_name;
        private TextView item_price;
        private TextView item_quantity;
        private Button removebtn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.orders_item_name);
            item_price = itemView.findViewById(R.id.orders_item_price);
            item_quantity = itemView.findViewById(R.id.orders_item_quantity);
            removebtn = itemView.findViewById(R.id.orders_popup_removebtn);
        }
    }

    String getOrder_total() {
        order_total = 0;

        for(OrderItem item: order.getOrderItemArrayList()){
            order_total+=(item.getItem_price()*item.getItem_quantity());
        }

        order.setOrder_total(order_total);

        String total = String.format("%.2f", order_total);
        total = "RM" + total;

        return total;
    }

    void updateOrderItem(){
        DocumentReference documentReference = db.collection("Orders").document(order.getOrder_id());
        documentReference.set(order);
        notifyDataSetChanged();
    }

    void getTotalTextview(TextView total_textview){
        this.total_textview = total_textview;
    }
}
