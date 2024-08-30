package com.example.recipeapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteRecipeDao {

    @Insert
    void insert(FavoriteRecipe recipe);

    @Delete
    void delete(FavoriteRecipe recipe);

    @Query("SELECT * FROM favorite_recipes WHERE title = :title LIMIT 1")
    FavoriteRecipe findRecipeByTitle(String title);

    @Query("SELECT * FROM favorite_recipes")
    List<FavoriteRecipe> getAllFavorites();
}
