package com.example.avescera.remindme;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.avescera.remindme.Classes.InitDataBase;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;

public class CategoryObjectList extends AppCompatActivity {

    private InitDataBaseHandlers dbHandlers;
    private ListView listVObjectCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_object_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        attachViewItems();
        initVariables();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddObjectToCat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initVariables(){
        dbHandlers = new InitDataBaseHandlers(this);
    }

    private void attachViewItems(){
        listVObjectCat = (ListView) findViewById(R.id.listVCatObject);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initVariables();
    }

}
