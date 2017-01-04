package com.example.avescera.remindme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.avescera.remindme.Classes.Category;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;
import com.example.avescera.remindme.Interfaces.ActivityClass;

public class CategoryCreationActivity extends AppCompatActivity {

    private InitDataBaseHandlers dbHandlers;
    private EditText editCategoryTitle;
    private Category editedCategory;

    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_creation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attachViewItems();
        initVariables();

        ImageView imgSave = (ImageView) findViewById(R.id.imgCategorySave);
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCategory();
            }
        });
    }

    private void initVariables(){
        dbHandlers = new InitDataBaseHandlers(this);
        int editedCategory_id = getIntent().getIntExtra(ActivityClass.CATEGORY_ITEM, 0);

        if(editedCategory_id > 2) {
            editedCategory = dbHandlers.getDbCategoryHandler().getCategory(editedCategory_id);
            editCategoryTitle.setText(editedCategory.get_category());
        }
    }

    private void attachViewItems(){
        editCategoryTitle = (EditText) findViewById(R.id.editTxtCategoryCreationTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void createCategory() {
        // Check if all the necessary data have been filled, return an alert instead.
        if(editCategoryTitle.getText().toString().isEmpty() ){
            final AlertDialog alertDialog = new AlertDialog.Builder(context)
                    // Set Dialog Icon
                    .setIcon(R.drawable.ic_bullet_key_permission)
                    // Set Dialog Title
                    .setTitle(R.string.incomplete_data)
                    // Set Dialog Message
                    .setMessage(R.string.error_message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something else
                        }
                    }).create();

            alertDialog.show();
        } else if(editedCategory == null) {
            Category category = new Category(dbHandlers.getDbCategoryHandler().getCategoryNextId(),
                    editCategoryTitle.getText().toString());

            dbHandlers.getDbCategoryHandler().createCategory(category);

            //Reset all fields
            editCategoryTitle.setText("");

            Toast.makeText(getApplicationContext(), R.string.added_category, Toast.LENGTH_SHORT).show();

        } else {
            editedCategory.set_category(editCategoryTitle.getText().toString());
            dbHandlers.getDbCategoryHandler().updateCategory(editedCategory);

            //Reset all fields
            editCategoryTitle.setText("");

            Toast.makeText(getApplicationContext(), R.string.updated_item, Toast.LENGTH_SHORT).show();
        }
    }

}
