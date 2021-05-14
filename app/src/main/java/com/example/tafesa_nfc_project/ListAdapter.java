package com.example.tafesa_nfc_project;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;



public class ListAdapter extends BaseExpandableListAdapter {
    //Initialize variable
    ArrayList<String> listGroup;
    HashMap<String,ArrayList<String>> listChild;

    //Create constructor
    public ListAdapter(ArrayList<String> listGroup,HashMap<String,ArrayList<String>> listChild){
        this.listGroup = listGroup;
        this.listChild = listChild;
    }

    @Override
    public int getGroupCount() {
        //Return group list size
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //Return child list size
        return listChild.get(listGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        //Return group item
        return listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        //Return child item
        return listChild.get(listGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        //Initialize view
        view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_expandable_list_item_1
                                   ,parent,false);
        //Initialize and assign variable
        TextView textView = view.findViewById(android.R.id.text1);
        //Initialize string
        String sGroup = String.valueOf(getGroup(groupPosition));
        //Set text on text view
        textView.setText(sGroup);
        //Set text style Bold
        textView.setTypeface(null, Typeface.BOLD);
        //Set text color
        textView.setTextColor(Color.RED);
        //return view
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        //Initialize view
        view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_expandable_list_item_1
                ,parent,false);
        //Initialize and assign variable
        TextView textView = view.findViewById(android.R.id.text1);
        //Initialize string
        String sChild = String.valueOf(getGroup(groupPosition));
        //Set text on text view
        textView.setText(sChild);

        //Set on click listener
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //Display toast
                Toast.makeText(parent.getContext(),sChild,Toast.LENGTH_SHORT).show();
            }
        });

        //return view
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}

