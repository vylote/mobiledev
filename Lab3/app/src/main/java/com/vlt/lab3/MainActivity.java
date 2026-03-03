package com.vlt.lab3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText edSearch;
    Button btnAdd, btnDelete;
    ListView listView;
    ContactRepository repo;
    ContactActionHandler actionHandler;
    ArrayAdapter<ContactItem> adapter;

    private int currentSelectedPosition = -1;
    static final int REQ_ADD_CONTACT = 999, REQ_EDIT_CONTACT = 1000,
            REQ_OPEN_CAMERA = 101, REQ_OPEN_GALLERY = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout .activity_main);

        // Ánh xạ
        edSearch = findViewById(R.id.edSearch);
        listView = findViewById(R.id.listView);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);

        repo = new ContactRepository(this);
        actionHandler = new ContactActionHandler(this);

        adapter = new ContactAdapter(this, repo.getDisplayData());
        listView.setAdapter(adapter);

        // Đã bỏ registerForContextMenu vì sẽ xử lý qua OnItemLongClickListener
        setUpEvents();
    }

    private void setUpEvents() {
        // 1. Sự kiện Thêm mới
        btnAdd.setOnClickListener(v -> {
            boolean hasSelection = false;
            for (ContactItem item : repo.getOriginalData()) {
                if (item.isStatus()) {
                    hasSelection = true;
                    break;
                }
            }
            if (hasSelection) {
                toast("Vui lòng bỏ chọn các mục trước khi thêm mới!");
            } else {
                startActivityForResult(new Intent(this, AddContactActivity.class), REQ_ADD_CONTACT);
            }
        });

        // 2. Sự kiện Xóa các mục đã chọn qua CheckBox
        btnDelete.setOnClickListener(v -> {
            if (repo.deleteSelected()) {
                adapter.notifyDataSetChanged();
                toast("Đã xóa");
            } else {
                toast("Chưa chọn mục nào để xóa");
            }
        });

        // 3. Sự kiện Tìm kiếm
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                repo.filter(s.toString());
                adapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 4. SỰ KIỆN NHẤN GIỮ PHẦN TỬ (Refactored Context Menu)
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            currentSelectedPosition = position; // Lưu vị trí item được chọn

            // Khởi tạo PopupMenu tại vị trí View (item) đang nhấn giữ
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
            popupMenu.getMenuInflater().inflate(R.menu.contact_context_menu, popupMenu.getMenu());

            // Lắng nghe sự kiện click vào item trong menu
            popupMenu.setOnMenuItemClickListener(item -> {
                ContactItem selected = repo.getDisplayData().get(position);
                int realIndex = repo.getOriginalData().indexOf(selected);

                // Chuyển xử lý cho actionHandler
                actionHandler.handle(item, selected, realIndex);
                return true;
            });

            popupMenu.show(); // Hiển thị menu
            return true; // Trả về true để báo hiệu đã xử lý xong sự kiện Long Click
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) return;

        // Xử lý kết quả trả về từ Add Activity
        if (requestCode == REQ_ADD_CONTACT) {
            ContactItem item = (ContactItem) data.getSerializableExtra("NEW_CONTACT");
            repo.add(item);
            adapter.notifyDataSetChanged();
        }

        // Xử lý kết quả trả về từ Edit Activity
        if (requestCode == REQ_EDIT_CONTACT) {
            ContactItem item = (ContactItem) data.getSerializableExtra("UPDATED_CONTACT");
            int index = data.getIntExtra("REAL_INDEX", -1);
            if (index != -1 && item != null) {
                repo.update(index, item);
                repo.filter(edSearch.getText().toString());
                adapter.notifyDataSetChanged();
            }
        }

        // Xử lý kết quả từ Camera/Gallery (dùng currentSelectedPosition đã lưu lúc Long Click)
        if (requestCode == REQ_OPEN_CAMERA || requestCode == REQ_OPEN_GALLERY) {
            if (currentSelectedPosition != -1) {
                ContactItem selectedItem = repo.getDisplayData().get(currentSelectedPosition);
                if (requestCode == REQ_OPEN_GALLERY) {
                    selectedItem.setImagePath(data.getData().toString());
                } else {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), photo, "Avatar_" + System.currentTimeMillis(), null);
                    selectedItem.setImagePath(path);
                }
                adapter.notifyDataSetChanged();
                currentSelectedPosition = -1; // Reset vị trí sau khi dùng xong
                toast("Cập nhật ảnh đại diện thành công");
            }
        }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}