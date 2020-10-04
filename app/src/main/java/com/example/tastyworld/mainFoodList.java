package com.example.tastyworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tastyworld.Module.FoodItems;
import com.example.tastyworld.viewHolder.foodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class mainFoodList extends AppCompatActivity {

    private DatabaseReference foodRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_food_list);

        foodRef = FirebaseDatabase.getInstance().getReference().child("Foods");
        recyclerView = findViewById(R.id.recyclerMenu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions <FoodItems>options  =
                new FirebaseRecyclerOptions.Builder<FoodItems>()
                .setQuery(foodRef,FoodItems.class)
                .build();
        FirebaseRecyclerAdapter<FoodItems, foodViewHolder> adapter =
                new FirebaseRecyclerAdapter<FoodItems, foodViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull foodViewHolder holder, int position, @NonNull final FoodItems model) {
                        holder.txtFoodName.setText(model.getProductName());
                        holder.txtFoodDes.setText(model.getDescription());
                        holder.txtFoodPrice.setText("Price ="+model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        //ch
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(mainFoodList.this,ProductDetailsActivity.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public foodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list,parent,false);
                        foodViewHolder holder = new foodViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}