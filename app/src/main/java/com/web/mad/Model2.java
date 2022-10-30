package com.web.mad;

public class Model2 {
    private String id, date, incomeTitle, incomeAmount;
    public Model2() {

    }
    public Model2(String id, String date, String incomeAmount, String incomeTitle) {

        this.id = id;
        this.date = date;
        this.incomeTitle = incomeTitle;
        this.incomeAmount = incomeAmount;

    }

    public String getIncomeTitle() {
        return incomeTitle;
    }

    public void setIncomeTitle(String incomeTitle) {
        this.incomeTitle = incomeTitle;
    }

    public String getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(String incomeAmount) {
        this.incomeAmount = incomeAmount;
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
}
