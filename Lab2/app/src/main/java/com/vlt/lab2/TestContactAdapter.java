package com.vlt.lab2;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TestContactAdapter extends ArrayAdapter<ContactItem> {

    // Cần Context dạng Activity để có thể lấy LayoutInflater
    private Activity context;
    private ArrayList<ContactItem> data;

    public TestContactAdapter(Activity context, ArrayList<ContactItem> data) {
        super(context, R.layout.item_contact, data);
        this.context = context;
        this.data = data;
    }

    // Class ViewHolder: Giúp giữ các View đã tìm thấy (findViewId)
    // Để khi cuộn danh sách không phải tìm lại từ đầu -> App chạy nhanh hơn
    static class ViewHolder {
        CheckBox cb;
        ImageView img;
        TextView name;
        TextView phone;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // BƯỚC 1: KHỞI TẠO VIEW (Nếu chưa có)
        if (convertView == null) {
            // "Thổi" file XML thành View thực tế
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_contact, parent, false);

            // Tìm và lưu các thành phần vào Holder
            holder = new ViewHolder();
            holder.cb = convertView.findViewById(R.id.cbContact);
            holder.img = convertView.findViewById(R.id.imgAvatar);
            holder.name = convertView.findViewById(R.id.tvName);     // ID bên XML
            holder.phone = convertView.findViewById(R.id.tvPhone);   // ID bên XML

            // Gắn cái mác (Tag) vào View để lần sau lấy ra dùng tiếp
            convertView.setTag(holder);
        } else {
            // Nếu View đã có rồi (do tái sử dụng khi cuộn), lấy lại Holder cũ
            holder = (ViewHolder) convertView.getTag();
        }

        // BƯỚC 2: LẤY DỮ LIỆU
        ContactItem item = data.get(position);

        // BƯỚC 3: ĐỔ DỮ LIỆU VÀO VIEW (BINDING)
        holder.name.setText(item.getName());
        holder.phone.setText(item.getPhone());

        // --- Xử lý Checkbox ---
        // Quan trọng: Phải gỡ listener cũ ra trước khi setChecked
        // Nếu không, khi setChecked(true) code sẽ tưởng người dùng bấm và kích hoạt logic sai
        holder.cb.setOnCheckedChangeListener(null);

        holder.cb.setChecked(item.isStatus());

        // Gắn sự kiện mới: Cập nhật status vào object gốc khi người dùng bấm
        holder.cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setStatus( isChecked);
        });

        // --- Xử lý Ảnh ---
        if (item.getImagePath() != null) {
            // Nếu có đường dẫn ảnh thì load ảnh
            holder.img.setImageURI(Uri.parse(item.getImagePath()));
        } else {
            // Nếu null thì để ảnh mặc định
            holder.img.setImageResource(android.R.drawable.ic_menu_camera);
        }

        return convertView;
    }
}