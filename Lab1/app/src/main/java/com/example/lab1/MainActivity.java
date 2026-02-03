package com.example.lab1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import androidx.annotation.Nullable; // Import này để dùng @Nullable
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.Toolbar;

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

    ArrayList<Student> data;
    StudentAdapter adapter;

    // Biến lưu ảnh tạm thời
    Uri selectedImageUri = null;

    // Mã Request Code để nhận diện việc chọn ảnh (số nào cũng được)
    int REQUEST_CODE_IMAGE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Thêm dòng này để thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        // --- SỬA ĐỔI: Dùng startActivityForResult ---
        ivAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Gọi Intent và chờ kết quả trả về với mã 999
            startActivityForResult(intent, REQUEST_CODE_IMAGE);
        });

        // Setup Spinner
        String[] queQuan = {"Quê quán", "Hà Nội", "Nam Định", "Hà Nam", "Thái Bình", "Hải Phòng"};
        ArrayAdapter<String> sp = new ArrayAdapter<String>(this, R.layout.sp_item, R.id.spItem, queQuan) {
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

        // Setup ListView
        data = new ArrayList<>();
        Uri defaultImg = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.pic1);
        data.add(new Student("Le Thanh Vy - 3423432 - Nam - Ha Noi", defaultImg));

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

            String hobby = "";
            if (cbH1.isChecked()) hobby += cbH1.getText().toString();
            if (cbH2.isChecked()) hobby += cbH2.getText().toString();
            if (cbH3.isChecked()) hobby += cbH3.getText().toString();
            if (cbH4.isChecked()) hobby += cbH4.getText().toString();
            if (cbH5.isChecked()) hobby += cbH5.getText().toString();
            if (cbH6.isChecked()) hobby += cbH6.getText().toString();


            String info = name + " - " + phone + " - " + gender + " - " + ht+" - "+hobby;

            // Thêm vào list
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

        registerForContextMenu(l);
    }

    // --- HÀM NÀY ĐỂ NHẬN KẾT QUẢ TRẢ VỀ ---
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Kiểm tra đúng mã Request (999) và kết quả OK
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            // Lấy đường dẫn ảnh
            selectedImageUri = data.getData();
            // Hiển thị lên ImageView
            ivAvatar.setImageURI(selectedImageUri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        if (item.getItemId() == R.id.menu_exit) {
            finish(); // Thoát ứng dụng
            return true;
        } else if (item.getItemId() == R.id.ctx_edit) {
            // Logic Sửa: Ví dụ lấy tên sinh viên đổ lại vào EditText
            Student selectedStudent = data.get(index);
            uName.setText(selectedStudent.getInfo());
            Toast.makeText(this, "Đang sửa mục số " + (index + 1), Toast.LENGTH_SHORT).show();
            return true;

        } else if (item.getItemId() == R.id.ctx_delete) {
            // Logic Xóa
            data.remove(index);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(android.view.ContextMenu menu, View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        menu.setHeaderTitle("Tùy chọn sinh viên");
    }
}