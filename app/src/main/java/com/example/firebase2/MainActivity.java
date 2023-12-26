package com.example.firebase2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Function to go to the User Interface
    public void goToUser(View view) {
        Intent intent = new Intent(this, user_store.class);
        startActivity(intent);

    }

    // Function to go to the Dev Interface
    public void GoToDev(View view) {
        Intent intent = new Intent(this, Dev.class);
        startActivity(intent);
    }


}
