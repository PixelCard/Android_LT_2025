package com.pixelcard.project_truyen_as.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.adapter.CategoryAdapter_Admin;
import com.pixelcard.project_truyen_as.model.Category;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Category_Admin extends Fragment {
    private RecyclerView recyclerViewCategory;
    private List<Category> categoryList = new ArrayList<>();
    private CategoryAdapter_Admin categoryAdapter;

    public Fragment_Category_Admin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment__category__admin, container, false);
        recyclerViewCategory = view.findViewById(R.id.receycleview_category_admin);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext()));

        categoryAdapter = new CategoryAdapter_Admin(getContext(),categoryList);
        recyclerViewCategory.setAdapter(categoryAdapter);

        loadCategoriesFromFirebase();

        return view;
    }

    private void loadCategoriesFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("categories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Category category = child.getValue(Category.class);
                    categoryList.add(category);
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}