package com.vlt.lab2;

import android.app.Activity;
import android.app.AlertDialog;
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

public class ContactActionHandler {
    private final Activity activity;

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

            ImageSourceAdapter adapter = new ImageSourceAdapter(activity, items);

            // 3. Hiển thị AlertDialog
            new AlertDialog.Builder(activity)
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

