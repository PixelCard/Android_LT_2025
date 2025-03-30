package com.pixelcard.project_truyen_as.Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelcard.project_truyen_as.MainActivity;
import com.pixelcard.project_truyen_as.Product;
import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.adapter.AdapterBanner;
import com.pixelcard.project_truyen_as.adapter.Product_customer_Recycleadapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewAll, recyclerViewLatest;
    private Product_customer_Recycleadapter adapterAll, adapterLatest;
    private List<Product> listAll=new ArrayList<>();
    private List<Product> listLatest=new ArrayList<>();
    private ViewPager2 viewPagerTruyen;
    private AdapterBanner adapterBanner;
    private final Handler bannerHandler = new Handler();
    private int currentPosition = 0;
    private static final int[] bannerImages = new int[] {
            R.drawable.daiquangialamaghoang,
            R.drawable.dialogbkg,
            R.drawable.dialogbkgg,

    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPagerTruyen = view.findViewById(R.id.viewPagerBanner);
        adapterBanner = new AdapterBanner(getContext(), bannerImages);
        viewPagerTruyen.setAdapter(adapterBanner);


        // RecyclerView 1: hien thi full list truyen
        recyclerViewAll = view.findViewById(R.id.recyclerViewAllStories);
        recyclerViewAll.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        adapterAll = new Product_customer_Recycleadapter(listAll);
        recyclerViewAll.setAdapter(adapterAll);

        // RecyclerView 2 : Hien thi cac truyen moi nhat
        recyclerViewLatest = view.findViewById(R.id.recyclerViewLatestStories);
        recyclerViewLatest.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        adapterLatest = new Product_customer_Recycleadapter(listLatest);
        recyclerViewLatest.setAdapter(adapterLatest);

        // Load dữ liệu từ Firebase cho từng list
        loadAllProducts();
        loadLatestProducts();

        startBannerAutoSlide();
    }
    private void startBannerAutoSlide() {
        Runnable bannerRunnable = () -> {
            currentPosition++;
            if (currentPosition >= bannerImages.length) {
                currentPosition = 0;
            }
            viewPagerTruyen.setCurrentItem(currentPosition, true);
        };
        bannerHandler.postDelayed(bannerRunnable, 3000);
    }


    private void loadLatestProducts() {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Product");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> allProducts = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    Product product = child.getValue(Product.class);
                    if (product != null && product.getCreateDate() != null) {
                        allProducts.add(product);
                    }
                }

                // Sắp xếp theo createDate mới nhất
                Collections.sort(allProducts, new Comparator<Product>() {
                    @Override
                    public int compare(Product p1, Product p2) {
                        return p2.getCreateDate().compareTo(p1.getCreateDate());
                    }
                });

                listLatest.clear();
                listLatest.addAll(allProducts);
                adapterLatest.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllProducts() {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Product");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listAll.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {
                    Product product = productSnap.getValue(Product.class);
                    if (product != null) {
                        listAll.add(product);
                    }
                }
                adapterAll.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        loadAllProducts();
        loadLatestProducts();
    }
}