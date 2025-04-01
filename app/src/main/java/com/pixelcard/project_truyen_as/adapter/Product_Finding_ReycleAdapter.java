package com.pixelcard.project_truyen_as.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.pixelcard.project_truyen_as.ProductActivity;
import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.model.Product;

import java.util.List;

public class Product_Finding_ReycleAdapter extends RecyclerView.Adapter<Product_Finding_ReycleAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;

    public Product_Finding_ReycleAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_truyen, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        String userUID= FirebaseAuth.getInstance().getUid();
        holder.txtTenTruyen.setText(product.getTentruyen());
        holder.txtTacGia.setText("Tác giả: " + product.getAuthor());

        // Load hình ảnh bằng Glide
        Glide.with(context)
                .load(product.getUrlhinhsp())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.imgTruyen);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ProductActivity.class);
            intent.putExtra("image", product.getUrlhinhsp());
            intent.putExtra("title", product.getTentruyen());
            intent.putExtra("author", product.getAuthor());
            intent.putExtra("description", product.getDescription());
            intent.putExtra("view", product.getView());
            intent.putExtra("id", product.getId());
            intent.putExtra("userid",userUID);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenTruyen, txtTacGia;
        ImageView imgTruyen;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenTruyen = itemView.findViewById(R.id.txtTenTruyen);
            txtTacGia = itemView.findViewById(R.id.txtTacGia);
            imgTruyen= itemView.findViewById(R.id.imgTruyen);
        }
    }
}
