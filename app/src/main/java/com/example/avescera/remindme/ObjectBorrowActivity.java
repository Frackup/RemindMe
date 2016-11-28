package com.example.avescera.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.avescera.remindme.Adapters.ObjectAdapter;
import com.example.avescera.remindme.Classes.Object;
import com.example.avescera.remindme.DBHandlers.DatabaseObjectHandler;

import java.sql.SQLException;
import java.util.List;

public class ObjectBorrowActivity extends AppCompatActivity {

    private DatabaseObjectHandler dbObjectHandler;
    private ListView listViewObjectBorrowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_borrow);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initiate the DBHandler
        dbObjectHandler = new DatabaseObjectHandler(this);
        try {
            dbObjectHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        listViewObjectBorrowed = (ListView) findViewById(R.id.listViewObjectBorrowedItems);

        List<Object> listObjectsItems = dbObjectHandler.getTypeObjects(2);

        ObjectAdapter adapter = new ObjectAdapter(this, listObjectsItems);
        listViewObjectBorrowed.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreateBorrowedObject);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToObjectCreationPage(view);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void goToObjectCreationPage(View view) {
        Intent intent = new Intent(this, ObjectCreationActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Initiate the DBHandler
        dbObjectHandler = new DatabaseObjectHandler(this);
        try {
            dbObjectHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Object> listObjectItems = dbObjectHandler.getTypeObjects(2);

        ObjectAdapter adapter = new ObjectAdapter(this, listObjectItems);
        listViewObjectBorrowed.setAdapter(adapter);
    }

}
