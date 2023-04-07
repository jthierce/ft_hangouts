package com.example.ft_hangouts.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Contact implements Serializable {

    private int contactId;
    private String contactFirstName;
    private String contactLastName;
    private String contactNumber;
    private String contactEmail;
    private String contactEntreprise;
    private String contactNote;

    public Contact()  {

    }

    public Contact(int contactId, String contactFirstName, String contactLastName, String contactNumber, String contactEmail, String contactEntreprise, String contactNote) {
        this.contactId = contactId;
        this.contactFirstName = contactFirstName;
        this.contactLastName = contactLastName;
        this.contactNumber = contactNumber;
        this.contactEmail = contactEmail;
        this.contactEntreprise = contactEntreprise;
        this.contactNote = contactNote;
    }

    public Contact(String contactFirstName, String contactLastName, String contactNumber, String contactEmail, String contactEntreprise, String contactNote) {
        this.contactFirstName = contactFirstName;
        this.contactLastName = contactLastName;
        this.contactNumber = contactNumber;
        this.contactEmail = contactEmail;
        this.contactEntreprise = contactEntreprise;
        this.contactNote = contactNote;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }


    public String getContactFirstName() {
        return contactFirstName;
    }

    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }


    public String getContactLastName() {
        return contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactEmail() { return contactEmail; }

    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getContactEntreprise() { return contactEntreprise; }

    public void setContactEntreprise(String contactEntreprise) { this.contactEntreprise = contactEntreprise; }

    public String getContactNote() { return contactNote; }

    public void setContactNote(String contactNote) { this.contactNote = contactNote; }

    @NonNull
    @Override
    public String toString()  {
        return this.contactFirstName + " " + this.contactLastName + "\n" + this.contactNumber ;
    }
}
