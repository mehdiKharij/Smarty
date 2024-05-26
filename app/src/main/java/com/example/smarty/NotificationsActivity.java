package com.example.smarty; // Assurez-vous que votre déclaration de package est correcte

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private TextView notificationTextView;
    private ImageButton dismissButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notificationTextView = findViewById(R.id.notificationTextView);
        dismissButton = findViewById(R.id.dismissButton);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // Gérer le cas où l'utilisateur n'est pas connecté
            finish(); // Fermer l'activité ou rediriger vers la connexion
            return;
        }

        // Initialiser la base de données Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("smart-Home");

        // Ajouter un écouteur pour détecter les changements
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
                // Échec de la lecture des données
            }
        });

        // Écouteur de clic sur le bouton Dismiss
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationTextView.setText(""); // Effacer la notification
            }
        });
    }

    private void displayNotification(boolean flameDetected, boolean gasDetected, boolean mouvementDetecte) {
        StringBuilder message = new StringBuilder("Alert: ");
        message.append(getCurrentDate()); // Ajouter la date actuelle
        if (flameDetected) {
            message.append(" Flame detected.");
            updateNotificationTitle("Flame"); // Mettre à jour le titre de la notification
        }
        if (gasDetected) {
            message.append(" Gas detected.");
            updateNotificationTitle("Gas"); // Mettre à jour le titre de la notification
        }
        if (mouvementDetecte) {
            message.append(" Movement detected.");
            updateNotificationTitle("Movement"); // Mettre à jour le titre de la notification
        }

        // Afficher la notification dans la mise en page
        notificationTextView.setText(message.toString());
    }

    // Méthode pour obtenir la date actuelle dans un format spécifique
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }


    // Méthode d'aide pour mettre à jour le titre de la notification
    private void updateNotificationTitle(String title) {
        TextView notificationTitle = findViewById(R.id.notificationTitle);
        notificationTitle.setText(title);
    }

}
