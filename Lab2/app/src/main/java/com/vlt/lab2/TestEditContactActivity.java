package com.vlt.lab2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestEditContactActivity extends AppCompatActivity {

    ImageView imgAvatar;
    EditText edName, edPhone;
    Button btnSave, btnCancel;

    String selectedImagePath = null;
    ContactItem item; // Lưu object đang sửa để cập nhật trực tiếp
    int realIndex = -1;

    static final int REQ_PICK_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Nạp giao diện từ XML
        setContentView(R.layout.activity_edit_contact);

        // Ánh xạ View
        imgAvatar = findViewById(R.id.imgAvatar);
        edName = findViewById(R.id.edName);
        edPhone = findViewById(R.id.edPhone);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // --- NHẬN DỮ LIỆU CŨ ---
        item = (ContactItem) getIntent().getSerializableExtra("DATA_TO_EDIT");
        realIndex = getIntent().getIntExtra("REAL_INDEX", -1);

        // Đổ dữ liệu lên giao diện (Binding)
        if (item != null) {
            edName.setText(item.getName());
            edPhone.setText(item.getPhone());

            if (item.getImagePath() != null) {
                selectedImagePath = item.getImagePath(); // Lưu lại đường dẫn cũ
                imgAvatar.setImageURI(Uri.parse(item.getImagePath()));
            } else {
                selectedImagePath = null;
                imgAvatar.setImageResource(android.R.drawable.ic_menu_camera);
            }
        }

        // --- SỰ KIỆN ---

        // 1. Chọn ảnh mới
        imgAvatar.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, REQ_PICK_IMAGE);
        });

        // 2. Lưu thay đổi
        btnSave.setOnClickListener(v -> {
            String newName = edName.getText().toString();
            String newPhone = edPhone.getText().toString();

            if (newName.isEmpty() || newPhone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cập nhật vào object cũ
            if (item != null) {
                item.setName(newName);
                item.setPhone(newPhone);
                item.setImagePath(selectedImagePath);
            }

            // Trả kết quả về
            Intent resultIntent = new Intent();
            resultIntent.putExtra("UPDATED_CONTACT", item);
            resultIntent.putExtra("REAL_INDEX", realIndex);

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });

        // 3. Hủy bỏ
        btnCancel.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
    }

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