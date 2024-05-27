package com.example.smarty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class NotificationsFragment extends Fragment {

    private DatabaseReference databaseReference;
    private TextView notificationTitleTextView;
    private TextView notificationDateTextView;
    private TextView notificationTextTextView;
    private ImageButton deleteButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentDate; // Variable pour stocker la date actuelle

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        notificationTitleTextView = view.findViewById(R.id.notificationTitle);
        notificationDateTextView = view.findViewById(R.id.notificationDate);
        notificationTextTextView = view.findViewById(R.id.notificationText);
        deleteButton = view.findViewById(R.id.deleteButton);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // Handle the case where the user is not logged in
            getActivity().finish(); // Close the activity or redirect to login
            return view;
        }

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("smart-Home");

        // Récupérer la date actuelle une seule fois au début
        currentDate = getCurrentDate();

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
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the notification
                notificationTitleTextView.setText("");
                notificationDateTextView.setText("");
                notificationTextTextView.setText("");

                // Hide notification boxes
                getActivity().findViewById(R.id.flameNotificationBox).setVisibility(View.GONE);
                getActivity().findViewById(R.id.gasNotificationBox).setVisibility(View.GONE);
                getActivity().findViewById(R.id.movementNotificationBox).setVisibility(View.GONE);
            }
        });

        return view;
    }

    private void displayNotification(boolean flameDetected, boolean gasDetected, boolean mouvementDetecte) {
        if (flameDetected) {
            // Afficher la boîte de notification de la flamme
            getActivity().findViewById(R.id.flameNotificationBox).setVisibility(View.VISIBLE);
            updateNotification("Flame Detection");
        } else {
            // Cacher la boîte de notification de la flamme si aucune alerte de flamme n'est détectée
            getActivity().findViewById(R.id.flameNotificationBox).setVisibility(View.GONE);
        }

        if (gasDetected) {
            // Afficher la boîte de notification du gaz
            getActivity().findViewById(R.id.gasNotificationBox).setVisibility(View.VISIBLE);
            updateNotification("Gas Detection");
        } else {
            // Cacher la boîte de notification du gaz si aucune alerte de gaz n'est détectée
            getActivity().findViewById(R.id.gasNotificationBox).setVisibility(View.GONE);
        }

        if (mouvementDetecte) {
            // Afficher la boîte de notification du mouvement
            getActivity().findViewById(R.id.movementNotificationBox).setVisibility(View.VISIBLE);
            updateNotification("Movement Detection");
        } else {
            // Cacher la boîte de notification du mouvement si aucun mouvement n'est détecté
            getActivity().findViewById(R.id.movementNotificationBox).setVisibility(View.GONE);
        }
    }

    // Helper method to update notification
    private void updateNotification(String title) {
        notificationTitleTextView.setText(title);
        // Ne pas définir le texte de notificationDateTextView ici pour éviter de l'afficher deux fois
        notificationTextTextView.setText(getCurrentDate()); // Utiliser getCurrentDate() pour le texte de notification
    }

    // Helper method to get current date in a specific format
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
