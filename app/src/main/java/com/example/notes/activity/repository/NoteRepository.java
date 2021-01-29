package com.example.notes.activity.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.notes.activity.model.Note;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class NoteRepository {
    DatabaseReference db;
    DatabaseReference childRef;
    FirebaseAuth auth;
    Application application;
    Query query;
    FirebaseRecyclerOptions<Note> options;
    MutableLiveData<Note> noteMutableLiveData;

    public NoteRepository(Application application) {
        this.auth = FirebaseAuth.getInstance();
        this.application = application;
        this.noteMutableLiveData = new MutableLiveData<>();
        if (auth.getCurrentUser() != null) {
            this.db = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid());
            this.query = db.orderByValue();
            this.options = new FirebaseRecyclerOptions.Builder<Note>().setQuery(query, Note.class).build();
            childRef = db.push();
        }
    }

    public FirebaseRecyclerOptions<Note> getOptions() {
        return options;
    }

    public void addNote(Note note) {
        childRef.setValue(note);
    }

    public void updateNote(String key, Map<String, Object> note) {
        db.child(key).updateChildren(note);
    }

    public void deleteNote(String key) {
        db.child(key).removeValue();
    }

    public MutableLiveData<Note> getNote(String key) {
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> map = (Map<String, String>) snapshot.child(key).getValue();
                ObjectMapper mapper = new ObjectMapper();
                Note note = mapper.convertValue(map, Note.class);
                Log.d("DEBUG", "onDataChange: " + note.toString());
                noteMutableLiveData.setValue(note);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return noteMutableLiveData;
    }

    public void searchQuery(String s) {
        this.query = db.orderByChild("titleLowerCase").startAt(s).endAt(s + "\uf8ff");
        this.options = new FirebaseRecyclerOptions.Builder<Note>().setQuery(query, Note.class).build();
    }


}
