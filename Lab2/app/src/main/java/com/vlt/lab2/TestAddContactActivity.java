package com.vlt.lab2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestAddContactActivity extends AppCompatActivity {

    ImageView imgAvatar;
    EditText edName, edPhone;
    Button btnSave, btnCancel;
    CheckBox cbStatus;

    String selectedImagePath = null;
    static final int REQ_PICK_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Nạp giao diện từ XML
        setContentView(R.layout.activity_add_contact);

        // Ánh xạ View
        imgAvatar = findViewById(R.id.imgAvatar);
        edName = findViewById(R.id.edName);
        edPhone = findViewById(R.id.edPhone);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        cbStatus = findViewById(R.id.cbStatus);


        // --- CÁC SỰ KIỆN (Logic giữ nguyên) ---

        // 1. Chọn ảnh
        imgAvatar.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, REQ_PICK_IMAGE);
        });

        // 2. Lưu (Save)
        btnSave.setOnClickListener(v -> {
            String name = edName.getText().toString();
            String phone = edPhone.getText().toString();
            boolean status = cbStatus.isChecked();

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo object ContactItem
            ContactItem newContact = new ContactItem(name, phone, status, selectedImagePath);

            // Trả dữ liệu về MainActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("NEW_CONTACT", newContact);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });

        // 3. Hủy (Cancel)
        btnCancel.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
    }

    // Nhận ảnh từ thư viện về
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            selectedImagePath = uri.toString();
            imgAvatar.setImageURI(uri);
        }
    }
}