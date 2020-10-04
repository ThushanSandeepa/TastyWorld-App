package com.example.tastyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tastyworld.Module.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    //variables
    EditText regName,regEmail,regPhone,regPassword;
    Button regBtn,regToLogBtn;

    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;

    //Method to clear all user inputs
    private void clearControls(){
        regName.setText("");
        regEmail.setText("");
        regPhone.setText("");
        regPassword.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regName = findViewById(R.id.reg_username);
        regEmail = findViewById(R.id.reg_email);
        regPhone = findViewById(R.id.reg_phone);
        regPassword = findViewById(R.id.reg_password);
        regBtn = findViewById(R.id.reg_btn);
        regToLogBtn = findViewById(R.id.reg_logBtn);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance();
                databaseReference = rootNode.getReference().child("User");

                if(TextUtils.isEmpty(regName.getText().toString()))
                    Toast.makeText(getApplicationContext(),"please enter an Name",Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(regEmail.getText().toString()))
                    Toast.makeText(getApplicationContext(),"please enter  a Email",Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(regPhone.getText().toString()))
                    Toast.makeText(getApplicationContext(),"Please enter an Phone",Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(regPassword.getText().toString()))
                    Toast.makeText(getApplicationContext(),"Please enter an Password",Toast.LENGTH_SHORT).show();
                else {
                    //get all the values
                    String username = regName.getText().toString().trim();
                    String email = regEmail.getText().toString().trim();
                    String phone = regPhone.getText().toString().trim();
                    String password = regPassword.getText().toString().trim();

                    User user = new User(username, email, phone, password);

                    //Feedback to the user via Toast...
                    Toast.makeText(getApplicationContext(),"Data Saved Successfully",Toast.LENGTH_SHORT).show();
                    clearControls();

                    databaseReference.child(username).setValue(user);
                }
            }
        });
    }
}