package com.example.notes.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    EditText email, password;
    TextView signUp, forgetPass;
    FirebaseAuth auth;
    FloatingActionButton signIn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        signUp = findViewById(R.id.signUp);
        signIn = findViewById(R.id.signIn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        forgetPass = findViewById(R.id.forgetPass);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();

        progressBar.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
        signUp.setPaintFlags(signUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        forgetPass.setPaintFlags(forgetPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetActivity.class));
            }
        });
        signIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validate(email.getText().toString(), password.getText().toString())) {

                            v.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    if (Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified()) {
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    } else {
                                        Toast.makeText(SignInActivity.this, "verify your Email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    v.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(SignInActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
        signUp.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));

    }

    private boolean validate(String email, String pass) {
        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Field can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null && auth.getCurrentUser().isEmailVerified()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
}