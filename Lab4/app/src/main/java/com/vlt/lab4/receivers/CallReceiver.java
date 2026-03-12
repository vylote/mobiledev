package com.vlt.lab4.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class CallReceiver extends BroadcastReceiver {
    private static final List<String> BLACKLIST = Arrays.asList(
            "0123456789",
            "8888888888",
            "0000000000",
            "464364453"
    );

    @Override
    public void onReceive(Context context, Intent intent) {
        // Kiểm tra hành động thay đổi trạng thái điện thoại
        if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(intent.getAction())) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            // Nếu có cuộc gọi đang đến (RINGING)
            if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
                // Lấy số điện thoại gọi đến
                String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

                if (incomingNumber != null) {
                    // Kiểm tra xem số gọi đến có nằm trong danh sách chặn không
                    boolean isBlocked = false;
                    for (String blackNumber : BLACKLIST) {
                        if (incomingNumber.contains(blackNumber)) {
                            isBlocked = true;
                            break;
                        }
                    }

                    if (isBlocked) {
                        // Hiển thị cảnh báo chặn số trong danh sách
                        Toast.makeText(context, "Lab4: Đang chặn số trong danh sách đen: " + incomingNumber, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}
