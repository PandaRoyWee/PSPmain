package com.example.pspmain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button mainlog;
    CardView abtus, database, products, location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainlog = findViewById(R.id.main_login);
        abtus = (CardView) findViewById(R.id.abtus);
        database = (CardView) findViewById(R.id.database);
        products = (CardView) findViewById(R.id.products);
        location = (CardView) findViewById(R.id.location);

        mainlog.setOnClickListener((View.OnClickListener) this);
        abtus.setOnClickListener((View.OnClickListener) this);
        database.setOnClickListener((View.OnClickListener) this);
        products.setOnClickListener((View.OnClickListener) this);
        location.setOnClickListener((View.OnClickListener) this);

    }

    @Override
    public void onClick(View v) {

        Intent i ;
        switch (v.getId()) {
            case R.id.main_login : i = new Intent(this, login.class); startActivity(i); break;
            case R.id.abtus : i = new Intent(this, AboutUs.class); startActivity(i); break;
            case R.id.database : i = new Intent(this, customersdatabase.class); startActivity(i); break;
            case R.id.products : i = new Intent(this, products.class); startActivity(i); break;
            case R.id.location : i = new Intent(this, googleMap.class); startActivity(i); break;
        }
    }
}