package com.vlt.lab4;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vlt.lab4.controllers.AddCandidateActivity;
import com.vlt.lab4.controllers.EditCandidateActivity;
import com.vlt.lab4.models.ThiSinh;
import com.vlt.lab4.repository.ThiSinhRepository;

public class MainActivity extends AppCompatActivity {

    TextView lbApp;
    EditText edtSearch;
    ListView lThiSinh;
    FloatingActionButton fabAdd;
    ThiSinhRepository repo;
    ThiSinhAdapter adapter;

    static final int REQ_ADD_CANDIDATE = 100, REQ_EDIT_CANDIDATE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        lbApp = findViewById(R.id.lbApp);
        edtSearch = findViewById(R.id.edtSearch);
        lThiSinh = findViewById(R.id.lThiSinh);
        fabAdd = findViewById(R.id.fabAdd);

        repo = new ThiSinhRepository(this);
        adapter = new ThiSinhAdapter(this, repo.getCopy());
        lThiSinh.setAdapter(adapter);

        registerForContextMenu(lThiSinh);
        setUpEvents();
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

        if (id == R.id.menu_edit) {
            ThiSinh ts = repo.getCopy().get(position);
            int realIndex = repo.getData().indexOf(ts);
            Intent i = new Intent(this, EditCandidateActivity.class);
            i.putExtra("DATA_TO_EDIT", ts);
            i.putExtra("REAL_INDEX", realIndex);
            startActivityForResult(i, REQ_EDIT_CANDIDATE);
        } else if (id == R.id.menu_delete) {
            ThiSinh ts = repo.getCopy().get(position);
            int realIndex = repo.getData().indexOf(ts);

            new AlertDialog.Builder(this)
                    .setTitle("Delete confirm")
                    .setMessage("Ban co chac chan muon xoa thi sinh: "+ts.getSbd())
                    .setIcon(android.R.drawable.ic_delete)
                    .setPositiveButton("Delete", (dialog, which) -> {
                        if (realIndex != -1) {
                            repo.delete(realIndex);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(this, "Xoa thanh cong", Toast.LENGTH_SHORT).show();
                        }
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) return;

        if (requestCode == REQ_ADD_CANDIDATE) {
            ThiSinh ts = (ThiSinh) data.getSerializableExtra("NEW_CANDIDATE");
            repo.add(ts);
            adapter.notifyDataSetChanged();
        }

        if (requestCode == REQ_EDIT_CANDIDATE) {
            ThiSinh ts = (ThiSinh) data.getSerializableExtra("UPDATE_CANDIDATE");
            int realIndex = data.getIntExtra("REAL_INDEX", -1);
            if (realIndex != -1 && ts != null) {
                repo.update(realIndex, ts);
                adapter.notifyDataSetChanged();
            }
        }
    }
}