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

        EditText restaurant_name = (EditText)findViewById(R.id.restaurant_name);
        String name = restaurant_name.getText().toString();

        EditText restaurant_address = (EditText)findViewById(R.id.restaurant_address);
        String address = restaurant_address.getText().toString();

        EditText cuisine = (EditText)findViewById(R.id.cuisine);
        String cuisine_type = cuisine.getText().toString();

        EditText rating = (EditText)findViewById(R.id.rating);
        String restaurant_rating = rating.getText().toString();

        bundle.putString("com.example.toby.solent_eat_out.name",name);
        bundle.putString("com.example.toby.solent_eat_out.address",address);
        bundle.putString("com.example.toby.solent_eat_out.cuisine_type",cuisine_type);
        bundle.putString("com.example.toby.solent_eat_out.restaurant_rating",restaurant_rating);

        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
    }

}


