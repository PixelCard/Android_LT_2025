package com.pixelcard.project_truyen_as.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.model.User;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private List<User> userList;
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.txtName.setText(String.format("Tên: %s", user.getHoten()));
        holder.txtEmail.setText(String.format("Email: %s", user.getEmail()));
        // Không hiển thị mật khẩu trong Realtime Database
        holder.txtPass.setVisibility(View.GONE); // Ẩn mật khẩu nếu có trong UI

        holder.btnDelete.setOnClickListener(v -> {
            // Thay vì dùng email, sử dụng UID người dùng để xóa
            String userId = user.getEmail().replace(".", ","); // Firebase không cho phép dấu '.' trong ID
            userRef.child(userId).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                        userList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, userList.size());
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        holder.btnEdit.setOnClickListener(v -> {
            Toast.makeText(context, "Chức năng sửa chưa được triển khai!", Toast.LENGTH_SHORT).show();
        });

        holder.btnBlock.setOnClickListener(v -> {
            Toast.makeText(context, "Chức năng khóa chưa được triển khai!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtEmail, txtPass;
        Button btnEdit, btnDelete, btnBlock;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtPass = itemView.findViewById(R.id.txtPass);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnBlock = itemView.findViewById(R.id.btnBlock);
        }
    }
}
