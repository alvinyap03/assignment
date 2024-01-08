package com.example.tutorial3android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class debit_cardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit_card);

        ArrayList<String> selectedGameNames = getIntent().getStringArrayListExtra("selectedGameNames");
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);

        SharedPreferences preferences = getSharedPreferences("user_info", MODE_PRIVATE);
        String username = preferences.getString("username", "");

        // Display total in TextView
        TextView totalTextView = findViewById(R.id.total4);
        totalTextView.setText(String.format("%.2f", totalPrice));

        Button debit_back = findViewById(R.id.debit_back);
        debit_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(debit_cardActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button debit_success = findViewById(R.id.debit_success);
        debit_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update UserData
                UserData userData = getUserData(username);
                if (userData != null) {
                    userData.getGames().addAll(selectedGameNames);
                    updateUserData(userData);
                }

                // Create NotificationData
                List<String> receivers = new ArrayList<>();
                receivers.add(username);
                NotificationData notificationData = new NotificationData(null, "system",
                        "You have bought games", new Date(), receivers);

                // Create IncomeData
                IncomeData incomeData = new IncomeData(username, totalPrice, new Date(), selectedGameNames);

                // Save notificationData and incomeData to wherever needed
                // For example, you can store them in a database or use them in your app as required

                // Navigate to payment_successActivity
                Intent intent = new Intent(debit_cardActivity.this, payment_successActivity.class);
                startActivity(intent);
            }
        });
    }

    private UserData getUserData(String username) {
        // Implement this method to retrieve UserData from your data source
        // (e.g., database, shared preferences, etc.)

        dbmanager dbManager = new dbmanager(debit_cardActivity.this);  // Using the activity's context
        dbManager.open();

        // Assuming you have a method to get UserData by username from your database manager
        UserData userData = dbManager.getUserByUsername(username);

        dbManager.close();

        return userData;
    }

    private void updateUserData(UserData userData) {
        // Implement this method to update UserData in your data source
        // (e.g., database, shared preferences, etc.)

        dbmanager dbManager = new dbmanager(debit_cardActivity.this);  // Using the activity's context
        dbManager.open();

        // Assuming you have a method to update UserData in your database manager
        dbManager.updateUserData(userData);

        dbManager.close();
    }
}
