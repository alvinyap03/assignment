package com.example.tutorial3android;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.tutorial3android.data.Genre;

import java.util.List;
import java.util.Set;

public class PickGerne2DataAdapter extends ArrayAdapter<Genre> {

    private Context context;
    private List<Genre> genreList;
    private Set<Genre> selectedGenres;

    public PickGerne2DataAdapter(Context context, List<Genre> genreList, Set<Genre> selectedGenres) {
        super(context, 0, genreList);
        this.context = context;
        this.genreList = genreList;
        this.selectedGenres = selectedGenres;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.list_item_genre_layout, parent, false);
        }

        Genre currentGenre = genreList.get(position);

        TextView genreName = listItem.findViewById(R.id.messageTextView);
        genreName.setText(currentGenre.getGenreName());

        // Check if the current genre is in the selected genres set
        if (selectedGenres.contains(currentGenre)) {
            // Change the background color to indicate selection
            listItem.setBackgroundColor(ContextCompat.getColor(context, R.color.selected_color));
        } else {
            // Reset the background color to the default color
            listItem.setBackgroundColor(Color.TRANSPARENT); // Change this to your default color
        }

        return listItem;
    }

    public void updateData(Set<Genre> selectedGenres) {
        this.selectedGenres = selectedGenres;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }
}