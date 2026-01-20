package com.vlt.lab2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

class ContactAdapter extends ArrayAdapter<ContactItem> {

    public ContactAdapter(Context context, ArrayList<ContactItem> data) {
        super(context, 0, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ContactRowView row;

        if (convertView == null) {
            row = new ContactRowView(getContext());
        } else {
            row = (ContactRowView) convertView;
        }

        ContactItem item = getItem(position);
        row.bind(item);

        row.cb.setOnCheckedChangeListener(null);
        row.cb.setChecked(item.isChecked);

        row.cb.setOnCheckedChangeListener((btn, checked) -> {
            item.isChecked = checked;
        });

        return row;
    }
}
