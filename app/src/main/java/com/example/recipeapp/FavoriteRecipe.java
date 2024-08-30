package com.example.recipeapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_recipes")
public class FavoriteRecipe {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String imageUrl;

    public FavoriteRecipe(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
}
