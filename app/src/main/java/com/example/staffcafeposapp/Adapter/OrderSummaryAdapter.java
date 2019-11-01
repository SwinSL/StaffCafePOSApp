package com.example.staffcafeposapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffcafeposapp.Model.OrderItem;
import com.example.staffcafeposapp.R;

import java.util.ArrayList;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<OrderItem> orderSummaryArray;

    public OrderSummaryAdapter(Context context, ArrayList<OrderItem> orderSummaryArray) {
        this.context = context;
        this.orderSummaryArray = orderSummaryArray;
    }

    @NonNull
    @Override
    public OrderSummaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ordersummary,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSummaryAdapter.ViewHolder holder, int position) {
        OrderItem orderitem = orderSummaryArray.get(position);

        holder.orderSummary_itemName.setText(orderitem.getItem_name());
        holder.orderSummary_itemQuantity.setText(String.valueOf(orderitem.getItem_quantity()));
        holder.orderSummary_itemPrice.setText(String.valueOf(orderitem.getItem_price()*orderitem.getItem_quantity()));
    }

    @Override
    public int getItemCount() {
        return orderSummaryArray.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView orderSummary_itemName, orderSummary_itemQuantity, orderSummary_itemPrice;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderSummary_itemName = itemView.findViewById(R.id.orderSummary_itemDetails);
            orderSummary_itemQuantity = itemView.findViewById(R.id.orderSummary_itemQuantity);
            orderSummary_itemPrice = itemView.findViewById(R.id.orderSummary_itemPrice);
        }
    }
}
