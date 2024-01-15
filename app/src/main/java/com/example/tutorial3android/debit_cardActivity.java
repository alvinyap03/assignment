package com.example.tutorial3android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tutorial3android.data.Income;
import com.example.tutorial3android.manager.IncomeManager;
import com.example.tutorial3android.manager.UserGameManager;
import com.example.tutorial3android.manager.NotificationManager;
import com.example.tutorial3android.data.Notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class debit_cardActivity extends AppCompatActivity {

    private static final String TAG = "DebitCardActivity"; // Define a tag for your logs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit_card);

        // Retrieve values from the intent
        ArrayList<String> selectedGameNames = getIntent().getStringArrayListExtra("selectedGameNames");
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);

        // Retrieve username from SharedPreferences
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
                handlePayment(selectedGameNames, totalPrice, username);
            }
        });

    }

    private void handlePayment(List<String> selectedGameNames, double totalPrice, String username) {
        // Create user-game associations and store them
        UserGameManager userGameManager = new UserGameManager(debit_cardActivity.this);
        for (String gameName : selectedGameNames) {
            userGameManager.addUserGameAssociation(username, gameName);
        }
        userGameManager.closeDatabase();

        // Concatenate the game names into a single string
        StringBuilder boughtGamesMessage = new StringBuilder("Bought ");
        for (int i = 0; i < selectedGameNames.size(); i++) {
            boughtGamesMessage.append(selectedGameNames.get(i));
            if (i < selectedGameNames.size() - 1) {
                boughtGamesMessage.append(", ");
            }
        }

        // Create NotificationData
        String receiver = username + " ";  // Concatenate the username
        Notification notificationData = new Notification(
                "system",
                boughtGamesMessage.toString(),
                receiver,
                String.valueOf(new Date().getTime())
        );

        // Insert notificationData into the database
        NotificationManager notificationManager = new NotificationManager(debit_cardActivity.this);
        notificationManager.insertNotification(notificationData);
        notificationManager.closeDatabase();

        // Create Income data
        IncomeManager incomeManager = new IncomeManager(debit_cardActivity.this);
        incomeManager.insertIncome(new Income(username, totalPrice, null, String.valueOf(new Date().getTime())));  // Assuming you don't have a message for Income

        // Navigate to payment_successActivity
        Intent intent = new Intent(debit_cardActivity.this, payment_successActivity.class);
        startActivity(intent);
    }
}
