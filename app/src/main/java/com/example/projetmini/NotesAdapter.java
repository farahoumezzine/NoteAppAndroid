package com.example.projetmini;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.google.android.material.card.MaterialCardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    interface DeleteNoteListener {
        void deleteNote(String note);
    }

    private final ArrayList<String> localDataSet;
    private DeleteNoteListener deleteNoteListener;

    public NotesAdapter(ArrayList<String> dataSet) {
        localDataSet = dataSet;
    }

    public void setDeleteNoteListener(DeleteNoteListener deleteNoteListener) {
        this.deleteNoteListener = deleteNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.note_card, viewGroup, false);
        return new ViewHolder(view, deleteNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        String note = localDataSet.get(position);
        viewHolder.noteText.setText(note);

        // Register for context menu
        viewHolder.itemView.setOnLongClickListener(v -> {
            v.showContextMenu();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final MaterialCardView cardView;
        private final TextView noteText;
        private final DeleteNoteListener deleteNoteListener;

        public ViewHolder(View view, DeleteNoteListener deleteNoteListener) {
            super(view);
            this.deleteNoteListener = deleteNoteListener;
            cardView = (MaterialCardView) view;
            noteText = view.findViewById(R.id.noteText);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");
            delete.setOnMenuItemClickListener(item -> {
                if (deleteNoteListener != null) {
                    deleteNoteListener.deleteNote(noteText.getText().toString());
                    return true;
                }
                return false;
            });
        }
    }
}