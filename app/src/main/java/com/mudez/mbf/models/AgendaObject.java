package com.mudez.mbf.models;

import java.io.Serializable;
import java.util.List;

public class AgendaObject implements Serializable {
   public long date;
   public String id;
   public List<AgendaItemObject> items;

    public AgendaObject(long date, List<AgendaItemObject> items,String id) {
        this.date = date;
        this.items = items;
        this.id = id;
    }

    public AgendaObject(){

    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<AgendaItemObject> getItems() {
        return items;
    }

    public void setItems(List<AgendaItemObject> items) {
        this.items = items;
    }
}
