package com.example.ft_hangouts.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ft_hangouts.R;
import com.example.ft_hangouts.Utils;
import com.example.ft_hangouts.helper.InformationEntryHelper;
import com.example.ft_hangouts.model.Contact;

import java.util.Objects;

public class AddContactActivity extends AppCompatActivity {
    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;
    private static final int MODE_INSPECT = 3;

    private EditText textFirstname;
    private EditText textLastname;
    private EditText textNumber;
    private EditText textEmail;
    private EditText textEntreprise;
    private EditText textNote;

    private Contact contact;
    private boolean needRefresh;
    private boolean inspect;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Delete the header title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_add_contact);

        TextView title = this.findViewById(R.id.textViewTitle);

        this.textFirstname = this.findViewById(R.id.editText_contact_first_name);
        this.textLastname = this.findViewById(R.id.editText_contact_last_name);
        this.textNumber = this.findViewById(R.id.editText_contact_number);
        this.textEmail = this.findViewById(R.id.editText_contact_email);
        this.textEntreprise = this.findViewById(R.id.editText_contact_entreprise);
        this.textNote = this.findViewById(R.id.editText_contact_note);

        Button buttonSave = findViewById(R.id.button_save);
        Button buttonCancel = findViewById(R.id.button_cancel);
        ImageButton buttonBack = findViewById(R.id.button_back);

        buttonSave.setOnClickListener(v -> buttonSaveClicked());

        buttonCancel.setOnClickListener(v -> buttonCancelClicked());

        buttonBack.setOnClickListener(v -> buttonCancelClicked());

        Intent intent = this.getIntent();
        this.contact = (Contact) intent.getSerializableExtra("contact");
        if(contact == null)  {
            this.mode = MODE_CREATE;
        } else  {
            this.mode = MODE_EDIT;
            this.textFirstname.setText(contact.getContactFirstName());
            this.textLastname.setText(contact.getContactLastName());
            this.textNumber.setText(contact.getContactNumber());
            this.textEmail.setText(contact.getContactEmail());
            this.textEntreprise.setText(contact.getContactEntreprise());
            this.textNote.setText(contact.getContactNote());
            title.setText(contact.getContactFirstName());

            this.inspect = (boolean) intent.getSerializableExtra("inspect");
            if (inspect) {
                this.textFirstname.setFocusable(false);
                this.textLastname.setFocusable(false);
                this.textNumber.setFocusable(false);
                this.textEmail.setFocusable(false);
                this.textEntreprise.setFocusable(false);
                this.textNote.setFocusable(false);
                buttonSave.setVisibility(View.GONE);
                buttonCancel.setVisibility(View.GONE);
            }
        }
    }

    // User Click on the Save button.
    public void buttonSaveClicked()  {
        InformationEntryHelper db = new InformationEntryHelper(this);

        String firstname = this.textFirstname.getText().toString();
        String lastname = this.textLastname.getText().toString();
        String number = this.textNumber.getText().toString().trim();
        String email = this.textEmail.getText().toString();
        String entreprise = this.textEntreprise.getText().toString();
        String note = this.textNote.getText().toString();

        if((firstname.equals("") && lastname.equals("")) || number.equals("")) {
            Toast.makeText(getApplicationContext(), R.string.misssing_information_contact, Toast.LENGTH_LONG).show();
            return;
        }
        if(mode == MODE_CREATE ) {
            this.contact = new Contact(firstname, lastname, number, email, entreprise, note);
            db.addContact(contact);
        } else  {
            this.contact.setContactFirstName(firstname);
            this.contact.setContactLastName(lastname);
            this.contact.setContactNumber(number);
            this.contact.setContactEmail(email);
            this.contact.setContactEntreprise(entreprise);
            this.contact.setContactNote(note);
            db.updateContact(contact);
        }

        this.needRefresh = true;

        // Back to MainActivity.
        this.onBackPressed();
    }

    // User click on the cancel button
    public void buttonCancelClicked() {
        this.onBackPressed();
    }

    // When completed this Activity,
    @Override
    public void finish() {

        // Create Intent
        Intent data = new Intent();
        data.putExtra("needRefresh", needRefresh);

        // Set Result
        this.setResult(Activity.RESULT_OK, data);
        super.finish();
    }
}