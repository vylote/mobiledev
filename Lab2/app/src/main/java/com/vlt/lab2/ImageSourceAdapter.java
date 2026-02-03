package com.vlt.lab2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class ImageSourceAdapter extends ArrayAdapter<MenuOption> {
    private final MenuOption[] items;

    public ImageSourceAdapter(Context context, MenuOption[] items) {
        super(context, android.R.layout.select_dialog_item, android.R.id.text1, items);
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        TextView tv = v.findViewById(android.R.id.text1);
        tv.setCompoundDrawablesWithIntrinsicBounds(items[position].iconRes, 0, 0, 0);
        int dp5 = (int) (5 * getContext().getResources().getDisplayMetrics().density);
        tv.setCompoundDrawablePadding(dp5);
        return v;
    }
}
