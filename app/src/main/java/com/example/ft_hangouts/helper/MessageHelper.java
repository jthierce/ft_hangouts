package com.example.ft_hangouts.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ft_hangouts.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Message_Manager";

    // Table name: Message.
    private static final String TABLE_MESSAGE = "Message";

    private static final String COLUMN_MESSAGE_ID = "Message_Id";
    private static final String COLUMN_CONTACT_NUMBER = "Contact_Number";
    private static final String COLUMN_MESSAGE_CONTENT = "Message_Content";
    private static final String COLUMN_MESSAGE_OWNER = "Message_owner";

    public MessageHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MessageHelper.onCreate ... ");
        // Script.
        String query = "CREATE TABLE " + TABLE_MESSAGE + "("
                + COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_CONTACT_NUMBER + " TEXT,"
                + COLUMN_MESSAGE_CONTENT + " TEXT,"
                + COLUMN_MESSAGE_OWNER + " TEXT" + ")";
        // Execute query.
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG, "MessageHelper.onUpgrade ... ");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);

        // Create tables again
        onCreate(db);
    }

    public void addMessage(Message message) {
        Log.i(TAG, "MessageHelper.MESSAGE ... " + message.getMessageContent());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_NUMBER, message.getContactNumber());
        values.put(COLUMN_MESSAGE_CONTENT, message.getMessageContent());
        values.put(COLUMN_MESSAGE_OWNER, message.getMessageOwner());
        // values.put(COLUMN_MESSAGE_TIME, format(message.getMessageTime()));

        // Inserting Row
        db.insert(TABLE_MESSAGE, null, values);

        // Closing database connection
        db.close();
    }

    public List<Message> getMessageConversation(String contactNumber) {
        Log.i(TAG, "MyDatabaseHelper.getMessageConversation ... " );

        List<Message> messageList = new ArrayList<>();
        // Select all message with the good contact number
        String selectQuery = "SELECT  * FROM " + TABLE_MESSAGE + " WHERE " + COLUMN_CONTACT_NUMBER + "= \"" + contactNumber + '"';

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Message message = new Message(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
                // Adding message to list
                messageList.add(message);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // return note list
        return messageList;
    }

    public int getMessageConversationCount(String contactNumber) {
        Log.i(TAG, "MyDatabaseHelper.getNotesCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_MESSAGE + " WHERE " + COLUMN_CONTACT_NUMBER + "= \"" + contactNumber + '"';
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }
}
