package com.example.tastyworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.tastyworld.Module.Card;
import com.example.tastyworld.Module.FoodItems;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class displayPayment extends AppCompatActivity {

    RecyclerView recyview;
    myadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_payment);

        recyview = (RecyclerView)findViewById(R.id.recyview);
        recyview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Card> options  =
                new FirebaseRecyclerOptions.Builder<Card>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Payment"),Card.class)
                        .build();


        adapter = new myadapter(options);
        recyview.setAdapter(adapter);
    }
}