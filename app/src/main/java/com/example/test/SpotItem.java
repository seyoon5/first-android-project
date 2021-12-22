package com.example.test;

public class SpotItem {

    private String iv_Rphoto;
    private String tv_Rname;
    private String tv_Rspot;
    private String tv_Rrate;

    public SpotItem(String iv_Rphoto, String tv_Rname, String tv_Rspot, String tv_Rrate) {
        this.iv_Rphoto = iv_Rphoto;
        this.tv_Rname = tv_Rname;
        this.tv_Rspot = tv_Rspot;

        this.tv_Rrate = tv_Rrate;
    }

    public SpotItem(String tv_Rname, String tv_Rspot, String tv_Rrate) {
        this.tv_Rname = tv_Rname;
        this.tv_Rspot = tv_Rspot;
        this.tv_Rrate = tv_Rrate;
    }

    public SpotItem(String iv_Rphoto) {
        this.iv_Rphoto = iv_Rphoto;
    }

    public SpotItem() {
    }


    public String getIv_Rphoto() {
        return iv_Rphoto;
    }

    public void setIv_Rphoto(String iv_Rphoto) {
        this.iv_Rphoto = iv_Rphoto;
    }

    public String getTv_Rname() {
        return tv_Rname;
    }

    public void setTv_Rname(String tv_Rname) {
        this.tv_Rname = tv_Rname;
    }

    public String getTv_Rspot() {
        return tv_Rspot;
    }

    public void setTv_Rspot(String tv_Rspot) {
        this.tv_Rspot = tv_Rspot;
    }

    public String getTv_Rrate() {
        return tv_Rrate;
    }

    public void setTv_Rrate(String tv_Rrate) {
        this.tv_Rrate = tv_Rrate;
    }

}
