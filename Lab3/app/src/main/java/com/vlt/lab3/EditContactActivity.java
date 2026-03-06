package com.vlt.lab3;

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

import java.io.File;

public class EditContactActivity extends AppCompatActivity {

    ImageView imgAvatar;
    EditText edName, edPhone;
    CheckBox cbStatus; // Khai báo thêm CheckBox
    Button btnSave, btnCancel;

    String selectedImagePath = null;
    ContactItem item;
    int realIndex = -1;

    static final int REQ_PICK_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        imgAvatar = findViewById(R.id.imgAvatar);
        edName = findViewById(R.id.edName);
        edPhone = findViewById(R.id.edPhone);
        cbStatus = findViewById(R.id.cbStatus); // Ánh xạ CheckBox
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // --- NHẬN DỮ LIỆU CŨ ---
        item = (ContactItem) getIntent().getSerializableExtra("DATA_TO_EDIT");
        realIndex = getIntent().getIntExtra("REAL_INDEX", -1);

        if (item != null) {
            edName.setText(item.getName());
            edPhone.setText(item.getPhone());

            // Set trạng thái cũ cho CheckBox
            cbStatus.setChecked(item.isStatus());

            if (item.getImagePath() != null) {
                // THÊM DÒNG NÀY ĐỂ GIỮ LẠI ĐƯỜNG DẪN ẢNH CŨ
                selectedImagePath = item.getImagePath();

                File imgFile = new File(item.getImagePath());
                if (imgFile.exists()) {
                    // Nếu file tồn tại trong bộ nhớ app, load nó lên
                    imgAvatar.setImageURI(Uri.fromFile(imgFile));
                } else {
                    // Fallback: Nếu dữ liệu cũ vẫn dùng link URI cũ, thử load (cho đỡ lỗi)
                    try {
                        imgAvatar.setImageURI(Uri.parse(item.getImagePath()));
                    } catch (Exception e) {
                        imgAvatar.setImageResource(android.R.drawable.ic_menu_camera);
                    }
                }
            } else {
                selectedImagePath = null;
                imgAvatar.setImageResource(android.R.drawable.ic_menu_camera);
            }

        }

        imgAvatar.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, REQ_PICK_IMAGE);
        });

        btnSave.setOnClickListener(v -> {
            String newName = edName.getText().toString();
            String newPhone = edPhone.getText().toString();
            boolean newStatus = cbStatus.isChecked(); // Lấy trạng thái của CheckBox

            if (newName.isEmpty() || newPhone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (item != null) {
                item.setName(newName);
                item.setPhone(newPhone);
                item.setStatus(newStatus); // Cập nhật trạng thái
                item.setImagePath(selectedImagePath);
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("UPDATED_CONTACT", item);
            resultIntent.putExtra("REAL_INDEX", realIndex);

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });

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

            // GỌI HÀM COPY ẢNH Ở ĐÂY
            String realPath = ImageUtils.getImagePathFromUri(this, uri);

            selectedImagePath = realPath;
            imgAvatar.setImageURI(Uri.fromFile(new File(realPath)));
        }
    }
}