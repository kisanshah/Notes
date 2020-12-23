package com.example.notes.activity;

import android.os.Bundle;
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
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class AddNote extends AppCompatActivity {

    TextInputEditText title, note;
    FloatingActionButton submit;
    FirebaseAuth auth;
    DatabaseReference ref;
    String color;
    RelativeLayout container;
    BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        container = findViewById(R.id.container);
        title = findViewById(R.id.title);
        note = findViewById(R.id.note);
        submit = findViewById(R.id.submit);
        auth = FirebaseAuth.getInstance();

        bottomAppBar = findViewById(R.id.bottomAppBar);

        setSupportActionBar(bottomAppBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        note.requestFocus();
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(auth.getUid()));

        submit.setOnClickListener(v -> {
            Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
            Date currentTime = localCalendar.getTime();
            String date = currentTime.toString().substring(4, 10);
            String year = currentTime.toString().substring(30);
            DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
            String time = dateFormat.format(new Date());
            System.out.println(date + " " + time + "," + year);


            DatabaseReference childRef = ref.push();
            String key = childRef.getKey();
            assert key != null;
            ref.child(key).child("title").setValue(Objects.requireNonNull(title.getText()).toString());
            ref.child(key).child("note").setValue(Objects.requireNonNull(note.getText()).toString());
            ref.child(key).child("titleLowerCase").setValue(title.getText().toString().toLowerCase());
            ref.child(key).child("date").setValue(date + " " + year);
            ref.child(key).child("time").setValue(time);

            if (color != null) {
                ref.child(key).child("color").setValue(color);
            } else {
                ref.child(key).child("color").setValue("-1");
            }
            Toast.makeText(AddNote.this, "Note Added", Toast.LENGTH_SHORT).show();
            finish();
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.color) {
            showDialog();
            return true;
        } else {
            return false;
        }
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
            container.setBackgroundColor(Objects.requireNonNull(chip.getChipBackgroundColor()).getDefaultColor());
            getWindow().setStatusBarColor(chip.getChipBackgroundColor().getDefaultColor());
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> container.setBackgroundColor(-1));
        group.setOnCheckedChangeListener((group1, checkedId) -> {
            Chip chip = group1.findViewById(checkedId);
            if (chip != null) {
                container.setBackgroundColor(Objects.requireNonNull(chip.getChipBackgroundColor()).getDefaultColor());
                getWindow().setStatusBarColor(chip.getChipBackgroundColor().getDefaultColor());
                color = String.valueOf(chip.getChipBackgroundColor().getDefaultColor());
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}