package com.space.models;

import java.util.Date;

import io.swagger.v3.core.util.Json;
import io.vertx.core.json.JsonObject;

public class Category {
    private String id;
    private String name;
    private String note;
    private String des;
    private Date date;
    private Date dateUpdate;
    private String sub;

    public Category() {
    }

    public Category(String id, String name, String note, String des, Date date, Date dateUpdate, String sub) {
        this.id = id;
        this.name = name;
        this.note = note;
        this.des = des;
        this.date = date;
        this.dateUpdate = dateUpdate;
        this.sub = sub;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((note == null) ? 0 : note.hashCode());
        result = prime * result + ((des == null) ? 0 : des.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((dateUpdate == null) ? 0 : dateUpdate.hashCode());
        result = prime * result + ((sub == null) ? 0 : sub.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Category other = (Category) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (note == null) {
            if (other.note != null)
                return false;
        } else if (!note.equals(other.note))
            return false;
        if (des == null) {
            if (other.des != null)
                return false;
        } else if (!des.equals(other.des))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (dateUpdate == null) {
            if (other.dateUpdate != null)
                return false;
        } else if (!dateUpdate.equals(other.dateUpdate))
            return false;
        if (sub == null) {
            if (other.sub != null)
                return false;
        } else if (!sub.equals(other.sub))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Category [id=" + id + ", name=" + name + ", note=" + note + ", des=" + des + ", date=" + date
                + ", dateUpdate=" + dateUpdate + ", sub=" + sub + "]";
    }

    public JsonObject toJsonObject() {
        return new JsonObject()
                .put("cate_id", id)
                .put("cate_name", name)
                .put("cate_note", note)
                .put("cate_date", date)
                .put("cate_date_update", dateUpdate)
                .put("cate_sub", sub);
    }
}
