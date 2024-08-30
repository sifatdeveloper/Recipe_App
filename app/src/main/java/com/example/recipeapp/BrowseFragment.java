package com.example.recipeapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BrowseFragment extends Fragment {
    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<Modal> recipeList = new ArrayList<>();
    private ArrayList<Modal> filteredList = new ArrayList<>();
    private SearchView searchView;
    private Spinner filterSpinner;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);

        searchView = view.findViewById(R.id.search_view);
        filterSpinner = view.findViewById(R.id.filter_spinner);
        recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter(filteredList, getContext(), false); // false for HomeFragment-like behavior
        recyclerView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(getContext());

        // Load the initial data
        loadData();

        // Set up search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterRecipes(query, filterSpinner.getSelectedItem().toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterRecipes(newText, filterSpinner.getSelectedItem().toString());
                return false;
            }
        });

        // Set up filter functionality
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterRecipes(searchView.getQuery().toString(), filterSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        return view;
    }

    private void loadData() {
        String url = "https://api.spoonacular.com/recipes/random?number=10&apiKey=289aef798bc9426b8a49e787ef245fc3";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray recipes = response.getJSONArray("recipes");
                            recipeList.clear();
                            for (int i = 0; i < recipes.length(); i++) {
                                JSONObject recipe = recipes.getJSONObject(i);
                                String title = recipe.getString("title");
                                String imageUrl = recipe.getString("image");
                                String category = recipe.optString("category", "Uncategorized"); // Replace with actual category if available

                                recipeList.add(new Modal(title, imageUrl, category));
                            }
                            filteredList.clear();
                            filteredList.addAll(recipeList);
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void filterRecipes(String query, String filter) {
        filteredList.clear();

        for (Modal recipe : recipeList) {
            boolean matchesQuery = TextUtils.isEmpty(query) || recipe.getTitle().toLowerCase().contains(query.toLowerCase());
            boolean matchesFilter = filter.equals("All") || recipe.getCategory().equalsIgnoreCase(filter);

            if (matchesQuery && matchesFilter) {
                filteredList.add(recipe);
            }
        }

        adapter.notifyDataSetChanged();
    }
}
