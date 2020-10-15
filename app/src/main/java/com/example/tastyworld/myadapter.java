package com.example.tastyworld;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastyworld.Module.Card;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class myadapter extends FirebaseRecyclerAdapter<Card,myadapter.myviewholder> {

    public myadapter(@NonNull FirebaseRecyclerOptions<Card> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull Card model) {

        holder.cardName.setText(model.getCardName());
        holder.cardNo.setText(model.getCardNo());
        holder.exp.setText(model.getCardDate());
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);

    }

    class myviewholder extends RecyclerView.ViewHolder{

        TextView cardNo, cardName, exp;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            cardNo = (TextView)itemView.findViewById(R.id.cardNo);
            cardName = (TextView)itemView.findViewById(R.id.cardName);
            exp = (TextView)itemView.findViewById(R.id.exp);
        }
    }
}
