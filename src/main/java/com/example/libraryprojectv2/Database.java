package com.example.libraryprojectv2;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Database {

    public List<User> users = new ArrayList<>();
    public List<Member> members =  new ArrayList<>();
    public List<Item> items =  new ArrayList<>();
    public List<Item> unavailableItems = new ArrayList<>();

    public List<Member> getMembers(){ return members; }
    public List<Item> getItems(){ return items; }

    public List<Item> getFilteredItems(){
        for (Item i: items){
            if(i.getAvailability() == Availability.No){
                unavailableItems.add(i);
            }
        }
        return unavailableItems;
    }

    //ctor
    public Database() throws IOException {
        setUsers(); // users aren't read or written from a file that's why this method is setting the users once the program starts.
        readItems(); // but members and items are saved and read from a file.
        readMembers();
    }

    //this method is used for items when the file is not found/ or it is empty.
    public void setMembers()
    {
        addMember("Captain", "America", LocalDate.of(1990, 9, 1) );
        addMember("Iron", "Man", LocalDate.of(1990, 10, 1) );
        addMember("Black", "Swan",  LocalDate.of(1990, 11, 1) );

    }

    //this method is used for items when the file is not found/ or it is empty.
    public void setItems()
    {
        addItem("Java for Beginners", "Adam Black" );
        addItem("C# for Beginners", "Adam Black" );
        addItem("PHP for Beginners", "Scarlet Lawrence" );
    }

    public void setUsers()
    {
        users.add(new User("Betül Beril", "Dündar","user1", "123"));
        users.add(new User("John", "White","user2", "456"));
    }

    public void addMember(String firstName, String lastName, LocalDate dateOfBirth) {
        members.add(new Member(getNewMemberID(), firstName, lastName, dateOfBirth));
    }
    public void addItem(String title, String author) {
        items.add(new Item(getNewItemID(), Availability.Yes, title, author));
    }

    public void readItems() throws IOException {
        try (FileInputStream fis = new FileInputStream(new File("items.dat"));
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            while (true) {
                try {
                    Item item = (Item) ois.readObject();
                    items.add(item);
                } catch (EOFException eofException) {
                    break;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        catch(IOException e){
            //if file isn't found or if it is empty, it is setting default items instead.
            setItems();
        }
    }

    public void readMembers() throws IOException {
        try (FileInputStream fis = new FileInputStream(new File("members.dat"));
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            while (true) {
                try {
                    Member member = (Member) ois.readObject();
                    members.add(member);
                } catch (EOFException eofException) {
                    break;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        catch(IOException e){
            //if file isn't found or if it is empty, it is setting default members instead.
            setMembers();
        }
    }

    public void writeItems() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(new File("items.dat"));
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            for (Item item : items) {
                oos.writeObject(item);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeMembers() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(new File("members.dat"));
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            for (Member member : members) {
                oos.writeObject(member);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    ////////////////////////

    public int getNewMemberID(){
        int id = 0;
        for (Member m : members) {
            if(m.getId() > id)
                id = m.getId();
        }
        return id + 1;
    }

    public int getNewItemID(){
        int id = 0;
        for (Item i : items) {
            if(i.getItemId() > id)
                id = i.getItemId();
        }
        return id + 1;
    }

    public Member getMemberById(int id) {
        for (Member m : members) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    public User getUserByUsername(String username){
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }
}
