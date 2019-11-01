package com.example.staffcafeposapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
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


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{
    private FoodListener foodListener;
    private Context context;
    private ArrayList<MenuItem> foodList;

    public MenuAdapter(Context context, ArrayList<MenuItem> foodList, FoodListener listener) {
        this.context = context;
        this.foodList = foodList;
        this.foodListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        MenuItem food =  foodList.get(position);

        holder.textView_food.setText(String.valueOf(food.getItem_name()));
        holder.textView_foodPrice.setText(String.format("%.2f",food.getItem_price()));

        final OrderItem orderItem = new OrderItem(foodList.get(position).getItem_name(), foodList.get(position).getItem_price(), 0);

        holder.button_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderItem.setItem_quantity(orderItem.getItem_quantity()+1);
                holder.textView_quantity.setText(String.valueOf(orderItem.getItem_quantity()));
                foodListener.onDataSent(orderItem);
            }
        });

        holder.button_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(orderItem.getItem_quantity() != 0){
                    orderItem.setItem_quantity(orderItem.getItem_quantity()-1);
                    holder.textView_quantity.setText(String.valueOf(orderItem.getItem_quantity()));

                    foodListener.onDataSent(orderItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView_food, textView_foodPrice, textView_quantity;
        Button button_decrease, button_increase;
        public View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            textView_food = itemView.findViewById(R.id.textView_food_tong);
            textView_foodPrice = itemView.findViewById(R.id.textView_foodPrice_tong);
            textView_quantity = itemView.findViewById(R.id.textView_quantity);
            button_decrease = itemView.findViewById(R.id.button_decrease);
            button_increase = itemView.findViewById(R.id.button_increase);

            textView_quantity.setText("0");
        }
    }

    public interface FoodListener{
        void onDataSent(OrderItem selectedFood);
    }



}
