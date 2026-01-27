package com.vlt.lab2;

import java.io.Serializable;

class ContactItem implements Serializable {
    String name;
    String phone;
    boolean status;
    public String imagePath;

    public ContactItem(String name, String phone, boolean status, String imagePath) {
        this.name = name;
        this.phone = phone;
        this.status = status;
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return name + " - " + phone;
    }
}
