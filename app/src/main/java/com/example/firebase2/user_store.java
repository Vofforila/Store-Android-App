package com.example.firebase2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class user_store extends AppCompatActivity {

    List<String> categories = new ArrayList<>();
    List<Product> itemList = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Products");
    Uri imageURl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_store);
        UpdateDropDownList();
    }

    // Function to go back to Main
    public void GoToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Function to go to the Basket
    public void GoToBasket(View view) {
        Intent intent = new Intent(this, user_basket.class);
        startActivity(intent);
    }

    public void UpdateDropDownList() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> dictionary = (Map<String, Object>) dataSnapshot.getValue();
                categories = new ArrayList<>();
                for (Map.Entry<String, Object> e : dictionary.entrySet()) {
                    String data = e.getKey();
                    categories.add(data);
                }
                // Function to Update the data coresponding to the category
                UpdateData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void UpdateData() {


        // Creating the DropDown Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner categorySpinner = findViewById(R.id.dropDown);
        categorySpinner.setAdapter(adapter);

        // Set a listener for the DropDown Spinner
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                String selectedCategory = (String) parentView.getItemAtPosition(position);
                myRef = database.getReference("Products").child(selectedCategory);


                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        itemList.clear();

                        // Getting the data for the Product
                        for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {

                            List<String> data = new ArrayList<>();
                            for (DataSnapshot itemSnapshot : categorySnapshot.getChildren()) {
                                // Iterate through each item in the category
                                String itemValue = String.valueOf(itemSnapshot.getValue());
                                data.add(itemValue);
                            }

                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReference("Products");

                            // Getting the Image URl
                            storageRef.child(selectedCategory).child(data.get(0)).getDownloadUrl().addOnSuccessListener(uri -> {
                                imageURl = uri;

                                // Adding the Product to the ListView
                                itemList.add(new Product(data.get(0), data.get(1), imageURl.toString()));
                                ProductAdapter adapter1 = new ProductAdapter(user_store.this, itemList, false);
                                ListView listView = findViewById(R.id.listaProduse);
                                listView.setAdapter(adapter1);
                            }).addOnFailureListener(exception -> {
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }
}