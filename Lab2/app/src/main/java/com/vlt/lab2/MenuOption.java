package com.vlt.lab2;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuOption {
    String title;
    int iconRes;

    @NonNull
    @Override
    public String toString() {
        return title; // Trả về tiêu đề để hiển thị lên Dialog
    }
}
