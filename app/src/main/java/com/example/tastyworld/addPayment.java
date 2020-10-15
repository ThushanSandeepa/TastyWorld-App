package com.example.tastyworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tastyworld.Module.Card;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class addPayment extends AppCompatActivity {

    EditText cardNo,cardName,expDate,cvvCard;
    Button cardBtn,btnShow,btnUpdate,btnDelete;
    DatabaseReference reference;
    Card card;

    private void clearControls(){
        cardNo.setText("");
        cardName.setText("");
        expDate.setText("");
        cvvCard.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        cardNo = findViewById(R.id.card_no);
        cardName = findViewById(R.id.card_name);
        expDate = findViewById(R.id.exp_card);
        cvvCard = findViewById(R.id.cvv_card);

        cardBtn = findViewById(R.id.pay_submit);
        btnShow = findViewById(R.id.pay_show);
        btnUpdate = findViewById(R.id.pay_update);
        btnDelete = findViewById(R.id.pay_delete);

        card = new Card();
        cardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference = FirebaseDatabase.getInstance().getReference().child("Payment");

//                    if(TextUtils.isEmpty(cardNo.getText().toString()))
//                        Toast.makeText(getApplicationContext(),"please enter card number",Toast.LENGTH_SHORT).show();
                    if(!validateCardNo() || !validateCardName() || !validatexpDate() || !validateCVV())
                        return;
//                    else if(TextUtils.isEmpty(cardName.getText().toString()))
//                        Toast.makeText(getApplicationContext(),"please enter name on a card",Toast.LENGTH_SHORT).show();
//                    else if (TextUtils.isEmpty(expDate.getText().toString()))
//                        Toast.makeText(getApplicationContext(),"Please enter an expire date",Toast.LENGTH_SHORT).show();
                    else{
                        //Take inputs from the user and assigning them to this instance(std) of the Student..
                        card.setCardNo(cardNo.getText().toString().trim());
                        card.setCardName(cardName.getText().toString().trim());
                        card.setCardDate(expDate.getText().toString().trim());
                        card.setCardCVV(cvvCard.getText().toString().trim());
                        //Insert in to the database...
                        //reference.push().setValue(card);
                        reference.child("card10").setValue(card);
                        //Feedback to the user via Toast...
                        Toast.makeText(getApplicationContext(),"Data Saved Successfully",Toast.LENGTH_SHORT).show();
                        clearControls();
                    }

            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference readRef = FirebaseDatabase.getInstance().getReference().child("Payment").child("card10");
                readRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren()){
                            cardNo.setText(snapshot.child("cardNo").getValue().toString());
                            cardName.setText(snapshot.child("cardName").getValue().toString());
                            expDate.setText(snapshot.child("cardDate").getValue().toString());
                            cvvCard.setText(snapshot.child("cardCVV").getValue().toString());

                        }
                        else
                            Toast.makeText(getApplicationContext(),"No Source to Display",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference updRef = FirebaseDatabase.getInstance().getReference().child("Payment");
                updRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("card10"))
                        {
                            try{


                                card.setCardNo(cardNo.getText().toString().trim());
                                card.setCardName(cardName.getText().toString().trim());
                                card.setCardDate(expDate.getText().toString().trim());
                                card.setCardCVV(cvvCard.getText().toString().trim());

                                reference = FirebaseDatabase.getInstance().getReference().child("Payment").child("card10");
                                reference.setValue(card);
                                clearControls();

                                Toast.makeText(getApplicationContext(), "Data Updated Sucessfully", Toast.LENGTH_SHORT).show();

                            }

                            catch(Exception e)
                            {
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "No Source to Update", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference delRef = FirebaseDatabase.getInstance().getReference().child("Payment");
                delRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("card10")) {
                            reference = FirebaseDatabase.getInstance().getReference().child("Payment").child("card10");
                            reference.removeValue();
                            clearControls();
                            Toast.makeText(getApplicationContext(), "Data Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(),"No source to delete",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });



    }

    //validations
    private boolean validateCardNo() {
        String val = cardNo.getText().toString();
        if (val.isEmpty()) {
            cardNo.setError("Field cannot be empty");
            return false;
        }else if(val.length() != 10){
            cardNo.setError("Card No is not valid");
            return false;
        } else{
            cardNo.setError(null);
            return true;
        }
    }

    private boolean validateCardName(){
        String val = cardName.getText().toString();
        if (val.isEmpty()) {
            cardName.setError("Field cannot be empty");
            return false;
        }else if(val.length() <= 4){
            cardName.setError("Card on Name is too short");
            return false;
        } else{
            cardName.setError(null);
            return true;
        }
    }

    private boolean validatexpDate(){
        String val = expDate.getText().toString();
        String datePattern = "[0-1][0-9]+/+[0-9][0-9]";
        if (val.isEmpty()) {
            expDate.setError("Field cannot be empty");
            return false;
        }else if(!val.matches(datePattern)){
            expDate.setError("Invalid Expiration Date");
            return false;
        } else{
            expDate.setError(null);
            return true;
        }
    }

    private boolean validateCVV(){
        String val = cvvCard.getText().toString();
        String datePattern = "[0-9][0-9][0-9]";
        if (val.isEmpty()) {
            cvvCard.setError("Field cannot be empty");
            return false;
        }else if(!val.matches(datePattern)){
            cvvCard.setError("Invalid CVV");
            return false;
        } else{
            cvvCard.setError(null);
            return true;
        }
    }

}