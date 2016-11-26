package com.example.avescera.remindme.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.avescera.remindme.Classes.Type;
import com.example.avescera.remindme.R;

import java.util.List;

/**
 * Created by Frackup on 26/11/2016.
 */

public class TypeSpinnerAdapter extends ArrayAdapter<Type> {

    public TypeSpinnerAdapter(Context context, List<Type> typesList) {
        super(context,0,typesList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.type_spinner_item, parent, false);
        }

        TypeSpinnerAdapter.TypeSpinnerViewHolder viewHolder = (TypeSpinnerAdapter.TypeSpinnerViewHolder) convertView.getTag();
        if(viewHolder == null) {
            viewHolder = new TypeSpinnerAdapter.TypeSpinnerViewHolder();
            viewHolder.type = (TextView) convertView.findViewById(R.id.txtViewTypeSpinnerType);
        }

        Type type = getItem(position);

        viewHolder.type.setText(type.get_type());

        return convertView;
    }

    private class TypeSpinnerViewHolder{
        public TextView type;
    }
}
