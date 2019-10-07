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


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{

    private Context context;
    private ArrayList<MenuItem> foodList;

    public MenuAdapter(Context context, ArrayList<MenuItem> foodList) {
        this.context = context;
        this.foodList = foodList;
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
        MenuItem food =  foodList.get(position);

        holder.textView_food.setText(String.valueOf(food.getItem_name()));
        holder.textView_foodPrice.setText(String.valueOf(food.getItem_price()));

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView_food, textView_foodPrice, textView_quantity;
        public Button button_decrease, button_increase;
        int quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_food = itemView.findViewById(R.id.textView_food_tong);
            textView_foodPrice = itemView.findViewById(R.id.textView_foodPrice_tong);
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
