package com.example.ft_hangouts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.ft_hangouts.controller.AddContactActivity;
import com.example.ft_hangouts.controller.ConversationActivity;
import com.example.ft_hangouts.helper.InformationEntryHelper;
import com.example.ft_hangouts.model.Contact;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private static final String TAG = "MainActivity";

    private static final int MENU_ITEM_INSPECT = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_DELETE = 444;
    private static final int MY_REQUEST_CODE = 1000;

    private final List<Contact> contactList = new ArrayList<>();
    private ArrayAdapter<Contact> listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Delete the header title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);

        checkForSmsPermission();

        this.listView = findViewById(R.id.listView);

        InformationEntryHelper db = new InformationEntryHelper(this);
        Log.i(TAG, "La db a "  + db.getContactsCount() + "content.");

        List<Contact> list = db.getAllContacts();
        this.contactList.addAll(list);

        // Define a new Adapter
        this.listViewAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, contactList);

        // Assign adapter to ListView
        this.listView.setAdapter(this.listViewAdapter);

        // Register the ListView for Context menu
        registerForContextMenu(this.listView);

        listView.setOnItemClickListener((adapterView, view, i, l) -> goToTheConversation(listViewAdapter.getItem(i)));

        // Refresh every second
        Thread thread = new Thread() {
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(() -> refreshListView());
                    }
                } catch (InterruptedException ignored) {
                }
            }
        };
        thread.start();
    }

    private void checkForSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1000);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 1001);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 1002);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 1001) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void refreshListView() {
        this.contactList.clear();
        InformationEntryHelper db = new InformationEntryHelper(this);
        List<Contact> list = db.getAllContacts();
        this.contactList.addAll(list);

        // Notify the data change (To refresh the ListView).
        this.listViewAdapter.notifyDataSetChanged();
    }

    // Create the menu when we holding button
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)    {
        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle(R.string.select_action);

        menu.add(0, MENU_ITEM_INSPECT, 2, R.string.inspect_contact);
        menu.add(0, MENU_ITEM_EDIT , 4, R.string.edit_contact);
        menu.add(0, MENU_ITEM_DELETE, 6, R.string.delete_contact);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Contact selectedContact = (Contact) this.listView.getItemAtPosition(info.position);

        if(item.getItemId() == MENU_ITEM_EDIT ){
            Intent intent = new Intent(this, AddContactActivity.class);
            intent.putExtra("contact", selectedContact);

            // Start AddEditNoteActivity, (with feedback).
            this.startActivityForResult(intent, MY_REQUEST_CODE);
        }
        else if(item.getItemId() == MENU_ITEM_DELETE){
            // Ask before deleting.
            new AlertDialog.Builder(this)
                    .setMessage(selectedContact.getContactFirstName() + R.string.confirmed_delete_contact)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, (dialog, id) -> deleteContact(selectedContact))
                    .setNegativeButton(R.string.no, null)
                    .show();
        }
        else if(item.getItemId() == MENU_ITEM_INSPECT){
            Intent intent = new Intent(this, AddContactActivity.class);
            intent.putExtra("contact", selectedContact);
            intent.putExtra("inspect", true);

            // Start AddEditNoteActivity, (with feedback).
            this.startActivityForResult(intent, MY_REQUEST_CODE);
        }
        else {
            return false;
        }
        return true;
    }

    // Delete a record
    private void deleteContact(Contact contact)  {
        InformationEntryHelper db = new InformationEntryHelper(this);
        db.deleteContact(contact);
        this.contactList.remove(contact);
        // Refresh ListView.
        this.listViewAdapter.notifyDataSetChanged();
    }

    // Add a record
    public void addContact(View view)
    {
        Intent intent = new Intent(this, AddContactActivity.class);
        this.startActivityForResult(intent, MY_REQUEST_CODE);
    }

    //Go to the conversation of the chosen people
    public void goToTheConversation(Contact item)
    {
        Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra("contact", item);
        this.startActivityForResult(intent, MY_REQUEST_CODE);
    }

    // When AddEditNoteActivity completed, it sends feedback.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == MY_REQUEST_CODE) {
            boolean needRefresh = data.getBooleanExtra("needRefresh", true);
            // Refresh ListView
            if (needRefresh) {
                refreshListView();
            }
        }
    }

    public void changeTheme(View view)
    {
        Utils.changeToTheme(MainActivity.this);
    }
}
