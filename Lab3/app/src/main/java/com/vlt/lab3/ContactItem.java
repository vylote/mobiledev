package com.vlt.lab3;

import java.io.Serializable;

public class ContactItem implements Serializable {
    private String name;
    private String phone;
    private boolean status;
    private String imagePath;

    public ContactItem(String name, String phone, boolean status, String imagePath) {
        this.name = name;
        this.phone = phone;
        this.status = status;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
