package com.example.test;

public class DetailItem {
    String iv_photo;
    int iv_photoInt;
    String et_content;

    public DetailItem() {
    }

    public int getIv_photoInt() {
        return iv_photoInt;
    }

    public void setIv_photoInt(int iv_photoInt) {
        this.iv_photoInt = iv_photoInt;
    }

    public DetailItem(String iv_photo, String et_content) {
        this.iv_photo = iv_photo;
        this.et_content = et_content;
    }

    public String getIv_photo() {
        return iv_photo;
    }

    public void setIv_photo(String iv_photo) {
        this.iv_photo = iv_photo;
    }

    public String getEt_content() {
        return et_content;
    }

    public void setEt_content(String et_content) {
        this.et_content = et_content;
    }
}
