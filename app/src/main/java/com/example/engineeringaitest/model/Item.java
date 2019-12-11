package com.example.engineeringaitest.model;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("created_at")
    private String created_at;
    @SerializedName("title")
    private String title;

    private boolean isSelected = false;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
