package com.example.firebase2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    private final Context context;
    private final boolean changeButton;

    public ProductAdapter(Context context, List<Product> itemList, boolean changeButton) {
        super(context, R.layout.list_item_layout, itemList);
        this.context = context;
        this.changeButton = changeButton;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_layout, parent, false);
        }


        Product currentItem = getItem(position);

        ImageView produsImg = convertView.findViewById(R.id.productImg);
        TextView produsName = convertView.findViewById(R.id.productName);
        TextView produsPrice = convertView.findViewById(R.id.productPrice);
        Button produsButton = convertView.findViewById(R.id.productAddButton);


        // Hide buttons for the Basket Interface
        if (changeButton == true) {
            produsButton.setText("Remove");
        } else {
            produsButton.setText("Add");
        }


        produsName.setText(currentItem.getItemName());
        produsPrice.setText(currentItem.getItemPrice());

        // Use Glide Library to load a image
        Glide.with(context)
                .load(currentItem.getItemURL())
                .placeholder(R.drawable.placeholder_image)
                .into(produsImg);

        if (changeButton == true) {
            produsButton.setOnClickListener(v -> removeFromBasket(currentItem.getItemName()));
        } else {
            produsButton.setOnClickListener(v -> addToBasket(currentItem.getItemName(), currentItem.getItemPrice(), currentItem.getItemURL()));
        }
        return convertView;
    }


    // Function for the button on the Product, to add to Basket
    private void addToBasket(String productName, String productPrice, String imgUrl) {

        Product productBasket = new Product(productName, productPrice, imgUrl);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("Basket").child(productName).setValue(productBasket);


    }

    // Function for the button on the Product, to remove from the Basket
    private void removeFromBasket(String productName) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("Basket").child(productName).removeValue();


    }

}
