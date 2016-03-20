package com.example.PhysiotherapistApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ListView;

import com.example.PhysiotherapistApp.Model.Message;
import com.example.PhysiotherapistApp.Model.Patient;
import com.example.PhysiotherapistApp.Model.Physiotherapist;
import com.example.PhysiotherapistApp.Model.UserState;
import com.example.PhysiotherapistApp.Network.DefaultRestClient;
import com.example.PhysiotherapistApp.Network.RestClient;
import com.example.PhysiotherapistApp.Utility.ChatListAdapter;
import com.example.PhysiotherapistApp.Utility.Utility;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class MessagingActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    String responseString  = RestClient.response;
    Physiotherapist physio;
    Patient patient;
    Set<Message> messageSet;
    String to;

    Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);


        if(UserState.isPhysio()) {
            Bundle b = getIntent().getExtras();
            to = b.getString("groupEmail");
        }
        final Activity rootActivity = this;
        refreshChatWindow();

        final EditText chatMsgBox = (EditText) findViewById(R.id.chatText);
        ImageButton sendBtn = (ImageButton) findViewById(R.id.sendMsgBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               /* String body = "";
                if(UserState.isPhysio())
                {*/
                    //messageSet.add();
                Message message;
                if(UserState.isPhysio())
                    message = new Message(chatMsgBox.getText().toString(), new Date(), to, physio.getEmail());
                else
                    message = new Message(chatMsgBox.getText().toString(), new Date(), patient.getPhysiotherapistEmail(), patient.getEmail());


                //physio.getMessages().add(new Message(chatMsgBox.getText().toString(), new Date(), to, physio.getEmail()));
                String body = message.getJSON();
                /*}
                else
                {
                    messageSet.add(new Message(chatMsgBox.getText().toString(),new Date(), patient.getPhysiotherapistEmail(), patient.getEmail()));
                    body = patient.getJSON();
                }*/

                pDialog = Utility.showTranslucentProgressDialog(rootActivity);
                DefaultRestClient defaultRestClient = new DefaultRestClient(RestClient.POST, getBaseContext().getString(R.string.rest_client_uri_messages), body, RestClient.APPLICATION_JSON, getBaseContext()) {
                    @Override
                    protected void onPostExecute(String s) {
                        Log.v("MessagingActivity",s);

                        pDialog.dismiss();
                        responseString = s;
                        chatMsgBox.setText("");
                        refreshChatWindow();
                    }
                };
                defaultRestClient.execute();
            }
        });

    }

    private void refreshChatWindow(){

        if(UserState.isPhysio()) {
            physio = gson.fromJson(responseString, Physiotherapist.class);


            for(Patient patient:physio.getPatients())
            {
                if(patient.getEmail().equals(to)){
                    messageSet = patient.getMessages();
                    String name = patient.getName();
                    setTitle(name.substring(0, 1).toUpperCase() + name.substring(1));
                    break;
                }
            }


        }
        else
        {
            patient = gson.fromJson(responseString, Patient.class);
            String name = patient.getName();
            setTitle(name.substring(0, 1).toUpperCase() + name.substring(1));

            messageSet = patient.getMessages();
        }

        /*List<String> msgList = new ArrayList<>();
        for(Message msg: messageSet)
            msgList.add(msg.getMessage());

        String[] chatMessages = new String[msgList.size()];
        chatMessages = msgList.toArray(chatMessages);

        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.list_item_layout,
                R.id.list_item_menu_item,
                chatMessages);*/

        ArrayList<Message> chatMessages = new ArrayList<Message>();
        chatMessages.addAll(messageSet);
        Collections.sort(chatMessages, new Comparator<Message>() {
            public int compare(Message m1, Message m2) {
                return m1.getMessageTime().compareTo(m2.getMessageTime());
            }
        });


        final ArrayAdapter<Message> chatAdapter = new ChatListAdapter(this,chatMessages, UserState.getUserName());

        //mProgressView = getActivity().findViewById(R.id.progressBar);

        final ListView myListView = (ListView) findViewById(R.id.chatView);
        myListView.setAdapter(chatAdapter);
        myListView.post(new Runnable() {
            @Override
            public void run() {
                myListView.smoothScrollToPosition(chatAdapter.getCount() - 1);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
