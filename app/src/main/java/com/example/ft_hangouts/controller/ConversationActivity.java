package com.example.ft_hangouts.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ft_hangouts.R;
import com.example.ft_hangouts.Utils;
import com.example.ft_hangouts.helper.MessageHelper;
import com.example.ft_hangouts.model.Contact;
import com.example.ft_hangouts.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConversationActivity extends AppCompatActivity {
    private static final int MY_PERMISSION_REQUEST_CODE_SEND_SMS = 1;
    private static final String LOG_TAG = "AndroidExample";

    private EditText editTextMessage;
    private Contact contact;

    private final List<Message> messageList = new ArrayList<>();
    private ArrayAdapter<Message> listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Delete the header title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_conversation);

        TextView title = this.findViewById(R.id.textViewTitle);
        ListView listView = findViewById(R.id.listView);
        Button buttonSend = this.findViewById(R.id.button_send);
        ImageButton buttonBack = findViewById(R.id.button_back);
        this.editTextMessage = this.findViewById(R.id.editText_message);

        MessageHelper db = new MessageHelper(this);

        Intent intent = this.getIntent();
        this.contact = (Contact) intent.getSerializableExtra("contact");

        title.setText(contact.getContactFirstName());

        List<Message> list = db.getMessageConversation(contact.getContactNumber());
        this.messageList.addAll(list);
        this.listViewAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, messageList);
        listView.setAdapter(this.listViewAdapter);
        registerForContextMenu(listView);

        buttonSend.setOnClickListener(v -> askPermissionAndSendSMS());

        buttonBack.setOnClickListener(v -> buttonCancelClicked());

        String number = contact.getContactNumber();
        // Refresh every seconde
        Thread thread = new Thread() {
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(() -> refreshList(number));
                    }
                } catch (InterruptedException ignored) {
                }
            }
        };
        thread.start();
    }

    private void askPermissionAndSendSMS() {
        // With Android Level >= 23, you have to ask the user
        // for permission to send SMS.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int sendSmsPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

            if (sendSmsPermission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{ Manifest.permission.SEND_SMS }, MY_PERMISSION_REQUEST_CODE_SEND_SMS
                );
                return ;
            }
        }
        this.sendSMS_by_smsManager();
    }

    private void sendSMS_by_smsManager() {
        String phoneNumber = contact.getContactNumber();
        String message = this.editTextMessage.getText().toString();

        if (message.equals(""))
            return ;
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber,
                    null,
                    message,
                    null,
                    null);
            Log.i( LOG_TAG, "Your sms has successfully sent!");
            Toast.makeText(getApplicationContext(), R.string.sms_sent, Toast.LENGTH_LONG).show();
            addMessageOnDb(message, 1);
            editTextMessage.getText().clear();
        } catch (Exception e) {
            Log.e( LOG_TAG,"Sms has failed...", e);
            Toast.makeText(getApplicationContext(), R.string.sms_failed + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void addMessageOnDb(String content, int owner) {
        Message message = new Message(contact.getContactNumber(), content, owner);

        MessageHelper db = new MessageHelper(this);
        db.addMessage(message);

        refreshList(contact.getContactNumber());
    }

    private void refreshList(String contactNumber) {
        MessageHelper db = new MessageHelper(this);

        this.messageList.clear();
        List<Message> list = db.getMessageConversation(contactNumber);
        this.messageList.addAll(list);

        // Notify the data change (To refresh the ListView).
        this.listViewAdapter.notifyDataSetChanged();
    }

    public void buttonCancelClicked() {
        this.onBackPressed();
    }
}