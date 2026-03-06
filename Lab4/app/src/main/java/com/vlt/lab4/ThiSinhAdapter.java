package com.vlt.lab4;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vlt.lab4.models.ThiSinh;

import java.util.ArrayList;

public class ThiSinhAdapter extends ArrayAdapter<ThiSinh> {
    private Activity context;
    private ArrayList<ThiSinh> data;

    public ThiSinhAdapter(Activity context, ArrayList<ThiSinh> data) {
        super(context, R.layout.candidate, data);
        this.context = context;
        this.data = data;
    }

    static class ViewHolder {
        TextView sbd;
        TextView ht;
        TextView td;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.candidate, parent, false);

            holder = new ViewHolder();
            holder.sbd = convertView.findViewById(R.id.tSBD);
            holder.ht = convertView.findViewById(R.id.tName);
            holder.td = convertView.findViewById(R.id.tTotalScore);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ThiSinh ts = data.get(position);

        holder.sbd.setText(ts.getSbd());
        holder.ht.setText(ts.getHoTen());
        holder.td.setText(String.format("%.2f", ts.tongDiem()));

        return convertView;
    }
}
