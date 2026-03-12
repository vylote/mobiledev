package com.vlt.lab4.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info == null || !info.isConnected()) {
            Toast.makeText(context, "Mất kểt nối internet", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Đã có internet trở lại", Toast.LENGTH_SHORT).show();
        }
    }
}
