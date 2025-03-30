package com.pixelcard.project_truyen_as.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.adapter.UserAdapter;
import com.pixelcard.project_truyen_as.model.User;
import java.util.ArrayList;
import java.util.List;

public class AdminUser_Fragment extends Fragment {

    RecyclerView recyclerViewUser;
    List<User> userList;
    DatabaseReference databaseReference;
    UserAdapter userAdapter;

    public AdminUser_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_user_, container, false);

        recyclerViewUser = view.findViewById(R.id.recyclerView_UsersAdmin);
        userList = new ArrayList<>();

        recyclerViewUser.setLayoutManager(new GridLayoutManager(getContext(), 1));
        userAdapter = new UserAdapter(getContext(), userList);
        recyclerViewUser.setAdapter(userAdapter);

        databaseReference = FirebaseDatabase.getInstance()
                .getReference("users");

        loadUserList(); // Gọi hàm tải dữ liệu

        return view;
    }

    private void loadUserList() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    User user = itemSnapshot.getValue(User.class);
                    if (user != null) {
                        user.setUID(itemSnapshot.getKey()); // Gán UID từ Firebase
                        Log.d("DEBUG", "User: " + user.getHoten() + ", UID: " + user.getUID());
                        userList.add(user);
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("DEBUG", "Lỗi lấy dữ liệu Firebase", error.toException());
            }
        });
    }
}
