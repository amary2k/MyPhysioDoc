package com.example.PhysiotherapistApp.Utility;

/**
 * Created by Amar on 2016-03-07.
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TreeMap;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.PhysiotherapistApp.Model.Message;
import com.example.PhysiotherapistApp.Model.UserState;
import com.example.PhysiotherapistApp.R;

public class ChatListAdapter extends ArrayAdapter<Message> {

    private final Context context;
    private final ArrayList<Message> itemsArrayList;
    private final String userFrom;

    public ChatListAdapter(Context context, ArrayList<Message> itemsArrayList, String userFrom) {

        super(context, R.layout.chat_list_item_layout, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
        this.userFrom =  userFrom;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.chat_list_item_layout, parent, false);


        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.chatMessage);
        TextView valueView = (TextView) rowView.findViewById(R.id.chatTime);

        // 4. Set the text for textView
        labelView.setText(itemsArrayList.get(position).getMessage());
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
        valueView.setText(sdf.format(itemsArrayList.get(position).getMessageTime()));

        View chatBox = rowView.findViewById(R.id.chatBox);
        RelativeLayout.LayoutParams chatBoxLayoutParams = (RelativeLayout.LayoutParams) chatBox.getLayoutParams();
        //if(UserState.isPhysio()) {
            if (itemsArrayList.get(position).getFromUser().equals(userFrom)){
                chatBoxLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                chatBox.setBackgroundColor(0x8899cc00);
            }
            else
                chatBoxLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        //}
        /*else{
            if (itemsArrayList.get(position).getFromUser().equals(userFrom))
                chatBoxLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            else {
                chatBoxLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                chatBox.setBackgroundColor(0xff99cc00);
            }
        }*/
        chatBox.setLayoutParams(chatBoxLayoutParams);

        // 5. retrn rowView
        return rowView;
    }
}