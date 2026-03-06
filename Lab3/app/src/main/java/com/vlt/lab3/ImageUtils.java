package com.vlt.lab3;

import android.content.Context;
import android.net.Uri;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageUtils {
    public static String getImagePathFromUri(Context context, Uri uri) {
        try {
            // Mở luồng đọc file từ Gallery
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            // Tạo một file mới trong bộ nhớ riêng của App (app không bao giờ bị mất quyền truy cập file này)
            File file = new File(context.getFilesDir(), "avatar_" + System.currentTimeMillis() + ".jpg");
            OutputStream outputStream = new FileOutputStream(file);

            // Copy dữ liệu
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            // Trả về đường dẫn tuyệt đối của file mới
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}