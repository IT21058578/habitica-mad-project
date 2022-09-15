package com.web.mad.rewards;

import java.time.LocalDateTime;

public class Reward {
    private String documentId;
    private String userId;
    private String name;
    private String description;
    private int price;
    private int bought;
    private int used;
    private RewardType type;
    private LocalDateTime createdAt;
    private LocalDateTime lastBoughtAt;
    private LocalDateTime lastUsedAt;
    private LocalDateTime lastEditedAt;

    public Reward() {
    }

    public Reward(String userId, String name, String description, int price, int bought, int used, RewardType type, LocalDateTime createdAt, LocalDateTime lastBoughtAt, LocalDateTime lastUsedAt, LocalDateTime lastEditedAt) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.bought = bought;
        this.used = used;
        this.type = type;
        this.createdAt = createdAt;
        this.lastBoughtAt = lastBoughtAt;
        this.lastUsedAt = lastUsedAt;
        this.lastEditedAt = lastEditedAt;
    }

    public Reward(String documentId, String userId, String name, String description, int price, int bought, int used, RewardType type, LocalDateTime createdAt, LocalDateTime lastBoughtAt, LocalDateTime lastUsedAt, LocalDateTime lastEditedAt) {
        this.documentId = documentId;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.bought = bought;
        this.used = used;
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

    public int getBought() {
        return bought;
    }

    public void setBought(int bought) {
        this.bought = bought;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public RewardType getType() {
        return type;
    }

    public void setType(RewardType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastBoughtAt() {
        return lastBoughtAt;
    }

    public void setLastBoughtAt(LocalDateTime lastBoughtAt) {
        this.lastBoughtAt = lastBoughtAt;
    }

    public LocalDateTime getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(LocalDateTime lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

    public LocalDateTime getLastEditedAt() {
        return lastEditedAt;
    }

    public void setLastEditedAt(LocalDateTime lastEditedAt) {
        this.lastEditedAt = lastEditedAt;
    }
}
