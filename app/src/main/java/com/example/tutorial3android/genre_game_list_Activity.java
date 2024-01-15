package com.example.tutorial3android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial3android.manager.GameGenreManager;

import java.util.List;

public class genre_game_list_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GameListAdapter gameListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_game_list);

        TextView genreNameTextView = findViewById(R.id.textView16);
        recyclerView = findViewById(R.id.notification1);

        Intent intent = getIntent();
        if (intent.hasExtra("selectedGenre")) {
            String selectedGenreName = intent.getStringExtra("selectedGenre");
            Log.d("Debug", "Selected Genre: " + selectedGenreName);
            genreNameTextView.setText("Games for Genre: " + selectedGenreName);

            // Retrieve game names related to the selected genre from the database
            List<String> relatedGameNames = getRelatedGameNames(selectedGenreName);

            // Log the size of the relatedGameNames list
            Log.d("Debug", "Related GameNames Size: " + relatedGameNames.size());

            // Set up the RecyclerView with the list of related game names
            setupRecyclerView(relatedGameNames);
        }

        Button backButton = findViewById(R.id.button22);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(genre_game_list_Activity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private List<String> getRelatedGameNames(String selectedGenreName) {
        // Use the GameGenreManager to retrieve the related game names from the database
        GameGenreManager gameGenreManager = new GameGenreManager(this);
        return gameGenreManager.getGameNamesByGenre(selectedGenreName);
    }

    private void setupRecyclerView(List<String> gameNames) {
        // Initialize the RecyclerView and set up the adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gameListAdapter = new GameListAdapter(gameNames);

        // Set the item click listener
        gameListAdapter.setOnItemClickListener(new GameListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle item click, for example, start buygameActivity
                String selectedGameName = gameNames.get(position);
                if (selectedGameName != null) {
                    // Implement the logic to start buygameActivity or perform any action
                    startBuyGameActivity(selectedGameName);
                } else {
                    // Handle the case where the selectedGameName is null
                    // Log an error or show a message
                }
            }
        });

        recyclerView.setAdapter(gameListAdapter);
    }

    private void startBuyGameActivity(String selectedGameName) {
        Intent intent = new Intent(genre_game_list_Activity.this, buygameActivity.class);
        intent.putExtra("selectedGameName", selectedGameName);
        startActivity(intent);
    }
}
