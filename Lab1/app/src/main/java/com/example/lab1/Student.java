package com.example.lab1;

import android.net.Uri;

public class Student {
    private String info;
    private Uri imageUri;

    public Student(String info, Uri imageUri) {
        this.info = info;
        this.imageUri = imageUri;
    }

    public String getInfo() { return info; }
    public Uri getImageUri() { return imageUri; }
}