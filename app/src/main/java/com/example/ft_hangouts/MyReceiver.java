package com.example.ft_hangouts;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.example.ft_hangouts.helper.InformationEntryHelper;
import com.example.ft_hangouts.helper.MessageHelper;
import com.example.ft_hangouts.model.Contact;
import com.example.ft_hangouts.model.Message;

public class MyReceiver extends BroadcastReceiver {
    private static final String SMS = "android.provider.Telephony.SMS_RECEIVED";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // get sms objects
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus.length == 0) {
                    return;
                }
                // large message might be broken into many
                SmsMessage[] messages = new SmsMessage[pdus.length];
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    sb.append(messages[i].getMessageBody());
                }
                String sender = messages[0].getOriginatingAddress();
                String message = sb.toString();
                addMessageInTheDb(sender, message, context);
            }
        }
    }

    private void addMessageInTheDb(String sender, String content, Context context) {
        Message message = new Message(sender.trim(), content, 0);

        MessageHelper db_message = new MessageHelper(context);
        db_message.addMessage(message);
        createContactIfNew(sender.trim(), context);
    }

    private void createContactIfNew(String sender, Context context) {
        InformationEntryHelper db = new InformationEntryHelper(context);
        Contact contact = db.getContactByNumber(sender);

        if (contact == null) {
            Contact newContact = new Contact(sender, "", sender, "", "", "");
            db.addContact(newContact);
        }
    }
}
