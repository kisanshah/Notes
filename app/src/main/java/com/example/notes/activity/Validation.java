package com.example.notes.activity;

public class Validation {

    public boolean checkPassword(String p1, String p2) {
        if (p1.length() < 8 && p2.length() < 8) {
            return false;
        }
        if (!p1.equals(p2)) {
            return false;
        } else {
            return true;
        }
    }
}
