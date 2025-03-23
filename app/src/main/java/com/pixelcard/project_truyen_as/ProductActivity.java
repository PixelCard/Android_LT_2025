package com.pixelcard.project_truyen_as;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pixelcard.project_truyen_as.Fragment.Fragment_ChapterProduct_Customer;
import com.pixelcard.project_truyen_as.Fragment.Fragment_Comment_product_customer;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Truyền Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment__chapter_product__customer,new Fragment_ChapterProduct_Customer()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_comment_product_customer,new Fragment_Comment_product_customer()).commit();
    }
}