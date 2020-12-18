package com.mudez.mbf.models;

import java.io.Serializable;

public class AgendaItemObject implements Serializable {
   public String title;
   String id;
   public long createdAt;

    public AgendaItemObject(String title, String subTitle, String to, String from,String id,long createdAt) {
        this.title = title;
        this.subTitle = subTitle;
        this.to = to;
        this.from = from;
        this.createdAt = createdAt;
        this.id =id;
    }
public AgendaItemObject(){

}
    public  String subTitle;
    public String to;
    public String from;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
