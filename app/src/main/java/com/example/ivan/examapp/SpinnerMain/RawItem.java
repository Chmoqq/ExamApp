package com.example.ivan.examapp.SpinnerMain;

public class RawItem {
    private int imageid;
    private String title;

    public RawItem(String title, int imageid) {
        this.title = title;
        this.imageid = imageid;
    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
