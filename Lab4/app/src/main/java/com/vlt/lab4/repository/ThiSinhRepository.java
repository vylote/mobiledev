package com.vlt.lab4.repository;

import android.content.Context;

import com.vlt.lab4.models.ThiSinh;

import java.util.ArrayList;

public class ThiSinhRepository {
    private final ArrayList<ThiSinh> data = new ArrayList<>();
    private final ArrayList<ThiSinh> copy = new ArrayList<>();

    public ThiSinhRepository(Context context) {
        data.add(new ThiSinh("SBD001", "Le Thanh Vy", 7.5f,7.5f,7.5f));
        data.add(new ThiSinh("SBD002", "Ngo Minh Tuan", 2.5f,2.5f,2.5f));
        data.add(new ThiSinh("SBD003", "Thac Duy Anh", 1.5f,1.5f,1.5f));
        data.add(new ThiSinh("SBD004", "Vu Quoc Trung", 3.5f,3.5f,3.5f));
        copy.clear();
        copy.addAll(data);
    }

    public ArrayList<ThiSinh> getCopy() {
        return copy;
    }

    public ArrayList<ThiSinh> getData() {
        return data;
    }

    public void add(ThiSinh ts) {
        data.add(ts);
        copy.clear();
        copy.addAll(data);
    }

    public void update(int realIndex, ThiSinh ts) {
        data.set(realIndex, ts);
        copy.clear();
        copy.addAll(data);
    }

    public void delete(int realIndex) {
        data.remove(realIndex);
        copy.clear();
        copy.addAll(data);
    }
}
