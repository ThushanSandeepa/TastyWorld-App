package com.example.tastyworld;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class addFood extends AppCompatActivity {

    private Button addFoodBtn;
    private EditText productName,productDes,productPrice;
    private ImageView imageAdd;
    private static final int GalleryPick = 1;
    private Uri imageUri;
    private String pName,pDes,pPrice,saveCurrentDate,saveCurrentTime;
    private String productRandomKey,downloadImageUrl;
    private StorageReference foodImageRef;
    private DatabaseReference foodRef;

    private void clearControls(){
        productName.setText("");
        productDes.setText("");
        productPrice.setText("");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        foodImageRef = FirebaseStorage.getInstance().getReference().child("Product images");
        foodRef = FirebaseDatabase.getInstance().getReference().child("Foods");

        addFoodBtn = (Button) findViewById(R.id.food_submit);
        imageAdd = (ImageView) findViewById(R.id.addImage);
        productName = (EditText) findViewById(R.id.productName);
        productDes = (EditText) findViewById(R.id.productDes);
        productPrice = (EditText) findViewById(R.id.price);

        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();

            }
        });

        addFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateFoodData();
            }
        });
    }

    private void openGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GalleryPick && resultCode == RESULT_OK && data != null){

            imageUri = data.getData();
            imageAdd.setImageURI(imageUri);
        }
    }
    private void validateFoodData(){
        pDes = productDes.getText().toString();
        pPrice = productPrice.getText().toString();
        pName = productName.getText().toString();

        if(TextUtils.isEmpty(pName)){
            Toast.makeText(this,"Please enter a product name",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pDes)){
            Toast.makeText(this,"Please write description about your food",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pPrice)){
            Toast.makeText(this,"Please enter a product price",Toast.LENGTH_SHORT).show();
        }
        else if (imageUri == null){
            Toast.makeText(this,"Product image not added ",Toast.LENGTH_SHORT).show();
        }
        else {
            storeFoodInformation();
        }
    }

    private void storeFoodInformation() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = foodImageRef.child(imageUri.getLastPathSegment()+ productRandomKey+".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String  message = e.toString();
                Toast.makeText(addFood.this,"Error:"+message,Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(addFood.this,"Image uploaded successfully",Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){

                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(addFood.this,"product image save successfully",Toast.LENGTH_SHORT).show();

                            saveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfoToDatabase() {
        HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",pDes);
        productMap.put("image",downloadImageUrl);
        productMap.put("productName",pName);
        productMap.put("price",pPrice);

        foodRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(addFood.this,"product is added successfully",Toast.LENGTH_SHORT).show();
                            clearControls();

                        }else {
                            String message = task.getException().toString();
                            Toast.makeText(addFood.this,"Error"+ message,Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}