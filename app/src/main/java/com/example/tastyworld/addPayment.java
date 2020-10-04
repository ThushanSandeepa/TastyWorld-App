package com.example.tastyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tastyworld.Module.Card;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addPayment extends AppCompatActivity {

    EditText cardNo,cardName,expDate,cvvCard;
    Button cardBtn;
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

        cardBtn = findViewById(R.id.food_submit);

        card = new Card();
        cardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference = FirebaseDatabase.getInstance().getReference().child("Payment");

                    if(TextUtils.isEmpty(cardNo.getText().toString()))
                        Toast.makeText(getApplicationContext(),"please enter card number",Toast.LENGTH_SHORT).show();
                    else if(TextUtils.isEmpty(cardName.getText().toString()))
                        Toast.makeText(getApplicationContext(),"please enter name on a card",Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(expDate.getText().toString()))
                        Toast.makeText(getApplicationContext(),"Please enter an expire date",Toast.LENGTH_SHORT).show();
                    else{
                        //Take inputs from the user and assigning them to this instance(std) of the Student..
                        card.setCardNo(cardNo.getText().toString().trim());
                        card.setCardName(cardName.getText().toString().trim());
                        card.setCardDate(expDate.getText().toString().trim());
                        card.setCardCVV(cvvCard.getText().toString().trim());
                        //Insert in to the database...
                        reference.push().setValue(card);
                        //Feedback to the user via Toast...
                        Toast.makeText(getApplicationContext(),"Data Saved Successfully",Toast.LENGTH_SHORT).show();
                        clearControls();
                    }

            }
        });
    }
}