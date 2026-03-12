package com.vlt.lab4;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vlt.lab4.activities.AddCandidateActivity;
import com.vlt.lab4.activities.EditCandidateActivity;
import com.vlt.lab4.adapter.ThiSinhAdapter;
import com.vlt.lab4.models.ThiSinh;
import com.vlt.lab4.receivers.BatteryReceiver;
import com.vlt.lab4.receivers.NetworkReceiver;
import com.vlt.lab4.repository.ThiSinhRepository;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edtSearch;
    ListView lThiSinh;
    FloatingActionButton fabAdd;
    ThiSinhRepository repo;
    ThiSinhAdapter adapter;
    Toolbar option;

    static final int REQ_ADD_CANDIDATE = 100, REQ_EDIT_CANDIDATE = 101;
    private String currentKeyword = "";
    private String currentSortType = "NONE";

    private NetworkReceiver networkReceiver;
    private BatteryReceiver batteryReceiver;

    @Override
    protected void onStart() {
        super.onStart();
        // 1. Khởi tạo receiver
        networkReceiver = new NetworkReceiver();
        batteryReceiver = new BatteryReceiver();
        // 2. Tạo bộ lọc hành động kết nối mạng
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        IntentFilter filter1 = new IntentFilter(Intent.ACTION_BATTERY_LOW);
        // 3. Đăng ký receiver động
        registerReceiver(networkReceiver, filter);
        registerReceiver(batteryReceiver, filter1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Đừng quên hủy đăng ký khi app đóng để tránh rò rỉ bộ nhớ
        if (networkReceiver != null) {
            unregisterReceiver(networkReceiver);
        }
        if (batteryReceiver != null) {
            unregisterReceiver(batteryReceiver);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        option = findViewById(R.id.toolbar);
        setSupportActionBar(option);
        edtSearch = findViewById(R.id.edtSearch);
        lThiSinh = findViewById(R.id.lThiSinh);
        fabAdd = findViewById(R.id.fabAdd);

        repo = new ThiSinhRepository(this);
        adapter = new ThiSinhAdapter(this, repo.getAll());
        lThiSinh.setAdapter(adapter);

        registerForContextMenu(lThiSinh);
        setUpEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_sort_score) {
            currentSortType = "SCORE_ASC";
            refreshUI();
            return true;
        } else if (id == R.id.menu_sort_sbd) {
            currentSortType = "SBD_ASC";
            refreshUI();
            return true;
        } else if (id == R.id.menu_sort_dtb) {
            currentSortType = "AVG_SCORE_ASC";
            refreshUI();
            return true;
        } else if (id == R.id.menu_sort_reset) {
            currentSortType = "";
            refreshUI();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int position = info.position;
        int id = item.getItemId();

        ThiSinh ts = adapter.getItem(position);

        if (id == R.id.menu_edit) {
            Intent i = new Intent(this, EditCandidateActivity.class);
            i.putExtra("DATA_TO_EDIT", ts);
            i.putExtra("OLD_SBD", ts.getSbd());
            startActivityForResult(i, REQ_EDIT_CANDIDATE);
        } else if (id == R.id.menu_delete) {

            new AlertDialog.Builder(this)
                    .setTitle("Delete confirm")
                    .setMessage("Ban co chac chan muon xoa thi sinh: "+ts.getSbd())
                    .setIcon(android.R.drawable.ic_delete)
                    .setPositiveButton("Delete", (dialog, which) -> {
                        repo.delete(ts.getSbd());
                        refreshUI();
                        Toast.makeText(this, "Xoa thanh cong", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        } else if (id == R.id.menu_find_phone_name) {
            String hoTen = ts.getHoTen();
            ArrayList<String> phones = findContactPhone(hoTen);

            if (!phones.isEmpty()) {
                // Gộp các số điện thoại tìm được thành một chuỗi để hiển thị
                StringBuilder sb = new StringBuilder();
                for (String p : phones) {
                    sb.append("- ").append(p).append("\n");
                }

                new AlertDialog.Builder(this)
                        .setTitle("SĐT tìm được cho: " + hoTen)
                        .setMessage(sb.toString())
                        .setPositiveButton("Đóng", null)
                        .show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("SĐT tìm cho: " + hoTen)
                        .setMessage("Không có sdt thí sinh này")
                        .setPositiveButton("Đóng", null)
                        .show();
            }
        }

        return super.onContextItemSelected(item);
    }

    private void setUpEvents() {
        fabAdd.setOnClickListener(v -> {
            Intent i = new Intent(this, AddCandidateActivity.class);
            startActivityForResult(i, REQ_ADD_CANDIDATE);
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentKeyword = s.toString().trim();
                refreshUI();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) return;

        if (requestCode == REQ_ADD_CANDIDATE) {
            ThiSinh ts = (ThiSinh) data.getSerializableExtra("NEW_CANDIDATE");
            repo.add(ts);
            refreshUI();
        }

        if (requestCode == REQ_EDIT_CANDIDATE) {
            ThiSinh ts = (ThiSinh) data.getSerializableExtra("UPDATE_CANDIDATE");
            String oldSbd = data.getStringExtra("OLD_SBD");
            if (ts != null && oldSbd != null) {
                repo.update(oldSbd, ts);
                refreshUI();
            }
        }
    }

    private void refreshUI() {
        ArrayList<ThiSinh> processedData = repo.getData(currentKeyword, currentSortType);
        adapter.updateList(processedData);
    }

    private ArrayList<String> findContactPhone(String candidateName) {
        ArrayList<String> list = new ArrayList<>();

        // --- ĐÂY LÀ ĐỊA CHỈ CỦA CONTENT PROVIDER ---
        // URI này trỏ thẳng tới "nhà" của ứng dụng Danh bạ hệ thống
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        //Chỉ lấy cột số điện thoại
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
        /*Điều kiện lọc dữ liệu (tương đương với mệnh đề WHERE trong SQL)
        Tìm người có tên khớp với biến truyền vào*/
        String selection = "UPPER(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") = UPPER(?)";
        //Các giá trị cụ thể để thay thế vào dấu '?' trong phần Selection
        String[] selectionArgs = {candidateName};
        //tuong tu order by
        String sortOrder = ContactsContract.CommonDataKinds.Phone.NUMBER + " ASC";

        // --- ĐÂY LÀ CONTENT RESOLVER ---
        // getContentResolver() là "người đi mượn" dữ liệu từ ứng dụng khác
        try (Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}