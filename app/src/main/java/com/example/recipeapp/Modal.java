package com.example.recipeapp;

public class Modal {
    private String title;
    private String imageUrl;
    private String category;
    private boolean isFavorite;
    public Modal(String title, String imageUrl, String category) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
    }


    public Modal(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.isFavorite = false; // Initially not a favorite
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
