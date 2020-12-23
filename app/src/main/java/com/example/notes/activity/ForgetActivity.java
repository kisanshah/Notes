package com.example.notes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.notes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgetActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText email;
    Button reset;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        toolbar = findViewById(R.id.toolbar);
        email = findViewById(R.id.email);
        reset = findViewById(R.id.reset);
        auth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty()) {
                    Toast.makeText(ForgetActivity.this, "Field Cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    auth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ForgetActivity.this, "Reset email has ben sent!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ForgetActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }
}