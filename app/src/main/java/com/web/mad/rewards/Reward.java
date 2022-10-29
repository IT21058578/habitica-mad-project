package com.web.mad.rewards;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Reward {
    private String documentId;
    private String userId;
    private String name;
    private String description;
    private int price;
    private int boughtCount;
    private int usedCount;
    private int available;
    private boolean isUsed; //For one-time rewards.
    private boolean isBought; //For One-time rewards.
    private RewardType type;
    private long createdAt;
    private long lastBoughtAt;
    private long lastUsedAt;
    private long lastEditedAt;

    public void buyOne() {
        this.boughtCount++;
        this.available++;
    }

    public void useOne() {
        this.usedCount++;
        this.available--;
    }

    public Reward() {

    }

    public Reward(String documentId, String userId, String name, String description, int price, int boughtCount, int usedCount, boolean isUsed, boolean isBought, RewardType type, long createdAt, long lastBoughtAt, long lastUsedAt, long lastEditedAt) {
        this.documentId = documentId;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.boughtCount = boughtCount;
        this.usedCount = usedCount;
        this.available = boughtCount - usedCount;
        this.isUsed = isUsed;
        this.isBought = isBought;
        this.type = type;
        this.createdAt = createdAt;
        this.lastBoughtAt = lastBoughtAt;
        this.lastUsedAt = lastUsedAt;
        this.lastEditedAt = lastEditedAt;
    }

    public Reward(String userId, String name, String description, int price, int boughtCount, int usedCount, boolean isUsed, boolean isBought, RewardType type, long createdAt, long lastBoughtAt, long lastUsedAt, long lastEditedAt) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.boughtCount = boughtCount;
        this.usedCount = usedCount;
        this.available = boughtCount - usedCount;
        this.isUsed = isUsed;
        this.isBought = isBought;
        this.type = type;
        this.createdAt = createdAt;
        this.lastBoughtAt = lastBoughtAt;
        this.lastUsedAt = lastUsedAt;
        this.lastEditedAt = lastEditedAt;
    }


    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBoughtCount() {
        return boughtCount;
    }

    public void setBoughtCount(int boughtCount) {
        this.boughtCount = boughtCount;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int used) {
        this.usedCount = used;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean usedCount) {
        isUsed = usedCount;
    }

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }

    public RewardType getType() {
        return type;
    }

    public void setType(RewardType type) {
        this.type = type;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getLastBoughtAt() {
        return lastBoughtAt;
    }

    public void setLastBoughtAt(long lastBoughtAt) {
        this.lastBoughtAt = lastBoughtAt;
    }

    public long getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(long lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

    public long getLastEditedAt() {
        return lastEditedAt;
    }

    public void setLastEditedAt(long lastEditedAt) {
        this.lastEditedAt = lastEditedAt;
    }
}
