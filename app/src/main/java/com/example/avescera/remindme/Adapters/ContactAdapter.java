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

import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;
import com.example.avescera.remindme.ContactCreationActivity;
import com.example.avescera.remindme.ContactExchangeActivity;
import com.example.avescera.remindme.DBHandlers.DatabaseContactHandler;
import com.example.avescera.remindme.Interfaces.ActivityClass;
import com.example.avescera.remindme.R;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by a.vescera on 05/12/2016.
 */

public class ContactAdapter extends ArrayAdapter<Contact> {

    private List<Contact> contactList;
    private InitDataBaseHandlers dbHandlers;

    public ContactAdapter(Context context, List<Contact> _contactList) {
        super(context,0,_contactList);

        this.contactList = _contactList;

        //Initiate the DBHandler
        dbHandlers = new InitDataBaseHandlers(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_list_item, parent, false);
        }

        ContactAdapter.ContactViewHolder viewHolder = (ContactAdapter.ContactViewHolder) convertView.getTag();
        if(viewHolder == null) {
            viewHolder = new ContactAdapter.ContactViewHolder();
            viewHolder.txtContactFName = (TextView) convertView.findViewById(R.id.txtViewContactFirstName);
            viewHolder.txtContactLName = (TextView) convertView.findViewById(R.id.txtViewContactLastName);
            viewHolder.txtContactPhone = (TextView) convertView.findViewById(R.id.txtViewContactPhone);
            viewHolder.txtContactEmail = (TextView) convertView.findViewById(R.id.txtViewContactEmail);
            viewHolder.imgViewContactEdit = (ImageView) convertView.findViewById(R.id.imgViewContactEdit);
            viewHolder.imgViewContactDelete = (ImageView) convertView.findViewById(R.id.imgViewContactDelete);
       }

        final Contact contact = getItem(position);

        if (viewHolder.txtContactFName != null) {
            viewHolder.txtContactFName.setText(contact.get_firstName());
            viewHolder.txtContactFName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToContactExchangePage(contact.get_id());
                }
            });
        }
        if (viewHolder.txtContactLName != null) {
            viewHolder.txtContactLName.setText(contact.get_lastName());
            viewHolder.txtContactLName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToContactExchangePage(contact.get_id());
                }
            });
        }
        if (viewHolder.txtContactPhone != null) { viewHolder.txtContactPhone.setText(contact.get_phone()); }
        if (viewHolder.txtContactEmail != null) { viewHolder.txtContactEmail.setText(contact.get_email()); }
        if(viewHolder.imgViewContactEdit != null) {
            viewHolder.imgViewContactEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editContactItem(contact.get_id());
                }
            });
        }
        if(viewHolder.imgViewContactDelete != null) {
            viewHolder.imgViewContactDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteContactItem(contact);
                }
            });
        }

        return convertView;
    }

    private class ContactViewHolder{
        public TextView txtContactFName;
        public TextView txtContactLName;
        public TextView txtContactPhone;
        public TextView txtContactEmail;
        public ImageView imgViewContactEdit;
        public ImageView imgViewContactDelete;
    }

    public void editContactItem(int contact_id) {
        Intent intent = new Intent(getContext(), ContactCreationActivity.class);
        intent.putExtra(ActivityClass.CONTACT_ITEM,contact_id);
        getContext().startActivity(intent);
    }

    public void deleteContactItem(final Contact contact) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                // Set Dialog Icon
                .setIcon(R.drawable.ic_bullet_key_permission)
                // Set Dialog Title
                .setTitle(R.string.contact_deletion_process)
                // Set Dialog Message
                .setMessage(R.string.contact_deletion_warning)
                .setPositiveButton(R.string.positive_answer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        contactList.remove(getPosition(contact));
                        notifyDataSetChanged();
                        dbHandlers.getDbContactHandler().deleteContact(contact, getContext());

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

    public void goToContactExchangePage(int contact_id){
        Intent intent = new Intent(getContext(), ContactExchangeActivity.class);
        intent.putExtra(ActivityClass.CONTACT_ITEM, contact_id);
        getContext().startActivity(intent);
    }
}
