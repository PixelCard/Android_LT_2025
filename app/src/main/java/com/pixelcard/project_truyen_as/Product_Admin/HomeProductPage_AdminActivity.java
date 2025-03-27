package com.pixelcard.project_truyen_as.Product_Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelcard.project_truyen_as.Admin.Admin_Home_Activity;
import com.pixelcard.project_truyen_as.Product;
import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.adapter.Product_RecycleAdapter_Admin;

import java.util.ArrayList;
import java.util.List;

public class HomeProductPage_AdminActivity extends AppCompatActivity {

    RecyclerView recyclerViewProduct;

    ImageButton imageButton;

    Button btnIntentToPageCreate;

    List<Product> datalist;

    DatabaseReference databaseReference;

    Product_RecycleAdapter_Admin productReycleAdapterAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_product_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControl();
        handleEvent();
    }

    private void handleEvent() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datalist.clear();
                for(DataSnapshot itemSnapshot : snapshot.getChildren()){
                    Product product = itemSnapshot.getValue(Product.class);
                    datalist.add(product);
                }
                productReycleAdapterAdmin.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeProductPage_AdminActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeProductPage_AdminActivity.this, Admin_Home_Activity.class);
            }
        });

        btnIntentToPageCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeProductPage_AdminActivity.this, Create_Product_Admin.class);
            }
        });

    }

    private void addControl() {
        recyclerViewProduct=findViewById(R.id.recyclerViewProduct);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(HomeProductPage_AdminActivity.this,1);
        recyclerViewProduct.setLayoutManager(gridLayoutManager);
        btnIntentToPageCreate=findViewById(R.id.btnIntentToPageCreate);
        imageButton=findViewById(R.id.imgbuttonIconHomeProduct);
        datalist=new ArrayList<>();
        productReycleAdapterAdmin=new Product_RecycleAdapter_Admin(HomeProductPage_AdminActivity.this,datalist);
        recyclerViewProduct.setAdapter(productReycleAdapterAdmin);
        databaseReference= FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Product");
    }
}