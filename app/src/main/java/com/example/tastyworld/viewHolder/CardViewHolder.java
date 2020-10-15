package com.example.tastyworld.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastyworld.Interface.ItemClickListner;
import com.example.tastyworld.R;

public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtCardNo, txtCardName, txtCardExp;
    public ItemClickListner listner;

    public CardViewHolder(@NonNull View itemView) {
        super(itemView);

        txtCardNo = (TextView) itemView.findViewById(R.id.cno);
        txtCardName = (TextView) itemView.findViewById(R.id.cname);
        txtCardExp = (TextView) itemView.findViewById(R.id.cexp);
    }


    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {

        listner.onClick(view, getAdapterPosition(), false);
    }
}
