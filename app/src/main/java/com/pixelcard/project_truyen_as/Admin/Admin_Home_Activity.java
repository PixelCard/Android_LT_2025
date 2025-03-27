package com.pixelcard.project_truyen_as.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pixelcard.project_truyen_as.Product_Admin.HomeProductPage_AdminActivity;
import com.pixelcard.project_truyen_as.R;

public class Admin_Home_Activity extends AppCompatActivity {
    Button btnPageProduct,btnPageChapter,btnPageComment,btnPageThongKe,btnPageAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControl();
        handleEvent();
    }

    private void handleEvent() {
        btnPageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Home_Activity.this, HomeProductPage_AdminActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addControl() {
        btnPageProduct=findViewById(R.id.btnIntentProductPage);
    }
}