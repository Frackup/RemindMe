package com.example.avescera.remindme.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.avescera.remindme.Classes.Category;
import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.R;

import java.util.List;

/**
 * Created by Frackup on 27/11/2016.
 */

public class CategoryAdapter extends ArrayAdapter<Category> {

    public CategoryAdapter(Context context, int textViewResourceId) {
        super(context,textViewResourceId);
    }

    public CategoryAdapter(Context context, int resource, List<Category> categoriesList) {
        super(context,resource,categoriesList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_list_item, null);
        }

        Category category = getItem(position);

        if (category != null) {


            TextView txtCategory = (TextView) convertView.findViewById(R.id.txtViewCategoryItem);

            if (txtCategory != null) {
                txtCategory.setText(category.get_category());
            }
        }
        return convertView;
    }
}
