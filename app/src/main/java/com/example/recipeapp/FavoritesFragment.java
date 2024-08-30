package com.example.recipeapp;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    private RecipeDatabase recipeDatabase;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<Modal> favoriteRecipes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recipeDatabase = RecipeDatabase.getInstance(getContext());
        recyclerView = view.findViewById(R.id.favorites_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new Adapter(favoriteRecipes, getContext(), true);
        recyclerView.setAdapter(adapter);

        loadFavoriteRecipes();

        return view;
    }

    private void loadFavoriteRecipes() {
        AsyncTask.execute(() -> {
            List<FavoriteRecipe> favorites = recipeDatabase.favoriteRecipeDao().getAllFavorites();
            for (FavoriteRecipe recipe : favorites) {
                favoriteRecipes.add(new Modal(recipe.getTitle(), recipe.getImageUrl()));
            }
            getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
        });
    }
}
