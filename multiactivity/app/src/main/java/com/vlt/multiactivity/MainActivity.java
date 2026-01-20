package com.vlt.multiactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lvContact;
    private EditText etSearch;
    private Button btnAdd, btnDelete;

    ArrayList<User> listUser;

    MyAdapter listUserAdapter;
    int selectedid = -1;

    // Khai báo DB nhưng chưa dùng
    MyDB mysqlitedb;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menuitem, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // SỬA LỖI: Dùng if-else thay cho switch-case
        int id = item.getItemId();

        if (id == R.id.mnuSort) {
            Toast.makeText(MainActivity.this, "Sort", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.mnuAdd) {
            // Chuyển sang màn hình AddUser
            Intent intent = new Intent(MainActivity.this, AddUser.class);
            // requestCode = 100 cho hành động Thêm mới
            startActivityForResult(intent, 100);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.contextmenuitem, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        // Lấy thông tin vị trí item được chọn trong listview
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        // Cập nhật selectedid dựa trên vị trí item được nhấn giữ
        selectedid = info.position;

        int id = item.getItemId();

        if (id == R.id.mnuEdit) {
            Intent intent = new Intent(MainActivity.this, AddUser.class);
            Bundle bundle = new Bundle();
            // Lấy dữ liệu từ listUser dựa trên selectedid
            bundle.putInt("Id", listUser.get(selectedid).getId());
            bundle.putString("Name", listUser.get(selectedid).getName());
            bundle.putString("Phone", listUser.get(selectedid).getPhoneNum());
            intent.putExtras(bundle);
            startActivityForResult(intent, 200); // 200 cho Edit
        } else if (id == R.id.mnuDelete) {
            User userToDelete = listUser.get(selectedid);
            Toast.makeText(this, "Delete: " + userToDelete.getName(), Toast.LENGTH_SHORT).show();

            listUser.remove(selectedid);
            listUserAdapter.notifyDataSetChanged();

            // --- PHẦN DB TẠM ĐÓNG ---
            // mysqlitedb.deleteContact(userToDelete.getId());
        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Nếu không có data trả về (người dùng bấm Back hoặc Cancel) thì thoát
        if (data == null) return;

        Bundle bundle = data.getExtras();
        int id = bundle.getInt("Id");
        String name = bundle.getString("Name");
        String phone = bundle.getString("Phone");

        String imageUri = bundle.getString("Image");

        // Xử lý Thêm mới (Request Code 100)
        if (requestCode == 100 && resultCode == 200) {
            // Thêm vào list tĩnh
            listUser.add(new User(id, name, phone, imageUri));

            // --- PHẦN DB TẠM ĐÓNG ---
            // mysqlitedb.addContact(new User(id, name, phone, ""));
        }
        // Xử lý Sửa (Request Code 200)
        else if (requestCode == 200 && resultCode == 200) { // Lưu ý: resultCode từ AddUser trả về là 200
            // Cập nhật vào list tĩnh
            listUser.set(selectedid, new User(id, name, phone, imageUri));

            // --- PHẦN DB TẠM ĐÓNG ---
            // mysqlitedb.updateContact(id, new User(id, name, phone, ""));
        }

        // Cập nhật adapter để ListView hiển thị dữ liệu mới
        listUserAdapter.notifyDataSetChanged();
        // Dòng này không cần thiết vì adapter vẫn đang gắn với listview
        // lvContact.setAdapter(listUserAdapter);
    }

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

        setContentView(R.layout.activity_main);

        lvContact = findViewById(R.id.lvContact);
        etSearch = findViewById(R.id.etSearch);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        etSearch.setText("");

        // Đăng ký context menu cho ListView
        registerForContextMenu(lvContact);

        // --- PHẦN DB TẠM ĐÓNG ---
        /*
        mysqlitedb = new MyDB(this, "ContactDB", null, 1);
        // Dữ liệu mẫu thêm vào DB
        // mysqlitedb.addContact(new User(1, "Nam", "09893838", ""));
        // mysqlitedb.addContact(new User(2, "Bich", "03393039", ""));
        // mysqlitedb.addContact(new User(3, "Hai", "098789089", ""));

        // Lấy dữ liệu từ DB
        listUser = mysqlitedb.getAllContact();
        */

        // --- DÙNG DỮ LIỆU TĨNH (STATIC DATA) ---
        listUser = new ArrayList<>();

        String pkg = getPackageName();

        listUser.add(new User(1, "Nam", "09893838",
                "android.resource://" + pkg + "/" + R.drawable.a1));
        listUser.add(new User(2, "Bich", "03393039",
                "android.resource://" + pkg + "/" + R.drawable.a2));
        listUser.add(new User(3, "Hai", "098789089",
                "android.resource://" + pkg + "/" + R.drawable.a3));

        // Tạo Adapter và gắn vào ListView
        listUserAdapter = new MyAdapter(this, listUser);
        lvContact.setAdapter(listUserAdapter);

        // --- XỬ LÝ NÚT ADD ---
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra xem có ai đang được check không
                boolean isAnyChecked = false;
                for (User u : listUser) {
                    if (u.isChecked()) {
                        isAnyChecked = true;
                        break;
                    }
                }

                if (isAnyChecked) {
                    // Nếu có người đang check thì báo lỗi như yêu cầu
                    Toast.makeText(MainActivity.this,
                            "User đang được chọn (đã tồn tại), vui lòng bỏ chọn để thêm mới!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Nếu không có ai check thì cho phép Add
                    Intent intent = new Intent(MainActivity.this, AddUser.class);
                    startActivityForResult(intent, 100);
                }
            }
        });

        // --- XỬ LÝ NÚT DELETE ---
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra xem có chọn ai để xóa không
                boolean hasSelection = false;
                for (User u : listUser) {
                    if (u.isChecked()) {
                        hasSelection = true;
                        break;
                    }
                }

                if (!hasSelection) {
                    Toast.makeText(MainActivity.this, "Vui lòng chọn User để xóa!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Hiển thị Dialog xác nhận
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa những User đã chọn không?");

                // Nút Đồng ý
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCheckedUsers();
                    }
                });

                // Nút Hủy
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

        // Xử lý sự kiện Long Click để lấy vị trí (để phục vụ cho Context Menu)
        lvContact.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedid = position;
                return false; // Trả về false để ContextMenu tiếp tục được hiển thị
            }
        });

        // Xử lý tìm kiếm
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listUserAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    // Hàm thực hiện xóa các user đã check
    private void deleteCheckedUsers() {
        // Tạo một list tạm để chứa những người cần xóa
        List<User> usersToRemove = new ArrayList<>();

        for (User u : listUser) {
            if (u.isChecked()) {
                usersToRemove.add(u);
                // Nếu dùng DB thì gọi lệnh xóa DB ở đây:
                // mysqlitedb.deleteContact(u.getId());
            }
        }

        // Xóa khỏi list chính
        listUser.removeAll(usersToRemove);

        // Cập nhật giao diện
        listUserAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Đã xóa " + usersToRemove.size() + " user.", Toast.LENGTH_SHORT).show();
    }
}
