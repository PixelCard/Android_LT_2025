package com.pixelcard.project_truyen_as.Comment_Admin;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
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
import com.pixelcard.project_truyen_as.R;

import java.util.List;

public class AdminCommentAdapter extends RecyclerView.Adapter<AdminCommentAdapter.CommentViewHolder> {
    Context context;
    List<Comment> commentList;
    DatabaseReference databaseReference;

    public AdminCommentAdapter(Context context, List<Comment> commentList, String productId) {
        this.context = context;
        this.commentList = commentList;
        this.databaseReference = FirebaseDatabase.getInstance().getReference("comments").child(productId);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.txtUsername.setText(comment.getUname());
        holder.txtComment.setText(comment.getContent());
        Glide.with(context).load(comment.getUimg()).into(holder.imgUser);

        // Xử lý xóa bình luận khi Admin nhấn giữ
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa bình luận")
                    .setMessage("Bạn có chắc muốn xóa bình luận này?")
                    .setPositiveButton("Xóa", (dialog, which) -> deleteComment(comment))
                    .setNegativeButton("Hủy", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUser;
        TextView txtUsername, txtComment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.imgUser);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtComment = itemView.findViewById(R.id.txtComment);
        }
    }

    private void deleteComment(Comment comment) {
        databaseReference.orderByChild("content").equalTo(comment.getContent()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    data.getRef().removeValue();
                }
                Toast.makeText(context, "Bình luận đã bị xóa", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AdminComment", "Lỗi khi xóa bình luận", error.toException());
            }
        });
    }
}