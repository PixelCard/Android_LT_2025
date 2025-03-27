package com.pixelcard.project_truyen_as.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.pixelcard.project_truyen_as.Product;
import com.pixelcard.project_truyen_as.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chapter_RecycleAdapter_Admin extends RecyclerView.Adapter<Chapter_RecycleAdapter_Admin.ChapterViewHolder> {
    private Context context;
    private List<Product> productList;

    public Chapter_RecycleAdapter_Admin(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public static class ChapterViewHolder  extends RecyclerView.ViewHolder {
        ImageView imgProduct;

        LinearLayout itemContainer;

        TextView tvTenTruyen, tvId, tvAuthor, tvDescription, tvCreateDate;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProductForChapter);
            tvTenTruyen = itemView.findViewById(R.id.tvTenTruyenForChapter);
            tvId = itemView.findViewById(R.id.tvIdForChapter);
            tvAuthor = itemView.findViewById(R.id.tvAuthorForChapter);
            itemContainer=itemView.findViewById(R.id.itemContainerChapter_Admin);
            tvDescription = itemView.findViewById(R.id.tvDescriptionForChapter);
            tvCreateDate = itemView.findViewById(R.id.tvCreateDateForChapter);
        }
    }

    @NonNull
    @Override
    public Chapter_RecycleAdapter_Admin.ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_chapter_admin, parent, false);
        return new Chapter_RecycleAdapter_Admin.ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvTenTruyen.setText(product.getTentruyen());
        holder.tvId.setText("ID: " + product.getId());
        holder.tvAuthor.setText("Tác giả: " + product.getAuthor());
        holder.tvDescription.setText("Mô tả: " + product.getDescription());
        holder.tvCreateDate.setText("Ngày tạo: " + product.getCreateDate());
        Glide.with(context).load(product.getUrlhinhsp()).into(holder.imgProduct);
        holder.itemContainer.setOnClickListener(v -> {
            // Gọi Dialog
            showAddChapterDialog(product.getId());
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    //Chapter
    private void showAddChapterDialog(String productID) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_add_chapter, null);

        // Ánh xạ
        EditText edtName = dialogView.findViewById(R.id.edtChapterName);
        EditText edtContent = dialogView.findViewById(R.id.edtChapterContent);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thêm chương mới");
        builder.setView(dialogView);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String name = edtName.getText().toString().trim();
            String content = edtContent.getText().toString();

            if (name.isEmpty()) {
                Toast.makeText(context, "Tên chương không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi hàm thêm chapter vào Firebase (truyền name + content nếu cần)
            addChapterToFirebase(productID, name, content);
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void addChapterToFirebase(String productID, String chapterName,String chapterContent) {
        DatabaseReference chapterRef = FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Chapters").child(productID);

        String chapterID = chapterRef.push().getKey(); // Tạo ID chapter ngẫu nhiên

        // Kiểm tra chapterName có trùng không
        chapterRef.orderByChild("chapterName").equalTo(chapterName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Toast.makeText(context, "Tên chương đã tồn tại!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Không trùng => tiến hành thêm
                            String chapterID = chapterRef.push().getKey();
                            Map<String, Object> chapterData = new HashMap<>();
                            chapterData.put("chapterID", chapterID);
                            chapterData.put("chapterName", chapterName);
                            chapterData.put("ChapterContent",chapterContent);

                            chapterRef.child(chapterID).setValue(chapterData)
                                    .addOnSuccessListener(aVoid ->
                                            Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e ->
                                            Toast.makeText(context, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(context, "Lỗi kiểm tra: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
