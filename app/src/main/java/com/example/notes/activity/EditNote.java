package com.example.notes.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notes.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EditNote extends AppCompatActivity {
    MaterialToolbar toolbar;
    TextInputEditText editTitle, editNote;
    FloatingActionButton editSubmit;
    FirebaseAuth auth;
    DatabaseReference ref;
    RelativeLayout container;
    MaterialTextView dateTime, charNumber;

    int color;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        editTitle = findViewById(R.id.editTitle);
        editNote = findViewById(R.id.editNote);
        editSubmit = findViewById(R.id.editSubmit);
        toolbar = findViewById(R.id.toolbar);
        container = findViewById(R.id.container);
        dateTime = findViewById(R.id.dateTime);
        charNumber = findViewById(R.id.charNumber);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        Bundle i = getIntent().getExtras();
        key = (String) i.get("key");
        editNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                charNumber.setText(count + " characters");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                charNumber.setText(s.length() + " characters");
            }
        });
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(auth.getUid()));

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String note = Objects.requireNonNull(snapshot.child(key).child("note").getValue()).toString();
                String title = Objects.requireNonNull(snapshot.child(key).child("title").getValue()).toString();
                color = Integer.parseInt(Objects.requireNonNull(snapshot.child(key).child("color").getValue()).toString());
                String date = Objects.requireNonNull(snapshot.child(key).child("date").getValue()).toString();
                String time = Objects.requireNonNull(snapshot.child(key).child("time").getValue()).toString();
                dateTime.setText(date + " " + time);
                editNote.setText(note);
                editTitle.setText(title);
                container.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
                getWindow().setNavigationBarColor(color);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editSubmit.setOnClickListener(v -> saveNote());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_note, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveNote();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                Snackbar.make(editTitle, "Delete note?", Snackbar.LENGTH_LONG).setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ref.child(key).removeValue();
                        Toast.makeText(EditNote.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).show();
                return true;
            case R.id.color:
                showDialog();
                return true;
            default:
                return false;
        }
    }

    void saveNote() {
        ref.child(key).child("title").setValue(Objects.requireNonNull(editTitle.getText()).toString());
        ref.child(key).child("note").setValue(Objects.requireNonNull(editNote.getText()).toString());
        ref.child(key).child("titleLowerCase").setValue(editTitle.getText().toString().toLowerCase());
        ref.child(key).child("color").setValue(String.valueOf(color));
        Toast.makeText(EditNote.this, "Edited Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.color_picker, viewGroup, false);
        ChipGroup group = view.findViewById(R.id.chip_group);
        builder.setView(view);
        builder.setTitle("Choose color");
        builder.setPositiveButton("Ok", (dialog, which) -> {
            Chip chip = group.findViewById(group.getCheckedChipId());
            color = Objects.requireNonNull(chip.getChipBackgroundColor()).getDefaultColor();
            container.setBackgroundColor(chip.getChipBackgroundColor().getDefaultColor());
            getWindow().setNavigationBarColor(color);
            getWindow().setStatusBarColor(color);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            container.setBackgroundColor(color);
            getWindow().setNavigationBarColor(color);
            getWindow().setStatusBarColor(color);
        });
        group.setOnCheckedChangeListener((group1, checkedId) -> {
            Chip chip = group1.findViewById(checkedId);
            if (chip != null) {
                getWindow().setNavigationBarColor(Objects.requireNonNull(chip.getChipBackgroundColor()).getDefaultColor());
                getWindow().setStatusBarColor(chip.getChipBackgroundColor().getDefaultColor());
                container.setBackgroundColor(chip.getChipBackgroundColor().getDefaultColor());
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}