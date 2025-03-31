package com.pixelcard.project_truyen_as.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelcard.project_truyen_as.ProductActivity;
import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.model.Product;

import java.util.List;
public class ProductReportAdapter extends RecyclerView.Adapter<ProductReportAdapter.ViewHolder> {
    private Context context;
    private List<Product> productList;
    private DatabaseReference chapterRef;

    public ProductReportAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.chapterRef = FirebaseDatabase.getInstance().getReference("Chapters");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvName.setText(product.getTentruyen());
        holder.tvViews.setText("Lượt xem: " + product.getView());

        // Load ảnh từ URL
        Glide.with(context).load(product.getUrlhinhsp()).into(holder.ivProduct);

        // Lấy tổng số chapter từ Firebase và đảm bảo cập nhật đúng item
        chapterRef.child(product.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long chapterCount = snapshot.getChildrenCount( ); // Đếm tổng số chapter
                    holder.tvChapters.setText("Số chương: " + chapterCount);
                } else {
                    holder.tvChapters.setText("Số chương: 0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                holder.tvChapters.setText("Số chương: N/A");
            }
        });
    }



    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvName, tvChapters, tvViews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvChapters = itemView.findViewById(R.id.tvTotalChapters);
            tvViews = itemView.findViewById(R.id.tvTotalViews);
        }
    }
}
