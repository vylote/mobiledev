package com.vlt.lab4;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vlt.lab4.controllers.AddCandidateActivity;
import com.vlt.lab4.controllers.EditCandidateActivity;
import com.vlt.lab4.models.ThiSinh;
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
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int id = item.getItemId();

        if (id == R.id.menu_sort_score) {
            adapter.updateList(repo.getAllSortedByTotalScore());
            return true;
        } else if (id == R.id.menu_sort_sbd) {
            adapter.updateList(repo.getAllSortedBySBD());
            return true;
        } else if (id == R.id.menu_sort_dtb) {
            adapter.updateList(repo.getAllSortedByDTB());
        } else if (id == R.id.menu_sort_reset) {
            adapter.updateList(repo.getAll());
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
                String keyword = s.toString().trim();
                ArrayList<ThiSinh> filteredList = repo.filter(keyword);
                adapter.updateList(filteredList);
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
        adapter.updateList(repo.getAll());
    }
}