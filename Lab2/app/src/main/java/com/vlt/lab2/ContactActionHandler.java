package com.vlt.lab2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;

public class ContactActionHandler {

    private final Activity activity;

    public ContactActionHandler(Activity activity) {
        this.activity = activity;
    }

    public void handle(MenuItem item, ContactItem contact, int realIndex) {

        if (item.getItemId() == R.id.menu_edit) {
            Intent i = new Intent(activity, TestEditContactActivity.class);
            i.putExtra("DATA_TO_EDIT", contact);
            i.putExtra("REAL_INDEX", realIndex);

            activity.startActivityForResult(i, TestMainActivity.REQ_EDIT_CONTACT);

        } else if (item.getItemId() == R.id.menu_call) {
            Intent i = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + contact.getPhone()));
            activity.startActivity(i);

        } else if (item.getItemId() == R.id.menu_send) {
            Intent i = new Intent(Intent.ACTION_SENDTO,
                    Uri.parse("smsto:" + contact.getPhone()));
            activity.startActivity(i);
        }
    }
}

