package com.example.notes.activity.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.notes.activity.model.Note;
import com.example.notes.activity.repository.AuthRepository;
import com.example.notes.activity.repository.NoteRepository;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class NoteViewModel extends AndroidViewModel {
    AuthRepository authRepository;
    NoteRepository noteRepository;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
        noteRepository = new NoteRepository(application);
    }

    public void searchQuery(String s) {
        noteRepository.searchQuery(s);
    }

    public void addNote(Note note) {
        noteRepository.addNote(note);
    }

    public void updateNote(String key, Map<String, Object> note) {
        noteRepository.updateNote(key, note);
    }

    public void deleteNote(String key) {
        noteRepository.deleteNote(key);
    }

    public MutableLiveData<Note> getNote(String key) {
        return noteRepository.getNote(key);
    }

    public FirebaseRecyclerOptions<Note> getOptions() {
        return noteRepository.getOptions();
    }

    public MutableLiveData<FirebaseUser> getLiveUser() {
        return authRepository.getUserLiveData();
    }

    public MutableLiveData<Boolean> userSignOutState() {
        return authRepository.getUserSignOutState();
    }

    public MutableLiveData<Boolean> userSignUpState() {
        return authRepository.getUserSignUpState();
    }

    public MutableLiveData<Boolean> userResetState() {
        return authRepository.getUserResetState();
    }

    public void signIn(String email, String pass) {
        authRepository.signIn(email, pass);
    }

    public void signUp(String email, String pass) {
        authRepository.signUp(email, pass);
    }

    public void signOut() {
        authRepository.signOut();
    }

    public void reset(String email) {
        authRepository.reset(email);
    }
}
