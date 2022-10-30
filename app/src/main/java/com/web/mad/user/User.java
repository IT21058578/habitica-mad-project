package com.web.mad.user;

import java.security.PublicKey;

public class User {

    int currency;

    public User() {
    }

    public User(int currency) {

        this.currency = currency;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }
}
