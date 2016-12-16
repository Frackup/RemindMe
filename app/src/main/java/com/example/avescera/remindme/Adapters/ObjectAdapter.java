package com.example.avescera.remindme.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avescera.remindme.CategoryActivity;
import com.example.avescera.remindme.Classes.Category;
import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;
import com.example.avescera.remindme.Classes.Object;
import com.example.avescera.remindme.ContactExchangeActivity;
import com.example.avescera.remindme.Interfaces.ActivityClass;
import com.example.avescera.remindme.ObjectCreationActivity;
import com.example.avescera.remindme.R;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by a.vescera on 05/12/2016.
 * Allows to manage the display of an Object item within a listView, and the interaction with it.
 */

public class ObjectAdapter extends ArrayAdapter<Object> {

    private Dialog dialog;
    private InitDataBaseHandlers dbHandlers;
    private List<Object> objectList;

    public ObjectAdapter(Context context, List<Object> _objectList) {
        super(context,0,_objectList);
        this.objectList = _objectList;

        //Initiate the DBHandler
        dbHandlers = new InitDataBaseHandlers(context);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.object_list_item, parent, false);
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
        assert object != null;
        final Contact contact = dbHandlers.getDbContactHandler().getContact(object.get_contactFkId());
        final int contact_id = contact.get_id();
        final Category category = dbHandlers.getDbCategoryHandler().getCategory(object.get_categoryFkId());
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
        dialog = new Dialog(getContext());

        if (viewHolder.txtTitle != null) { viewHolder.txtTitle.setText(object.get_title()); }
        if (viewHolder.txtFName != null) {
            viewHolder.txtFName.setText(contact.get_firstName());
            //The 2 first ids are reserved to display add contact and select a contact.
            if(contact_id > 2) {
                viewHolder.txtFName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToContactExchangePage(contact_id);
                    }
                });
            }
        }
        if (viewHolder.txtLName != null) {
            viewHolder.txtLName.setText(contact.get_lastName());
            if(contact_id > 2) {
                viewHolder.txtLName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToContactExchangePage(contact_id);
                    }
                });
            }
        }
        if (viewHolder.txtNumber != null) { viewHolder.txtNumber.setText(String.format(Locale.getDefault(), "%d", object.get_quantity())); }
        if (viewHolder.txtDate != null) { viewHolder.txtDate.setText(dateFormat.format(object.get_date())); }
        if (viewHolder.txtCategory != null) { viewHolder.txtCategory.setText(category.get_category());
            viewHolder.txtCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToCategoryActivity(category.get_id(), contact_id);
                }
            });
        }
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
        TextView txtTitle;
        TextView txtFName;
        TextView txtLName;
        TextView txtNumber;
        TextView txtDate;
        TextView txtCategory;
        ImageView imgDetail;
        ImageView imgEdit;
        ImageView imgDelete;
    }

    private void createDetailDialog(Object object){
        //Custom dialog
        dialog.setContentView(R.layout.detail_dialog);
        dialog.setTitle(getContext().getResources().getString(R.string.dialog_detail_title));

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

    private void editObjectItem(Object object) {
        Intent intent = new Intent(getContext(), ObjectCreationActivity.class);
        intent.putExtra(ActivityClass.OBJECT_ITEM,object.get_id());
        if(object.get_typeFkId() == ActivityClass.DATABASE_LOAN_TYPE) {
            intent.putExtra(ActivityClass.CALLING_ACTIVITY, ActivityClass.ACTIVITY_LOAN);
        } else if (object.get_typeFkId() == ActivityClass.DATABASE_BORROW_TYPE) {
            intent.putExtra(ActivityClass.CALLING_ACTIVITY, ActivityClass.ACTIVITY_BORROW);
        }
        getContext().startActivity(intent);
    }

    private void deleteObjectItem(final Object object) {
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
                        dbHandlers.getDbObjectHandler().deleteObject(object);

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

    private void goToContactExchangePage(int contact_id){
        Intent intent = new Intent(getContext(), ContactExchangeActivity.class);
        intent.putExtra(ActivityClass.CONTACT_ITEM, contact_id);
    }

    private void goToCategoryActivity(int category_id, int contact_id){
        Intent intent = new Intent(getContext(), CategoryActivity.class);
        intent.putExtra(ActivityClass.CATEGORY_ITEM, category_id);
        intent.putExtra(ActivityClass.CONTACT_ITEM, contact_id);
        getContext().startActivity(intent);
    }
}
