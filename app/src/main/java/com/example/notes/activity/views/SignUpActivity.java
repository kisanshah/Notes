package com.example.notes.activity.views;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
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
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {

    EditText email, password, password2;
    TextInputLayout passLayout, pass2Layout;
    FloatingActionButton signUp;
    TextView signIn;
    NoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passLayout = findViewById(R.id.passLayout);
        pass2Layout = findViewById(R.id.pass2Layout);
        password2 = findViewById(R.id.password2);
        signUp = findViewById(R.id.signUp);
        signIn = findViewById(R.id.signIn);

        viewModel = new ViewModelProvider
                .AndroidViewModelFactory(getApplication())
                .create(NoteViewModel.class);

        signIn.setPaintFlags(signIn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
        });

        viewModel.userSignUpState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean signedUp) {
                if (signedUp) {
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(email.getText().toString(), password.getText().toString(), password2.getText().toString())) {
                    viewModel.signUp(email.getText().toString(), password.getText().toString());
                }
            }
        });


    }

    private boolean validate(String email, String p1, String p2) {
        if (email.isEmpty() || p1.isEmpty() || p2.isEmpty()) {
            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_LONG).show();
        } else if (!p2.equals(p1)) {
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_LONG).show();
        } else if (p2.length() < 8 || p1.length() < 8) {
            Toast.makeText(this, "Password is too short", Toast.LENGTH_LONG).show();
        } else {
            return true;
        }
        return false;
    }
}