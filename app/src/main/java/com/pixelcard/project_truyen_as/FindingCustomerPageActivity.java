package com.pixelcard.project_truyen_as;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pixelcard.project_truyen_as.adapter.Product_Finding_ReycleAdapter;

import java.util.ArrayList;
import java.util.List;

public class FindingCustomerPageActivity extends AppCompatActivity {
    private SearchView searchViewTimKiem;
    private RecyclerView viewHienthiListTimKiemDaLoc;

    private List<Product> productList;

    private Product_Finding_ReycleAdapter ProductAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_finding_customer_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControl();
        handleEvent();
    }

    private void handleEvent() {
        searchViewTimKiem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }

    private void filterList(String text) {
        List<Product> filterList = new ArrayList<>();

        for(Product product : productList){
            if(product.getTentruyen().toLowerCase().contains(text.toLowerCase())){
                filterList.add(product);
            }
        }
        
        if(filterList.isEmpty()){
            Toast.makeText(this, "No data Found", Toast.LENGTH_SHORT).show();
        }
        else{
            ProductAdapter.setFilterList(filterList);
        }
    }

    private void addControl() {
        searchViewTimKiem=findViewById(R.id.searchViewFinding);
        searchViewTimKiem.clearFocus();
        viewHienthiListTimKiemDaLoc = findViewById(R.id.recycleviewFinding);
        viewHienthiListTimKiemDaLoc.setHasFixedSize(true);
        viewHienthiListTimKiemDaLoc.setLayoutManager(new LinearLayoutManager(this));
        productList=new ArrayList<>();

        //add data
//        productList.add(new Product("Giang Ho",R.drawable.backiemgiangho));
//        productList.add(new Product("Giang Ho",R.drawable.backiemgiangho));
//        productList.add(new Product("Giang Ho",R.drawable.backiemgiangho));
//        productList.add(new Product("Hentai",R.drawable.anh_nen));
//        productList.add(new Product("Hentai",R.drawable.anh_nen));
//        productList.add(new Product("Hentai",R.drawable.anh_nen));

        ProductAdapter=new Product_Finding_ReycleAdapter(this,productList);
        viewHienthiListTimKiemDaLoc.setAdapter(ProductAdapter);
    }
}