package com.vlt.lab4.models;

import java.io.Serializable;

public class ThiSinh implements Serializable {
    private String sbd, HoTen;
    private float Toan, Ly, Hoa;


    public ThiSinh(String sbd, String hoTen, float toan, float ly, float hoa) {
        this.Hoa = hoa;
        this.HoTen = hoTen;
        this.Ly = ly;
        this.sbd = sbd;
        this.Toan = toan;
    }

    public float getHoa() {
        return Hoa;
    }

    public String getHoTen() {
        return HoTen;
    }

    public float getLy() {
        return Ly;
    }

    public String getSbd() {
        return sbd;
    }
    public float getToan() {
        return Toan;
    }

    public float tongDiem() { return getToan()+getHoa()+getLy(); }

    public float diemTB() { return tongDiem()/3.0f; }
}
