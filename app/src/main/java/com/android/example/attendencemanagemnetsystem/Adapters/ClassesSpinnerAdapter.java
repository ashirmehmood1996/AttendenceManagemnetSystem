package com.android.example.attendencemanagemnetsystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.example.attendencemanagemnetsystem.Models.ClassModel;
import com.android.example.attendencemanagemnetsystem.R;

import java.util.List;

public class ClassesSpinnerAdapter extends ArrayAdapter<ClassModel> {

    public ClassesSpinnerAdapter(Context context, List<ClassModel> objects) {
        super(context, 0, objects);
    }

    //for simple display of spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.classes_spinner_list_item, parent, false);
        }
        //get references
        TextView titleTextView = view.findViewById(R.id.tv_classes_spinner_li_title);
        ClassModel currentModel = getItem(position);
        titleTextView.setText(currentModel.getTitle());
        return view;
    }

    //for drop down view
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.classes_spinner_list_item, parent, false);
        }
        //get references
        TextView titleTextView = view.findViewById(R.id.tv_classes_spinner_li_title);
        ClassModel currentModel = getItem(position);
        titleTextView.setText(currentModel.getTitle());
        return view;
    }
}
