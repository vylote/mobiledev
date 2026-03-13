package com.vlt.lab4.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import java.util.Arrays;
import java.util.List;

public class CallReceiver extends BroadcastReceiver {
    private static final List<String> BLACKLIST = Arrays.asList(
            "0123456789",
            "8888888888",
            "0000000000"
    );

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(intent.getAction())) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
                String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

                if (incomingNumber != null) {
                    for (String blackNum : BLACKLIST) {
                        // Kiểm tra nếu số gọi đến chứa chuỗi số đen
                        if (incomingNumber.replace(" ", "").contains(blackNum)) {

                            // --- BƯỚC QUAN TRỌNG: NGẮT CUỘC GỌI ---
                            terminateCall(context);

                            Toast.makeText(context, "ĐÃ TỰ ĐỘNG CHẶN: " + incomingNumber, Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                }
            }
        }
    }

    // Hàm thực hiện lệnh ngắt máy
    private void terminateCall(Context context) {
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
        try {
            // Phải có quyền ANSWER_PHONE_CALLS mới gọi được lệnh này
            if (context.checkSelfPermission(android.Manifest.permission.ANSWER_PHONE_CALLS)
                    == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                telecomManager.endCall(); // Lệnh ngắt máy
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}