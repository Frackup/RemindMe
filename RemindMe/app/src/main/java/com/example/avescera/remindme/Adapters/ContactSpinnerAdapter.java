package com.example.avescera.remindme.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.avescera.remindme.R;

/**
 * Created by a.vescera on 23/11/2016.
 */

public class ContactSpinnerAdapter extends BaseAdapter {
    Context context;
    String[] contactNames;
    LayoutInflater inflater;

    public ContactSpinnerAdapter(Context applicationContext, String[] contactNames) {
        this.context = applicationContext;
        this.contactNames = contactNames;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return contactNames.length;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.contact_spinner_item, null);
        TextView names = (TextView) view.findViewById(R.id.txtViewContactSpinnerFirstName);
        names.setText(contactNames[i]);
        return view;
    }
}
