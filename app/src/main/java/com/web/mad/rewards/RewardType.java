package com.web.mad.rewards;

public enum RewardType {
    REPEATABLE("Repeatable"),
    LIMITED("Limited"),
    ONETIME("One-time");

    private String text;

    RewardType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
