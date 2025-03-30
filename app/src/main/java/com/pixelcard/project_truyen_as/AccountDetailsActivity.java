package com.pixelcard.project_truyen_as;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AccountDetailsActivity extends AppCompatActivity {

    ImageView backgroundImage,profilePicture;
    TextView username,realName,userRealName,date,userDate,address,userAddress,phone,userPhone;
    Button btnChangeImage,btnEditInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AddControlls();
        HandlEvents();
    }

    private void AddControlls() {
        backgroundImage = findViewById(R.id.background_image);
        profilePicture = findViewById(R.id.profile_picture);
        username = findViewById(R.id.username);
       realName = findViewById(R.id.username);
       userRealName = findViewById(R.id.userRealName);

       date = findViewById(R.id.Date);
       userDate = findViewById(R.id.userDate);

       address = findViewById(R.id.diachi);
       userAddress = findViewById(R.id.userdiachi);

        phone = findViewById(R.id.SĐT);
       userPhone = findViewById(R.id.userphone);

       btnEditInfo = findViewById(R.id.Chinhsuathongtin);
    }

    private void HandlEvents() {
        btnChangeImage.setOnClickListener(v -> {
            Toast.makeText(this, "Chỉnh sửa ảnh được nhấn!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });

        btnEditInfo.setOnClickListener(v -> {
            // Mở một Activity mới hoặc Dialog để chỉnh sửa thông tin cá nhân
            Toast.makeText(this, "Chỉnh sửa thông tin được nhấn!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            username.setText(data.getStringExtra("USERNAME"));
            userDate.setText(data.getStringExtra("DATE"));
            userAddress.setText(data.getStringExtra("ADDRESS"));
            userPhone.setText(data.getStringExtra("PHONE"));

            String imageUri = data.getStringExtra("IMAGE_URI");
            if (imageUri != null) {
                profilePicture.setImageURI(Uri.parse(imageUri));
            }
        }
    }
}