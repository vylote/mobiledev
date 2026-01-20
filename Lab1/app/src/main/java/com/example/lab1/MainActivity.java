package com.example.lab1;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText uName, uPhone;
    RadioGroup rdGender;
    RadioButton rdbNam, rdbNu;
    Spinner spHomeTown;
    CheckBox cbH1, cbH2, cbH3, cbH4, cbH5, cbH6;
    Button btnAdd;
    ListView l;
    ArrayList<String> data;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        uName = findViewById(R.id.uName);
        uPhone = findViewById(R.id.uPhone);
        rdGender = findViewById(R.id.rdGender);
        rdbNam = findViewById(R.id.rdbNam);
        rdbNu = findViewById(R.id.rdbNu);
        spHomeTown = findViewById(R.id.spHomeTown);
        cbH1 = findViewById(R.id.cbH1);
        cbH2 = findViewById(R.id.cbH2);
        cbH3 = findViewById(R.id.cbH3);
        cbH4 = findViewById(R.id.cbH4);
        cbH5 = findViewById(R.id.cbH5);
        cbH6 = findViewById(R.id.cbH6);
        btnAdd = findViewById(R.id.btnAdd);
        l = findViewById(R.id.listView3);

        String[] queQuan = {"Quê quán","Hà Nội", "Nam Định", "Hà Nam", "Thái Bình", "Hải Phòng"};
        ArrayAdapter<String> sp = new ArrayAdapter<String>(
                this,
                R.layout.sp_item,
                R.id.spItem,
                queQuan
        ){
            @Override
            public boolean isEnabled(int position) {
                return position != 0; // ❌ không cho chọn hint
            }
            // View hiển thị khi spinner ĐÓNG
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView tv = v.findViewById(R.id.spItem);

                if (position == 0) {
                    tv.setTextColor(Color.GRAY); // hint mờ
                } else {
                    tv.setTextColor(Color.BLACK); // item chọn
                }
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                TextView tv = v.findViewById(R.id.spItem);
                tv.setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
                return v;
            }
        };
        sp.setDropDownViewResource(R.layout.sp_item);
        spHomeTown.setAdapter(sp);
        spHomeTown.setSelection(0);

        data = new ArrayList<>();
        data.add("Le Thanh Vy - 3423432 - Nam - Ha Noi");
        adapter = new ArrayAdapter<>(
                this,
                R.layout.item_list,
                R.id.tvItem,
                data
        );
        l.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            String name = uName.getText().toString();
            if (name.equals("")) {
                Toast.makeText(this, "Tên không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }
            String phone = uPhone.getText().toString();
            if (phone.equals("")) {
                Toast.makeText(this, "SDT không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            String gender = "";
            int isChecked = rdGender.getCheckedRadioButtonId();
            if (isChecked == -1) {
                Toast.makeText(this, "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isChecked == R.id.rdbNam) gender = "Nam";
            if (isChecked == R.id.rdbNu) gender = "Nu";

            String ht = spHomeTown.getSelectedItemId() > 0? spHomeTown.getSelectedItem().toString():"";
            if (ht.equals("")) {
                Toast.makeText(this, "Quê không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            String item = name+" - "+phone+" - "+gender+" - "+ht;
            data.add(item);
            adapter.notifyDataSetChanged();

            uName.setText("");
            uPhone.setText("");
            rdGender.clearCheck();
            cbH1.setChecked(false);
            cbH2.setChecked(false);
            cbH3.setChecked(false);
            cbH4.setChecked(false);
            cbH5.setChecked(false);
            cbH6.setChecked(false);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}