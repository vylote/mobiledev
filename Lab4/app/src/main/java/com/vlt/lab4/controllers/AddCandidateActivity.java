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

public class AddCandidateActivity extends AppCompatActivity {
    EditText edtSbd, edtHoTen, edtToan, edtLy, edtHoa;
    Button btnThem, btnQV;
    boolean isDataValid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_candidate);

        edtSbd = findViewById(R.id.edtSbd);
        edtHoTen = findViewById(R.id.edtHt);
        edtToan = findViewById(R.id.edtToan);
        edtLy = findViewById(R.id.edtLy);
        edtHoa = findViewById(R.id.edtHoa);
        btnThem = findViewById(R.id.btnThem);
        btnQV = findViewById(R.id.btnQV);

        btnQV.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        btnThem.setOnClickListener(v -> {
            isDataValid = true;

            String sbd = edtSbd.getText().toString().trim();
            String ht = edtHoTen.getText().toString().trim();

            float toan = validateAndGetScore(edtToan);
            float ly = validateAndGetScore(edtLy);
            float hoa = validateAndGetScore(edtHoa);

            if (isDataValid && !sbd.isEmpty()) {
                ThiSinh ts = new ThiSinh(sbd, ht, toan, ly, hoa);

                Intent resIntent = new Intent();
                resIntent.putExtra("NEW_CANDIDATE", ts);
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
