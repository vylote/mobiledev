package com.vlt.lab2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddContactActivity extends AppCompatActivity {

    ImageView imgAvatar;
    EditText edName, edPhone;
    Button btnSave, btnCancel;
    String selectedImagePath = null;
    static final int REQ_PICK_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Layout chính (Vertical)
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(32, 32, 32, 32);
        root.setBackgroundColor(Color.WHITE);
        root.setFitsSystemWindows(true);

        // 2. Ảnh đại diện (Giả lập hình tròn bằng background)
        imgAvatar = new ImageView(this);
        int imgSize = (int) (120 * getResources().getDisplayMetrics().density);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(imgSize, imgSize);
        imgParams.setMargins(0, 50, 0, 50);

        imgAvatar.setBackgroundColor(Color.LTGRAY); // Placeholder
        imgAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // Mẹo: Bo tròn ảnh bằng GradientDrawable (chỉ bo viền, ảnh bên trong cần thư viện để tròn hẳn)
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(Color.LTGRAY);
        imgAvatar.setBackground(shape);
        imgAvatar.setClipToOutline(true); // Yêu cầu API 21+, cắt ảnh theo khung

        imgAvatar.setImageResource(android.R.drawable.ic_menu_camera);
        root.addView(imgAvatar, imgParams);

        // Sự kiện chọn ảnh
        imgAvatar.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, REQ_PICK_IMAGE);
        });

        // 3. Form nhập liệu
        edName = new EditText(this);
        edName.setHint("Nhập họ tên");
        root.addView(edName, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        edPhone = new EditText(this);
        edPhone.setHint("Nhập số điện thoại");
        edPhone.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
        root.addView(edPhone, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // 4. Layout nút bấm (Horizontal)
        LinearLayout btnLayout = new LinearLayout(this);
        btnLayout.setOrientation(LinearLayout.HORIZONTAL);
        btnLayout.setGravity(Gravity.CENTER);
        btnLayout.setPadding(0, 50, 0, 0);

        btnSave = new Button(this);
        btnSave.setText("SAVE");
        btnCancel = new Button(this);
        btnCancel.setText("CANCEL");


        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        btnParams.setMargins(10, 0, 10, 0);

        btnLayout.addView(btnSave, btnParams);
        btnLayout.addView(btnCancel, btnParams);
        root.addView(btnLayout);

        setContentView(root);

        // Xử lý nút SAVE
        btnSave.setOnClickListener(v -> {
            String name = edName.getText().toString();
            String phone = edPhone.getText().toString();

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo object ContactItem
            ContactItem newContact = new ContactItem(name, phone, false, selectedImagePath);

            // Trả dữ liệu về MainActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("NEW_CONTACT", newContact);
            setResult(Activity.RESULT_OK, resultIntent);
            finish(); // Đóng Activity này
        });

        // Xử lý nút CANCEL
        btnCancel.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            selectedImagePath = uri.toString();
            imgAvatar.setImageURI(uri);
        }
    }
}