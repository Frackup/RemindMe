package com.example.avescera.remindme.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.avescera.remindme.Classes.Category;
import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.Money;
import com.example.avescera.remindme.Classes.Object;
import com.example.avescera.remindme.DBHandlers.DatabaseCategoryHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseContactHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseObjectHandler;
import com.example.avescera.remindme.R;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.List;

/**
 * Created by a.vescera on 25/11/2016.
 */

public class ObjectAdapter extends ArrayAdapter<Object> {

    private DatabaseContactHandler dbContactHandler;
    private DatabaseCategoryHandler dbCategoryHandler;

    public ObjectAdapter(Context context, List<Object> objectsList){
        super(context,0,objectsList);
        //Initiate the DBHandler
        dbContactHandler = new DatabaseContactHandler(context);
        try {
            dbContactHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbCategoryHandler = new DatabaseCategoryHandler(context);
        try {
            dbCategoryHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.object_list_item, parent,false);
        }

        ObjectAdapter.ObjectViewHolder viewHolder = (ObjectAdapter.ObjectViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ObjectAdapter.ObjectViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.txtViewObjectTitle);
            viewHolder.firstName = (TextView) convertView.findViewById(R.id.txtViewObjectFName);
            viewHolder.lastName = (TextView) convertView.findViewById(R.id.txtViewObjectLName);
            viewHolder.number = (TextView) convertView.findViewById(R.id.txtViewObjectNumber);
            viewHolder.date = (TextView) convertView.findViewById(R.id.txtViewObjectDate);
            viewHolder.category = (TextView) convertView.findViewById(R.id.txtViewObjectCategory);
            viewHolder.details = (ImageView) convertView.findViewById(R.id.imgViewMoneyDetail);
        }

        Object object = getItem(position);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());

        Contact contact = dbContactHandler.getContact(object.get_contactFkId());
        Category category = dbCategoryHandler.getCategory(object.get_categoryFkId());

        viewHolder.title.setText(object.get_title());
        viewHolder.firstName.setText(contact.get_firstName());
        viewHolder.lastName.setText(contact.get_lastName());
        viewHolder.number.setText(object.get_number());
        viewHolder.category.setText(category.get_category());
        viewHolder.date.setText(dateFormat.format(object.get_date()));

        return convertView;
    }

    private class ObjectViewHolder{
        public TextView title;
        public TextView firstName;
        public TextView lastName;
        public TextView number;
        public TextView category;
        public TextView date;
        public ImageView details;
    }
}
