package com.web.mad;

public class Model3 {
    private String id, date, expenceTitle, expenceAmount;
    public Model3() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpenceTitle() {
        return expenceTitle;
    }

    public void setExpenceTitle(String expenceTitle) {
        this.expenceTitle = expenceTitle;
    }

    public String getExpenceAmount() {
        return expenceAmount;
    }

    public void setExpenceAmount(String expenceAmount) {
        this.expenceAmount = expenceAmount;
    }

    public Model3(String id, String date, String expenceTitle, String expenceAmount) {

        this.id = id;
        this.date = date;
        this.expenceTitle = expenceTitle;
        this.expenceAmount = expenceAmount;

    }


}

