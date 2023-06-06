package com.oop.model;

public class NhanVatModel extends Model {
    private String description;
    private String thoiKyName;
    private String thoiKyCode;
    private String Code;
    public NhanVatModel(String ten) {
        super(ten);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThoiKyName() {
        return thoiKyName;
    }

    public void setThoiKyName(String thoiKyName) {
        this.thoiKyName = thoiKyName;
    }

    public String getThoiKyCode() {
        return thoiKyCode;
    }

    public void setThoiKyCode(String thoiKyCode) {
        this.thoiKyCode = thoiKyCode;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

//    @Override
//    public String toString() {
//        return "\n{ \"Tên\":\"" + super.getTen() + "\", "
//                + "\n\"\":\"" + this.thoiGian + "\", "
//                + "\n\"Địa điểm\":\"" + this.diaDiem + "\", "
//                + "\n\"kết quả\":\"" + this.ketQua + "\", "
//                + "\n\"Nhân vật liên quan\":\"" + this.nhanVatLienQuan + "\", "
//                + "\n\"Địa điểm liên quan\":\"" + this.diaDiemLienQuan + "\" }" + "\n";
//
//    }
}
