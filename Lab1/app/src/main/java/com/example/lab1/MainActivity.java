package com.example.lab1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText uName, uPhone;
    RadioGroup rdGender;
    RadioButton rdbNam, rdbNu;
    Spinner spHomeTown;
    CheckBox cbH1, cbH2, cbH3, cbH4, cbH5, cbH6;
    Button btnAdd;
    ListView l;
    ImageView ivAvatar;

    // --- 1. SỬA KHAI BÁO TẠI ĐÂY ---
    ArrayList<Student> data; // Đổi từ String sang Student
    StudentAdapter adapter;  // Đổi từ ArrayAdapter sang StudentAdapter

    Uri selectedImageUri = null; // Biến lưu ảnh tạm thời
    ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ánh xạ View
        ivAvatar = findViewById(R.id.ivAvatar);
        uName = findViewById(R.id.uName);
        uPhone = findViewById(R.id.uPhone);
        rdGender = findViewById(R.id.rdGender);
        rdbNam = findViewById(R.id.rdbNam);
        rdbNu = findViewById(R.id.rdbNu);
        spHomeTown = findViewById(R.id.spHomeTown);
        cbH1 = findViewById(R.id.cbH1); cbH2 = findViewById(R.id.cbH2);
        cbH3 = findViewById(R.id.cbH3); cbH4 = findViewById(R.id.cbH4);
        cbH5 = findViewById(R.id.cbH5); cbH6 = findViewById(R.id.cbH6);
        btnAdd = findViewById(R.id.btnAdd);
        l = findViewById(R.id.listView3);

        // Launcher chọn ảnh
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        ivAvatar.setImageURI(selectedImageUri);
                    }
                }
        );

        ivAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        // Setup Spinner
        String[] queQuan = {"Quê quán","Hà Nội", "Nam Định", "Hà Nam", "Thái Bình", "Hải Phòng"};
        ArrayAdapter<String> sp = new ArrayAdapter<String>(this, R.layout.sp_item, R.id.spItem, queQuan){
            @Override public boolean isEnabled(int position) { return position != 0; }
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView tv = v.findViewById(R.id.spItem);
                tv.setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
                return v;
            }
            @Override public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                TextView tv = v.findViewById(R.id.spItem);
                tv.setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
                return v;
            }
        };
        spHomeTown.setAdapter(sp);
        spHomeTown.setSelection(0);

        // --- 2. SỬA KHỞI TẠO LIST VÀ ADAPTER TẠI ĐÂY ---
        data = new ArrayList<>();

        // Tạo ảnh mẫu cho dữ liệu có sẵn
        Uri defaultImg = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.pic1);

        // Thêm Student thay vì String
        data.add(new Student("Le Thanh Vy - 3423432 - Nam - Ha Noi", defaultImg));

        // Khởi tạo StudentAdapter (Custom Adapter)
        adapter = new StudentAdapter(this, data);
        l.setAdapter(adapter);

        // Sự kiện nút Add
        btnAdd.setOnClickListener(v -> {
            String name = uName.getText().toString();
            if (name.equals("")) { Toast.makeText(this, "Thiếu tên", Toast.LENGTH_SHORT).show(); return; }
            String phone = uPhone.getText().toString();
            if (phone.equals("")) { Toast.makeText(this, "Thiếu SDT", Toast.LENGTH_SHORT).show(); return; }

            String gender = "";
            int isChecked = rdGender.getCheckedRadioButtonId();
            if (isChecked == -1) { Toast.makeText(this, "Thiếu giới tính", Toast.LENGTH_SHORT).show(); return; }
            if (isChecked == R.id.rdbNam) gender = "Nam";
            if (isChecked == R.id.rdbNu) gender = "Nu";

            String ht = spHomeTown.getSelectedItemId() > 0 ? spHomeTown.getSelectedItem().toString() : "";
            if (ht.equals("")) { Toast.makeText(this, "Thiếu quê quán", Toast.LENGTH_SHORT).show(); return; }

            String info = name + " - " + phone + " - " + gender + " - " + ht;

            // --- 3. SỬA LẠI LOGIC THÊM VÀO LIST ---
            // Tạo đối tượng Student từ thông tin và ảnh đang chọn (selectedImageUri)
            data.add(new Student(info, selectedImageUri));
            adapter.notifyDataSetChanged();

            // Reset Form
            uName.setText("");
            uPhone.setText("");
            rdGender.clearCheck();
            cbH1.setChecked(false); cbH2.setChecked(false); cbH3.setChecked(false);
            cbH4.setChecked(false); cbH5.setChecked(false); cbH6.setChecked(false);
            spHomeTown.setSelection(0);

            // Reset ảnh
            ivAvatar.setImageResource(android.R.drawable.ic_menu_camera);
            selectedImageUri = null;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}