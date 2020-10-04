package com.example.tastyworld.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastyworld.Interface.ItemClickListner;
import com.example.tastyworld.R;

public class foodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtFoodName,txtFoodDes,txtFoodPrice;
    public ImageView imageView;
    public ItemClickListner listner;

    public foodViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.food_image);
        txtFoodName = (TextView) itemView.findViewById(R.id.food_name);
        txtFoodDes= (TextView) itemView.findViewById(R.id.food_des);
        txtFoodPrice= (TextView) itemView.findViewById(R.id.food_price);
    }
    public void setItemClickListner(ItemClickListner listner){

        this.listner = listner;

    }

    @Override
    public void onClick(View view) {
        listner.onClick(view, getAdapterPosition(),false);
    }
}
