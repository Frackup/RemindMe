package com.example.avescera.remindme.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avescera.remindme.CategoryCreationActivity;
import com.example.avescera.remindme.Classes.Category;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;
import com.example.avescera.remindme.DBHandlers.DatabaseCategoryHandler;
import com.example.avescera.remindme.Interfaces.ActivityClass;
import com.example.avescera.remindme.R;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by a.vescera on 05/12/2016.
 */

public class CategoryAdapter extends ArrayAdapter<Category> {

    private List<Category> categoryList;
    private InitDataBaseHandlers dbHandlers;

    public CategoryAdapter(Context context, List<Category> _categoryList) {
        super(context,0,_categoryList);

        this.categoryList = _categoryList;

        //Initiate the DBHandler
        dbHandlers = new InitDataBaseHandlers(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_list_item, parent, false);
        }

        CategoryAdapter.CategoryViewHolder viewHolder = (CategoryAdapter.CategoryViewHolder) convertView.getTag();
        if(viewHolder == null) {
            viewHolder = new CategoryAdapter.CategoryViewHolder();
            viewHolder.category = (TextView) convertView.findViewById(R.id.txtViewCategoryItem);
            viewHolder.imgViewCategoryEdit = (ImageView) convertView.findViewById(R.id.imgViewCategoryEdit);
            viewHolder.imgViewCategoryDelete = (ImageView) convertView.findViewById(R.id.imgViewCategoryDelete);
        }

        final Category category = getItem(position);

        if (viewHolder.category != null) { viewHolder.category.setText(category.get_category()); }
        if (viewHolder.imgViewCategoryEdit != null) {
            viewHolder.imgViewCategoryEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editCategoryItem(category);
                }
            });
        }
        if (viewHolder.imgViewCategoryDelete != null) {
            viewHolder.imgViewCategoryDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteCategoryItem(category);
                }
            });
        }

        return convertView;
    }

    private class CategoryViewHolder{
        public TextView category;
        public ImageView imgViewCategoryEdit;
        public ImageView imgViewCategoryDelete;
    }

    public void editCategoryItem(Category category) {
        Intent intent = new Intent(getContext(), CategoryCreationActivity.class);
        intent.putExtra(ActivityClass.CATEGORY_ITEM, category.get_id());
        getContext().startActivity(intent);
    }

    public void deleteCategoryItem(final Category category){

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                // Set Dialog Icon
                .setIcon(R.drawable.ic_bullet_key_permission)
                // Set Dialog Title
                .setTitle(R.string.deletion_process)
                // Set Dialog Message
                .setMessage(R.string.deletion_warning)
                .setPositiveButton(R.string.positive_answer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        categoryList.remove(getPosition(category));
                        notifyDataSetChanged();
                        dbHandlers.getDbCategoryHandler().deleteCategory(category, getContext());

                        Toast.makeText(getContext(), R.string.deletion_confirmation, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.negative_answer, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();

        alertDialog.show();
    }
}
