package com.example.notes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.notes.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NoteAdapter adapter;
    FirebaseAuth auth;
    DatabaseReference db;
    FloatingActionButton add;
    BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        recyclerView = findViewById(R.id.recyclerview);
        add = findViewById(R.id.add);


        setSupportActionBar(bottomAppBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(auth.getUid()));
        Query query = db.orderByValue();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));


        FirebaseRecyclerOptions<Note> options = new FirebaseRecyclerOptions.Builder<Note>().setQuery(query, Note.class).build();
        adapter = new NoteAdapter(options);
        recyclerView.setAdapter(adapter);

        add.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AddNote.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();
        searchView.setOnClickListener(v -> add.setVisibility(View.INVISIBLE));
        searchView.setOnFocusChangeListener((v, hasFocus) -> {

        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.signOut) {
            auth.signOut();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
            return true;
        }
        return false;
    }

    private void firebaseSearch(String s) {
        Query query = db.orderByChild("titleLowerCase").startAt(s).endAt(s + "\uf8ff");
        FirebaseRecyclerOptions<Note> option = new FirebaseRecyclerOptions.Builder<Note>().setQuery(query, Note.class).build();
        adapter.updateOptions(option);
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}