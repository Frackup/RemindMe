package com.example.avescera.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.avescera.remindme.DBHandlers.DatabaseContactHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseMoneyHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseObjectHandler;
import com.example.avescera.remindme.Interfaces.ActivityClass;

import java.sql.SQLException;
import java.sql.Struct;

//TODO : Finaliser la connexion avec la BDD pour mettre à jour les informations affichées.

public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseMoneyHandler dbMoneyHandler;
    private DatabaseObjectHandler dbObjectHandler;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private TextView txtViewMoneyAmountLoan;
    private TextView txtViewMoneyAmountBorrowed;
    private TextView txtViewObjectQtyLoan;
    private TextView txtViewObjectQtyBorrowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initDbHandlers();
        attachViewItems();
        initVariables();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initDbHandlers(){
        //Initiate the DBHandlers
        dbMoneyHandler = new DatabaseMoneyHandler(this);
        try {
            dbMoneyHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbObjectHandler = new DatabaseObjectHandler(this);
        try {
            dbObjectHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void attachViewItems() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        txtViewMoneyAmountLoan = (TextView) findViewById(R.id.txtViewHomeMoneyAmountLoan);
        txtViewMoneyAmountBorrowed = (TextView) findViewById(R.id.txtViewHomeMoneyAmountBorrowed);
        txtViewObjectQtyLoan = (TextView) findViewById(R.id.txtViewHomeObjectQtyLoan);
        txtViewObjectQtyBorrowed = (TextView) findViewById(R.id.txtViewHomeObjectQtyBorrowed);
    }

    private void initVariables(){
        //notice, no use of String.valueOf for money part, due to the use of " + " €"" that implicitly involves text.
        txtViewMoneyAmountLoan.setText(dbMoneyHandler.getTotalAmountByType(ActivityClass.DATABASE_LOAN_TYPE) + " €");
        txtViewMoneyAmountBorrowed.setText(dbMoneyHandler.getTotalAmountByType(ActivityClass.DATABASE_BORROW_TYPE) + " €");
        txtViewObjectQtyLoan.setText(String.valueOf(dbObjectHandler.getTotalQtyByType(ActivityClass.DATABASE_LOAN_TYPE)));
        txtViewObjectQtyBorrowed.setText(String.valueOf(dbObjectHandler.getTotalQtyByType(ActivityClass.DATABASE_BORROW_TYPE)));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_category) {
            Intent intent = new Intent(this, CategoryActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_drawer_money_loan) {
            Intent intent = new Intent(this, MoneyLoanActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_drawer_money_borrow) {
            Intent intent = new Intent(this, MoneyBorrowActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_drawer_object_loan) {
            Intent intent = new Intent(this, ObjectLoanActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_drawer_object_borrow) {
            Intent intent = new Intent(this, ObjectBorrowActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_drawer_contact) {
            Intent intent = new Intent(this, ContactListActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_drawer_statistic) {
            Intent intent = new Intent(this, StatisticsActivity.class);
            startActivity(intent);
            //TODO : Follow the tutorial to add bar charts for statistics (define which statistics)
        } else if (id == R.id.nav_drawer_contact_us) {
            //TODO : Study the way to open the email app of the user, pre filled with a dedicated email.
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
