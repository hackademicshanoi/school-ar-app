package com.example.daomy.uar2.adapter;

import android.app.Activity;
<<<<<<< HEAD
import android.graphics.Color;
=======
>>>>>>> c3b4787... code màn hình login & register + update api + code web php login & register
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.daomy.uar2.R;
import com.example.daomy.uar2.model.School;

import java.util.List;

public class SpinnerSchoolAdapter extends ArrayAdapter<School> {
    @NonNull Activity context;
    @LayoutRes int resource;
    @NonNull List<School> objects;
    public SpinnerSchoolAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<School> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        View row=inflater.inflate(R.layout.spinner_item,parent,false);
        TextView txtSpSchool= (TextView) row.findViewById(R.id.txtSpSchool);

        School school=objects.get(position);
        txtSpSchool.setText(school.getSchool_name());
<<<<<<< HEAD
        if (position ==0){
            txtSpSchool.setTextColor(Color.GRAY);
        }
=======
>>>>>>> c3b4787... code màn hình login & register + update api + code web php login & register
        return row;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        View row=inflater.inflate(R.layout.spinner_item,parent,false);
        TextView txtSpSchool= (TextView) row.findViewById(R.id.txtSpSchool);

        School school=objects.get(position);
        txtSpSchool.setText(school.getSchool_name());
<<<<<<< HEAD
        if (position ==0){
            txtSpSchool.setTextColor(Color.GRAY);
        }
        return row;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position==0){
            return false;
        } else {
            return true;
        }
    }

    public void setError(View v, CharSequence s) {
        TextView name = (TextView) v.findViewById(R.id.txtSpSchool);
        name.setError(s);
    }
=======
        return row;
    }
>>>>>>> c3b4787... code màn hình login & register + update api + code web php login & register
}
