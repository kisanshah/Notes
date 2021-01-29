package com.example.notes.activity.views;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.notes.R;
import com.example.notes.activity.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "DEBUG";
    EditText email, password;
    TextView signUp, forgetPass;
    FloatingActionButton signIn;
    NoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        viewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())
                .create(NoteViewModel.class);

        signUp = findViewById(R.id.signUp);
        signIn = findViewById(R.id.signIn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        forgetPass = findViewById(R.id.forgetPass);

        signUp.setPaintFlags(signUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        forgetPass.setPaintFlags(forgetPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        viewModel.getLiveUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                Log.d(TAG, "onChanged: " + firebaseUser.isEmailVerified());
                if (firebaseUser != null && firebaseUser.isEmailVerified()) {
                    goToMainActivity();
                }
            }
        });


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
                            viewModel.signIn(email.getText().toString(), password.getText().toString());
                        }
                    }
                });
        signUp.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));

    }

    private void goToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
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
//        if (auth.getCurrentUser() != null && auth.getCurrentUser().isEmailVerified()) {
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            finish();
//        }
    }
}