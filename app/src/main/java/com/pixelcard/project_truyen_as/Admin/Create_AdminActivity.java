package com.pixelcard.project_truyen_as.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pixelcard.project_truyen_as.Category_Admin.Create_Category_AdminActivity;
import com.pixelcard.project_truyen_as.Chapter_Admin.CreateChapter_Admin;
import com.pixelcard.project_truyen_as.Product_Admin.Create_Product_Admin;
import com.pixelcard.project_truyen_as.R;

public class Create_AdminActivity extends AppCompatActivity {
    Button btnIntentProduct,btnIntentChapter,btnIntentCategory;
    ImageButton imgButtonCallbackhome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addControl();

        handleEvent();
    }

    private void handleEvent() {
        btnIntentProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Create_AdminActivity.this, Create_Product_Admin.class);
                startActivity(intent);
            }
        });

        btnIntentChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Create_AdminActivity.this, CreateChapter_Admin.class);
                startActivity(intent);
            }
        });

        btnIntentCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Create_AdminActivity.this, Create_Category_AdminActivity.class);
                startActivity(intent);
            }
        });

        imgButtonCallbackhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Create_AdminActivity.this,Admin_Home_Activity.class);
                startActivity(intent);
            }
        });
    }

    private void addControl() {
        btnIntentProduct=findViewById(R.id.btnProductPageAdmin);
        btnIntentChapter=findViewById(R.id.btnChapterPageAdmin);
        btnIntentCategory=findViewById(R.id.btn_CategoryPageAdmin);
        imgButtonCallbackhome=findViewById(R.id.imgHomeAdminButton);
    }
}