package com.example.smarty;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, telEditText;
    private Button saveButton;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        db = FirebaseFirestore.getInstance();

        nameEditText = findViewById(R.id.NameEditText);
        emailEditText = findViewById(R.id.EmailEditText);
        telEditText = findViewById(R.id.telEditText);
        saveButton = findViewById(R.id.saveButton);

        // Récupérer l'utilisateur actuellement connecté
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Afficher l'e-mail de l'utilisateur
            String email = user.getEmail();
            emailEditText.setText(email);

            // Récupérer et afficher les autres informations de l'utilisateur depuis Firestore
            db.collection("user")
                    .document(user.getEmail())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String tel = documentSnapshot.getString("tel");
                            nameEditText.setText(name);
                            telEditText.setText(tel);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Gérer les erreurs de récupération des informations de l'utilisateur
                    });
        }

        saveButton.setOnClickListener(view -> saveProfile());
    }

    private void saveProfile() {
        // Récupérer les nouvelles valeurs des champs EditText
        String name = nameEditText.getText().toString().trim();
        String tel = telEditText.getText().toString().trim();

        // Récupérer l'utilisateur actuellement connecté
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Mettre à jour les informations du profil de l'utilisateur dans Firestore
            db.collection("user")
                    .document(user.getEmail())
                    .update("name", name, "tel", tel)
                    .addOnSuccessListener(aVoid -> {
                        // Afficher un message de confirmation
                        Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                        // Rediriger vers l'activité ProfileActivity
                        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish(); // Optionnel : fermer l'activité actuelle pour empêcher l'utilisateur de revenir en arrière
                    })
                    .addOnFailureListener(e -> {
                        // Gérer les erreurs lors de la mise à jour du profil
                        Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    });
        }
    }

}
