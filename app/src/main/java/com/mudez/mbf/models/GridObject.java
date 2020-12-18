package com.mudez.mbf.models;

public class GridObject {

    public GridObject(String title, boolean isTapped, Integer id) {
        this.title = title;
        this.isTapped = isTapped;
        this.id = id;
    }

    private String title;
    private boolean isTapped;
    private Integer id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isTapped() {
        return isTapped;
    }

    public void setTapped(boolean tapped) {
        isTapped = tapped;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
