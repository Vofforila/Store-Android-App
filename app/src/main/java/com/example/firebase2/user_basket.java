package com.example.firebase2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class user_basket extends AppCompatActivity {


    // Total price
    public int totalPrice = 0;
    List<Product> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_basket);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Basket");

// Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Reset price to 0
                totalPrice = 0;
                itemList.clear();
                // If there is only one item in the list and we remove it delete the list
                if (itemList.size() == 0) {
                    TextView totalPriceText = findViewById(R.id.totalPrice);
                    totalPriceText.setText("0 lei");
                    ListView listView = findViewById(R.id.listaProduse);
                    ProductAdapter adapter = new ProductAdapter(user_basket.this, itemList, true);
                    listView.setAdapter(adapter);
                }
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    List<String> data = new ArrayList<>();
                    for (DataSnapshot itemSnapshot : categorySnapshot.getChildren()) {

                        String itemValue = String.valueOf(itemSnapshot.getValue());
                        data.add(itemValue);


                    }

                    // Calculate totalPrice
                    totalPrice = totalPrice + Integer.parseInt(data.get(1));

                    // Add products to Store ImageView
                    itemList.add(new Product(data.get(0), data.get(1), data.get(2)));
                    ProductAdapter adapter = new ProductAdapter(user_basket.this, itemList, true);
                    ListView listView = findViewById(R.id.listaProduse);
                    listView.setAdapter(adapter);

                    // Add the TotalPrice
                    TextView totalPriceText = findViewById(R.id.totalPrice);
                    String stringPrice = totalPrice + " lei";
                    totalPriceText.setText(stringPrice);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Function to go to Store
    public void GoToStore(View view) {
        Intent intent = new Intent(this, user_store.class);
        startActivity(intent);
    }

    public void GoToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void CreditCard(View view) {
        Toast toast = Toast.makeText(user_basket.this, "Plate cu CreditCard", Toast.LENGTH_LONG);
        toast.show();
    }

    public void PayPall(View view) {
        Toast toast = Toast.makeText(user_basket.this, "Plata cu PayPall", Toast.LENGTH_LONG);
        toast.show();
    }
}



