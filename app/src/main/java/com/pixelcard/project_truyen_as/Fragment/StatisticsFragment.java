package com.pixelcard.project_truyen_as.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelcard.project_truyen_as.adapter.ProductReportAdapter;
import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.model.Product;
import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private TextView tvTotalAccounts, tvTotalProducts, tvTotalViews;
    private RecyclerView recyclerViewProducts;
    private DatabaseReference accountRef, productRef;
    private List<Product> productList = new ArrayList<>();
    private ProductReportAdapter productReportAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_report, container, false);
        tvTotalAccounts = view.findViewById(R.id.tvTotalUsers);
        tvTotalProducts = view.findViewById(R.id.tvTotalProducts);
        tvTotalViews = view.findViewById(R.id.tvTotalViews);
        recyclerViewProducts = view.findViewById(R.id.recyclerProducts);

        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        productReportAdapter = new ProductReportAdapter(getContext(), productList);
        recyclerViewProducts.setAdapter(productReportAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accountRef = FirebaseDatabase.getInstance().getReference("users");
        productRef = FirebaseDatabase.getInstance().getReference("Product");

        // Lấy tổng số tài khoản
        accountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long accountCount = snapshot.getChildrenCount();
                tvTotalAccounts.setText("Tổng số account: " + accountCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("StatisticsFragment", "Lỗi khi đọc accounts: " + error.getMessage());
            }
        });

        // Lấy danh sách sản phẩm và tổng số lượt xem
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                long totalViews = 0;

                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                        if (product.getView() != null && !product.getView().isEmpty()) {
                            try {
                                totalViews += Long.parseLong(product.getView());
                            } catch (NumberFormatException e) {
                                Log.e("StatisticsFragment", "Lỗi chuyển đổi view: " + product.getView());
                            }
                        }

                    }
                }

                productReportAdapter.notifyDataSetChanged();
                tvTotalProducts.setText("Tổng số sản phẩm: " + snapshot.getChildrenCount());
                tvTotalViews.setText("Tổng số lượt view: " + totalViews);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("StatisticsFragment", "Lỗi khi đọc products: " + error.getMessage());
            }
        });
    }
}
