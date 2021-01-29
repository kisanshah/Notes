package com.example.notes.activity.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.notes.R;
import com.example.notes.activity.adapter.NoteAdapter;
import com.example.notes.activity.viewmodel.NoteViewModel;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NoteAdapter adapter;
    FloatingActionButton add;
    BottomAppBar bottomAppBar;
    NoteViewModel viewModel;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider
                .AndroidViewModelFactory(getApplication())
                .create(NoteViewModel.class);

        text = findViewById(R.id.text);
        recyclerView = findViewById(R.id.recyclerview);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        add = findViewById(R.id.add);
        setSupportActionBar(bottomAppBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);

        viewModel.userSignOutState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                if (bool) {
                    goToSignIn();
                }
            }
        });

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        adapter = new NoteAdapter(viewModel.getOptions());
        recyclerView.setAdapter(adapter);

        add.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AddNote.class)));
    }

    private void goToSignIn() {
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    private void searchQuery(String s) {
        viewModel.searchQuery(s);
        adapter.updateOptions(viewModel.getOptions());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();


        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        add.hide();
                        text.setVisibility(View.GONE);
                    }
                }, 300);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        add.show();
                        text.setVisibility(View.VISIBLE);
                    }
                }, 300);
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.signOut) {
            viewModel.signOut();
            return true;
        }
        if (item.getItemId() == R.id.listMode) {
            if (item.getTitle().equals("list")) {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

                item.setTitle("staggered");
                item.setIcon(R.drawable.list_svg);
            } else {
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                item.setIcon(R.drawable.staggered_svg);
                item.setTitle("list");

            }
        }
        return false;
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