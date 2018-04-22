package com.example.toby.solent_eat_out;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class AddRestaurantActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);
        Button search = (Button) findViewById(R.id.btnSearchRestaurant);
        search.setOnClickListener(this);
    }

    public void onClick(View view) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
    }

}


