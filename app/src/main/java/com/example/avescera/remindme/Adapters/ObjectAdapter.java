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
 * Created by a.vescera on 05/12/2016.
 */

public class ObjectAdapter extends ArrayAdapter<Object> {

    private Dialog dialog;
    private DatabaseContactHandler dbContactHandler;
    private DatabaseObjectHandler dbObjectHandler;
    private DatabaseCategoryHandler dbCategoryHandler;
    private List<Object> objectList;

    public ObjectAdapter(Context context, List<Object> _objectList) {
        super(context,0,_objectList);
        this.objectList = _objectList;

        //Initiate the DBHandler
        dbContactHandler = new DatabaseContactHandler(context);
        try {
            dbContactHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbObjectHandler = new DatabaseObjectHandler(context);
        try {
            dbObjectHandler.open();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.money_list_item, parent, false);
        }

        ObjectAdapter.ObjectViewHolder viewHolder = (ObjectAdapter.ObjectViewHolder) convertView.getTag();
        if(viewHolder == null) {
            viewHolder = new ObjectAdapter.ObjectViewHolder();
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.txtViewObjectTitle);
            viewHolder.txtFName = (TextView) convertView.findViewById(R.id.txtViewObjectFName);
            viewHolder.txtLName = (TextView) convertView.findViewById(R.id.txtViewObjectLName);
            viewHolder.txtNumber = (TextView) convertView.findViewById(R.id.txtViewObjectNumber);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.txtViewObjectDate);
            viewHolder.txtCategory = (TextView) convertView.findViewById(R.id.txtViewObjectCategory);
            viewHolder.imgDetail = (ImageView) convertView.findViewById(R.id.imgViewObjectDetail);
            viewHolder.imgEdit = (ImageView) convertView.findViewById(R.id.imgViewObjectEdit);
        }

        final Object object = getItem(position);
        Contact contact = dbContactHandler.getContact(object.get_contactFkId());
        Category category = dbCategoryHandler.getCategory(object.get_categoryFkId());
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
        dialog = new Dialog(getContext());

        if (viewHolder.txtTitle != null) { viewHolder.txtTitle.setText(object.get_title()); }
        if (viewHolder.txtFName != null) { viewHolder.txtFName.setText(contact.get_firstName()); }
        if (viewHolder.txtLName != null) { viewHolder.txtLName.setText(contact.get_lastName()); }
        if (viewHolder.txtNumber != null) { viewHolder.txtNumber.setText(Float.toString(object.get_number())); }
        if (viewHolder.txtDate != null) { viewHolder.txtDate.setText(dateFormat.format(object.get_date())); }
        if (viewHolder.txtCategory != null) { viewHolder.txtCategory.setText(category.get_category()); }
        if (viewHolder.imgEdit != null) {
            viewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editObjectItem(object);
                }
            });
        }
        if (viewHolder.imgDelete != null) {
            viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteObjectItem(object);
                }
            });
        }
        if (viewHolder.imgDetail != null) {
            viewHolder.imgDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDetailDialog(object);
                }
            });
        }

        return convertView;
    }

    private class ObjectViewHolder{
        public TextView txtTitle;
        public TextView txtFName;
        public TextView txtLName;
        public TextView txtNumber;
        public TextView txtDate;
        public TextView txtCategory;
        public ImageView imgDetail;
        public ImageView imgEdit;
        public ImageView imgDelete;
    }

    public void createDetailDialog(Object object){
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

    public void editObjectItem(Object object) {
        Intent intent = new Intent(getContext(), ObjectCreationActivity.class);
        intent.putExtra(ActivityClass.OBJECT_ITEM,object);
        if(object.get_typeFkId() == ActivityClass.DATABASE_LOAN_TYPE) {
            intent.putExtra(ActivityClass.CALLING_ACTIVITY, ActivityClass.ACTIVITY_LOAN);
        } else if (object.get_typeFkId() == ActivityClass.DATABASE_BORROW_TYPE) {
            intent.putExtra(ActivityClass.CALLING_ACTIVITY, ActivityClass.ACTIVITY_BORROW);
        }
        getContext().startActivity(intent);
    }

    public void deleteObjectItem(final Object object) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                // Set Dialog Icon
                .setIcon(R.drawable.ic_bullet_key_permission)
                // Set Dialog Title
                .setTitle(R.string.deletion_process)
                // Set Dialog Message
                .setMessage(R.string.deletion_warning)
                .setPositiveButton(R.string.positive_answer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        objectList.remove(getPosition(object));
                        notifyDataSetChanged();
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
