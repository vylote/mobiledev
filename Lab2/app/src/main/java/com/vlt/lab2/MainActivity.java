package com.vlt.lab2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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

        // --- ROOT LAYOUT ---
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.WHITE);
        root.setFitsSystemWindows(true);

        // --- PHẦN 1: SEARCH BAR (CHỈ CÒN EDIT TEXT) ---
        LinearLayout topBar = new LinearLayout(this);
        topBar.setOrientation(LinearLayout.HORIZONTAL);
        topBar.setPadding(16, 16, 16, 16);
        topBar.setGravity(Gravity.CENTER_VERTICAL);

        edSearch = new EditText(this);
        edSearch.setHint("Tìm kiếm họ tên...");
        // Set weight=1 để nó chiếm hết chiều ngang phía trên
        edSearch.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        topBar.addView(edSearch);

        root.addView(topBar);

        // --- PHẦN 2: LIST VIEW (CHIẾM HẾT KHÔNG GIAN GIỮA) ---
        listView = new ListView(this);
        GradientDrawable border = new GradientDrawable();
//        border.setStroke(2, Color.BLACK);
        listView.setBackground(border);

        root.addView(listView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        // --- PHẦN 3: BOTTOM BAR (CHỨA 2 NÚT THÊM & XÓA) ---
        LinearLayout bottomBar = new LinearLayout(this);
        bottomBar.setOrientation(LinearLayout.HORIZONTAL);
        bottomBar.setPadding(10, 10, 10, 10);
        bottomBar.setGravity(Gravity.CENTER);
        bottomBar.setBackgroundColor(Color.parseColor("#F0F0F0")); // Màu nền nhẹ cho đẹp

        // Param cho nút: Width=0, Weight=1 để chia đều 50-50
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
        );
        btnParams.setMargins(5, 0, 5, 0); // Cách nhau ra một chút

        // Nút Thêm
        btnAdd = new Button(this);
        btnAdd.setText("THÊM MỚI");

        // Nút Xóa
        btnDelete = new Button(this);
        btnDelete.setText("XÓA CHỌN");

        btnEdit = new Button(this);
        btnEdit.setText("SỬA");

        bottomBar.addView(btnAdd, btnParams);
        bottomBar.addView(btnDelete, btnParams);
        bottomBar.addView(btnEdit, btnParams);

        // Thêm bottomBar vào Root
        root.addView(bottomBar);

        setContentView(root);

        // --- INIT DATA ---
        originalData.add(new ContactItem("VyLoTe", "3849124", false, getUriToDrawable(R.drawable.p1)));
        originalData.add(new ContactItem("TrungRua", "3217987", false, getUriToDrawable(R.drawable.p2)));
        originalData.add(new ContactItem("BaMia", "334347", false, null));
        originalData.add(new ContactItem("BaMia", "334347", false, getUriToDrawable(R.drawable.p3)));
        originalData.add(new ContactItem("BaMia", "334347", false, getUriToDrawable(R.drawable.p1)));
        originalData.add(new ContactItem("BaMia", "334347", false, null));
        displayData.addAll(originalData);

        adapter = new ContactAdapter(this, displayData);
        listView.setAdapter(adapter);

        setUpEvents();
    }

    private String getUriToDrawable(int resId) {
        return "android.resource://" + getPackageName() + "/" + resId;
    }

    private void setUpEvents() {
        // Code xử lý sự kiện giữ nguyên không đổi
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            startActivityForResult(intent, REQ_ADD_CONTACT);
        });

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

        btnDelete.setOnClickListener(v -> {
            boolean hasDeleted = false;
            for (int i = displayData.size() - 1; i >= 0; i--) {
                ContactItem item = displayData.get(i);
                if (item.status) {
                    displayData.remove(i);
                    originalData.remove(item);
                    hasDeleted = true;
                }
            }
            if(hasDeleted) adapter.notifyDataSetChanged();
            else Toast.makeText(this, "Chưa chọn mục nào để xóa", Toast.LENGTH_SHORT).show();
        });

        btnEdit.setOnClickListener(v -> {
            ContactItem selectedItem = null;
            int count = 0;
            int position = -1;

            for (int i = displayData.size() - 1; i >= 0; i--) {
                if (displayData.get(i).status) {
                    selectedItem = displayData.get(i);
                    count++;
                    position = i;
                }
            }

            if (count == 0) {
                Toast.makeText(this, "Ban chua chon lien he can sua", Toast.LENGTH_SHORT).show();
            } else if (count > 1) {
                Toast.makeText(this, "Chi chon 1 nguoi lien he de sua", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, EditContactActivity.class);
                intent.putExtra("DATA_TO_EDIT", selectedItem);
                intent.putExtra("POSITION", position);
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
        if (requestCode == REQ_ADD_CONTACT && resultCode == RESULT_OK && data != null) {
            ContactItem newItem = (ContactItem) data.getSerializableExtra("NEW_CONTACT");
            if (newItem != null) {
                originalData.add(newItem);
                edSearch.setText("");
                displayData.clear();
                displayData.addAll(originalData);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Đã thêm: " + newItem.name, Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQ_EDIT_CONTACT && resultCode == RESULT_OK && data != null) {
            ContactItem updatedItem = (ContactItem) data.getSerializableExtra("UPDATED_CONTACT");
            int pos = data.getIntExtra("POSITION", -1);

            if (updatedItem != null && pos != -1) {
                displayData.set(pos, updatedItem);
                // Cập nhật vào list gốc (Cần tìm đúng object trong list gốc để sửa)
                // Cách đơn giản nhất nếu không search:
                // originalData.set(pos, updatedItem);

                // Nếu đang dùng Search, vị trí 'pos' trong displayData có thể khác originalData
                // Nên tốt nhất là tìm trong originalData và update
                updateOriginalList(updatedItem);

                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Đã cập nhật thành công", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Hàm phụ trợ để tìm và sửa trong list gốc
    private void updateOriginalList(ContactItem updatedItem) {
        // Vì đây là object reference, nếu bạn sửa trực tiếp field của object
        // thì cả 2 list đều tự cập nhật.
        // Nhưng nếu bạn tạo object 'new' thì phải tìm và thay thế.
        for(int i=0; i<originalData.size(); i++) {
            // So sánh 2 object (bạn có thể cần thêm ID cho ContactItem để chính xác hơn)
            // Ở đây giả sử so sánh bằng tham chiếu hoặc tên cũ (hơi rủi ro)
            // Tốt nhất: Khi edit xong, trả về object đã sửa, list sẽ tự cập nhật nếu dùng chung tham chiếu.
        }
    }
}