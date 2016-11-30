package com.example.avescera.remindme.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private Dialog dialog;
    private Object object;

    public ObjectAdapter(Context context, int textViewResourceId) {
        super(context,textViewResourceId);

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

    public ObjectAdapter(Context context, int resource, List<Object> objectList) {
        super(context,resource,objectList);

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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.object_list_item, null);
        }

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
        object = getItem(position);
        Contact contact = dbContactHandler.getContact(object.get_contactFkId());
        Category category = dbCategoryHandler.getCategory(object.get_categoryFkId());

        if (object != null) {
            dialog = new Dialog(getContext());

            TextView txtTitle = (TextView) convertView.findViewById(R.id.txtViewObjectTitle);
            TextView txtFName = (TextView) convertView.findViewById(R.id.txtViewObjectFName);
            TextView txtLName = (TextView) convertView.findViewById(R.id.txtViewObjectLName);
            TextView txtNumber = (TextView) convertView.findViewById(R.id.txtViewObjectNumber);
            TextView txtDate = (TextView) convertView.findViewById(R.id.txtViewObjectDate);
            TextView txtCategory = (TextView) convertView.findViewById(R.id.txtViewObjectCategory);
            ImageView imgDetail = (ImageView) convertView.findViewById(R.id.imgViewObjectDetail);

            if (txtTitle != null) { txtTitle.setText(object.get_title()); }
            if (txtFName != null) { txtFName.setText(contact.get_firstName()); }
            if (txtLName != null) { txtLName.setText(contact.get_lastName()); }
            if (txtNumber != null) { txtNumber.setText(Float.toString(object.get_number())); }
            if (txtDate != null) { txtDate.setText(dateFormat.format(object.get_date())); }
            if (txtCategory != null) { txtCategory.setText(category.get_category()); }
            if (imgDetail != null) {
                imgDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createDetailDialog();
                    }
                });
            }
        }
        return convertView;
    }

    public void createDetailDialog(){
        //Custom dialog
        dialog.setContentView(R.layout.detail_dialog);
        dialog.setTitle(getContext().getResources().getString(R.string.dialog_detail_title).toString());

        //set the custom dialog component
        TextView txtDetail = (TextView) dialog.findViewById(R.id.txtViewDetailDisplay);
        FloatingActionButton fabDetail = (FloatingActionButton) dialog.findViewById(R.id.fabDetailOK);

        if (txtDetail != null) { txtDetail.setText(object.get_details()); }

        fabDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
