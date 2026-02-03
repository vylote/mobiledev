package com.vlt.lab2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;


public class TestMainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);

        edSearch = findViewById(R.id.edSearch);
        listView = findViewById(R.id.listView);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);

        repo = new ContactRepository(this);
        actionHandler = new ContactActionHandler(this);

        adapter = new TestContactAdapter(this, repo.getDisplayData());
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        setUpEvents();
    }

    private void setUpEvents() {

        // Thêm mới
        btnAdd.setOnClickListener(v -> {
            // Kiểm tra xem có bất kỳ item nào đang được chọn không
            boolean hasSelection = false;
            for (ContactItem item : repo.getOriginalData()) {
                if (item.isStatus()) { // Giả sử isStatus() trả về giá trị của CheckBox
                    hasSelection = true;
                    break;
                }
            }

            if (hasSelection) {
                // Nếu có CheckBox đang tick, báo Toast và không làm gì thêm
                Toast.makeText(this, "Vui lòng bỏ chọn các mục trước khi thêm mới!", Toast.LENGTH_SHORT).show();
            } else {
                // Nếu không có mục nào được chọn, tiến hành mở Activity thêm mới
                startActivityForResult(
                        new Intent(this, TestAddContactActivity.class),
                        REQ_ADD_CONTACT);
            }
        });

        // Xóa
        btnDelete.setOnClickListener(v -> {
            if (repo.deleteSelected()) {
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Chưa chọn gì", Toast.LENGTH_SHORT).show();
            }
        });

        // Tìm kiếm (giữ nguyên style của bạn)
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
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        // Ép kiểu để lấy vị trí
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        currentSelectedPosition = info.position; // Lưu lại ngay lập tức

        getMenuInflater().inflate(R.menu.contact_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Log.d("CTX", "Context menu triggered");
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        ContactItem selected = repo.getDisplayData().get(info.position);
        int realIndex = repo.getOriginalData().indexOf(selected);

        actionHandler.handle(item, selected, realIndex);
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Kiểm tra resultCode trước để tránh xử lý khi người dùng nhấn Back
        if (resultCode != RESULT_OK || data == null) return;

        if (requestCode == REQ_ADD_CONTACT) {
            ContactItem item = (ContactItem) data.getSerializableExtra("NEW_CONTACT");
            repo.add(item);
            adapter.notifyDataSetChanged();
        }

        if (requestCode == REQ_EDIT_CONTACT) {
            ContactItem item = (ContactItem) data.getSerializableExtra("UPDATED_CONTACT");
            int index = data.getIntExtra("REAL_INDEX", -1);
            // Kiểm tra kĩ index trước khi update vào Repo
            if (index != -1 && item != null) {
                repo.update(index, item);
                // Quan trọng: Sau khi update, phải gọi lại filter để đồng bộ danh sách hiển thị
                repo.filter(edSearch.getText().toString());
                adapter.notifyDataSetChanged();
            }
        }

        if (requestCode == REQ_OPEN_CAMERA || requestCode == REQ_OPEN_GALLERY) {
            if (currentSelectedPosition != -1) {
                ContactItem selectedItem = repo.getDisplayData().get(currentSelectedPosition);

                if (requestCode == REQ_OPEN_GALLERY) {
                    selectedItem.setImagePath(data.getData().toString());
                } else {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");

                    String path = MediaStore.Images.Media
                            .insertImage(getContentResolver(),
                                    photo,
                                    "Avatar_" + System.currentTimeMillis(), null);
                    selectedItem.setImagePath(path);
                }

                adapter.notifyDataSetChanged();
                currentSelectedPosition = -1;
                Toast.makeText(this, "Cập nhật ảnh đại diện thành công", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
