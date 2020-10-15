package com.example.tastyworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tastyworld.Module.Card;
import com.example.tastyworld.Module.FoodItems;
import com.example.tastyworld.viewHolder.CardViewHolder;
import com.example.tastyworld.viewHolder.foodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class mainCardList extends AppCompatActivity {

    private DatabaseReference CardRef;
    private RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_card_list);

        CardRef = FirebaseDatabase.getInstance().getReference().child("Payment");
        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Card> options  =
                new FirebaseRecyclerOptions.Builder<Card>()
                        .setQuery(CardRef,Card.class)
                        .build();
        FirebaseRecyclerAdapter<Card, CardViewHolder> adapter =
                new FirebaseRecyclerAdapter<Card, CardViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CardViewHolder holder, int position, @NonNull final Card model) {

                        holder.txtCardNo.setText("Card No :"+model.getCardNo());
                        holder.txtCardName.setText("Card Name :"+model.getCardName());
                        holder.txtCardExp.setText("Exp Date :"+model.getCardDate());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[] = new CharSequence[]{
                                        "Edit",
                                        "Remove"
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(mainCardList.this);
                                builder.setTitle("Card Options: ");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if(i == 0){
                                            Intent intent = new Intent(mainCardList.this, addPayment.class);
                                            startActivity(intent);
                                        }
                                        if(i == 1){
//                                            CardRef = FirebaseDatabase.getInstance().getReference().child("Payment").child(model.getCardNo());
//                                            CardRef.removeValue();
//                                            Toast.makeText(getApplicationContext(),"Card Removed Successfully...",Toast.LENGTH_SHORT).show();
//
//                                            Intent intent = new Intent(mainCardList.this,mainCardList.class);
//                                                        startActivity(intent);

                                            CardRef.child("Payment").child(model.getCardNo()).removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {


                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(mainCardList.this,"Card removed Successfully...",Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(mainCardList.this,Payment.class);
                                                        startActivity(intent);

                                                    }

                                                }
                                            });

                                        }

                                    }
                                });

                                builder.show();


                            }
                        });

                    }

                    @NonNull
                    @Override
                    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_items_layout,parent,false);
                        CardViewHolder holder = new CardViewHolder(view);
                        return holder;
                    }
                };
//                    }

//                    @NonNull
//                    @Override
//                    public foodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list,parent,false);
//                        foodViewHolder holder = new foodViewHolder(view);
//                        return holder;
//                    }
//                };
        recycler_menu.setAdapter(adapter);
        adapter.startListening();
    }
}