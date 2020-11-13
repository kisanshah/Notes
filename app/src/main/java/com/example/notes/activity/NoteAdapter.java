package com.example.notes.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class NoteAdapter extends FirebaseRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {


    @Override
    public void updateOptions(@NonNull FirebaseRecyclerOptions<Note> options) {
        super.updateOptions(options);
    }



    public NoteAdapter(@NonNull FirebaseRecyclerOptions<Note> options) {
        super(options);

    }


    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note model) {
        holder.title.setText(model.getTitle());
        holder.note.setText(model.getNote());
        holder.container.setCardBackgroundColor(Integer.parseInt(model.getColor()));
        holder.date.setText(model.getDate());
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note, parent, false);
        return new NoteViewHolder(view);
    }

    @NonNull
    @Override
    public Note getItem(int position) {
        return super.getItem(getItemCount() - 1 - position);
    }


    class NoteViewHolder extends RecyclerView.ViewHolder {
        final CardView container;
        final TextView title;
        final TextView note;
        final TextView date;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            note = itemView.findViewById(R.id.note);
            container = itemView.findViewById(R.id.container);
            date = itemView.findViewById(R.id.date);
            container.setOnClickListener(v -> {
                Intent i = new Intent(itemView.getContext(), EditNote.class);
                i.putExtra("key", getRef(getItemCount() - 1 - getAdapterPosition()).getKey());
                itemView.getContext().startActivity(i);
            });
        }
    }
}