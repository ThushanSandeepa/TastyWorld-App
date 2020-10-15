package com.example.tastyworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    EditText username, password;
    Button loginBtn, regCallBtn, frogetPWBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.log_username);
        password = (EditText) findViewById(R.id.log_password);
        regCallBtn=(Button) findViewById(R.id.callregBtn);

        regCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this,register.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateUsername() {
        String val = username.getText().toString();
        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = password.getText().toString();
        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void loginUser(View view) {
        //validate
         if (!validateUsername() || !validatePassword()) {
              return;
          } else {
        isUser();
          }
    }

    private void isUser() {
        final String userInputUsername = username.getText().toString().trim();
        final String userInputPassword = password.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");

        Query checkUser = reference.orderByChild("username").equalTo(userInputUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    username.setError(null);

                    String passwordFromDB = snapshot.child(userInputUsername).child("password").getValue(String.class);
                    if (passwordFromDB.equals(userInputPassword)) {
                        String usernameFromDB = snapshot.child(userInputUsername).child("username").getValue(String.class);
                        String emailFromDB = snapshot.child(userInputUsername).child("email").getValue(String.class);
                        String phoneFromDB = snapshot.child(userInputUsername).child("phone").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("phone", phoneFromDB);
                        intent.putExtra("password", passwordFromDB);

                        startActivity(intent);
                    } else {
                         password.setError("Wrong password");
                         password.requestFocus();
                    }
                } else {
                     username.setError("No such user exist");
                    password.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}