package com.pixelcard.project_truyen_as.Chapter_Admin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pixelcard.project_truyen_as.Product;
import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.adapter.Product_ReycleAdapter_Admin;

import java.util.ArrayList;
import java.util.List;

public class CreateChapter_Admin extends AppCompatActivity {
    private RecyclerView recyclerViewadmin;

    private Product_ReycleAdapter_Admin admin_adapter;

    private List<Product> productList = new ArrayList<>();

    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_chapter_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addcontrol();

        addFireBase();
//        loadProducts();
    }

    private void addFireBase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Product");
    }

//    private void loadProducts() {
////        databaseReference.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                productList.clear();
////                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
////                    Product product = productSnapshot.getValue(Product.class);
////                    productList.add(product);
////                }
////                adapter = new ProductAdapter(MainActivity.this, productList);
////                recyclerView.setAdapter(adapter);
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////                Toast.makeText(MainActivity.this, "Lỗi khi tải sản phẩm", Toast.LENGTH_SHORT).show();
////            }
////        });
////    }

    private void addcontrol() {
        recyclerViewadmin = findViewById(R.id.product_chapter_recyleview_admin);
        recyclerViewadmin.setLayoutManager(new LinearLayoutManager(this));
    }
}