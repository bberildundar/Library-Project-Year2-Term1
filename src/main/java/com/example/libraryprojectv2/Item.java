package com.example.libraryprojectv2;

import java.io.Serializable;
import java.time.LocalDate;

public class Item implements Serializable {

    private int itemId;
    private String title;
    private String author;
    private LocalDate lendingDate;
    private Availability availability;
    private String lentMemberName;

    public int getItemId(){
        return itemId;
    }
    public String getTitle(){
        return title;
    }
    public String getAuthor(){
        return author;
    }
    public Availability getAvailability(){
        return availability;
    }

    public LocalDate getLendingDate() {
        return lendingDate;
    }

    public String getLentMemberName() {
        return lentMemberName;
    }

    public void setItemId(int id) {
        this.itemId = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public void setLendingDate(LocalDate lendingDate) {
        this.lendingDate = lendingDate;
    }

    public void setLentMember(String lentMemberName) {
        this.lentMemberName = lentMemberName;
    }

    public Item(int id, Availability availability, String title, String author)
    {
        this.itemId = id;
        this.title = title;
        this.author = author;
        this.availability = availability;
    }

    public Item(String title, String author)
    {
        this.title = title;
        this.author = author;
    }

    public Item(int id, Availability availability, String title, String author, String member)
    {
        this.itemId = id;
        this.title = title;
        this.author = author;
        this.availability = availability;
        this.lentMemberName = member;
    }
}

