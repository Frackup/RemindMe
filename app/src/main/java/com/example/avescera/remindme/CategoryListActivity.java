package com.example.avescera.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.avescera.remindme.Adapters.CategoryAdapter;
import com.example.avescera.remindme.Classes.Category;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;

import java.util.List;

public class CategoryListActivity extends AppCompatActivity {

    private List<Category> categoriesList;
    private InitDataBaseHandlers dbHandlers;
    private ListView categoriesListView;
    private int addCategoryItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHandlers = new InitDataBaseHandlers(this);

        attachViewItems();
        initVariables();
        populateListView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCategoryCreation);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCategoryCreation();
            }
        });
    }

    private void attachViewItems(){
        categoriesListView = (ListView) findViewById(R.id.listViewCategories);
    }

    private void initVariables(){
        categoriesList = dbHandlers.getDbCategoryHandler().getAllCategories();
    }

    private void populateListView(){
        //Remove the first item "add a category", to not display it within the list of existing categories.
        categoriesList.remove(addCategoryItem);
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, categoriesList);
        categoriesListView.setAdapter(categoryAdapter);
    }

    public void goToCategoryCreation() {
        Intent intent = new Intent(this, CategoryCreationActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        dbHandlers = new InitDataBaseHandlers(this);

        categoriesList = dbHandlers.getDbCategoryHandler().getAllCategories();
        //Remove the first item "add a category", to not display it within the list of existing categories.
        categoriesList.remove(addCategoryItem);
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, categoriesList);
        categoriesListView.setAdapter(categoryAdapter);
    }
}
