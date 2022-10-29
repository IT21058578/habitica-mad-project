package com.web.mad.user;

import java.security.PublicKey;

public class User {
    int userId;
    int email;
    int currency;

    public User() {
    }

    public User(int userId, int email, int currency) {
        this.userId = userId;
        this.email = email;
        this.currency = currency;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEmail() {
        return email;
    }

    public void setEmail(int email) {
        this.email = email;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }
}
