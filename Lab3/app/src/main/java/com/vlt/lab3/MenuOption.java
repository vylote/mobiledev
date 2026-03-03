package com.vlt.lab3;

import androidx.annotation.NonNull;

public class MenuOption {
    String title;
    int iconRes;

    public MenuOption(String title, int iconRes) {
        this.title = title;
        this.iconRes = iconRes;
    }

    @NonNull
    @Override
    public String toString() {
        return title; // Trả về tiêu đề để hiển thị lên Dialog
    }
}
