package com.example.recipeapp;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<Modal> arrayList;
    private Context context;
    private RecipeDatabase recipeDatabase;
    private boolean isFavoritesFragment;

    public Adapter(ArrayList<Modal> arrayList, Context context, boolean isFavoritesFragment) {
        this.arrayList = arrayList;
        this.context = context;
        this.isFavoritesFragment = isFavoritesFragment;
        recipeDatabase = RecipeDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Modal modal = arrayList.get(position);
        holder.title.setText(modal.getTitle());

        // Use Picasso to load the image
        Picasso.get().load(modal.getImageUrl()).into(holder.imageView);

        // Display icons based on the fragment type
        if (isFavoritesFragment) {
            holder.favoriteIcon.setVisibility(View.GONE);
            holder.deleteIcon.setVisibility(View.VISIBLE);
        } else {
            holder.favoriteIcon.setVisibility(View.VISIBLE);
            holder.deleteIcon.setVisibility(View.GONE);
        }

        // Handle favorite icon click
        holder.favoriteIcon.setOnClickListener(v -> {
            modal.setFavorite(true);
            addRecipeToFavorites(modal);
        });

        // Handle delete icon click
        holder.deleteIcon.setOnClickListener(v -> {
            removeRecipeFromFavorites(modal);
            arrayList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, arrayList.size());
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView, favoriteIcon, deleteIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recipe_title);
            imageView = itemView.findViewById(R.id.recipe_image);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
            deleteIcon = itemView.findViewById(R.id.delete_icon);
        }
    }

    private void addRecipeToFavorites(Modal modal) {
        FavoriteRecipe favoriteRecipe = new FavoriteRecipe(modal.getTitle(), modal.getImageUrl());
        AsyncTask.execute(() -> recipeDatabase.favoriteRecipeDao().insert(favoriteRecipe));
    }

    private void removeRecipeFromFavorites(Modal modal) {
        AsyncTask.execute(() -> {
            FavoriteRecipe recipe = recipeDatabase.favoriteRecipeDao().findRecipeByTitle(modal.getTitle());
            if (recipe != null) {
                recipeDatabase.favoriteRecipeDao().delete(recipe);
            }
        });
    }
}
