package com.example.notes.activity.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Note implements Serializable {


    String title;
    String note;
    String color;
    String titleLowerCase;
    String date;
    String time;

    public Note() {
    }


    public Note(String title, String note, String color, String titleLowerCase, String date, String time) {
        this.title = title;
        this.note = note;
        this.color = color;
        this.titleLowerCase = titleLowerCase;
        this.date = date;
        this.time = time;
    }

    public Note(String title, String note, String color, String titleLowerCase) {
        this.title = title;
        this.note = note;
        this.color = color;
        this.titleLowerCase = titleLowerCase;
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

    public String getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return "Note{\n" +
                "title='" + title + '\'' +
                ", note='" + note + '\'' +
                ", color='" + color + '\'' +
                ", titleLowerCase='" + titleLowerCase + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
