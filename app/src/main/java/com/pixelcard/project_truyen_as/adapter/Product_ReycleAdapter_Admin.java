package com.pixelcard.project_truyen_as.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pixelcard.project_truyen_as.Product;
import com.pixelcard.project_truyen_as.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product_ReycleAdapter_Admin extends RecyclerView.Adapter<Product_ReycleAdapter_Admin.ViewHolder> {

    private Activity activity;

    private List<Product> productList;

    public Product_ReycleAdapter_Admin(Activity context, List<Product> productList) {
        this.activity = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public Product_ReycleAdapter_Admin.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_timkiem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Product_ReycleAdapter_Admin.ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.imgview=holder.imgview.findViewById(R.id.imgProductFinding);
        holder.txtTenTacGia=holder.txtTenTacGia.findViewById(R.id.tvProductNameFinding);
        holder.txtTenTacGia.setText(product.getTentruyen());
        holder.itemView.setOnClickListener(view -> {
            showAddChapterDialog(product.getProductID());
        });

    }

    private void showAddChapterDialog(String productID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Thêm Chapter");

        EditText input = new EditText(activity);
        input.setHint("Nhập tên chapter");
        builder.setView(input);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String chapterName = input.getText().toString().trim();
            if (!chapterName.isEmpty()) {
                addChapterToFirebase(productID, chapterName);
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void addChapterToFirebase(String productID, String chapterName) {
        DatabaseReference chapterRef = FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Chapters").child(productID);

        String chapterID = chapterRef.push().getKey(); // Tạo ID chapter ngẫu nhiên
        Map<String, Object> chapterData = new HashMap<>();
        chapterData.put("chapterID", chapterID);
        chapterData.put("chapterName", chapterName);

        chapterRef.child(chapterID).setValue(chapterData).addOnSuccessListener(aVoid ->
                        Toast.makeText(activity, "Thêm thành công!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(activity, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return 0;
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
}
