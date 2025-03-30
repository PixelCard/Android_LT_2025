package com.pixelcard.project_truyen_as;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView imgProfile;
    EditText edtUsername, edtDate, edtAddress, edtPhone;
    Button btnSave, btnChangeImage;
    Uri selectedImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AddControls();
        HandelEvents();
    }

    private void HandelEvents() {
        btnChangeImage.setOnClickListener(v -> {
            Intent intentHinh = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intentHinh.setType("image/*");
            startActivityForResult(intentHinh,PICK_IMAGE_REQUEST);
        });

        btnSave.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String date = edtDate.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();

            if (username.isEmpty() || date.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent resultIntent = new Intent();
            resultIntent.putExtra("USERNAME", username);
            resultIntent.putExtra("DATE", date);
            resultIntent.putExtra("ADDRESS", address);
            resultIntent.putExtra("PHONE", phone);

            if (selectedImageUri != null) {
                resultIntent.putExtra("IMAGE_URI", selectedImageUri.toString());
            }

            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            try{
                Bitmap img = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                imgProfile.setImageURI(selectedImageUri);

                // lưu ảnh vào bộ nhớ (tùy chọn)
                saveImageToInternalStorage(selectedImageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImageToInternalStorage(Uri selectedImageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

            File file = new File(getFilesDir(), "user_avatar.jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();

            Toast.makeText(this, "Đã lưu ảnh vào máy", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lưu ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    private void AddControls() {
        imgProfile = findViewById(R.id.imgProfile);
        edtUsername = findViewById(R.id.edtUsername);
        edtDate = findViewById(R.id.edtDate);
        edtAddress = findViewById(R.id.edtAddress);
        edtPhone = findViewById(R.id.edtPhone);
        btnSave = findViewById(R.id.btnSave);
        btnChangeImage = findViewById(R.id.btnChangeImage);
    }
}