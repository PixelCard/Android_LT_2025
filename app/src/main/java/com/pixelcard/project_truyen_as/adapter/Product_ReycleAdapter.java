package com.pixelcard.project_truyen_as.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pixelcard.project_truyen_as.Product;
import com.pixelcard.project_truyen_as.ProductActivity;
import com.pixelcard.project_truyen_as.R;

import java.util.List;

public class Product_ReycleAdapter extends RecyclerView.Adapter<Product_ReycleAdapter.ViewHolder> {
    private Activity activity;

    private List<Product> productList;
    public Product_ReycleAdapter(Activity context, List<Product> productList) {
        this.activity = context;
        this.productList = productList;
    }

    public void setFilterList(List<Product> filterList ){
        this.productList=filterList;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
       ImageView imgview;
       TextView txtTenTacGia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgview=itemView.findViewById(R.id.imgProductFinding);
            txtTenTacGia=itemView.findViewById(R.id.tvProductNameFinding);
        }
    }

    //Khởi tạo
    @NonNull
    @Override
    public Product_ReycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_timkiem, parent, false);
        return new ViewHolder(view);
    }


    //Sau khi khởi tạo thì xử lý
    @Override
    public void onBindViewHolder(@NonNull Product_ReycleAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.txtTenTacGia.setText(product.getTentruyen());
        holder.imgview.setImageResource(product.getHinhsp());
        holder.imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ProductActivity.class);
                intent.putExtra("tentruyen", product.getTentruyen());
                intent.putExtra("hinhsp", product.getHinhsp());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
