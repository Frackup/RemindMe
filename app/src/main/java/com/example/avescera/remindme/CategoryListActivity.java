package com.example.avescera.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.category_creation) {
            goToCategoryCreation();
        }

        return super.onOptionsItemSelected(item);
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
