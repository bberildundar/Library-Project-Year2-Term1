package com.example.libraryprojectv2;

import java.io.Serializable;
import java.time.LocalDate;

public class Member implements Serializable {

    private int id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    //ctor
    public Member(int id, String firstName, String lastName, LocalDate dateOfBirth) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public Member(String firstName, String lastName, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public int getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {return lastName;}
    public String getFullName() {
        return firstName + " " + lastName;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}

