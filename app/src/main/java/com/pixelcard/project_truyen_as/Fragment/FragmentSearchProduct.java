package com.pixelcard.project_truyen_as.Fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelcard.project_truyen_as.Product;
import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.adapter.Product_Finding_ReycleAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentSearchProduct extends Fragment {

    private  androidx.appcompat.widget.SearchView searchView;
    private RecyclerView recyclerView;
    private Product_Finding_ReycleAdapter productFindingReycleAdapter;
    private List<Product> productList = new ArrayList<>();
    private List<Product> filteredList = new ArrayList<>();
    public FragmentSearchProduct() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_product, container, false);

        searchView = view.findViewById(R.id.searchViewFinding);
        recyclerView = view.findViewById(R.id.recycleviewFinding);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productFindingReycleAdapter = new Product_Finding_ReycleAdapter(getContext(), filteredList);
        recyclerView.setAdapter(productFindingReycleAdapter);

        // Lấy EditText bên trong SearchView và chỉnh màu
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.BLACK);         // màu chữ
        searchEditText.setHintTextColor(Color.GRAY);      // màu gợi ý hint

        loadProductsFromFirebase(); // tải toàn bộ product

        handleSearch(); // xử lý tìm kiếm

        return view;
    }

    private void handleSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Không làm gì khi bấm enter
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProduct(newText);
                return true;
            }
        });
    }

    private void filterProduct(String newText) {
        filteredList.clear();
        if (newText.isEmpty()) {
            filteredList.addAll(productList);
        } else {
            for (Product product : productList) {
                if (product.getTentruyen().toLowerCase().contains(newText.toLowerCase())) {
                    filteredList.add(product);
                }
            }
        }
        productFindingReycleAdapter.notifyDataSetChanged();
    }

    private void loadProductsFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Product");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Product product = snap.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                    }
                }

                // Ban đầu hiển thị toàn bộ danh sách
                filteredList.clear();
                filteredList.addAll(productList);
                productFindingReycleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi khi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }
}