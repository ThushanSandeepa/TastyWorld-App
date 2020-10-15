package com.example.tastyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

        regToLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register.this,login.class);
                startActivity(intent);
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance();
                databaseReference = rootNode.getReference().child("User");

                //check validation
                if(!validateUsername() || !validateEmail() || !validatePhone() ||!validatePassword())
                    return;
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
    //validations.....................................................................
    private boolean validateUsername() {
        String val = regName.getText().toString();
        if (val.isEmpty()) {
            regName.setError("Field cannot be empty");
            return false;
        }else if(val.length()>=12){
            regName.setError("Username too long");
            return false;
        } else{
            regName.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = regEmail.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        } else if(!val.matches(emailPattern)){
            regEmail.setError("Invalid email address");
            return false;
        }else {
            regEmail.setError(null);
            return true;
        }
    }

    private boolean validatePhone() {
        String val = regPhone.getText().toString();
        if (val.isEmpty()) {
            regPhone.setError("Field cannot be empty");
            return false;
        } else if(val.length()!=10){
            regPhone.setError("Invalid phone number");
            return false;
        }else {
            regEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = regPassword.getText().toString();
        if (val.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        }else if(val.length()<=4){
            regPassword.setError("password is too short");
            return false;
        } else{
            regPassword.setError(null);
            return true;
        }
    }
}