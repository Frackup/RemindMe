package com.example.avescera.remindme.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.avescera.remindme.Classes.Category;
import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.DBHandlers.DatabaseCategoryHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseContactHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseTypeHandler;
import com.example.avescera.remindme.R;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Frackup on 26/11/2016.
 */

public class CategorySpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<Category> listCategories;
    private LayoutInflater inflater;
    private DatabaseCategoryHandler dbCategoryHandler;

    public CategorySpinnerAdapter(Context applicationContext, List<Category> listCategories) {
        this.context = applicationContext;
        this.listCategories = listCategories;
        inflater = (LayoutInflater.from(applicationContext));

        //Initiate the DBHandler
        dbCategoryHandler = new DatabaseCategoryHandler(applicationContext);
        try {
            dbCategoryHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return listCategories.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null){
            inflater = LayoutInflater.from(context);

            view = inflater.inflate(R.layout.contact_spinner_item, null);
        }

        Category category = dbCategoryHandler.getCategory(position);

        TextView names = (TextView) view.findViewById(R.id.txtViewCategorySpinnerCategory);
        names.setText(category.get_category());
        return view;
    }
}
