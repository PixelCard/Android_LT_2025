package com.pixelcard.project_truyen_as;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseHelper {
    private DatabaseReference dbref;
    public FireBaseHelper(){
        dbref= FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    // Thêm user
    public void addUser() {

    }

    // Sửa user
    public void updateUser() {
    }

    // Xoá user
    public void deleteUser(String id) {
    }

    // Lấy danh sách user
    public void getAllUsers() {
    }
}
