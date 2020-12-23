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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitleLowerCase() {
        return titleLowerCase;
    }

    public void setTitleLowerCase(String titleLowerCase) {
        this.titleLowerCase = titleLowerCase;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
