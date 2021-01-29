package com.example.notes.activity.repository;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.notes.activity.views.ForgetActivity;
import com.example.notes.activity.views.SignInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {
    private final Application application;
    private final FirebaseAuth auth;
    private final MutableLiveData<FirebaseUser> userLiveData;
    private final MutableLiveData<Boolean> userSignOutState;
    private final MutableLiveData<Boolean> userSignUpState;
    private final MutableLiveData<Boolean> userResetState;

    public AuthRepository(Application application) {
        this.application = application;
        this.auth = FirebaseAuth.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.userSignOutState = new MutableLiveData<>();
        this.userSignUpState = new MutableLiveData<>();
        this.userResetState = new MutableLiveData<>();
        if (auth.getCurrentUser() != null) {
            userLiveData.setValue(auth.getCurrentUser());
            userSignOutState.setValue(false);
        }
    }

    public MutableLiveData<Boolean> getUserResetState() {
        return userResetState;
    }

    public MutableLiveData<Boolean> getUserSignUpState() {
        return userSignUpState;
    }

    public MutableLiveData<Boolean> getUserSignOutState() {
        return userSignOutState;
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public void reset(String email) {
        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(application, "Reset email has ben sent!", Toast.LENGTH_LONG).show();
                userResetState.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void signOut() {
        auth.signOut();
        userSignOutState.postValue(true);
    }

    public void signIn(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (auth.getCurrentUser().isEmailVerified()) {
                                userLiveData.setValue(auth.getCurrentUser());
                            } else {
                                Toast.makeText(application, "Email not verified", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(application, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signUp(String email, String pass) {
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(application, "Registration Successful", Toast.LENGTH_SHORT).show();
                            auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(application, "Verification email has been sent!", Toast.LENGTH_LONG).show();
                                        userSignUpState.setValue(true);
                                    } else {
                                        Toast.makeText(application, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(application, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
