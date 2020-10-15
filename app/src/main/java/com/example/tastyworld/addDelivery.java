package com.example.tastyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tastyworld.Module.Card;
import com.example.tastyworld.Module.Delivery;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addDelivery extends AppCompatActivity {

    EditText fName,cNo,province,city,address;
    Button deliBtn;
    DatabaseReference reference;
    Delivery delivery;

    private void clearControls(){
        fName.setText("");
        cNo.setText("");
        province.setText("");
        city.setText("");
        address.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery);

        fName = findViewById(R.id.del_fName);
        cNo = findViewById(R.id.Del_phone);
        province = findViewById(R.id.Del_province);
        city = findViewById(R.id.Del_city);
        address = findViewById(R.id.Del_address);

        deliBtn = findViewById(R.id.Del_submit);

        delivery = new Delivery();

        deliBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference().child("Delivery");

                if(TextUtils.isEmpty(fName.getText().toString()))
                    Toast.makeText(getApplicationContext(),"please enter full name",Toast.LENGTH_SHORT).show();
                else if(!validatePhone())
                    return;
                else if (TextUtils.isEmpty(province.getText().toString()))
                    Toast.makeText(getApplicationContext(),"Please enter your province",Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(city.getText().toString()))
                    Toast.makeText(getApplicationContext(),"Please enter your city",Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(address.getText().toString()))
                    Toast.makeText(getApplicationContext(),"Please enter your address",Toast.LENGTH_SHORT).show();
                else{
                    //Take inputs from the user and assigning them to this instance(std) of the Student..
                    delivery.setfName(fName.getText().toString().trim());
                    delivery.setContactNo(cNo.getText().toString().trim());
                    delivery.setProvince(province.getText().toString().trim());
                    delivery.setCity(city.getText().toString().trim());
                    delivery.setAddress(address.getText().toString().trim());
                    //Insert in to the database...
                    reference.push().setValue(delivery);
                    //Feedback to the user via Toast...
                    Toast.makeText(getApplicationContext(),"Data Saved Successfully",Toast.LENGTH_SHORT).show();
                    clearControls();
                }
            }
        });
    }
//validate phone
    private boolean validatePhone() {
        String val = cNo.getText().toString();
        if (val.isEmpty()) {
            cNo.setError("Field cannot be empty");
            return false;
        } else if(val.length()!=10){
            cNo.setError("Invalid phone number");
            return false;
        }else {
            cNo.setError(null);
            return true;
        }
    }
}