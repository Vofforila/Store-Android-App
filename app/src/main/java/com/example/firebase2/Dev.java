package com.example.firebase2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Dev extends AppCompatActivity {

    String productName;
    String productPrice;
    String productCategory;
    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result ->
            {

                // Save the Image to Firebase Storage
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                    Uri selectedImageUri = result.getData().getData();

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference("Products");
                    StorageReference imagesRef = storageRef.child(productCategory).child(productName);

                    UploadTask uploadTask = imagesRef.putFile(selectedImageUri);

                    // Register observers to listen for the upload task
                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        // Handle successful uploads
                        // You can get the download URL of the uploaded file
                        imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            // Show a confirmation Message
                            Toast toast = Toast.makeText(Dev.this, "Produs Added!!", Toast.LENGTH_LONG);
                            toast.show();
                        });
                    }).addOnFailureListener(e -> {
                        // Handle unsuccessful uploads
                    });
                }
            }
    );
    private EditText productNameInput;
    private EditText productPriceInput;
    private EditText productCategoryInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev);

        // Get the palace where to put the data
        productNameInput = findViewById(R.id.numeProdusInput);
        productPriceInput = findViewById(R.id.pretProdusInput);
        productCategoryInput = findViewById(R.id.categorieProdusInput);
    }


    // Function to go back to Main
    public void GoToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void AddProdusToDatabase(View view) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Products");

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);


        productName = productNameInput.getText().toString().substring(0, 1).toUpperCase() + productNameInput.getText().toString().substring(1);
        productPrice = productPriceInput.getText().toString();
        productCategory = productCategoryInput.getText().toString().substring(0, 1).toUpperCase() + productCategoryInput.getText().toString().substring(1);


        ProductWithoutImage newProduct = new ProductWithoutImage(productName, productPrice);


        myRef.child(productCategory).child(productName).setValue(newProduct);


    }
}