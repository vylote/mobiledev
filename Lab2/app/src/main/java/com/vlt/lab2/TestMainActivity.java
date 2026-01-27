package com.vlt.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class TestMainActivity extends AppCompatActivity {

    EditText edSearch;
    Button btnAdd, btnDelete, btnEdit;
    ListView listView;

    // Dữ liệu gốc và Adapter
    ArrayList<ContactItem> originalData = new ArrayList<>();
    ArrayList<ContactItem> displayData = new ArrayList<>();

    ArrayAdapter<ContactItem> adapter;
    static final int REQ_ADD_CONTACT = 999, REQ_EDIT_CONTACT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ View
        edSearch = findViewById(R.id.edSearch);
        listView = findViewById(R.id.listView);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);

        // --- INIT DATA ---
        originalData.add(new ContactItem("VyLoTe", "3849124", false, getUriToDrawable(R.drawable.p1)));
        originalData.add(new ContactItem("TrungRua", "3217987", false, getUriToDrawable(R.drawable.p2)));
        originalData.add(new ContactItem("BaMia", "334347", false, null));
        originalData.add(new ContactItem("BaMia", "334347", false, getUriToDrawable(R.drawable.p3)));
        originalData.add(new ContactItem("BaMia", "334347", false, getUriToDrawable(R.drawable.p1)));
        originalData.add(new ContactItem("BaMia", "334347", false, null));

        // Copy dữ liệu gốc sang hiển thị
        displayData.addAll(originalData);

        adapter = new ContactAdapter(this, displayData);
        listView.setAdapter(adapter);

        setUpEvents();
    }

    private String getUriToDrawable(int resId) {
        return "android.resource://" + getPackageName() + "/" + resId;
    }

    private void setUpEvents() {
        // 1. Thêm mới
        btnAdd.setOnClickListener(v -> {
            for (ContactItem item : originalData) {
                item.status = false;
            }
            // Cập nhật lại giao diện ngay để người dùng thấy dấu tích biến mất
            adapter.notifyDataSetChanged();

            /*for (ContactItem item : originalData) {
                if (item.status) {
                    Toast.makeText(this, "Ban k the them nguoi nay vi ho da ton tai", Toast.LENGTH_SHORT).show();
                    return;
                }
            }*/

            Intent intent = new Intent(TestMainActivity.this, TestAddContactActivity.class);
            startActivityForResult(intent, REQ_ADD_CONTACT);
        });

        // 2. Tìm kiếm
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 3. Xóa
        btnDelete.setOnClickListener(v -> {
            boolean hasDeleted = false;
            for (int i = displayData.size() - 1; i >= 0; i--) {
                ContactItem item = displayData.get(i);
                if (item.status) {
                    displayData.remove(i);
                    originalData.remove(item); // Xóa luôn trong gốc
                    hasDeleted = true;
                }
            }
            if(hasDeleted) {
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Chưa chọn mục nào để xóa", Toast.LENGTH_SHORT).show();
            }
        });

        // 4. Sửa (Logic quan trọng đã được Fix)
        btnEdit.setOnClickListener(v -> {
            ContactItem selectedItem = null;
            int count = 0;

            // Đếm số lượng item được chọn
            for (ContactItem item : displayData) {
                if (item.status) {
                    selectedItem = item;
                    count++;
                }
            }

            if (count == 0) {
                Toast.makeText(this, "Bạn chưa chọn liên hệ cần sửa", Toast.LENGTH_SHORT).show();
            } else if (count > 1) {
                Toast.makeText(this, "Chỉ được chọn 1 người để sửa", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(TestMainActivity.this, TestEditContactActivity.class);

                // Gửi dữ liệu item sang
                intent.putExtra("DATA_TO_EDIT", selectedItem);

                // --- FIX QUAN TRỌNG ---
                // Tìm vị trí thực sự của item này trong danh sách gốc (originalData)
                // Thay vì dùng vị trí trong danh sách hiển thị (displayData)
                int realIndex = originalData.indexOf(selectedItem);
                intent.putExtra("REAL_INDEX", realIndex);

                startActivityForResult(intent, REQ_EDIT_CONTACT);
            }
        });
    }

    private void filterData(String keyword) {
        displayData.clear();
        if (keyword == null || keyword.isEmpty()) {
            displayData.addAll(originalData);
        } else {
            String searchStr = keyword.toLowerCase();
            for (ContactItem item : originalData) {
                // Kiểm tra null để tránh Crash
                if (item != null && item.name != null) {
                    if (item.name.toLowerCase().contains(searchStr)) {
                        displayData.add(item);
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Xử lý khi Thêm mới thành công
        if (requestCode == REQ_ADD_CONTACT && resultCode == RESULT_OK && data != null) {
            ContactItem newItem = (ContactItem) data.getSerializableExtra("NEW_CONTACT");
            if (newItem != null) {
                originalData.add(newItem);

                // Reset tìm kiếm để hiện item mới nhất
                edSearch.setText("");
                filterData("");

                Toast.makeText(this, "Đã thêm: " + newItem.name, Toast.LENGTH_SHORT).show();
            }
        }

        // Xử lý khi Sửa thành công
        if (requestCode == REQ_EDIT_CONTACT && resultCode == RESULT_OK && data != null) {
            ContactItem updatedItem = (ContactItem) data.getSerializableExtra("UPDATED_CONTACT");

            // Nhận về vị trí thực (REAL_INDEX)
            int realIndex = data.getIntExtra("REAL_INDEX", -1);

            if (updatedItem != null && realIndex != -1) {
                // 1. Cập nhật vào kho gốc
                if (realIndex < originalData.size()) {
                    originalData.set(realIndex, updatedItem);
                }

                // 2. Cập nhật lại giao diện
                // Gọi filterData để danh sách hiển thị tự động lấy dữ liệu mới từ originalData
                // Điều này giúp giữ nguyên từ khóa tìm kiếm (nếu đang tìm dở)
                filterData(edSearch.getText().toString());

                Toast.makeText(this, "Đã cập nhật thành công", Toast.LENGTH_SHORT).show();
            }
        }
    }
}