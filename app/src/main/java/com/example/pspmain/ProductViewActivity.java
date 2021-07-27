package com.example.pspmain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductViewActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    TextView textView1;

    productmodel productModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        imageView = findViewById(R.id.imageViewProduct);
        textView = findViewById(R.id.tvNameProduct);
        textView1 = findViewById(R.id.tvNameDesc);

        Intent intent = getIntent();

        if(intent.getExtras() != null){

            productModel = (productmodel) intent.getSerializableExtra("item");
            imageView.setImageResource(productModel.getImage());
            textView.setText(productModel.getName());
            textView1.setText(productModel.getDesc());

        }
    }
}