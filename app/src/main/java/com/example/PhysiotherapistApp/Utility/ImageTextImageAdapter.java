package com.example.PhysiotherapistApp.Utility;

/**
 * Created by Amar on 2016-02-25.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.example.PhysiotherapistApp.R;

import java.util.List;
import java.util.Map;

public class ImageTextImageAdapter extends SimpleExpandableListAdapter {
    public ImageTextImageAdapter(Context context, List<? extends Map<String, ?>> groupData, int groupLayout, String[] groupFrom, int[] groupTo, List<? extends List<? extends Map<String, ?>>> childData, int childLayout, String[] childFrom, int[] childTo) {
        super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo);
    }/*

    private final Activity context;
    List<String> itemname;
    //private final Integer[] imgid;
    private final List<byte[]> images;

    public ImageTextImageAdapter(Activity context, List<String> itemname, List<byte[]> images) {
        super(context,
                groupData,                         // Data for Group
                R.layout.imagetextimage_group_row,        // Layout of Group
                new String[]{NAME},                 // the name of the field data
                new int[]{R.id.exerciseRowName},   // the text field to populate with the field data
                childData,                          // Data for child
                R.layout.imagetextimage_child_row,        // Layout of Child
                new String[]{NAME},                 // the name of the child field data
                new int[]{R.id.exerciseDescription} // the text field to populate with the child field data
        );
        this.context=context;
        this.itemname = itemname;
        this.images = images;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final View v = super.getGroupView(groupPosition, isExpanded, convertView, parent);

        // Populate your custom view here
        ((TextView) v.findViewById(R.id.exerciseRowName)).setText((String) ((Map<String, Object>) getGroup(groupPosition)).get(NAME));
        ((ImageView) v.findViewById(R.id.exerciseIcon)).setImageDrawable((Drawable) ((Map<String, Object>) getGroup(groupPosition)).get(IMAGE));

        return v;
    }

    @Override
    public View newGroupView(boolean isExpanded, ViewGroup parent) {
        return layoutInflater.inflate(R.layout.imagetextimage_group_row, null, false);
    }*/

    /*public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.image_text_list, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.itemName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.itemIcon);

        byte[] b = images.get(position);
        Drawable image = new BitmapDrawable(BitmapFactory.decodeByteArray(b, 0, b.length));
        txtTitle.setText(itemname.get(position));
        imageView.setImageDrawable(image);
        return rowView;

    };*/
}