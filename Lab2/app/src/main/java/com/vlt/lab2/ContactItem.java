package com.vlt.lab2;

import java.io.Serializable;

class ContactItem implements Serializable {
    String name;
    String phone;
    boolean isChecked;
    public String imagePath;

    public ContactItem(String name, String phone, boolean isChecked, String imagePath) {
        this.name = name;
        this.phone = phone;
        this.isChecked = isChecked;
        this.imagePath = imagePath;
    }
}
