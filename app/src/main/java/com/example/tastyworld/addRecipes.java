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

public class addRecipes extends AppCompatActivity {

    private Button addRecipesBtn;
    private EditText recipeName,recipeIng,recipeDirection;
    private ImageView imageAdd;
    private static final int GalleryPick = 1;
    private Uri imageUri;
    private String rName,rIng,rDir,saveCurrentDate,saveCurrentTime;
    private String productRandomKey,downloadImageUrl;
    private StorageReference recipesImageRef;
    private DatabaseReference recipesRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipes);

        recipesImageRef = FirebaseStorage.getInstance().getReference().child("Recipe images");
        recipesRef = FirebaseDatabase.getInstance().getReference().child("Recipes");

        addRecipesBtn = (Button) findViewById(R.id.addReciepie);
        imageAdd = (ImageView) findViewById(R.id.img_photo);
        recipeName = (EditText) findViewById(R.id.rname);
        recipeIng = (EditText) findViewById(R.id.ingredients);
        recipeDirection = (EditText) findViewById(R.id.directions);

        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();

            }
        });

        addRecipesBtn.setOnClickListener(new View.OnClickListener() {
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
        rName = recipeName.getText().toString();
        rIng = recipeIng.getText().toString();
        rDir = recipeDirection.getText().toString();

        if(imageUri == null){
            Toast.makeText(this,"Product image not added",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(rDir)){
            Toast.makeText(this,"Please write direction about your recipe",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(rIng)){
            Toast.makeText(this,"Please enter a recipe ingredients",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(rName)){
            Toast.makeText(this,"Please enter a recipe name ",Toast.LENGTH_SHORT).show();
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

        final StorageReference filePath = recipesImageRef.child(imageUri.getLastPathSegment()+ productRandomKey+".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String  message = e.toString();
                Toast.makeText(addRecipes.this,"Error:"+message,Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(addRecipes.this,"Image uploaded successfully",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(addRecipes.this,"product image save successfully",Toast.LENGTH_SHORT).show();

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
        productMap.put("direction",rDir);
        productMap.put("image",downloadImageUrl);
        productMap.put("recipeName",rName);
        productMap.put("ingredients",rIng);

        recipesRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(addRecipes.this,"product is added successfully",Toast.LENGTH_SHORT).show();
                        }else {
                            String message = task.getException().toString();
                            Toast.makeText(addRecipes.this,"Error"+ message,Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}