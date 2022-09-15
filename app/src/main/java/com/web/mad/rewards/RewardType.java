package com.web.mad.rewards;

public enum RewardType {
    REPEATABLE("Repeatable"),
    ONETIME("One-time");

    private String text;

    RewardType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
