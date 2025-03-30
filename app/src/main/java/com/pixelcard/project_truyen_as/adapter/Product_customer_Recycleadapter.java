package com.pixelcard.project_truyen_as.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pixelcard.project_truyen_as.Product;
import com.pixelcard.project_truyen_as.ProductActivity;
import com.pixelcard.project_truyen_as.R;
import java.util.List;

public class Product_customer_Recycleadapter extends RecyclerView.Adapter<Product_customer_Recycleadapter.ProductViewHolder> {
    private List<Product> productList;

    public Product_customer_Recycleadapter(List<Product> list) {
        this.productList = list;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_product_item_customer, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvTenTruyen.setText("Tên truyện: " + product.getTentruyen());
        holder.tvLuotXem.setText("View Peak:" + product.getView() + " lượt xem");
        if (!product.getUrlhinhsp().equals(null)) {
            Glide.with(holder.itemView.getContext()).load(product.getUrlhinhsp()).into(holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(R.drawable.ic_launcher_background); // ảnh mặc định
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProductActivity.class);
                intent.putExtra("image", product.getUrlhinhsp());
                intent.putExtra("title", product.getTentruyen());
                intent.putExtra("author", product.getAuthor());
                intent.putExtra("description", product.getDescription());
                intent.putExtra("view", product.getView());
                intent.putExtra("id", product.getId()); // Nếu cần lấy thêm chapter
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvTenTruyen, tvLuotXem;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct_Customer);
            tvTenTruyen = itemView.findViewById(R.id.tvTenTruyen_Customer);
            tvLuotXem = itemView.findViewById(R.id.tvLuotXem_Customer);
        }
    }
}
