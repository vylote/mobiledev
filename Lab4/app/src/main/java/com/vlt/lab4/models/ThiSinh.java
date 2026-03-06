package com.vlt.lab4.models;

import java.io.Serializable;

public class ThiSinh implements Serializable {
    private String sbd;
    private String HoTen;
    private float Toan, Ly, Hoa;


    public ThiSinh(String sbd, String hoTen, float toan, float ly, float hoa) {
        Hoa = hoa;
        HoTen = hoTen;
        Ly = ly;
        this.sbd = sbd;
        Toan = toan;
    }

    public float getHoa() {
        return Hoa;
    }

    public void setHoa(float hoa) {
        Hoa = hoa;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public float getLy() {
        return Ly;
    }

    public void setLy(float ly) {
        Ly = ly;
    }

    public String getSbd() {
        return sbd;
    }

    public void setSbd(String sbd) {
        this.sbd = sbd;
    }

    public float getToan() {
        return Toan;
    }

    public void setToan(float toan) {
        Toan = toan;
    }

    public float tongDiem() { return getToan()+getHoa()+getLy(); }

    public float diemTB() { return tongDiem()/3.0f; }
}
