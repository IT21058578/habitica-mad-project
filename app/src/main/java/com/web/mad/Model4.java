package com.web.mad;

public class Model4 {
    private String id, date, GoalTitle, GoalDiscription;

    public Model4(String id, String date, String GoalTitle, String GoalDiscription) {

        this.id = id;
        this.date = date;
        this.GoalTitle = GoalTitle;
        this.GoalDiscription = GoalDiscription;
    }
    public Model4() {

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

    public String getGoalTitle() {
        return GoalTitle;
    }

    public void setGoalTitle(String goalTitle) {
        GoalTitle = goalTitle;
    }

    public String getGoalDiscription() {
        return GoalDiscription;
    }

    public void setGoalDiscription(String goalDiscription) {
        GoalDiscription = goalDiscription;
    }


}
