package com.pixelcard.project_truyen_as.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.adapter.Product_Finding_ReycleAdapter;
import com.pixelcard.project_truyen_as.model.Product;

import java.util.ArrayList;
import java.util.List;

public class Fragment_filter_category_customer extends Fragment {

    private LinearLayout layoutCategoryCheckboxes;
    private RecyclerView recyclerViewProduct;
    private Product_Finding_ReycleAdapter adapter;
    private List<Product> productList = new ArrayList<>();

    public Fragment_filter_category_customer() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_category_customer, container, false);

        layoutCategoryCheckboxes = view.findViewById(R.id.layoutCategoryCheckboxes_Customer_filter);
        recyclerViewProduct = view.findViewById(R.id.recyclerViewProduct_filter);

        recyclerViewProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Product_Finding_ReycleAdapter(getContext(),productList);
        recyclerViewProduct.setAdapter(adapter);

        loadCategoryCheckboxes();

        return view;
    }

    private void loadCategoryCheckboxes() {
        DatabaseReference categoryRef = FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("categories");

        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                layoutCategoryCheckboxes.removeAllViews();

                for (DataSnapshot child : snapshot.getChildren()) {
                    String categoryId = child.child("id").getValue(String.class);
                    String categoryName = child.child("name").getValue(String.class);

                    CheckBox cb = new CheckBox(getContext());
                    cb.setText(categoryName);
                    cb.setTag(categoryId);

                    cb.setChecked(true);

                    cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        filterProductBySelectedCategories();
                    });

                    layoutCategoryCheckboxes.addView(cb);
                }
                // Gọi lọc sản phẩm sau khi load checkbox xong
                filterProductBySelectedCategories();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi tải thể loại", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void filterProductBySelectedCategories() {
        List<String> selectedCategoryIds = new ArrayList<>();

        for (int i = 0; i < layoutCategoryCheckboxes.getChildCount(); i++) {
            View view = layoutCategoryCheckboxes.getChildAt(i);
            if (view instanceof CheckBox) {
                CheckBox cb = (CheckBox) view;
                if (cb.isChecked()) {
                    selectedCategoryIds.add((String) cb.getTag());
                }
            }
        }

        DatabaseReference productRef = FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Product");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> filtered = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Product product = child.getValue(Product.class);
                    if (product != null && product.getCategoryIds() != null) {
                        for (String id : product.getCategoryIds()) {
                            if (selectedCategoryIds.contains(id)) {
                                filtered.add(product);
                                break;
                            }
                        }
                    }
                }
                adapter.updateData(filtered);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
