package com.example.notes.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    EditText email, password, password2;
    TextInputLayout passLayout, pass2Layout;
    FloatingActionButton signUp;
    TextView signIn;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passLayout = findViewById(R.id.passLayout);
        pass2Layout = findViewById(R.id.pass2Layout);
        password2 = findViewById(R.id.password2);
        signUp = findViewById(R.id.signUp);
        signIn = findViewById(R.id.signIn);

        signIn.setPaintFlags(signIn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(email.getText().toString(), password.getText().toString(), password2.getText().toString())) {
                    auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            Objects.requireNonNull(auth.getCurrentUser()).sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(SignUpActivity.this, "Verification Email has been Sent!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUpActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
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