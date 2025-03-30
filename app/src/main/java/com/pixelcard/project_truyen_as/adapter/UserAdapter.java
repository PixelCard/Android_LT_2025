package com.pixelcard.project_truyen_as.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.pixelcard.project_truyen_as.Account_Admin.EditUserActivity;
import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.model.User;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private List<User> userList;

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

        String roleText = user.getRole().equals("0") ? "Customer" : "Admin";
        holder.txtRole.setText(String.format("Role: %s", roleText));

        // Click vào user để hiển thị hộp thoại chức năng
        holder.contener_user_admin.setOnClickListener(v -> showOptionsDialog(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtEmail, txtRole;
        LinearLayout contener_user_admin;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtUserName);
            txtEmail = itemView.findViewById(R.id.txtUserEmail);
            txtRole = itemView.findViewById(R.id.txtUserRole);
            contener_user_admin = itemView.findViewById(R.id.contenter_user_admin);
        }
    }

    private void showOptionsDialog(User user) {
        if (user == null || user.getUID() == null || user.getUID().trim().isEmpty()) {
            Toast.makeText(context, "Lỗi: Không thể chỉnh sửa, userId không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn chức năng")
                .setItems(new String[]{"Sửa"}, (dialog, which) -> {
                    if (which == 0) { // Chọn "Sửa"
                        Log.d("DEBUG", "Mở EditUserActivity với userId: " + user.getUID());
                        Intent intent = new Intent(context, EditUserActivity.class);
                        intent.putExtra("userId", user.getUID());
                        intent.putExtra("name", user.getHoten());
                        intent.putExtra("email", user.getEmail());
                        intent.putExtra("role", user.getRole());
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
