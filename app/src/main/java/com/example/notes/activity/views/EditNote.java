package com.example.notes.activity.views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.notes.R;
import com.example.notes.activity.model.Note;
import com.example.notes.activity.viewmodel.NoteViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.Map;
import java.util.Objects;

public class EditNote extends AppCompatActivity {
    MaterialToolbar toolbar;
    TextInputEditText editTitle, editNote;
    FloatingActionButton editSubmit;
    NoteViewModel viewModel;
    RelativeLayout container;
    MaterialTextView date, time, charNumber;

    int color;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);


        viewModel = new ViewModelProvider
                .AndroidViewModelFactory(getApplication())
                .create(NoteViewModel.class);
        editTitle = findViewById(R.id.editTitle);
        editNote = findViewById(R.id.editNote);
        editSubmit = findViewById(R.id.editSubmit);
        toolbar = findViewById(R.id.toolbar);
        container = findViewById(R.id.container);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        charNumber = findViewById(R.id.charNumber);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Bundle i = getIntent().getExtras();
        key = (String) i.get("key");
        editNote.addTextChangedListener(new TextWatcher() {
            @SuppressLint("SetTextI18n")
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
        editSubmit.setOnClickListener(v -> saveNote());
        viewModel.getNote(key).observe(this, new Observer<Note>() {
            @Override
            public void onChanged(Note note) {
                editTitle.setText(note.getTitle());
                editNote.setText(note.getNote());
                date.setText(note.getDate());
                time.setText(note.getTime());
                color = Integer.parseInt(note.getColor());
                container.setBackgroundColor(color);
                getWindow().setNavigationBarColor(color);
                getWindow().setStatusBarColor(color);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_note, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        saveNote();
        super.onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                Snackbar.make(editTitle, "Delete note?", Snackbar.LENGTH_LONG).setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.deleteNote(key);
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
        String titleStr = editTitle.getText().toString();
        String noteStr = editNote.getText().toString();
        String colorStr = String.valueOf(color);
        Note note = new Note(titleStr, noteStr, colorStr, titleStr.toLowerCase(), date.getText().toString(), time.getText().toString());
        Log.d("DEBUG", "saveNote: " + color);
        viewModel.updateNote(key, toMap(note));
        Toast.makeText(EditNote.this, "Edited Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }


    public Map toMap(Note note) {
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.convertValue(note, Map.class);
        Log.d("DEBUG", "toMap: " + map);
        return map;
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