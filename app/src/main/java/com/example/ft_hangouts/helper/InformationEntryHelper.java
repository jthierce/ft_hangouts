package com.example.ft_hangouts.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ft_hangouts.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class InformationEntryHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Contact_Manager";

    // Table name: Contact.
    private static final String TABLE_CONTACT = "Contact";

    private static final String COLUMN_CONTACT_ID ="Contact_Id";
    private static final String COLUMN_CONTACT_FIRST_NAME ="Contact_First_Name";
    private static final String COLUMN_CONTACT_LAST_NAME = "Contact_Last_Name";
    private static final String COLUMN_CONTACT_NUMBER = "Contact_Number";
    private static final String COLUMN_CONTACT_EMAIL = "Contact_Email";
    private static final String COLUMN_CONTACT_ENTREPRISE = "Contact_Entreprise";
    private static final String COLUMN_CONTACT_NOTE = "Contact_Note";

    public InformationEntryHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "InformationEntryHelper.onCreate ... ");
        // Script.
        String query = "CREATE TABLE " + TABLE_CONTACT + "("
                + COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_CONTACT_FIRST_NAME + " TEXT,"
                + COLUMN_CONTACT_LAST_NAME + " TEXT,"
                + COLUMN_CONTACT_NUMBER + " TEXT,"
                + COLUMN_CONTACT_EMAIL + " TEXT,"
                + COLUMN_CONTACT_ENTREPRISE + " TEXT,"
                + COLUMN_CONTACT_NOTE + " TEXT" + ")";
        // Execute query.
        db.execSQL(query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "InformationEntryHelper.onUpgrade ... ");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);

        // Create tables again
        onCreate(db);
    }

    public void addContact(Contact contact) {
        Log.i(TAG, "InformationEntryHelper.addContact ... " + contact.getContactFirstName());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_FIRST_NAME, contact.getContactFirstName());
        values.put(COLUMN_CONTACT_LAST_NAME, contact.getContactLastName());
        values.put(COLUMN_CONTACT_NUMBER, contact.getContactNumber());
        values.put(COLUMN_CONTACT_EMAIL, contact.getContactEmail());
        values.put(COLUMN_CONTACT_ENTREPRISE, contact.getContactEntreprise());
        values.put(COLUMN_CONTACT_NOTE, contact.getContactNote());

        // Inserting Row
        db.insert(TABLE_CONTACT, null, values);

        // Closing database connection
        db.close();
    }


    public Contact getContact(int id) {
        Log.i(TAG, "InformationEntryHelper.getContact ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACT, new String[] { COLUMN_CONTACT_ID,
                        COLUMN_CONTACT_FIRST_NAME, COLUMN_CONTACT_LAST_NAME,
                        COLUMN_CONTACT_NUMBER, COLUMN_CONTACT_EMAIL, COLUMN_CONTACT_ENTREPRISE,
                        COLUMN_CONTACT_NOTE }, COLUMN_CONTACT_ID + "=?",
                        new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        assert cursor != null;
        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                          cursor.getString(1), cursor.getString(2), cursor.getString(3),
                          cursor.getString(4), cursor.getString(5), cursor.getString(6));
        cursor.close();
        // return contact
        return contact;
    }

    public Contact getContactByNumber(String number) {
        Log.i(TAG, "InformationEntryHelper.getContactByNumber ... " + number);
        Contact contact = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACT, new String[] { COLUMN_CONTACT_ID,
                        COLUMN_CONTACT_FIRST_NAME, COLUMN_CONTACT_LAST_NAME,
                        COLUMN_CONTACT_NUMBER, COLUMN_CONTACT_EMAIL, COLUMN_CONTACT_ENTREPRISE,
                        COLUMN_CONTACT_NOTE }, COLUMN_CONTACT_NUMBER + "=?",
                new String[] { number }, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5), cursor.getString(6));
                cursor.close();
            }
        }
        return contact;
    }

    public List<Contact> getAllContacts() {
        Log.i(TAG, "InformationEntryHelper.getAllContacts ... " );

        List<Contact> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // reset the position of cursor
        if (cursor.moveToFirst()) {
            // get all contact
            do {
                Contact contact = new Contact();
                contact.setContactId(Integer.parseInt(cursor.getString(0)));
                contact.setContactFirstName(cursor.getString(1));
                contact.setContactLastName(cursor.getString(2));
                contact.setContactNumber(cursor.getString(3));
                contact.setContactEmail(cursor.getString(4));
                contact.setContactEntreprise(cursor.getString(5));
                contact.setContactNote(cursor.getString(6));
                // Adding Contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // return contact list
        return contactList;
    }

    public int getContactsCount() {
        Log.i(TAG, "InformationEntryHelper.getContactsCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_CONTACT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public int updateContact(Contact contact) {
        Log.i(TAG, "InformationEntryHelper.updateContact ... "  + contact.getContactFirstName());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_FIRST_NAME, contact.getContactFirstName());
        values.put(COLUMN_CONTACT_LAST_NAME, contact.getContactLastName());
        values.put(COLUMN_CONTACT_NUMBER, contact.getContactNumber());
        values.put(COLUMN_CONTACT_EMAIL, contact.getContactEmail());
        values.put(COLUMN_CONTACT_ENTREPRISE, contact.getContactEntreprise());
        values.put(COLUMN_CONTACT_NOTE, contact.getContactNote());

        // updating row
        return db.update(TABLE_CONTACT, values, COLUMN_CONTACT_ID + " = ?",
                new String[]{String.valueOf(contact.getContactId())});
    }

    public void deleteContact(Contact contact) {
        Log.i(TAG, "InformationEntryHelper.updateContact ... " + contact.getContactFirstName() );

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACT, COLUMN_CONTACT_ID + " = ?",
                new String[] { String.valueOf(contact.getContactId()) });
        db.close();
    }

}
