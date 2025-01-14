package com.example.smarty;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.google.firebase.database.ValueEventListener;


public class NotificationsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private TextView notificationTitleTextView;
    private TextView notificationDateTextView;
    private TextView notificationTextTextView;
    private ImageButton deleteButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notificationTitleTextView = findViewById(R.id.notificationTitle);
        notificationDateTextView = findViewById(R.id.notificationDate);
        notificationTextTextView = findViewById(R.id.notificationText);
        deleteButton = findViewById(R.id.deleteButton);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // Handle the case where the user is not logged in
            finish(); // Close the activity or redirect to login
            return;
        }

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("smart-Home");

        // Add a listener to detect changes
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean flameDetected = dataSnapshot.child("flame/flameDetected").getValue(Boolean.class);
                boolean gasDetected = dataSnapshot.child("Gas/gasDetected").getValue(Boolean.class);
                boolean mouvementDetecte = dataSnapshot.child("movement/mouvementDetecte").getValue(Boolean.class);

                if (flameDetected || gasDetected || mouvementDetecte) {
                    displayNotification(flameDetected, gasDetected, mouvementDetecte);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
            }
        });

        // Delete button click listener
        // Delete button click listener
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the notification
                notificationTitleTextView.setText("");
                notificationDateTextView.setText("");
                notificationTextTextView.setText("");

                // Hide notification boxes
                findViewById(R.id.flameNotificationBox).setVisibility(View.GONE);
                findViewById(R.id.gasNotificationBox).setVisibility(View.GONE);
                findViewById(R.id.movementNotificationBox).setVisibility(View.GONE);
            }
        });

    }

    private void displayNotification(boolean flameDetected, boolean gasDetected, boolean mouvementDetecte) {
        if (flameDetected) {
            // Afficher la boîte de notification de la flamme
            findViewById(R.id.flameNotificationBox).setVisibility(View.VISIBLE);
            updateNotification("Flame", getCurrentDate());
        } else {
            // Cacher la boîte de notification de la flamme si aucune alerte de flamme n'est détectée
            findViewById(R.id.flameNotificationBox).setVisibility(View.GONE);
        }

        if (gasDetected) {
            // Afficher la boîte de notification du gaz
            findViewById(R.id.gasNotificationBox).setVisibility(View.VISIBLE);
            updateNotification("Gas", getCurrentDate());
        } else {
            // Cacher la boîte de notification du gaz si aucune alerte de gaz n'est détectée
            findViewById(R.id.gasNotificationBox).setVisibility(View.GONE);
        }

        if (mouvementDetecte) {
            // Afficher la boîte de notification du mouvement
            findViewById(R.id.movementNotificationBox).setVisibility(View.VISIBLE);
            updateNotification("Movement", getCurrentDate());
        } else {
            // Cacher la boîte de notification du mouvement si aucun mouvement n'est détecté
            findViewById(R.id.movementNotificationBox).setVisibility(View.GONE);
        }
    }


    // Helper method to update notification
    // Helper method to update notification
    private void updateNotification(String title, String message) {
        notificationTitleTextView.setText(title);
        notificationTextTextView.setText(message);
    }


    // Helper method to get current date in a specific format
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Delete button click listener


}

