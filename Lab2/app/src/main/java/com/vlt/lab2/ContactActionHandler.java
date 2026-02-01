package com.vlt.lab2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContactActionHandler {
    Activity activity;

    public ContactActionHandler(Activity activity) {
        this.activity = activity;
    }

    public void handle(MenuItem item, ContactItem contact, int realIndex) {
        int id = item.getItemId();
        
        if (id == R.id.menu_edit) {
            Intent i = new Intent(activity, TestEditContactActivity.class);
            i.putExtra("DATA_TO_EDIT", contact);
            i.putExtra("REAL_INDEX", realIndex);

            activity.startActivityForResult(i, TestMainActivity.REQ_EDIT_CONTACT);

        } else if (id == R.id.menu_call) {
            Intent i = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + contact.getPhone()));
            activity.startActivity(i);

        } else if (id == R.id.menu_send) {
            Intent i = new Intent(Intent.ACTION_SENDTO,
                    Uri.parse("smsto:" + contact.getPhone()));
            activity.startActivity(i);
        } else if (id == R.id.menu_share) {
            String content = "Thông tin liên hệ:\n" +
                    "Tên: "+ contact.getName()+
                    " SDT: "+contact.getPhone();

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Chia sẻ liên hệ");
            i.putExtra(Intent.EXTRA_TEXT, content);

            activity.startActivity(Intent.createChooser(i, "Chia sẻ qua:"));
        } else if (id == R.id.menu_shot) {
            final MenuOption[] items= {
                new MenuOption("Mở máy ảnh", android.R.drawable.ic_menu_camera),
                new MenuOption("Chọn từ thư viện", android.R.drawable.ic_menu_gallery)
            };

            ArrayAdapter<MenuOption> adapter = new ArrayAdapter<>(
                    activity,
                    android.R.layout.select_dialog_item, // Layout mặc định của Android cho dialog có icon
                    android.R.id.text1,
                    items) {
                @NonNull
                @Override
                public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    TextView tv = v.findViewById(android.R.id.text1);

                    // Đặt icon sang bên trái của văn bản
                    tv.setCompoundDrawablesWithIntrinsicBounds(items[position].iconRes, 0, 0, 0);

                    // Thêm khoảng cách giữa icon và chữ
                    int dp5 = (int) (5 * activity.getResources().getDisplayMetrics().density);
                    tv.setCompoundDrawablePadding(dp5);

                    return v;
                }
            };

            // 3. Hiển thị AlertDialog
            new android.app.AlertDialog.Builder(activity)
                    .setTitle("Chọn nguồn ảnh")
                    .setAdapter(adapter, (dialog, which) -> {
                        if (which == 0) {
                            // Mở Camera
                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            activity.startActivityForResult(intent, TestMainActivity.REQ_OPEN_CAMERA);
                        } else {
                            // Mở Bộ sưu tập (Gallery/DCIM)
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            activity.startActivityForResult(intent, TestMainActivity.REQ_OPEN_GALLERY);
                        }
                    })
                    .show();
        }
    }
}

