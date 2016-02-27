package com.example.PhysiotherapistApp.Utility;

/**
 * Created by Amar on 2016-02-25.
 */

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.PhysiotherapistApp.R;

import java.util.List;

public class ImageTextAdapter extends ArrayAdapter<String> {

    private final Activity context;
    List<String> itemname;
    //private final Integer[] imgid;
    private final List<Drawable> images;

    public ImageTextAdapter(Activity context, List<String> itemname,List<Drawable> images) {
        super(context, R.layout.image_text_list, itemname);

        this.context=context;
        this.itemname = itemname;
        this.images = images;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.image_text_list, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.itemName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.itemIcon);

        //byte[] b = images.get(position);
        Drawable image = images.get(position);
        txtTitle.setText(itemname.get(position));
        imageView.setImageDrawable(image);
        return rowView;

    };
}