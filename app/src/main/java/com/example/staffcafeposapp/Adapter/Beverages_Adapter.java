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
import com.example.staffcafeposapp.R;

import java.util.ArrayList;

public class Beverages_Adapter extends RecyclerView.Adapter<Beverages_Adapter.ViewHolder> {

    private Context context;
    private ArrayList<MenuItem> beveragesList;

    public Beverages_Adapter(Context context, ArrayList<MenuItem> beveragesList) {
        this.context = context;
        this.beveragesList = beveragesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem beverages = beveragesList.get(position);

        holder.textView_beverages.setText(String.valueOf(beverages.getItem_name()));
        holder.textView_beveragesPrice.setText(String.format("%.2f",beverages.getItem_price()));


    }

    @Override
    public int getItemCount() {
        return beveragesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView_beverages, textView_beveragesPrice, textView_quantity;
        public Button button_decrease, button_increase;
        int quantity;

        public ViewHolder(View view) {
            super(view);
            textView_beverages = itemView.findViewById(R.id.textView_food_tong);
            textView_beveragesPrice = itemView.findViewById(R.id.textView_foodPrice_tong);
            textView_quantity = itemView.findViewById(R.id.textView_quantity);
            button_decrease = itemView.findViewById(R.id.button_decrease);
            button_increase = itemView.findViewById(R.id.button_increase);
            textView_quantity.setText("0");

            button_increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    quantity = quantity + 1;
                    textView_quantity.setText(String.valueOf(quantity));
                }
            });

            button_decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    quantity = quantity - 1;
                    textView_quantity.setText(String.valueOf(quantity));
                }
            });

        }
    }
}
