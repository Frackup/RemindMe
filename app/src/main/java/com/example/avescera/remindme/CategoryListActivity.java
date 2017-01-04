package com.example.avescera.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.avescera.remindme.Adapters.CategoryAdapter;
import com.example.avescera.remindme.Classes.Category;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;

import java.util.List;

public class CategoryListActivity extends AppCompatActivity {

    private InitDataBaseHandlers dbHandlers;
    private ListView categoriesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHandlers = new InitDataBaseHandlers(this);

        attachViewItems();
        populateListView();
    }

    private void attachViewItems(){
        categoriesListView = (ListView) findViewById(R.id.listViewCategories);
    }

    private void populateListView(){
        List<Category> categoriesList = dbHandlers.getDbCategoryHandler().getAllCategories();
        //Remove the two first items "add a category" and "select a category" to not display them within the list of existing categories.
        int noCategoryItem = 1;
        categoriesList.remove(noCategoryItem);
        int addCategoryItem = 0;
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
        } else if (id == R.id.category_home) {
            goToHomePage();
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToCategoryCreation() {
        Intent intent = new Intent(this, CategoryCreationActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        dbHandlers = new InitDataBaseHandlers(this);
        populateListView();
    }

    private void goToHomePage(){
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }
}
