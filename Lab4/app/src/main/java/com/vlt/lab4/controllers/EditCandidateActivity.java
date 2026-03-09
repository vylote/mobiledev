package com.vlt.lab4.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vlt.lab4.R;
import com.vlt.lab4.models.ThiSinh;

public class EditCandidateActivity extends AppCompatActivity {
    EditText edtSbd, edtHoTen, edtToan, edtLy, edtHoa;
    Button btnSua, btnQV;
    boolean isDataValid;
    ThiSinh ts;
//    int realIndex = -1;
    String oldSbd = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_candidate);

        edtSbd = findViewById(R.id.edtSbd);
        edtHoTen = findViewById(R.id.edtHt);
        edtToan = findViewById(R.id.edtToan);
        edtLy = findViewById(R.id.edtLy);
        edtHoa = findViewById(R.id.edtHoa);
        btnSua = findViewById(R.id.btnSua);
        btnQV = findViewById(R.id.btnQV);

        ts = (ThiSinh) getIntent().getSerializableExtra("DATA_TO_EDIT");
        oldSbd = getIntent().getStringExtra("OLD_SBD");

        if (ts != null) {
            edtSbd.setText(ts.getSbd());
            edtHoTen.setText(ts.getHoTen());
            edtToan.setText(String.format("%.2f", ts.getToan()));
            edtLy.setText(String.format("%.2f", ts.getLy()));
            edtHoa.setText(String.format("%.2f", ts.getHoa()));
        }

        btnQV.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        btnSua.setOnClickListener(v -> {
            isDataValid = true;

            String sbd = edtSbd.getText().toString().trim();
            String ht = edtHoTen.getText().toString().trim();

            float toan = validateAndGetScore(edtToan);
            float ly = validateAndGetScore(edtLy);
            float hoa = validateAndGetScore(edtHoa);

            if (isDataValid && !sbd.isEmpty() && ts != null) {
                ThiSinh ts = new ThiSinh(sbd, ht, toan, ly, hoa);

                Intent resIntent = new Intent();
                resIntent.putExtra("UPDATE_CANDIDATE", ts);
                resIntent.putExtra("OLD_SBD", oldSbd);
                setResult(RESULT_OK, resIntent);
                finish();
            }
        });
    }

    private float validateAndGetScore(EditText et) {
        try {
            return Float.parseFloat(et.getText().toString());
        } catch (NumberFormatException ex) {
            et.setError("vui long nhap so hop le");
            isDataValid = false;
            return 0;
        }
    }
}
