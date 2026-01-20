package com.vlt.multiactivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AddUser extends AppCompatActivity {
    EditText etId, etName, etPhone;
    ImageView ivAvatar;
    Button btnOk, btnCancel;
    // Biến lưu đường dẫn ảnh
    String strUri = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- THÊM ĐOẠN NÀY ĐỂ ẨN THANH TRẠNG THÁI (FULL SCREEN) ---
        try {
            this.getSupportActionBar().hide(); // Ẩn thanh tiêu đề (tên App)
        } catch (NullPointerException e) { }

        // Ẩn thanh status bar (pin, wifi, giờ)
        this.getWindow().setFlags(
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_add_user);
        etId = findViewById(R.id.etId);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        ivAvatar = findViewById(R.id.ivAvatar);
        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null) {
            int id = bundle.getInt("Id");
            String name = bundle.getString("Name");
            String phone = bundle.getString("Phone");
            String img = bundle.getString("Image");
            etId.setText(String.valueOf(id));
            etName.setText(name);
            etPhone.setText(phone);
            // Hiển thị ảnh cũ nếu có
            if (img != null && !img.isEmpty()) {
                strUri = img;
                ivAvatar.setImageURI(Uri.parse(img));
            }
            btnOk.setText("Edit");
        }

        // SỰ KIỆN CHỌN ẢNH
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở thư viện ảnh trên điện thoại
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 999);
            }
        });

        // --- SỬA LOGIC NÚT OK ĐỂ VALIDATE ---
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Lấy dữ liệu ra trước
                String idText = etId.getText().toString().trim();
                String name = etName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();

                // 2. Validate ID
                if (TextUtils.isEmpty(idText)) {
                    etId.setError("Vui lòng nhập ID");
                    etId.requestFocus(); // Đưa con trỏ chuột về ô này
                    return; // Dừng lại, không chạy tiếp
                }

                // 3. Validate Tên
                if (TextUtils.isEmpty(name)) {
                    etName.setError("Vui lòng nhập tên");
                    etName.requestFocus();
                    return;
                }

                // 4. Validate Số điện thoại
                if (TextUtils.isEmpty(phone)) {
                    etPhone.setError("Vui lòng nhập số điện thoại");
                    etPhone.requestFocus();
                    return;
                }

                // --- NẾU QUA ĐƯỢC HẾT CÁC IF TRÊN THÌ MỚI XỬ LÝ ---

                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                // Lúc này an toàn để parse Int vì đã check isEmpty ở trên
                // Tuy nhiên vẫn nên dùng try-catch để an toàn tuyệt đối (tránh trường hợp nhập quá giới hạn int)
                try {
                    bundle.putInt("Id", Integer.parseInt(idText));
                } catch (NumberFormatException e) {
                    etId.setError("ID không hợp lệ (quá lớn hoặc sai định dạng)");
                    return;
                }

                bundle.putString("Name", name);
                bundle.putString("Phone", phone);
                bundle.putString("Image", strUri);

                intent.putExtras(bundle);
                setResult(200, intent);
                finish();
            }
        });

        // Thêm nút Cancel để đóng form nếu không muốn nhập
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // HÀM NHẬN KẾT QUẢ CHỌN ẢNH
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                // Cấp quyền đọc file vĩnh viễn (để lần sau mở app vẫn load được ảnh)
                getContentResolver().takePersistableUriPermission(uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                strUri = uri.toString(); // Lưu uri thành chuỗi
                ivAvatar.setImageURI(uri); // Hiển thị lên view
            }
        }
    }
}
