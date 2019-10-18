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
import com.example.staffcafeposapp.Model.OrderItem;
import com.example.staffcafeposapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Beverages_Adapter extends RecyclerView.Adapter<Beverages_Adapter.ViewHolder> {
    private BeverageListener beverageListener;
    private Context context;
    private ArrayList<MenuItem> beveragesList;

    public Beverages_Adapter(Context context, ArrayList<MenuItem> beveragesList, BeverageListener listener) {
        this.context = context;
        this.beveragesList = beveragesList;
        this.beverageListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        MenuItem beverages = beveragesList.get(position);

        holder.textView_beverages.setText(String.valueOf(beverages.getItem_name()));
        holder.textView_beveragesPrice.setText(String.valueOf(beverages.getItem_price()));


        final OrderItem orderItem = new OrderItem(beveragesList.get(position).getItem_name(), beveragesList.get(position).getItem_price(), 0);

        holder.button_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderItem.setItem_quantity(orderItem.getItem_quantity()+1);
                holder.textView_quantity.setText(String.valueOf(orderItem.getItem_quantity()));
                beverageListener.onDataSent(orderItem);
            }
        });

        holder.button_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderItem.setItem_quantity(orderItem.getItem_quantity()-1);
                holder.textView_quantity.setText(String.valueOf(orderItem.getItem_quantity()));

                beverageListener.onDataSent(orderItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beveragesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_beverages, textView_beveragesPrice, textView_quantity;
        Button button_decrease, button_increase;

        ViewHolder(View view) {
            super(view);
            textView_beverages = itemView.findViewById(R.id.textView_food_tong);
            textView_beveragesPrice = itemView.findViewById(R.id.textView_foodPrice_tong);
            textView_quantity = itemView.findViewById(R.id.textView_quantity);
            button_decrease = itemView.findViewById(R.id.button_decrease);
            button_increase = itemView.findViewById(R.id.button_increase);
            textView_quantity.setText("0");

        }
    }

    public interface BeverageListener{
        void onDataSent(OrderItem selectedBeverage);
    }
}
