package com.fiuba.campus2015.adapter;


import com.fiuba.campus2015.dto.user.Personal;

public class ContactItem implements Comparable <ContactItem> {
    private String name;
    public String _id;
    public Personal personal;
    private char title;
    private boolean mark;

    public ContactItem(String name, String photo) {
        this.name = name;
      //  this.photo = photo;

        title = Character.toUpperCase(name.charAt(0));
    }

   // public String getPhoto() {return photo;}
    public String getName() {return name;}

    @Override
    public int compareTo(ContactItem another) {
        if(title > another.title) {
            return 1;
        } else if (another.title > title){
            return -1;
        }
        return 0;
    }
    public char getTitle() { return title;}
    public void setMark(boolean show) { this.mark = show; }
    public boolean getMark() { return mark;}
}
