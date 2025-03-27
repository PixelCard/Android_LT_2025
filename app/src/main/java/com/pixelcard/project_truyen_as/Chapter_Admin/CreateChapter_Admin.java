package com.pixelcard.project_truyen_as.Chapter_Admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelcard.project_truyen_as.Product;
import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.adapter.Chapter_RecycleAdapter_Admin;
import com.pixelcard.project_truyen_as.adapter.Product_RecycleAdapter_Admin;

import java.util.ArrayList;
import java.util.List;

public class CreateChapter_Admin extends AppCompatActivity {
     RecyclerView recyclerViewadmin;

     Chapter_RecycleAdapter_Admin admin_adapter;

     List<Product> datalist;
     DatabaseReference databaseReference;
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
        loadProducts();
    }

    private void addFireBase() {
        databaseReference= FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Product");
    }

    private void loadProducts() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datalist.clear();
                for(DataSnapshot itemSnapshot : snapshot.getChildren()){
                    Product product = itemSnapshot.getValue(Product.class);
                    datalist.add(product);
                }
                admin_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CreateChapter_Admin.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addcontrol() {
        recyclerViewadmin = findViewById(R.id.product_chapter_recyleview_admin);
        recyclerViewadmin.setLayoutManager(new GridLayoutManager(this,1));
        datalist=new ArrayList<>();
        admin_adapter = new Chapter_RecycleAdapter_Admin(CreateChapter_Admin.this, datalist);
        recyclerViewadmin.setAdapter(admin_adapter);
    }
}