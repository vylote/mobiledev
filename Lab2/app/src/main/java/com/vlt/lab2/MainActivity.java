package com.vlt.lab2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnAdd, btnDelete, imgSel;
    ImageView imgPV;
    EditText edHt, edPhone;
    ListView listView;
    ArrayList<ContactItem> data = new ArrayList<>();
    ArrayAdapter<ContactItem> adapter;
    static final int REQ_PICK_IMAGE = 101;
    String selectedImagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.WHITE);
        root.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // layout 1: form (weight = 2)
        root.addView(buildFormUI(),
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        2
                )
        );

        //layout 2: list view (weight = 6)
        listView = new ListView(this);
        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.WHITE);          // nền
        border.setStroke(2, Color.BLACK);
        listView.setBackground(border);
        listView.setPadding(8,8,8,8);
        root.addView(listView,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        5.5f
                )
        );

        //layout3
        root.addView(buildActionUI(),
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        1.5f
                ));

        setContentView(root);

        data.add(new ContactItem("VyLoTe", "3849124", false, null));
        imgPV.setImageResource(android.R.drawable.ic_menu_gallery);

        adapter = new ContactAdapter(this, data);
        listView.setAdapter(adapter);
        setUpActionEvents();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_PICK_IMAGE
                && resultCode == RESULT_OK
                && data != null) {

            Uri uri = data.getData();
            selectedImagePath = uri.toString();

            imgPV.setImageURI(uri);   // preview ngay
        }
    }


    private View buildFormUI() {
        // layout l1 (VERTICAL)
        LinearLayout l1 = new LinearLayout(this);
        l1.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams l1Params =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
        l1Params.setMargins(20, 20, 20, 20);
        l1.setLayoutParams(l1Params);

        // params chung cho EditText
        LinearLayout.LayoutParams edParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        // EditText họ tên
        edHt = new EditText(this);
        edHt.setHint("Ho ten");
        edHt.getBackground().mutate()
                .setTint(Color.parseColor("#FF04B4"));
        l1.addView(edHt, edParams);

        // EditText SĐT
        edPhone = new EditText(this);
        edPhone.setHint("SDT");
        edPhone.getBackground().mutate()
                .setTint(Color.parseColor("#FF04B4"));
        l1.addView(edPhone, edParams);

        // layout chọn ảnh (HORIZONTAL)
        LinearLayout imgPart = new LinearLayout(this);
        imgPart.setOrientation(LinearLayout.HORIZONTAL);
        imgPart.setGravity(Gravity.CENTER);
        GradientDrawable d = new GradientDrawable();
        d.setStroke(2, Color.BLACK);
        imgPart.setBackground(d);

        int imgRowHeight = (int)(96 * getResources().getDisplayMetrics().density);

        LinearLayout.LayoutParams imgPartParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        imgRowHeight
                );
        l1.addView(imgPart, imgPartParams);

        LinearLayout.LayoutParams imgParams =
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        7
                );

        LinearLayout.LayoutParams btnParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
        btnParams.gravity = Gravity.CENTER;

        imgSel = new Button(this);
        imgSel.setText("Choose");


        imgPV = new ImageView(this);
        imgPV.setBackgroundColor(Color.LTGRAY);
        imgPV.setScaleType(ImageView.ScaleType.CENTER_INSIDE); // KHÔNG bị cắt
        imgPV.setAdjustViewBounds(true);

        imgPart.addView(imgSel, btnParams);
        imgPart.addView(imgPV, imgParams);

        return l1;
    }

    private View buildActionUI() {
        LinearLayout action = new LinearLayout(this);
        action.setOrientation(LinearLayout.HORIZONTAL);
        action.setGravity(Gravity.CENTER);
        action.setPadding(20,20,20,20);

        LinearLayout.LayoutParams btnParams =
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1
                );

        btnParams.setMargins(20,0,20,0);

        btnAdd = new Button(this);
        btnAdd.setText("THEM");
        btnDelete = new Button(this);
        btnDelete.setText("XOA");

        action.addView(btnAdd, btnParams);
        action.addView(btnDelete, btnParams);
        return action;
    }

    private void setUpActionEvents() {
        imgSel.setOnClickListener( v -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, REQ_PICK_IMAGE);
        });

        btnAdd.setOnClickListener(v ->{
            String name = edHt.getText().toString();
            String phone = edPhone.getText().toString();

            if (name.isEmpty()) {
                Toast.makeText(this, "Ten khong dc de trong", Toast.LENGTH_SHORT).show();
                return;
            }
            if (phone.isEmpty()) {
                Toast.makeText(this, "SDT khong dc de trong", Toast.LENGTH_SHORT).show();
                return;
            }

            data.add(new ContactItem(name, phone, false, selectedImagePath));
            adapter.notifyDataSetChanged();
            selectedImagePath = null;
            imgPV.setImageResource(android.R.drawable.ic_menu_gallery);

            edHt.setText("");
            edPhone.setText("");
        });

        btnDelete.setOnClickListener( v ->{
            boolean hasChecked = false;

            for (int i = data.size()-1; i>= 0; --i) {
                if (data.get(i).isChecked) {
                    data.remove(i);
                    hasChecked = true;
                }
            }

            if (!hasChecked) {
                Toast.makeText(this, "Chua chon dong nao xoa", Toast.LENGTH_SHORT).show();
                return;
            }

            adapter.notifyDataSetChanged();
        });
    }
}