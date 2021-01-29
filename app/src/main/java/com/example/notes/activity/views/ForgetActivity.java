package com.example.notes.activity.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.notes.R;
import com.example.notes.activity.viewmodel.NoteViewModel;

import java.util.Objects;

public class ForgetActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText email;
    Button reset;
    NoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        toolbar = findViewById(R.id.toolbar);
        email = findViewById(R.id.email);
        reset = findViewById(R.id.reset);

        viewModel = new ViewModelProvider
                .AndroidViewModelFactory(getApplication())
                .create(NoteViewModel.class);

        viewModel.userResetState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean reset) {
                if (reset) {
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                }
            }
        });

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty()) {
                    Toast.makeText(ForgetActivity.this, "Field Cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.reset(email.getText().toString());
                }
            }
        });
    }
}