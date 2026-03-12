package com.vlt.lab4.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BatteryReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Kiểm tra xem tín hiệu gửi đến có phải là "Pin yếu" hay không
        if (Intent.ACTION_BATTERY_LOW.equals(intent.getAction())) {
            // Hiển thị thông báo Toast cảnh báo người dùng
            Toast.makeText(context, "Pin của bạn đang dưới 15%!", Toast.LENGTH_LONG).show();
        }
    }
}
