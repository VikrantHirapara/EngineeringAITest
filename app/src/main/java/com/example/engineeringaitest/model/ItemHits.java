package com.example.engineeringaitest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemHits {
    @SerializedName("hits")
    private List<Item> hits;

    @SerializedName("nbPages")
    private int nbPages;

    @SerializedName("page")
    private int page;

    public List<Item> getHits() {
        return hits;
    }

    public void setHits(List<Item> hits) {
        this.hits = hits;
    }

    public int getNbPages() {
        return nbPages;
    }

    public void setNbPages(int nbPages) {
        this.nbPages = nbPages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
