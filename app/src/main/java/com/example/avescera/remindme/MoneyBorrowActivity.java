package com.example.avescera.remindme;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.avescera.remindme.Adapters.MoneyAdapter;
import com.example.avescera.remindme.Classes.Money;
import com.example.avescera.remindme.DBHandlers.DatabaseMoneyHandler;
import com.example.avescera.remindme.Interfaces.ActivityClass;

import java.sql.SQLException;
import java.util.List;

public class MoneyBorrowActivity extends AppCompatActivity {

    private ListView listViewMoneyBorrowed;
    private ImageView imgDetails;
    private TextView txtviewDetail;
    private DatabaseMoneyHandler dbMoneyHandler;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_borrow);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attachViewItems();
        initVariables();
        initDbHandlers();
        populateListView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreateBorrowedMoney);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMoneyCreationPage(view);
            }
        });
    }

    private void initVariables() {
        dialog = new Dialog(this);
    }

    private void initDbHandlers() {
        //Initiate the DBHandler
        dbMoneyHandler = new DatabaseMoneyHandler(this);
        try {
            dbMoneyHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void attachViewItems(){
        listViewMoneyBorrowed = (ListView) findViewById(R.id.listViewMoneyBorrowedItems);
        imgDetails = (ImageView) findViewById(R.id.imgViewMoneyDetail);
    }

    private void populateListView(){
        List<Money> moneysList = dbMoneyHandler.getTypeMoneys(ActivityClass.DATABASE_BORROW_TYPE);
        MoneyAdapter adapter = new MoneyAdapter(MoneyBorrowActivity.this, R.layout.money_list_item, moneysList);
        listViewMoneyBorrowed.setAdapter(adapter);
    }

    public void goToMoneyCreationPage(View view) {
        Intent intent = new Intent(this, MoneyCreationActivity.class);
        intent.putExtra(ActivityClass.CALLING_ACTIVITY, ActivityClass.ACTIVITY_BORROW);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initDbHandlers();
        populateListView();
    }

    public void displayDetailDialog(){
        //Custom dialog
        dialog.setContentView(R.layout.detail_dialog);
        dialog.setTitle(getResources().getString(R.string.txtview_detail_display).toString());
        FloatingActionButton fabDetail = (FloatingActionButton) dialog.findViewById(R.id.fabDetailOK);

        //set the custom dialog component
        txtviewDetail = (TextView) dialog.findViewById(R.id.txtViewDetailDisplay);

        fabDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
