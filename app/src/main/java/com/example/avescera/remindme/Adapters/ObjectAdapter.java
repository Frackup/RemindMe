package com.example.avescera.remindme.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.avescera.remindme.Interfaces.ActivityClass;
import com.example.avescera.remindme.ObjectCreationActivity;
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
    private DatabaseObjectHandler dbObjectHandler;
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

        dbObjectHandler = new DatabaseObjectHandler(context);
        try {
            dbObjectHandler.open();
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
            ImageView imgEdit = (ImageView) convertView.findViewById(R.id.imgViewObjectEdit);
            ImageView imgDelete = (ImageView) convertView.findViewById(R.id.imgViewObjectDelete);

            if (txtTitle != null) { txtTitle.setText(object.get_title()); }
            if (txtFName != null) { txtFName.setText(contact.get_firstName()); }
            if (txtLName != null) { txtLName.setText(contact.get_lastName()); }
            if (txtNumber != null) { txtNumber.setText(Float.toString(object.get_number())); }
            if (txtDate != null) { txtDate.setText(dateFormat.format(object.get_date())); }
            if (txtCategory != null) { txtCategory.setText(category.get_category()); }
            if (imgEdit != null) {
                imgEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editObjectItem();
                    }
                });
            }
            if (imgDelete != null) {
                imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteObjectItem();
                    }
                });
            }
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

    public void editObjectItem() {
        Intent intent = new Intent(getContext(), ObjectCreationActivity.class);
        intent.putExtra(ActivityClass.OBJECT_ITEM,object);
        if(object.get_typeFkId() == ActivityClass.DATABASE_LOAN_TYPE) {
            intent.putExtra(ActivityClass.CALLING_ACTIVITY, ActivityClass.ACTIVITY_LOAN);
        } else if (object.get_typeFkId() == ActivityClass.DATABASE_BORROW_TYPE) {
            intent.putExtra(ActivityClass.CALLING_ACTIVITY, ActivityClass.ACTIVITY_BORROW);
        }
        getContext().startActivity(intent);
    }

    public void deleteObjectItem() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                // Set Dialog Icon
                .setIcon(R.drawable.ic_bullet_key_permission)
                // Set Dialog Title
                .setTitle(R.string.deletion_process)
                // Set Dialog Message
                .setMessage(R.string.deletion_warning)
                .setPositiveButton(R.string.positive_answer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbObjectHandler.deleteObject(object, getContext());

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
