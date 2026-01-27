package com.example.lab1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentAdapter extends ArrayAdapter<Student> {
    private Activity context;
    private ArrayList<Student> list;

    public StudentAdapter(Activity context, ArrayList<Student> list) {
        super(context, R.layout.item_list, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Tạo View dòng (Inflate)
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.item_list, null, true);

        // 2. Ánh xạ View trong dòng đó
        TextView tvInfo = rowView.findViewById(R.id.tvItemInfo);
        ImageView ivAvatar = rowView.findViewById(R.id.ivItemAvatar);

        // 3. Lấy dữ liệu tại vị trí position
        Student s = list.get(position);

        // 4. Đổ dữ liệu vào View
        tvInfo.setText(s.getInfo());

        // Kiểm tra nếu có ảnh thì hiển thị, không thì để icon mặc định
        if (s.getImageUri() != null) {
            ivAvatar.setImageURI(s.getImageUri());
        } else {
            ivAvatar.setImageResource(R.drawable.ic_account);
        }

        return rowView;
    }
}