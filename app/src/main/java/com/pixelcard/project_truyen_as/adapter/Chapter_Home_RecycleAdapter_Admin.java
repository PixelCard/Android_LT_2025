package com.pixelcard.project_truyen_as.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.model.Chapter;

import java.util.List;

public class Chapter_Home_RecycleAdapter_Admin extends RecyclerView.Adapter<Chapter_Home_RecycleAdapter_Admin.ChapterHomeViewHolder>{
    private Context context;
    private List<Chapter> chapterList;
    @NonNull
    @Override
    public Chapter_Home_RecycleAdapter_Admin.ChapterHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_fragement_admin_home_chapter, parent, false);
        return new ChapterHomeViewHolder(view);
    }

    public static class ChapterHomeViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvTenTruyen, tvChapterNumber, tvChapterContent;
        LinearLayout itemContainer;
        public ChapterHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProductForChapterHome);
            tvTenTruyen = itemView.findViewById(R.id.tvTenTruyenForChapterHome);
            tvChapterNumber = itemView.findViewById(R.id.tvChapterNumberHome);
            tvChapterContent = itemView.findViewById(R.id.tvChapterContentHome);
            itemContainer = itemView.findViewById(R.id.itemContainerChapter_AdminForHome);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Chapter_Home_RecycleAdapter_Admin.ChapterHomeViewHolder holder, int position) {
        Chapter chapter = chapterList.get(position);
        holder.tvTenTruyen.setText(chapter.getTenTruyen());
        holder.tvChapterNumber.setText("Chương:" + chapter.getChapterName());
        holder.tvChapterContent.setText("Nội dung chương đó:"+ chapter.getChapterContent());

        // Load ảnh
        Glide.with(context).load(chapter.getImageUrl()).into(holder.imgProduct);

        holder.itemContainer.setOnClickListener(v -> {
            // Mở dialog sửa/xoá
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_chapter, null);
            builder.setView(dialogView);

            EditText edtChapterName = dialogView.findViewById(R.id.edtChapterName);
            EditText edtChapterContent = dialogView.findViewById(R.id.edtChapterContent);
            Button btnUpdate = dialogView.findViewById(R.id.btnUpdateChapter);
            Button btnDelete = dialogView.findViewById(R.id.btnDeleteChapter);

            edtChapterName.setTextColor(Color.BLACK);
            edtChapterContent.setTextColor(Color.BLACK);

            edtChapterName.setText(chapter.getChapterName());
            edtChapterContent.setText(chapter.getChapterContent());

            AlertDialog dialog = builder.create();
            dialog.show();

            btnUpdate.setOnClickListener(view -> {
                String newName = edtChapterName.getText().toString().trim();
                String newContent = edtChapterContent.getText().toString().trim();


                //Kiểm tra nếu bỏ trống
                if (newName.isEmpty() || newContent.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập đầy đủ tên chương và nội dung!", Toast.LENGTH_SHORT).show();

                    //Trả lại giá trị gốc
                    edtChapterName.setText(chapter.getChapterName());
                    edtChapterContent.setText(chapter.getChapterContent());
                    return;
                }
                DatabaseReference chapterRootRef = FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference("Chapters")
                        .child(chapter.getProductID());

                // Kiểm tra nếu chapterName đã tồn tại (nhưng không phải chính nó)
                chapterRootRef.orderByChild("chapterName").equalTo(newName)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean nameConflict = false;

                                for (DataSnapshot snap : snapshot.getChildren()) {
                                    // Nếu là chính chương đang sửa thì bỏ qua
                                    if (!snap.getKey().equals(chapter.getChapterID())) {
                                        nameConflict = true;
                                        break;
                                    }
                                }

                                if (nameConflict) {
                                    Toast.makeText(context, "Tên chương này đã tồn tại!", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Không trùng → cập nhật
                                    DatabaseReference chapterRef = chapterRootRef.child(chapter.getChapterID());
                                    chapterRef.child("chapterName").setValue(newName);
                                    chapterRef.child("chapterContent").setValue(newContent);

                                    Toast.makeText(context, "Đã cập nhật chương!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(context, "Lỗi kiểm tra trùng chương: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            });

            btnDelete.setOnClickListener(view -> {
                DatabaseReference chapterRef = FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference("Chapters")
                        .child(chapter.getProductID())
                        .child(chapter.getChapterID());

                chapterRef.removeValue().addOnSuccessListener(unused -> {
                    Toast.makeText(context, "Đã xoá chương!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }).addOnFailureListener(e -> {
                    Toast.makeText(context, "Lỗi xoá: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            });
        });
    }

    public Chapter_Home_RecycleAdapter_Admin(Context context, List<Chapter> chapterList){
        this.context=context;
        this.chapterList=chapterList;
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }
}
