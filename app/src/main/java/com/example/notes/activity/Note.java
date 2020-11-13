package com.example.notes.activity;

public class Note {
    String title;
    String note;
    String color;
    String titleLowerCase;
    String date;
    String time;

    public Note() {
    }

    public String getDate() {
        return date;
    }

    public Note(String title, String note, String color) {
        this.title = title;
        this.note = note;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

}
